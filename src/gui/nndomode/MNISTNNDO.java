package gui.nndomode;

import java.util.HashMap;

import gui.Hyperparameter;
import gui.ModeDataLoader;
import matrix.Matrix;
import neuralnet.NeuralNet;

public class MNISTNNDO extends NeuralNetTrainDataObject {
	private static final long serialVersionUID = 1L;
	public Matrix[] lastTestData;
	public boolean[] lastTestDataCorrect;
	public MNISTNNDO(NeuralNet nn, String path, Hyperparameter hp) {
		super(nn, path, hp);
		this.data.put("Epochs", new HashMap<Double, Double>());
		this.data.put("Accuracy", new HashMap<Double, Double>());
		this.mdl = new ModeDataLoader("MNIST");
	}

}
