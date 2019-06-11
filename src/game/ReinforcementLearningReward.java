package game;

/**
 * This class assigns each GameEvent a reward, so that it can be used for
 * Reinforcement Learning.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class ReinforcementLearningReward extends GameEvent {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reward of the GameEvent
	 */
	public double reward;
}
