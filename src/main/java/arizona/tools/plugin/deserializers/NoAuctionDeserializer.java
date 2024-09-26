package arizona.tools.plugin.deserializers;

import arizona.tools.plugin.dto.APIMapResponse;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class NoAuctionDeserializer extends JsonDeserializer<List<APIMapResponse.Item>> {
    @Override
    public List<APIMapResponse.Item> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<APIMapResponse.Item> result = new ArrayList<>();
        JsonNode node = p.getCodec().readTree(p);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode itemNode = field.getValue();

            if (itemNode.isArray()) {
                for (JsonNode arrayItem : itemNode) {
                    APIMapResponse.Item item = new APIMapResponse.Item();
                    item.setOwner(arrayItem.get("owner").asText());
                    item.setId(arrayItem.get("id").asInt());

                    result.add(item);
                }
            }
        }
        return result;
    }
}
