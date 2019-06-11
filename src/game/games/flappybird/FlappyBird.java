package game.games.flappybird;

import java.util.ArrayList;

import game.Game;
import game.GameEvent;
import neuroevolution.NeuroEvolutionGame;

/**
 * This class implements basic FlappyBird logic, such as physics, updating the
 * game after an jump event etc.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class FlappyBird extends NeuroEvolutionGame {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Player height in pixels
	 */
	public static final  double PLAYER_HEIGHT = 50;

	/**
	 * Player width in pixels
	 */
	public static final  double PLAYER_WIDTH = 50;

	/**
	 * Earth velocity constant, tweaked for ingame physics
	 */
	final static double EARTH_CONSTANT = 98.1;

	/**
	 * Pipe width in pixels
	 */
	final static double PIPE_WIDTH = 100;

	/**
	 * Pipe height in pixels
	 */
	final static double PIPE_HEIGHT = 300;

	/**
	 * Distance between pipes in pixels
	 */
	final static double PIPE_DISTANCE = 700;

	/**
	 * Players x position
	 */
	double playerX = 150;

	/**
	 * Players y position
	 */
	double playerY = 400;

	/**
	 * Velocity in x-direction
	 */
	double velX = 500;

	/**
	 * Velocity in y-direction
	 */
	double velY = 0;

	/**
	 * if the y value falls below this limit, the player dies
	 */
	double minY = 0;

	/**
	 * if the y value exceeds this limit, the player dies
	 */
	double maxY = 800;

	/**
	 * Pipe object for first pipe. There are always only two pipes on the
	 * screen, with the off-screen pipes being deleted after unseen.
	 */
	Pipe pipe1;

	/**
	 * Pipe object for second pipe
	 * 
	 * @see #pipe1
	 */
	Pipe pipe2;

	/**
	 * Score of player.
	 */
	double score = 0.1;

	/**
	 * If this is set to true, the player is currently in a pipe
	 */
	boolean inPipe = false;

	/**
	 * If this is set to true, the game has not been finished yet. Or in other
	 * words: The player is still alive.
	 */
	boolean ingame = true;

	/**
	 * Initializes pipes
	 */
	public FlappyBird() {

		pipe1 = new Pipe(1000, getRandomPipe());
		pipe2 = new Pipe(1000 + PIPE_DISTANCE, getRandomPipe());
		this.ablauf = new ArrayList<GameEvent>();

	}

	/**
	 * Updates the gamestate to the next gamestate Note that this method will
	 * produce different results depending on the GameEvent and especially from
	 * the timeframe it has been played in. This method, when used in a loop can
	 * produce a whole game process.
	 */
	public void update(GameEvent g) {
		FlappyBirdMove fbm = (FlappyBirdMove) g;
		double timespan = fbm.timespan;
		boolean jump = fbm.jump;
		if (!ingame) {
			return;
		}
		if (jump) {
			this.ablauf.add(new FlappyBirdMove(timespan, jump));
			this.jump();
		} else {
			this.ablauf.add(new FlappyBirdMove(timespan, jump));
		}
		// Player
		velY -= 2 * timespan * EARTH_CONSTANT;
		if (velY > 350) {
			velY = 349;
		}
		if (velY < -100) {
			velY = -99;
		}
		playerY -= 10 * velY * timespan;
		if (playerY < minY) {
			playerY = 0;
			this.ingame = false;
			this.inPipe = true;
		} else if (playerY > maxY) {
			// Player lost
			playerY = maxY;
			this.ingame = false;
			this.inPipe = true;
		}
		// Pipes
		if (pipe1 != null) {
			pipe1.startX -= velX * timespan;
			pipe1.endX = pipe1.startX + FlappyBird.PIPE_WIDTH;
			if (pipe1.startX < -FlappyBird.PIPE_WIDTH) {
				score += 100;
				pipe1 = new Pipe(pipe2.startX + FlappyBird.PIPE_DISTANCE, getRandomPipe());
				if (score >= 20000) {
					this.ingame = false;
					System.out.println("HOLY SHIT");
					throw new RuntimeException();
				}
			}
		}
		if (pipe2 != null) {
			pipe2.startX -= velX * timespan;
			pipe2.endX = pipe2.startX + FlappyBird.PIPE_WIDTH;
			if (pipe2.startX < -FlappyBird.PIPE_WIDTH) {
				score += 100;
				pipe2 = new Pipe(pipe1.startX + FlappyBird.PIPE_DISTANCE, getRandomPipe());
				if (score >= 20000) {
					this.ingame = false;
					System.out.println("HOLY SHIT");
					throw new RuntimeException();
				}
			}
		}

		// Checken ob Spieler in Pipe ist
		boolean inPipe1 = false;
		boolean inPipe2 = false;
		if (pipe1 != null) {
			if (pipe1.startX < playerX + PLAYER_WIDTH / 2 && pipe1.endX > playerX - PLAYER_WIDTH / 2) {
				if (!(pipe1.minY < playerY - PLAYER_HEIGHT / 2 && pipe1.maxY > playerY + PLAYER_HEIGHT / 2)) {
					inPipe1 = true;
				}
			}
		}
		if (pipe2 != null) {
			if (pipe2.startX < playerX + PLAYER_WIDTH / 2 && pipe2.endX > playerX - PLAYER_WIDTH / 2) {
				if (!(pipe2.minY < playerY - PLAYER_HEIGHT / 2 && pipe2.maxY > playerY + PLAYER_HEIGHT / 2)) {
					inPipe2 = true;
				}
			}
		}
		this.inPipe = inPipe1 || inPipe2;
		if (this.inPipe) {
			this.ingame = false;
		}

	}

	/**
	 * Makes the player jump, thus changing the players velocity.
	 */
	public void jump() {
		if (velY < 0) {
			velY = 75;
		} else {
			velY = 75;
		}
	}

	/**
	 * Generates random pipe y positions
	 * 
	 * @return a random double value, which will be the pipes upper y value.
	 */
	public double getRandomPipe() {
		return Math.random() * ((maxY - PIPE_HEIGHT - 50));
	}

	/**
	 * Not yet implemeneted. Fix this!
	 */
	// TODO
	@Override
	protected boolean ueberpruefeEndbedingung(GameEvent g) {

		return !this.ingame;
	}

	/**
	 * Makes a new game
	 * 
	 * @return new FlappyBird game instance
	 */
	@Override
	public Game getNewInstance() {
		// TODO Auto-generated method stub
		return new FlappyBird();
	}
}
