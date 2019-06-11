package game.games.connect4;

/**
 * This class stores the raw output of a neural net when fed forward a certain
 * input. Moreover it is stored if the output was calculated randomly, which
 * happens if the neural net was null.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class Connect4ThinkObject {
	/**
	 * raw output of the neural net
	 */
	double[] output;
	/**
	 * true if raw output was calculated randomly
	 */
	boolean random;

	/**
	 * Initializes class fields
	 * 
	 * @param output
	 *            raw output
	 * @param random
	 *            true if raw output was calculated randomly
	 */
	public Connect4ThinkObject(double[] output, boolean random) {
		this.output = output;
		this.random = random;
	}
}
