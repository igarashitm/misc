package com.github.igarashitm.atlasmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class AtlasMappingXmlToJsonConverterTest {

    @Test
    public void testJsonRead() throws Exception {
        JsonNode root = new ObjectMapper().readTree(Thread.currentThread().getContextClassLoader().getResourceAsStream("adm-catalog-files.json"));
        System.out.println(root.get("exportMappings").get("value").asText());
    }

}