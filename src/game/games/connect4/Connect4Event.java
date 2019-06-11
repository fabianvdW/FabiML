package game.games.connect4;

import game.GameEvent;

/**
 * This class represents one move being made by a player. It specifies the
 * player, the position of the token, and if this move was made randomly.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class Connect4Event extends GameEvent {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Specifies the position of the token on the board
	 */
	public int y, x;

	/**
	 * Specifies the player who placed the token
	 */
	public int player;

	/**
	 * If this is set to true, the position of the token was chosen randomly
	 */
	boolean random;

	/**
	 * @param y
	 *            row position of the token
	 * @param x
	 *            column position of the token
	 * @param player
	 *            player who placed token
	 * @param random
	 *            whether token position was chosen randomly or not
	 */
	public Connect4Event(int y, int x, int player, boolean random) {
		super();
		this.y = y;
		this.x = x;
		this.player = player;
		this.random = random;
	}

}
