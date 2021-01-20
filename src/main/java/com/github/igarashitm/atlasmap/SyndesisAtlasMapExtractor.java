package com.github.igarashitm.atlasmap;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SyndesisAtlasMapExtractor {

    public int process(Path path) throws Exception {
        int count = 0;
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(path.toFile());
        om.enable(SerializationFeature.INDENT_OUTPUT);

        for (JsonNode am : root.findValues("atlasmapping")) {
            JsonNode mapping = om.readTree(am.asText());
            om.writeValue(new File(String.format("data/extracted/atlasmapping-%s.json", count)), mapping);
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        SyndesisAtlasMapExtractor extractor = new SyndesisAtlasMapExtractor();
        int count = extractor.process(Paths.get("data/model.json"));
        System.out.println(String.format("Extracted %s mappings", count));
    }

}
