package com.github.igarashitm.camel

import groovy.util.logging.Slf4j
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.fge.jsonschema.core.report.ProcessingReport

import java.lang.management.ManagementFactory
import java.util.stream.Collector
import java.util.stream.Collectors

@Slf4j
class JsonSchemaYamlValidator {
    static def MAPPER = new ObjectMapper(new YAMLFactory())

    def process(String schemaFileName, String dirName, String filter) {
        def schemaRes = JsonLoader.fromResource(schemaFileName)
        def schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaRes)
        def reports = new ArrayList<ProcessingReport>()
        def yamlDir = new File(getClass().getResource(dirName).toURI())
        def fnFilter = new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return name.matches(filter);
            }
        }
        println(">>>>> Using schema file:" + schemaFileName)
        yamlDir.listFiles(fnFilter).each { yaml ->
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
                new JsonSchemaYamlValidator().process(args[0], args[1], args[2]);
        if (reports.size() > 0) {
            throw new IllegalArgumentException("${reports}")
        } else {
            println(">>>>> All YAML files are valid.")
        }
        def infos = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
        println(Arrays.stream(infos).map(Object::toString).collect(Collectors.joining()))

    }
}
