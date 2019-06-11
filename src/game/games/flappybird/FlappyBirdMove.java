package game.games.flappybird;

import game.GameEvent;

/**
 * 
 * This class is the FlappyBird gameevent. It contains if the player decided to
 * jump or not, and the timeframe for this game event.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class FlappyBirdMove extends GameEvent {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Time span for this event. In this case, the game has a fixed frame rate
	 * so the time span will always be 1/frame rate
	 */
	double timespan;

	/**
	 * Whether the player jumped or not
	 */
	boolean jump;

	/**
	 * Initializing class fields.
	 * 
	 * @param timespan
	 *            time span of event
	 * @param jump
	 *            whether the player jumped or not
	 */
	public FlappyBirdMove(double timespan, boolean jump) {
		this.timespan = timespan;
		this.jump = jump;
	}
}
