package shared.adapter;

import com.google.gson.*;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

import java.lang.reflect.Type;

public class EdgeLocationTypeAdapter implements JsonSerializer<EdgeLocation>, JsonDeserializer {
	Gson gson = new Gson();

	public JsonElement serialize(EdgeLocation src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("direction", gson.toJsonTree(src.getDirection()));
		HexLocation hexLocation = src.getHexLoc();
		result.addProperty("x", hexLocation.getX());
		result.addProperty("y", hexLocation.getY());
		return result;
	}

	public EdgeLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject src = json.getAsJsonObject();
		HexLocation hexLocation = new HexLocation(src.get("x").getAsInt(), src.get("y").getAsInt());
		EdgeDirection edgeDirection = gson.fromJson(src.get("direction"), EdgeDirection.class);
		EdgeLocation result = new EdgeLocation(hexLocation, edgeDirection);
		return result;
	}
}
