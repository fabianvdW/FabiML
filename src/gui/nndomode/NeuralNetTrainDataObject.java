package gui.nndomode;

import java.io.Serializable;
import java.util.HashMap;

import gui.Hyperparameter;
import gui.ModeDataLoader;
import neuralnet.NeuralNet;

public abstract class NeuralNetTrainDataObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public NeuralNet nn;
	public HashMap<String, HashMap<Double, Double>> data;
	public String path;
	public Hyperparameter hp;
	public ModeDataLoader mdl;
	public double best_cost;
	public double best_epoch;
	public long overall_train_time;
	public int epoch;

	public NeuralNetTrainDataObject(NeuralNet nn, String path, Hyperparameter hp) {
		this.data= new HashMap<String,HashMap<Double,Double>>();
		this.nn = nn;
		this.path = path;
		this.hp = hp;
		this.best_cost = Double.MAX_VALUE;
		this.best_epoch = -1;

	}
}
