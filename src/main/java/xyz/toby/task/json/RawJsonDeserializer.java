package xyz.toby.task.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * The old value of 'params' field in json (TaskData) is in json format, but the new value is string type.
 * In order to compatible with old data, the JSON property need to be deserialized as `String`.
 */
public class RawJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        if (node instanceof TextNode) { // the property value is `String`, directly return it
            return node.textValue();
        } else { // the property is JSON type, deserialize it as `String`
            return mapper.writeValueAsString(node);
        }
    }
}