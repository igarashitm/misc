package com.github.igarashitm.camel

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class ExpressionTest extends Specification {

    def "Expression valid"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                "expression-valid.yaml");
        then:
        reports.size() == 0
    }

    def "Expression invalid add prop under explicit"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-add-explicit.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

    def "Expression invalid add prop under inline"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-add-inline.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

    def "Expression invalid add prop"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-add-prop.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

    def "Expression invalid duplicate explicit"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-explicit-duplicate.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

    def "Expression invalid duplicate inline"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-inline-duplicate.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

    def "Expression invalid inline and explicit"() {
        given:
        when:
        def reports = new JsonSchemaYamlValidator().process(
                "/schema/whenAndExpression.json",
                "/yaml",
                ".*expression-invalid-inline-explicit.yaml");
        then:
        reports.size() > 0
        reports.forEach {r -> println r}
    }

}
