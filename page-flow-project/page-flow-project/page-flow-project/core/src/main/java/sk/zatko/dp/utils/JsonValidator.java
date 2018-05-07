package sk.zatko.dp.utils;

import java.io.IOException;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.stereotype.Component;

@Component("jsonValidator")
public class JsonValidator {

    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();

    public boolean isValid(String schemaFilePath, String jsonFilePath) throws IOException, ProcessingException {

        JsonSchema schema = this.schemaFactory.getJsonSchema(JsonLoader.fromResource(schemaFilePath));
        return schema.validate(JsonLoader.fromFile(new java.io.File(jsonFilePath))).isSuccess();
    }

}
