package sk.zatko.dp.indexer.serialization;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class IndexConfigSerializationStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {

        return fieldAttributes.getAnnotation(JsonSerialize.class) == null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
