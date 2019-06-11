package backpropagation;

import matrix.Matrix;

/**
 * Abstract class used to load and dispose test and training data of a certain
 * type of exercise, for example the MNIST dataset.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public interface Dataset {
	/**
	 * Loads or optionally calculates train data of a certain type, returns the
	 * train data in a matrix
	 * 
	 * @return train data in a matrix
	 * @see Matrix
	 */
	public abstract Matrix getTrainData();

	/**
	 * Loads or optionally calculates train data labels of a certain type,
	 * returns the train data labels in a matrix
	 * 
	 * @return train data labels in a matrix
	 * @see Matrix
	 */
	public abstract Matrix getTrainLabels();

	/**
	 * Loads or optionally calculates test data of a certain type, returns the
	 * test data in a matrix
	 * 
	 * @return test data in a matrix
	 * @see Matrix
	 */
	public abstract Matrix getTestData();

	/**
	 * Loads or optionally calculates test data labels of a certain type,
	 * returns the test data labels in a matrix
	 * 
	 * @return test data labels in a matrix
	 * @see Matrix
	 */
	public abstract Matrix getTestLabels();
	
}
