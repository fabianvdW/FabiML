package gui;

import java.io.Serializable;

public class Hyperparameter implements Serializable {
	private static final long serialVersionUID = 1L;
	public int train_size;
	public int test_size;
	public int batch_size;
	public int epochs;
	public int cores;
	public double learning_rate;

	public Hyperparameter(int train_size, int test_size, int batch_size, int epochs, int cores, double learning_rate) {
		this.train_size = train_size;
		this.test_size = test_size;
		this.batch_size = batch_size;
		this.epochs = epochs;
		this.cores = cores;
		this.learning_rate = learning_rate;
	}
}
