package org.camptocamp.watchconnect;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(
            final JsonParser jsonParser,
            final DeserializationContext deserializationContext
    ) throws IOException {
        return ZonedDateTime.parse(jsonParser.getText());
    }
}
