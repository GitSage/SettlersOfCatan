package client.proxy;

import shared.request.game.ChangeLogLevelRequest;

/**
 * This class will be used to change how the server functions
 */
public class UtilProxy {

    private Proxy proxy;
    /**
     * Should initialize HttpProxy
     */
    public UtilProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Changes the log level of the server
     * @pre logLevel values accepted are (all, sever, warning, info, config, fine, finer, finest, off)
     * @post returns a -1 if failure and a 1 if success 
     * @param logLevel
     * @return int
     */
    public int changeLogLevel(ChangeLogLevelRequest logLevel) {
        if (this.proxy.post("/util/changeLogLevel", logLevel) == null ) {
            return -1;
        } else {
            return 1;
        }
    }

}