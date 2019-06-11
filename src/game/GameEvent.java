package game;

import java.io.Serializable;

/**
 * Base class for a GameEvent.
 * <p>
 * Note that for every game, there should be a subclass of this, since this
 * class provides nothing but polymorphism and an abstract base.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public abstract class GameEvent implements Serializable {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

}
