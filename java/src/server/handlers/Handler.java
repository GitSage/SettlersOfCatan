package server.handlers;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;
import server.commands.Command;
import server.model.User;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.Hex;
import shared.model.Port;
import shared.adapter.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

/**
 * Abstract class for handling requests.
 * Implements methods useful for all handlers.
 */
public abstract class Handler implements HttpHandler {
    protected Gson gson = new GsonBuilder()
		    .registerTypeAdapter(EdgeLocation.class, new EdgeLocationTypeAdapter())
		    .registerTypeAdapter(VertexLocation.class, new VertexLocationTypeAdapter())
		    .registerTypeAdapter(Hex.class, new HexTypeAdapter())
		    .registerTypeAdapter(Port.class, new PortTypeAdapter())
            .registerTypeAdapter(Command.class, new CommandTypeAdapter())
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    String fieldName = fieldAttributes.getName();
                    return fieldName.equals("changed")
                            || fieldName.equals("playerMap")
                            || fieldName.equals("obs")
                            || fieldName.equals("gameHistory");
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            })
		    .create();
	protected Logger logger = Logger.getLogger("catanserver");

	public void sendJsonResponse(HttpExchange exchange, int responseCode, String response) {
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		sendResponse(exchange, responseCode, response);
	}

	public void sendTextResponse(HttpExchange exchange, int responseCode, String response) {
		exchange.getResponseHeaders().add("Content-Type", "text/html");
		sendResponse(exchange, responseCode, response);
	}

	private void sendResponse(HttpExchange exchange, int responseCode, String response) {
		exchange.getResponseHeaders().add("Access-control-allow-origin", "*");
        try {
            if (response == null) {
                exchange.sendResponseHeaders(responseCode, -1);
                logger.finest(String.format("Sending %d response.", responseCode));
            } else {
                exchange.sendResponseHeaders(responseCode, response.length());
                setResponseBody(exchange, response);
                logger.finest(String.format("Sending %d response: %s", responseCode, response));
            }
        } catch (IOException ex) {
            logger.severe("Send response failed: " + ex.toString());
        }
	}

    public String getRequestBody(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void setResponseBody(HttpExchange exchange, String body) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
    }

    public void setCookie(HttpExchange exchange, String key, String value) {
	    exchange.getResponseHeaders().add("Set-cookie", key + "=" + encodeURIComponent(value) + ";Path=/;");
    }

	public boolean authUser(HttpExchange exchange) {
		logger.finest("auth user called");
		User user = getUserCookie(exchange);
		return Server.state.authenticate(user);
	}

	public boolean authGame(HttpExchange exchange) {
		User user = getUserCookie(exchange);
		int gameId = getGameCookie(exchange);
		return Server.state.authenticate(user, gameId);
	}

    public User getUserCookie(HttpExchange exchange) {
        Map<String, String> cookies = getCookies(exchange);
        if (cookies == null) {
            return null;
        } else {
            Gson gson = new Gson();
            String userString = cookies.get("catan.user");
            if (userString == null) {
                return null;
            } else {
                return gson.fromJson(decodeURIComponent(userString), User.class);
            }
        }
    }

    public int getGameCookie(HttpExchange exchange) {
        Map<String, String> cookies = getCookies(exchange);
        if (cookies == null) {
            return -1;
        } else {
            String gameString = cookies.get("catan.game");
            if (gameString == null) {
                return -1;
            } else {
                return Integer.parseInt(gameString);
            }
        }
    }

    private String encodeURIComponent(String s) {
        String result;
        try
        {
            result = URLEncoder.encode(s, "UTF-8");
        }
        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            result = s;
        }

        return result;
    }

    private String decodeURIComponent(String s) {
        String result;

        try
        {
            result = URLDecoder.decode(s, "UTF-8");
        }
        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            result = s;
        }

        return result;
    }

    private Map<String, String> getCookies(HttpExchange exchange) {
        Map<String, List<String>> headers = exchange.getRequestHeaders();
        List<String> cookieHeaders = headers.get("Cookie");
        Map<String, String> cookieMap = new HashMap<>();
        if (cookieHeaders == null) {
            return null;
        } else {
            for (String header : cookieHeaders) {
                String[] cookies = header.split(";");
                for (String cookie : cookies) {
                    String[] parts = cookie.split("=");
                    cookieMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            return cookieMap;
        }

    }
}

