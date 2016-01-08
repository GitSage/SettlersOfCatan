package shared.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the model class for a list of messages. It consists entirely
 * of a list of messages.
 */
public class MessageList{
    private List<MessageLine> lines;

	public MessageList() {
		this.lines = new ArrayList<>();
	}

	/**
     * Return the current list of messages.
     * @return a list of MessageLine objects
     */
    public List<MessageLine> getLines() {
        return lines;
    }

    /**
     * Set the current list of messages.
     * @param lines A list of MessageLine objects
     */
    public void setLines(List<MessageLine> lines) {
        this.lines = lines;
    }

	/**
	 * Adds a single line to the list
	 */
	public void addLine(MessageLine line) {
		if (line.getIgnoreMe()) {
			return;
		}
		//Size can only be 10 or less
		if (lines.size() >= 20) {
			lines.remove(0);
		}
		lines.add(line);
	}

}