package com.github.igarashitm.camel

import io.apptik.json.JsonElement
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;

class GenerateExampleJson {

    static void main(String[] args) {
        JsonElement element = JsonElement.readFrom(
                new InputStreamReader(
                        GenerateExampleJson.class.getResourceAsStream("/schema/camelYamlDsl.json")));
        Schema schema = new SchemaV4().wrap(element.asJsonObject());
        var json = new JsonGenerator(schema, new JsonGeneratorConfig()).generate();
        System.out.println(json.toString());
    }
}
