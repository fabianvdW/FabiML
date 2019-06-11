package game.games.flappybird;

import java.util.ArrayList;

import processing.core.PApplet;

/**
 * This class is responsible for drawing the FlappyBird game to the screen.
 * <p>
 * Note that a game can manually be started here by calling the main function
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class FlappyBirdVisualizer extends PApplet {

	/**
	 * Frame rate of game when started manually
	 */
	public static final  int FRAMES = 30;

	/**
	 * Height of the game window in pixels
	 */
	public static final  int GAME_HEIGHT = 800;

	/**
	 * Width of the game window in pixels
	 */
	public static final int GAME_WIDTH = 1200;

	/**
	 * To be visualized game instance
	 */
	FlappyBird b;

	/**
	 * Whether there is a live player or the game will be shown with already
	 * calculated moves
	 */
	boolean player;

	/**
	 * Already calculated moves waiting to be visualized
	 */
	ArrayList<FlappyBirdMove> fbm;

	/**
	 * Position in the list {@link #fbm}
	 */
	int listpos = 0;

	/**
	 * Visualizes the game with a real player and reacts to real player input
	 * 
	 * @param player if this is true, then there is a human player
	 */
	public FlappyBirdVisualizer(boolean player) {
		this.b = new FlappyBird();
		this.player = player;
	}

	/**
	 * Visualizes the game with already calculated moves
	 * 
	 * @param fbm
	 *            already calculated moves
	 */
	public FlappyBirdVisualizer(ArrayList<FlappyBirdMove> fbm) {
		this(false);
		this.fbm = fbm;
	}

	/**
	 * Starts a game with a real player
	 * 
	 * @param args
	 *            command line parameters - None
	 */
	public static void main(String[] args) {
		FlappyBirdVisualizer v = new FlappyBirdVisualizer(true);
		String[] args2 = { "Visualizer" };
		PApplet.runSketch(args2, v);
	}

	/**
	 * Settings of game window
	 */
	public void settings() {
		size(GAME_WIDTH, GAME_HEIGHT);
	}

	/**
	 * Setting up game window
	 */
	public void setup() {
		background(255, 255, 255);
		frameRate(frameRate);
		textSize(20);
	}

	/**
	 * Drawing game to screen
	 */
	public void draw() {
		if (!b.ingame) {
			exit();
		}
		background(255, 255, 255);
		// Drawing
		// Pipes
		if (b.pipe1 != null) {
			fill(0, 255, 0);
			rect((int) b.pipe1.startX, 0, (int) FlappyBird.PIPE_WIDTH, (int) b.pipe1.minY);
			rect((int) b.pipe1.startX, (int) (b.pipe1.minY + FlappyBird.PIPE_HEIGHT), (int) FlappyBird.PIPE_WIDTH,
					(int) (800 - (b.pipe1.minY + FlappyBird.PIPE_HEIGHT)));
		}
		if (b.pipe2 != null) {
			fill(0, 255, 0);
			rect((int) b.pipe2.startX, 0, (int) FlappyBird.PIPE_WIDTH, (int) b.pipe2.minY);
			rect((int) b.pipe2.startX, (int) (b.pipe2.minY + FlappyBird.PIPE_HEIGHT), (int) FlappyBird.PIPE_WIDTH,
					(int) (800 - (b.pipe2.minY + FlappyBird.PIPE_HEIGHT)));
		}
		if (b.inPipe) {
			fill(255, 0, 0);
		} else {
			fill(0, 255, 255);
		}
		rect((int) (b.playerX - FlappyBird.PLAYER_WIDTH / 2), (int) (b.playerY - FlappyBird.PLAYER_HEIGHT / 2),
				(int) FlappyBird.PLAYER_WIDTH, (int) FlappyBird.PLAYER_HEIGHT);
		// Updating
		fill(255, 0, 0);
		text("Score: " + b.score + "", 50, 50);
		if (player) {
			this.b.update(new FlappyBirdMove(1.0 / (FlappyBirdVisualizer.FRAMES + 0.0), false));
		} else {
			if (!b.ingame) {
				return;
			}
			this.b.update(this.fbm.get(listpos));
			listpos++;
			if (listpos >= this.fbm.size()) {
				b.ingame = false;
			}
		}
	}

	/**
	 * Player input by clicking the mouse. By clicking the mouse the bird will
	 * jump once.
	 */
	public void mouseClicked() {
		if (player) {
			b.jump();

			// System.out.println("jumped");
		}
	}

	/**
	 * Player input by pressing a key. By pressing a key the bird will jump
	 * once.
	 */
	public void keyPressed() {
		if (player) {
			if (!b.ingame) {
				b = new FlappyBird();
			}
			b.jump();
			// System.out.println("jumped");
		}
	}
}
