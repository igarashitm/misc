package com.github.igarashitm.camelyamlexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.spi.Resource;
import org.apache.camel.spi.RoutesLoader;
import org.apache.camel.support.PluginHelper;
import org.apache.camel.support.ResourceHelper;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public abstract class TestBase extends CamelTestSupport {
    protected RoutesLoader routesLoader;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        this.routesLoader = PluginHelper.getRoutesLoader(context);
    }

    protected void loadYamlRoute(String... fileNames) throws Exception {
        Resource[] resources = Arrays.stream(fileNames).map(f -> ResourceHelper.resolveResource(context, f)).toArray(Resource[]::new);
        this.routesLoader.loadRoutes(resources);
    }

    protected String prettyPrintJson(String json) throws Exception {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(json));
    }
}
