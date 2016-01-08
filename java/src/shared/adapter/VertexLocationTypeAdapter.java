package shared.adapter;

import com.google.gson.*;
import shared.locations.*;

import java.lang.reflect.Type;

public class VertexLocationTypeAdapter implements JsonSerializer<VertexLocation>, JsonDeserializer {
	Gson gson = new Gson();

	public JsonElement serialize(VertexLocation src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("direction", gson.toJsonTree(src.getDir()));
		HexLocation hexLocation = src.getHexLoc();
		result.addProperty("x", hexLocation.getX());
		result.addProperty("y", hexLocation.getY());
		return result;
	}

	public VertexLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject src = json.getAsJsonObject();
		HexLocation hexLocation = new HexLocation(src.get("x").getAsInt(), src.get("y").getAsInt());
		VertexDirection vertexDirection = gson.fromJson(src.get("direction"), VertexDirection.class);
		VertexLocation result = new VertexLocation(hexLocation, vertexDirection);
		return result;
	}
}
