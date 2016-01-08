package shared.adapter;

import com.google.gson.*;
import shared.definitions.PortType;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import shared.model.Port;

import java.lang.reflect.Type;

public class PortTypeAdapter implements JsonSerializer<Port>, JsonDeserializer {
    Gson gson = new Gson();

    public JsonElement serialize(Port src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        if (src.getType() != PortType.THREE) {
            result.add("resource", gson.toJsonTree(src.getType()));
        }
        result.add("location", gson.toJsonTree(src.getLocation()));
        result.add("direction", gson.toJsonTree(src.getDirection()));
        result.addProperty("ratio", src.getRatio());
        return result;
    }

    public Port deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Port result = new Port();
        JsonObject src = json.getAsJsonObject();
        result.setDirection(gson.fromJson(src.get("direction"), EdgeDirection.class));
        result.setLocation(gson.fromJson(src.get("location"), HexLocation.class));
        JsonElement typeElement = src.get("resource");
        if (typeElement == null) {
            result.setType(PortType.THREE);
        } else {
            result.setType(gson.fromJson(typeElement, PortType.class));
        }
        return result;
    }
}
