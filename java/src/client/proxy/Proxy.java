package client.proxy;

import client.data.PlayerInfo;

/**
 * Used to negotiate connection with server
 */
public interface Proxy {

	/**
     * Sends a http GET request to the specified url.
     * The request body will contain the JSON serialization of the given requestObject.
     * @param path
     * @param requestObject
     * @return
     * @pre path contains only the '/path' part of URLs set up like this - 'http://host:port/path'
     */
	public String get(String path);


    /**
     * Sends a http POST request to the specified url.
     * The request body will contain the JSON serialization of the given requestObject.
     * @param path
     * @param requestObject
     * @return
     * @pre path contains only the '/path' part of URLs set up like this - 'http://host:port/path'
     */
	public String post(String path, Object requestObject);
	
	/**
     * returns the player cookie as a PlayerInfo object or null if no cookie is set
     * @return
     */
    public PlayerInfo getPlayerCookie();


    /**
     * Deletes the game cookie
     */
	public void deleteGameCookie();
}
