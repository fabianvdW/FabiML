package gui.nndomode;

import java.util.HashMap;

import gui.Hyperparameter;
import gui.ModeDataLoader;
import neuralnet.NeuralNet;

public class VierBitXorNNDO extends NeuralNetTrainDataObject {

	private static final long serialVersionUID = 1L;

	public VierBitXorNNDO(NeuralNet nn, String path, Hyperparameter hp) {
		super(nn, path, hp);
		this.data.put("Epochs", new HashMap<Double, Double>());
		this.mdl = new ModeDataLoader("4BitXOR");
	}

}
