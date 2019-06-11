package game.games.flappybird;

import java.io.Serializable;

/**
 * This class stores any information belonging to a pipe.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class Pipe implements Serializable {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * X coordinates of the left edge of the pipe
	 */
	double startX;

	/**
	 * X coordinates of the right edge of the pipe
	 */
	double endX;

	/**
	 * Y coordinates of the lower edge of the upper half of the pipe
	 */
	double minY;

	/**
	 * Y coordinates of the upper edge of the lower half of the pipe
	 */
	double maxY;

	/**
	 * Initializing class fields.
	 * 
	 * @param startX
	 *            {@link #startX}
	 * @param minY
	 *            {@link #minY}
	 */
	public Pipe(double startX, double minY) {
		this.startX = startX;
		this.endX = startX + FlappyBird.PIPE_WIDTH;
		this.minY = minY;
		this.maxY = minY + FlappyBird.PIPE_HEIGHT;
	}

}