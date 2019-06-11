package gui.nndomode;

import java.util.HashMap;

import gui.Hyperparameter;
import gui.ModeDataLoader;
import neuralnet.NeuralNet;

public class HammingCodeNNDO extends NeuralNetTrainDataObject {

	private static final long serialVersionUID = 1L;

	public HammingCodeNNDO(NeuralNet nn, String path, Hyperparameter hp) {
		super(nn, path, hp);
		this.data.put("Epochs", new HashMap<Double, Double>());
		this.mdl = new ModeDataLoader("Hamming");
	}

}
