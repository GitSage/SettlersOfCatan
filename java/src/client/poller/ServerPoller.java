package client.poller;

import shared.model.GameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ServerPoller {
	ScheduledExecutorService timer;

	GameState state;
	private boolean running;
	private int pollInterval;

	/**
	 * Initialize a server poller with proxy and game state information.
	 * @pre None
	 * @post None
	 * @param pollInterval (In Milliseconds)
	 * @param state
	 */
	public ServerPoller(int pollInterval, GameState state) {
		this.pollInterval = pollInterval;
		this.state = state;
		timer = null;
		this.running = false;
	}

	public void start() {
		if (!running) {
			//System.out.println("Started polling");
			timer = Executors.newSingleThreadScheduledExecutor();
			timer.scheduleWithFixedDelay(state, 0, pollInterval, TimeUnit.MILLISECONDS);
		}
		running = true;
	}

	public void stop() {
		if (running) {
			//System.out.println("Stopped polling");
			timer.shutdownNow();
		}
		running = false;
	}

}
