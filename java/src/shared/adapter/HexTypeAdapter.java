package shared.adapter;

import com.google.gson.*;
import shared.definitions.HexType;
import shared.locations.HexLocation;
import shared.model.Hex;

import java.lang.reflect.Type;

public class HexTypeAdapter implements JsonSerializer<Hex>, JsonDeserializer {
    Gson gson = new Gson();

    public JsonElement serialize(Hex src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("location", gson.toJsonTree(src.getLocation()));
        if (src.getType() != HexType.DESERT && src.getType() != HexType.WATER) {
            result.add("resource", gson.toJsonTree(src.getType()));
            result.addProperty("number", src.getNumber());
        }
        return result;
    }

    public Hex deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Hex result = new Hex();
        JsonObject src = json.getAsJsonObject();
        result.setLocation(gson.fromJson(src.get("location"), HexLocation.class));
        JsonElement numberElement = src.get("number");
        if (numberElement != null) {
            result.setNumber(numberElement.getAsInt());
        }
        JsonElement typeElement = src.get("resource");
        if (typeElement != null) {
            result.setType(gson.fromJson(typeElement, HexType.class));
        }
        return result;
    }
}
