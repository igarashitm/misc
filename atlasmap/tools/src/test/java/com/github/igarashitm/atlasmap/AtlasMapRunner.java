package com.github.igarashitm.atlasmap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import io.atlasmap.core.DefaultAtlasContextFactory;
import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.DataSource;
import io.atlasmap.v2.DataSourceType;
import io.atlasmap.api.AtlasContext;
import io.atlasmap.api.AtlasSession;
import io.atlasmap.core.AtlasMappingService.AtlasMappingFormat;

public class AtlasMapRunner {

    private static final String MAPPING_FILE_PATH = "mapping.json";
    private static final String SOURCE_FILE_PATH = "source.xml";

    private DefaultAtlasContextFactory factory = DefaultAtlasContextFactory.getInstance();

    @Test
    public void run() throws Exception {
        URL mappingUrl = Thread.currentThread().getContextClassLoader().getResource(MAPPING_FILE_PATH);
        AtlasContext context = factory.createContext(mappingUrl.toURI(), AtlasMappingFormat.JSON);
        AtlasSession session = context.createSession();
        AtlasMapping mapping = session.getMapping();
        String sourceDocId = getDocId(mapping, DataSourceType.SOURCE);
        InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(SOURCE_FILE_PATH);
        session.setSourceDocument(sourceDocId, IOUtils.toString(new InputStreamReader(source)));
        context.process(session);
        System.out.println(session.getTargetDocument(getDocId(mapping, DataSourceType.TARGET)));
    }

    private String getDocId(AtlasMapping mapping, DataSourceType type) {
        for (DataSource ds : mapping.getDataSource()) {
            if (ds.getDataSourceType() == type) {
                return ds.getId();
            }
        }
        throw new RuntimeException("No source document ID was found in the mapping definition");
    }
}
