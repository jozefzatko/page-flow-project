package sk.zatko.dp.indexer.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class IndexConfigDeserializationStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {

        return fieldAttributes.getAnnotation(JsonDeserialize.class) == null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
