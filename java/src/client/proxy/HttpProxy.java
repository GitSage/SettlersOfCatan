package client.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URLDecoder;
import java.util.List;

import client.data.PlayerInfo;

import com.google.gson.*;

import shared.model.Hex;
import shared.model.Port;
import shared.adapter.HexTypeAdapter;
import shared.adapter.PortTypeAdapter;

/**
 * Used to send and receive data from server
 */
public class HttpProxy implements Proxy {

	private String host;
    private String port;
    private Gson serializer;
    private CookieManager cookieManager;
    
    public void deleteGameCookie() {
    	CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookieList = cookieStore.getCookies();
        
        HttpCookie temp = null;
        // iterate HttpCookie object
        for (HttpCookie cookie : cookieList) {
        	try {
				String name = URLDecoder.decode(cookie.getName().replace("+", "%2B"), "UTF-8").replace("%2B", "+");
				if(name.equals("catan.game")) {
					System.out.println("catan.game cookie found");
					temp = cookie;
				}
			} catch (UnsupportedEncodingException e) {
				//System.out.println("Error decoding cookie... bummer...");
				e.printStackTrace();
			}
        }
        cookieStore.remove(null, temp);
        int x = 0;
    }
    
    /**
     * The methods get and post call this. It does all the work.
     * @param requestType
     * @param path
     * @param body
     * @return
     * @pre type is in (GET,POST)
     * @pre path is not null
     * @pre body is not null
     */
    private String send(String requestType, String path, Object body) {

    	if(!requestType.equals("GET") && !requestType.equals("POST")) {
    		//System.out.println("Error in send - requestType must be either GET or POST");
    		return null;
    	}

		HttpURLConnection connection = null;
		String response = null;
		try {

			URL url = new URL("http://" + host + ":" + port + path);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestType);
			connection.setDoInput(true);

			if(body != null) {
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");

		    	String jsonObjectString = serializer.toJson(body);

		    	// //System.out.println("JSON to send: " + jsonObjectString);

		    	OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
		    	wr.write(jsonObjectString);
		    	wr.close();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			response = sb.toString();

			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Error");
			response = null;
		}

		return response;
    }

    /**
     * returns the player cookie as a PlayerInfo object or null if no cookie is set
     * @return
     */
    @Override
    public PlayerInfo getPlayerCookie() {
    	
    	CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookieList = cookieStore.getCookies();
        
        // iterate HttpCookie object
        for (HttpCookie cookie : cookieList) {
        	try {
				String name = URLDecoder.decode(cookie.getName().replace("+", "%2B"), "UTF-8").replace("%2B", "+");
				if(name.equals("catan.user")) {
					//System.out.println("catan.user cookie found");
					String value = URLDecoder.decode(cookie.getValue().replace("+", "%2B"), "UTF-8").replace("%2B", "+");
					CatanCookie catanCookie = serializer.fromJson(value, CatanCookie.class);
					PlayerInfo playerInfo = new PlayerInfo();
					playerInfo.setId(catanCookie.getPlayerID());
					playerInfo.setName(catanCookie.getName());
					return playerInfo;
				}
			} catch (UnsupportedEncodingException e) {
				//System.out.println("Error decoding cookie... bummer...");
				e.printStackTrace();
			}
        }

    	return null;
    }
    
    /**
     * Creates a HttpProxy pointed at given host and port.
     * @param host
     * @param port
     */
    public HttpProxy(String host, String port) {
    	cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    	CookieHandler.setDefault(cookieManager);
        this.host = host;
        this.port = port;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Hex.class, new HexTypeAdapter());
        gsonBuilder.registerTypeAdapter(Port.class, new PortTypeAdapter());
        this.serializer = gsonBuilder.create();
    }

	@Override
	public String get(String path) {
		return this.send("GET",path,null);
	}

	@Override
	public String post(String path, Object requestObject) {
		return this.send("POST",path,requestObject);
	}

	private class CatanCookie {
		private long authentication;
		private String name;
		private String password;
		private int playerID;
		public long getAuthentication() {
			return authentication;
		}
		public void setAuthentication(long authentication) {
			this.authentication = authentication;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public int getPlayerID() {
			return playerID;
		}
		public void setPlayerID(int playerID) {
			this.playerID = playerID;
		}
		public CatanCookie(long authentication, String name, String password, int playerId) {
			super();
			this.authentication = authentication;
			this.name = name;
			this.password = password;
			this.playerID = playerId;
		}
		
	}
	
	
}
