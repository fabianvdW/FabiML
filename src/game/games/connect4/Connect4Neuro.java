package game.games.connect4;

import java.util.ArrayList;

import game.Game;
import game.GameEvent;
import neuroevolution.Genome;
import neuroevolution.NeuroEvolutionGame;

public class Connect4Neuro extends NeuroEvolutionGame {

	/**
	 * Defines the size of the board
	 */
	int rows, columns;

	/**
	 * Integer array representing the board
	 * <p>
	 * Note that for each field in the board, there is on entry. This entry is
	 * set to 0, if there is no token placed on the field, set to 1, if player 1
	 * placed a token on the field and set to 2, if player 2 placed a token on
	 * the field.
	 * </p>
	 */
	int[][] spielfeld;

	/**
	 * 1, if it is player 1's turn, 2 if it is player 2's turn
	 */
	int current_player;

	/**
	 * -1, if game is not over yet, 1 if player 1 won, 2 if player 2 won, 3 if
	 * it is a draw
	 */
	int winner = -1;

	/**
	 * The neural net represented as genome which will represent the players.
	 * One genome can be set to null if the player should choose his moves
	 * randomly.
	 */
	Genome player1, player2;

	/**
	 * Determines who will start the game, is randomly chosen.
	 */
	int starting_player;

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a game of connect4
	 * 
	 * @param rows
	 *            amount of rows in the board
	 * @param columns
	 *            amount of columns in the board
	 * @param player1
	 *            genome of player 1
	 * @param player2
	 *            genome of player 2
	 */
	public Connect4Neuro(int rows, int columns, Genome player1, Genome player2) {
		this.rows = rows;
		this.columns = columns;
		this.spielfeld = new int[rows][columns];
		this.ablauf = new ArrayList<GameEvent>();
		this.player1 = player1;
		this.player2 = player2;
		starting_player = (int) (Math.random() * 2) + 1;
		current_player = starting_player;
	}

	/**
	 * Initializes game of connect4 with the second player being a random agent,
	 * thus only making random moves in the game
	 * 
	 * @param rows
	 *            amount of rows in the board
	 * @param columns
	 *            amount of columuns in the board
	 * @param player1
	 *            genome of player 1
	 */
	public Connect4Neuro(int rows, int columns, Genome player1) {
		this(rows, columns, player1, null);
	}

	/**
	 * Plays a full game of connect4
	 * 
	 * @see #erzeugeEvent()
	 */
	public void play() {
		while (!this.finished) {
			Connect4Event c = this.erzeugeEvent();
			this.update(c);
		}
		this.score = this.getPlayer1Score1();
	}

	/**
	 * Generates the next move that is to be made
	 * 
	 * @return the generated event
	 */
	public Connect4Event erzeugeEvent() {
		if (this.finished) {
			throw new RuntimeException();
		}
		Genome nn = null;
		if (this.current_player == 1) {
			nn = player1;
		} else {
			nn = player2;
		}
		Connect4ThinkObject c4to = Connect4Base.think(nn, this.spielfeld);
		double[] zug = c4to.output;
		// Maximum ermitteln
		double max = -1;
		int maxIndex = -1;
		for (int i = 0; i < zug.length; i++) {
			if (zug[i] > max) {
				max = zug[i];
				maxIndex = i;
			}
		}
		if (max == -1 || maxIndex == -1) {
			throw new RuntimeException();
		}

		int row = -1;
		boolean random2 = false;
		if (Connect4Base.columnFilled(maxIndex, spielfeld)) {
			random2 = true;
			while (row == -1) {
				maxIndex = (int) (Math.random() * spielfeld[0].length);
				row = Connect4Base.getRow(maxIndex, spielfeld);
			}
		} else {
			row = Connect4Base.getRow(maxIndex, spielfeld);

		}
		if (row == -1) {
			throw new RuntimeException();
		}
		Connect4Event c = new Connect4Event(row, maxIndex, this.current_player, c4to.random || random2);
		return c;
	}

	/**
	 * Calculates the score of player 1
	 * <p>
	 * It is calculated as follows: The score is 100+ the amount of moves done
	 * in the game, if player 1 won. The score is the amount of moves done in
	 * the game, if player 2 won. If the game is drawn, the score is 50.5.
	 * </p>
	 * 
	 * @return double score
	 */
	public double getPlayer1Score1() {

		if (this.winner == 1) {
			return 100 + this.ablauf.size();
		} else if (this.winner == 2) {
			return this.ablauf.size();
		} else {
			return 50 + 0.5;
		}
	}

	/**
	 * Calculates the score of player 2
	 * <p>
	 * It is calculated as follows: The score is 1, if player 2 won. The score
	 * is 0, if player 1 won. If the game is drawn, the score is 0.5.
	 * </p>
	 * 
	 * @return double score
	 */
	public double getPlayer2Score1() {
		if (this.winner == 2) {
			return 1;
		} else if (this.winner == 1) {
			return 0;
		} else {
			return 0.5;
		}
	}

	/**
	 * Updates the board after one event was generated
	 * 
	 * @param g
	 *            generated event
	 * @see #erzeugeEvent()
	 */
	@Override
	public void update(GameEvent g) {
		if (this.finished) {
			return;
		}
		Connect4Event c = (Connect4Event) g;
		this.ablauf.add(c);
		if (c.player != this.current_player) {
			throw new RuntimeException();
		}
		this.spielfeld = Connect4Base.enterGameEventInBoard(c, this.spielfeld);
		if (c.random && c.player == 1) {
			this.finished = true;
			this.winner = 2;
			return;
		}
		this.finished = this.ueberpruefeEndbedingung(g);
		if (this.finished) {
			this.winner = this.current_player;
		}
		if (this.ablauf.size() == this.rows * this.columns) {
			this.finished = true;
			this.winner = 3;
		}
		if (this.current_player == 1) {
			this.current_player = 2;
		} else {
			this.current_player = 1;
		}
	}

	/**
	 * Checks if the specified event created a row of four
	 * 
	 * @param g
	 *            event
	 * @return true if a row of four was created, false if not
	 * @see Connect4Base#ueberpruefeEndbedingung(Connect4Event, int[][])
	 */
	@Override
	protected boolean ueberpruefeEndbedingung(GameEvent g) {
		return Connect4Base.ueberpruefeEndbedingung((Connect4Event) g, this.spielfeld);

	}

	/**
	 * @return a new Connect4Game
	 */
	@Override
	public Game getNewInstance() {
		return new Connect4Neuro(this.rows, this.columns, null, null);
	}

}
