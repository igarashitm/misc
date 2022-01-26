package com.github.igarashitm.atlasmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.Json;

public class AtlasMappingXmlToJsonConverter {
    private JAXBContext jaxbContext;
    private ObjectMapper objectMapper;
    private int xmlCount = 0;
    private int admCount = 0;

    public AtlasMappingXmlToJsonConverter() throws Exception {
        jaxbContext = JAXBContext
                .newInstance("io.atlasmap.v2:io.atlasmap.java.v2:io.atlasmap.xml.v2:io.atlasmap.json.v2");
        objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .setSerializationInclusion(Include.NON_NULL);
;
    }

    public void processDirectory(Path dir) throws Exception {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path f, BasicFileAttributes attrs) {
                try {
                    if (Files.isDirectory(f)) {
                        processDirectory(f);
                        return FileVisitResult.CONTINUE;
                    }

                    if (f.getFileName().toString().toLowerCase().endsWith(".adm")) {
                        System.out.println("Checking ADM file: " + f.toString());
                        try (FileSystem fs = FileSystems.newFileSystem(f,
                                Thread.currentThread().getContextClassLoader())) {
                            admCount++;
                            Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                                    try {
                                        Path answer = null;
                                        if (file.toString().endsWith(".xml")) {
                                            if ((answer = convertMapping(file)) != null) {
                                                System.out.println(String.format("\tConverted mapping: %s -> %s",
                                                        file.toAbsolutePath(), answer.toAbsolutePath()));
                                            }
                                        } else if (file.toString().endsWith("adm-catalog-files.gz")) {
                                            if ((answer = convertCatalog(file)) != null) {
                                                System.out.println(String.format("\tConverted catalog: %s -> %s",
                                                        file.toAbsolutePath(), answer.toAbsolutePath()));
                                            }
                                        }
                                    } catch (Exception e) {
                                        return FileVisitResult.CONTINUE;
                                    }
                                    return FileVisitResult.CONTINUE;
                                }
                            });
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    if (!f.getFileName().toString().toLowerCase().endsWith(".xml")) {
                        return FileVisitResult.CONTINUE;
                    }

                    System.out.println("Checking XML file: " + f.toString());
                    Path answer = null;
                    if ((answer = convertMapping(f)) != null) {
                        xmlCount++;
                        System.out.println(String.format("\tConverted mapping: %s -> %s", f.toAbsolutePath(),
                                answer.toAbsolutePath()));
                    }
                    return FileVisitResult.CONTINUE;
                } catch (Exception e) {
                    return FileVisitResult.CONTINUE;
                }
            }
        });
    }

    private Path convertMapping(Path file) {
        AtlasMapping mapping = null;
        try {
            try (InputStream sourceStream = Files.newInputStream(file)) {
                StreamSource fileSource = new StreamSource(sourceStream);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                MyValidationEventHandler handler = new MyValidationEventHandler();
                jaxbUnmarshaller.setEventHandler(handler);
                JAXBElement<AtlasMapping> jaxb = jaxbUnmarshaller.unmarshal(fileSource, AtlasMapping.class);
                if (jaxb != null && jaxb.getValue() != null
                        && jaxb.getName().equals(new QName("http://atlasmap.io/v2", "AtlasMapping"))) {
                    mapping = jaxb.getValue();
                } else {
                    return null;
                }
            }

            String fileName = file.getFileName().toString();
            Path newFile = file.getParent().resolve(fileName.substring(0, fileName.length() - 3) + "json");
            try (CloseOnlyOnceOutputStream targetStream = new CloseOnlyOnceOutputStream(Files.newOutputStream(newFile))) {
                Json.mapper().writeValue(targetStream, mapping);
            }
            Files.delete(file);
            return newFile;
        } catch (Exception e) {
            return null;
        }
    }

    private Path convertCatalog(Path file) {
        try {
            AtlasMapping mapping = null;
            JsonNode json = null;
            try (GZIPInputStream sourceStream = new GZIPInputStream(Files.newInputStream(file))) {
                json = objectMapper.readTree(sourceStream);
            }
            JsonNode exportMappingsNode = json.get("exportMappings");
            String xml = exportMappingsNode.get("value").asText();
            StreamSource source = new StreamSource(new StringReader(xml));
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            MyValidationEventHandler handler = new MyValidationEventHandler();
            jaxbUnmarshaller.setEventHandler(handler);
            JAXBElement<AtlasMapping> jaxb = jaxbUnmarshaller.unmarshal(source, AtlasMapping.class);
            if (jaxb != null && jaxb.getValue() != null
                    && jaxb.getName().equals(new QName("http://atlasmap.io/v2", "AtlasMapping"))) {
                mapping = jaxb.getValue();
            } else {
                return null;
            }

            String converted = Json.mapper().writeValueAsString(mapping);
            ((ObjectNode) exportMappingsNode).put("value", converted);
            try (GZIPOutputStream targetStream = new GZIPOutputStream(Files.newOutputStream(file, StandardOpenOption.TRUNCATE_EXISTING))) {
                objectMapper.writeValue(targetStream, json);
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    class MyValidationEventHandler implements ValidationEventHandler {
        @Override
        public boolean handleEvent(ValidationEvent event) {
            switch (event.getSeverity()) {
                case ValidationEvent.FATAL_ERROR:
                case ValidationEvent.ERROR:
                    return false;
                default:
                    return true;
            }
        }
    }

    /**
     * A workaround for https://bugs.openjdk.java.net/browse/JDK-8069211
     */
    class CloseOnlyOnceOutputStream extends FilterOutputStream {
        private boolean closed = false;

        public CloseOnlyOnceOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public synchronized void close() throws IOException {
            if (!closed) {
                super.close();
                closed = true;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        AtlasMappingXmlToJsonConverter converter = new AtlasMappingXmlToJsonConverter();
        converter.processDirectory(Paths.get("."));
        System.out.println(String.format("Converted %s XML files, %s ADM files", converter.xmlCount, converter.admCount));
    }

}
