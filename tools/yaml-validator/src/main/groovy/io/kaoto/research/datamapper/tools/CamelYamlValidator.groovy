package io.kaoto.research.datamapper.tools

import groovy.util.logging.Slf4j
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.jsonschema.core.report.ProcessingReport

@Slf4j
class CamelYamlValidator {
    static def MAPPER = new ObjectMapper(new YAMLFactory())
    //static def SCHEMA_RES = JsonLoader.fromResource('/schema/camel-yaml-dsl.json')
    //static def SCHEMA_RES = JsonLoader.fromResource('/schema/camel-yaml-dsl-additionalProperties-false.json')
    static def SCHEMA_RES = JsonLoader.fromResource('/schema/oneOfTest.json')
    static def SCHEMA = JsonSchemaFactory.byDefault().getJsonSchema(SCHEMA_RES)

    def process() {
        def reports = new ArrayList<ProcessingReport>()
        def yamlDir = new File(getClass().getResource("/yaml").toURI())
        def filter = new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("test.yaml")
            }
        }
        yamlDir.listFiles(filter).each { yaml ->
            println(">>>>> " + yaml.name)
            def target = MAPPER.readTree(yaml)
            def report = SCHEMA.validate(target);
            if (!report.isSuccess()) {
                reports.add(report)
            }
        }
        if (reports.size() > 0) {
            throw new IllegalArgumentException("${reports}")
        }
    }

    static void main(String[] args) {
        new CamelYamlValidator().process();
    }
}
