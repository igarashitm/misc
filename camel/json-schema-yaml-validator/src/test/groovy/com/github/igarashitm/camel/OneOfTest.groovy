package com.github.igarashitm.camel

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class OneOfTest extends Specification {

    def "oneOf invalid"() {
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfInvalidTest.json",
                "/yaml",
                "inline-expression.yaml");
        then:
        reports.size() == 1
        println(reports.get(0))
    }

    def "oneOf invalid duplicate"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfInvalidTest.json",
                "/yaml",
                "duplicate-inline-expression.yaml");
        then:
        reports.size() == 1
        println(reports.get(0))
    }

    def "oneOf corrected"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfModified.json",
                "/yaml",
                "inline-expression.yaml");
        then:
        reports.size() == 0
    }

    def "oneOf corrected explicit"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfModified.json",
                "/yaml",
                "explicit-expression.yaml");
        then:
        reports.size() == 0
    }

    def "oneOf corrected duplicate"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfModified.json",
                "/yaml",
                "duplicate-inline-expression.yaml");
        then:
        reports.size() == 1
        println(reports.get(0))
    }

    def "oneOf corrected inline and explicit"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/oneOfModified.json",
                "/yaml",
                "inline-and-explicit-expression.yaml");
        then:
        reports.size() == 1
        println(reports.get(0))
    }
}
