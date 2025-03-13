package com.github.igarashitm.camelyamlexample;

import org.junit.jupiter.api.Test;

public class VariableTest extends TestBase {
    @Test
    public void testVariableReceive() throws Exception {
        loadYamlRoute( "variableReceive.camel.yaml");
        var template = context.createFluentProducerTemplate();
        var exchange = template.to("direct:start").send();
    }
}
