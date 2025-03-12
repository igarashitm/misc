package com.github.igarashitm.camel

import spock.lang.Specification

class RouteConfigurationTest extends Specification {
    def "routeConfiguration"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/camelYamlDsl.json",
                "/yaml",
                "route-configuration.yaml");
        then:
        reports.size() == 0
    }
}
