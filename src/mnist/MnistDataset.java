package mnist;

import java.util.ArrayList;
import java.util.List;

import backpropagation.Dataset;
import matrix.Matrix;
import neuralnet.NeuralNet;
import processing.core.PApplet;

public class MnistDataset implements Dataset {
	MnistDataReader mr;

	public MnistDataset() {
		// TODO
		String path = "./";
		mr = new MnistDataReader(path);

	}

	public static void main(String[] args) {
		// NeuralNet n= new NeuralNet(inputNeurons, outputNeurons,
		// hiddenNeurons, activationFunctions)
		/*
		 * ArrayList<Integer> hiddenNeurons= new ArrayList<Integer>();
		 * hiddenNeurons.add(100); hiddenNeurons.add(100); ArrayList<Activator>
		 * activationFunctions = new ArrayList<Activator>();
		 * activationFunctions.add(new Sigmoid()); activationFunctions.add(new
		 * Sigmoid()); activationFunctions.add(new Sigmoid()); NeuralNet nn =
		 * new NeuralNet(784,10,hiddenNeurons,activationFunctions);
		 */

		NeuralNet nn = (NeuralNet) ea.ObjectLoader.loadObject("mnistclassifier02.nn");
		// train(500,nn,"mnistclassifier03.nn");
		visualize(nn);

	}

	public static void train(int epochs, NeuralNet nn, String path) {
		MnistDataReader mnr = new MnistDataReader("./");
		for (int i = 0; i < epochs; i++) {
			int correct = 0;
			int insg = mnr.testD.hoehe;
			for (int j = 0; j < insg; j++) {
				Matrix res = nn.feedForward(mnr.testD.getMatrix()[j]);
				double max = -1;
				int mI = -1;
				for (int m = 0; m < 10; m++) {
					double vergleich = res.getMatrix()[m][0];
					if (vergleich > max) {
						max = vergleich;
						mI = m;
					}
				}
				if (mnr.testL.getMatrix()[j][mI] == 1) {
					correct++;
				}
			}
			System.out.println("Classification rate in Epoch " + i + ": " + (correct + 0.0) / insg);
			nn.SGD(mnr.trainD, mnr.trainL, mnr.testD, mnr.testL, 20, 1, 2, 0.01, 4, false);
			ea.ObjectWriter.saveObject(path, nn);
		}
	}

	public static void visualize(NeuralNet nn) {
		MnistDataReader mnr = new MnistDataReader("./");
		List<Integer> labels = new ArrayList<>();
		List<double[]> inputs = new ArrayList<>();
		List<Double> certainity = new ArrayList<>();
		List<Integer> prediction = new ArrayList<>();
		int insg = mnr.testD.hoehe;
		for (int j = 0; j < insg; j++) {
			inputs.add(mnr.testD.getMatrix()[j]);
			labels.add((int) mnr.testL.getMatrix()[j][0]);
			Matrix res = nn.feedForward(mnr.testD.getMatrix()[j]);
			double max = -1;
			int mI = -1;
			for (int m = 0; m < 10; m++) {
				double vergleich = res.getMatrix()[m][0];
				if (vergleich > max) {
					max = vergleich;
					mI = m;
				}
			}
			prediction.add(mI);
			certainity.add(max);
		}
		MnistVisualizer mnv = new MnistVisualizer(inputs, labels, prediction, certainity);
		PApplet.runSketch(new String[] { "Mnist.MnistVisualizer" }, mnv);
	}

	@Override
	public Matrix getTrainData() {
		return mr.trainD;
	}

	@Override
	public Matrix getTrainLabels() {
		return mr.trainL;
	}

	@Override
	public Matrix getTestData() {
		return mr.testD;
	}

	@Override
	public Matrix getTestLabels() {
		return mr.testL;
	}
}
