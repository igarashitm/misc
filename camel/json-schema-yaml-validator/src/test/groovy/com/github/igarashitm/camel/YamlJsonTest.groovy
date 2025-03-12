package com.github.igarashitm.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import spock.lang.Specification;

public class YamlJsonTest extends Specification {
    def "json yaml"() {
        when:
        def json = """
{
  "test": [
    {
      "a": "aaa",
      "b": "bbb"
    },
    {
      "c": "ccc",
      "d": "ddd"
    }
  ]
}
"""
        then:
        def jsonMapper = new ObjectMapper();
        def yamlMapper = new ObjectMapper(new YAMLFactory())
        def gen = yamlMapper.getFactory().createGenerator(System.out).useDefaultPrettyPrinter()
        yamlMapper.writeTree(gen, jsonMapper.readTree(json))
    }

    def "yaml json"() {
        when:
        def json = """
- routeConfiguration:
    onException:
      - handled:
          constant: "true"
        exception:
          - ${Exception.name}
        steps:
          - transform:
              constant: "Sorry"
          - to: "mock:on-exception"  
"""
            then:
            def jsonMapper = new ObjectMapper();
            def yamlMapper = new ObjectMapper(new YAMLFactory())
            def gen = jsonMapper.getFactory().createGenerator(System.out).useDefaultPrettyPrinter()
            jsonMapper.writeTree(gen, yamlMapper.readTree(json))

    }
}
