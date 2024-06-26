package game;

/**
 * Every game has its own Visualizer, who visualizes the game.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public abstract class GameVisualizer {

	/**
	 * To be visualized game
	 */
	Game g;

	/**
	 * Initializing class fields.
	 * 
	 * @param g
	 *            {@link #g}
	 */
	public GameVisualizer(Game g) {
		this.g = g;
	}

	/**
	 * Starts visualizing the game instance.
	 */
	public abstract void visualize();

}
