package client.proxy;

import shared.request.game.UserRequest;

/**
 * This proxy will be used for registering and logging in of users.
 */
public class UserProxy {
    private Proxy proxy;

    /**
     * Constructor class
     * Should initialize the Proxy
     */
    public UserProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Connects to the server and sends a login request
     * @pre requires a userRequest format {username (string), password (string)}
     * @post returns a 1 if successful and a -1 if the operation failed
     * @param request a UserRequest object containing the user's credentials.
     * @return -1 if no login
     * @return id of user
     */
    public int login(UserRequest request) {
        if (this.proxy.post("/user/login", request) == null ) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Connects to the server and creates a new user
     * @pre requires a userRequest format {username (string), password (string)}
     * @post returns a 1 if successful and a -1 if the operation failed
     * @param request a UserRequest object containing the user's credentials.
     * @return -1 if registration fails
     * @return id of new user
     */
    public int register(UserRequest request) {
        if (this.proxy.post("/user/register", request) == null ) {
            return -1;
        } else {
            return 1;
        }
    }

}