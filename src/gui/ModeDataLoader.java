package gui;

import java.io.Serializable;

import backpropagation.Dataset;
import backpropagation.HammingCodeDataset;
import backpropagation.VierBitXorDataSet;
import backpropagation.XORDataset;
import matrix.Matrix;
import mnist.MnistDataset;

public class ModeDataLoader implements Serializable {
	private static final long serialVersionUID = 1L;
	public Matrix trainData;
	public Matrix trainLables;
	public Matrix testData;
	public Matrix testLabels;

	public ModeDataLoader(String mode) {
		Dataset x;
		if (mode.equals("XOR")) {
			x = new XORDataset();
		} else if (mode.equals("4BitXOR")) {
			x = new VierBitXorDataSet();
		} else if (mode.equals("Hamming")) {
			x = new HammingCodeDataset();
		} else if (mode.equals("MNIST")) {
			x = new MnistDataset();
		} else {
			throw new RuntimeException();
		}
		this.trainData = getTrainData(x);
		this.trainLables = getTrainLabels(x);
		this.testData = getTestData(x);
		this.testLabels = getTestLabels(x);

	}

	public static Matrix getTrainData(Dataset t) {
		return t.getTrainData();
	}

	public static Matrix getTrainLabels(Dataset t) {
		return t.getTrainLabels();
	}

	public static Matrix getTestData(Dataset t) {
		return t.getTestData();
	}

	public static Matrix getTestLabels(Dataset t) {
		return t.getTestLabels();
	}
}
