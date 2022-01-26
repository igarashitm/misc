package com.github.igarashitm.atlasmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.atlasmap.service.AtlasService;

import org.junit.Test;

public class AtlasMappingXmlToJsonConverterTest {

    @Test
    public void testJsonRead() throws Exception {
        JsonNode root = new ObjectMapper().readTree(Thread.currentThread().getContextClassLoader().getResourceAsStream("adm-catalog-files.json"));
        System.out.println(root.get("exportMappings").get("value").asText());
    }

    @Test
    public void testVersion() {
        Package p = AtlasService.class.getPackage();
        System.out.println(p.getImplementationVersion());
    }

    @Test
    public void testParse() {
        System.out.println(Double.parseDouble("1.8"));
        System.out.println(Double.parseDouble("11"));
    }
}