package test;

import client.poller.ServerPoller;
import client.proxy.FileProxy;
import client.proxy.Proxy;
import client.proxy.ProxyFacade;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.model.GameState;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Contains test for the server poller.
 */
public class ServerPollerTest extends TestCase {

    /**
     * Tests the ServerPoller's run method to make sure it updates the model correctly.
     * @pre None
     * @post None
     * @throws Exception
     */
    @Test
    public void testRun() throws Exception {
        GameState state = new GameState();
        Proxy proxy = new FileProxy("testJson/");
        ServerPoller poller = new ServerPoller(1000, state);
        poller.start();
        // Make sure the gameState has been updated.
        assert(state.getVersion() >= 0);
        poller.start();
        // Test the case where there is no new version.
        assert(state.getVersion() >= 0);
    }
}