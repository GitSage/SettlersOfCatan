package shared.adapter;

import com.google.gson.*;
import server.commands.Command;
import server.commands.moves.*;

import java.lang.reflect.Type;

public class CommandTypeAdapter implements JsonDeserializer {
	Gson gson = new Gson();

	public Command deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject src = json.getAsJsonObject();
		JsonObject request = src.getAsJsonObject("request");
		String type = request.getAsJsonPrimitive("type").getAsString();
		Command result = null;
		switch (type) {
			case ("acceptTrade"):
				result = gson.fromJson(json, AcceptTradeCommand.class);
				break;
			case ("buildCity"):
				result = gson.fromJson(json, BuildCityCommand.class);
				break;
			case ("buildRoad"):
				result = gson.fromJson(json, BuildRoadCommand.class);
				break;
			case ("buildSettlement"):
				result = gson.fromJson(json, BuildSettlementCommand.class);
				break;
			case ("buyDevCard"):
				result = gson.fromJson(json, BuyDevCardCommand.class);
				break;
			case ("discardCards"):
				result = gson.fromJson(json, DiscardCardsCommand.class);
				break;
			case ("finishTurn"):
				result = gson.fromJson(json, FinishTurnCommand.class);
				break;
			case ("maritimeTrade"):
				result = gson.fromJson(json, MaritimeTradeCommand.class);
				break;
			case ("Monopoly"):
				result = gson.fromJson(json, MonopolyCardCommand.class);
				break;
			case ("Monument"):
				result = gson.fromJson(json, MonumentCardCommand.class);
				break;
			case ("offerTrade"):
				result = gson.fromJson(json, OfferTradeCommand.class);
				break;
			case ("Road_Building"):
				result = gson.fromJson(json, RoadBuildingCardCommand.class);
				break;
			case ("robPlayer"):
				result = gson.fromJson(json, RobPlayerCommand.class);
				break;
			case ("rollNumber"):
				result = gson.fromJson(json, RollNumberCommand.class);
				break;
			case ("sendChat"):
				result = gson.fromJson(json, SendChatCommand.class);
				break;
			case ("Soldier"):
				result = gson.fromJson(json, SoldierCardCommand.class);
				break;
			case ("Year_of_Plenty"):
				result = gson.fromJson(json, YearOfPlentyCommand.class);
				break;
		}
		return result;
	}
}
