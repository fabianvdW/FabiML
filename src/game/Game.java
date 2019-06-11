package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract base class of every game, providing enough information for the gui.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public abstract class Game implements Serializable {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * If this is set to true, this instance of the game has already been played
	 * and is finished
	 */
	protected boolean finished;

	/**
	 * sequence of events in the game
	 */
	public ArrayList<GameEvent> ablauf;

	/**
	 * 
	 * @return if the game is finished or not, {@link #finished}
	 */
	public boolean isFinished() {
		return this.finished;
	}

	/**
	 * Updates the game
	 * <p>
	 * Note that this is always used in a loop, that will loop as long as the
	 * game is not finished, it will continue to update it.
	 * </p>
	 * 
	 * @param g
	 *            GameEvent that happened, so that the game gets updated
	 */
	public abstract void update(GameEvent g);

	/**
	 * Checks if the game has reached a final state after a certain GameEvent
	 * 
	 * @param g
	 *            last game event that happened
	 * @return true, if the game is in a final state
	 */
	protected abstract boolean ueberpruefeEndbedingung(GameEvent g);

	/**
	 * Returns a new instance of a game.
	 * 
	 * @return new Game
	 */
	public abstract Game getNewInstance();

}
