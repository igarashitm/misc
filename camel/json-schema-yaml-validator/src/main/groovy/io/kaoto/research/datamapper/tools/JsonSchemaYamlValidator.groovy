package io.kaoto.research.datamapper.tools

import groovy.util.logging.Slf4j
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.jsonschema.core.report.ProcessingReport

@Slf4j
class JsonSchemaYamlValidator {
    static def MAPPER = new ObjectMapper(new YAMLFactory())

    def process(String schemaFileName, String suffixFilter) {
        def schemaRes = JsonLoader.fromResource(schemaFileName)
        def schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaRes)
        def reports = new ArrayList<ProcessingReport>()
        def yamlDir = new File(getClass().getResource("/yaml").toURI())
        def filter = new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(suffixFilter)
            }
        }
        yamlDir.listFiles(filter).each { yaml ->
            println(">>>>> " + yaml.name)
            def target = MAPPER.readTree(yaml)
            def report = schema.validate(target);
            if (!report.isSuccess()) {
                reports.add(report)
            }
        }
        return reports;
    }

    static void main(String[] args) {
        def reports =
                new JsonSchemaYamlValidator().process(args[0], args[1]);
        if (reports.size() > 0) {
            throw new IllegalArgumentException("${reports}")
        } else {
            println(">>>>> All YAML files are valid.")
        }
    }
}
