package neuralnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import matrix.Matrix;

public class NeuralNet implements Serializable {

	private static final long serialVersionUID = 1L;
	public int inputNeurons;
	public int outputNeurons;
	public ArrayList<Integer> hiddenNeurons;
	int sumOfNeurons;
	public ArrayList<Activator> activationFunctions;
	private ArrayList<Matrix> eingaben;
	private ArrayList<Matrix> ausgaben;
	public ArrayList<Matrix> weights;

	public NeuralNet(int inputNeurons, int outputNeurons, ArrayList<Integer> hiddenNeurons,
			ArrayList<Activator> activationFunctions) {
		if (hiddenNeurons.size() + 1 != activationFunctions.size()) {
			throw new IllegalArgumentException(
					"Activation Function size does not correspond with amount of hidden Layers!");
		}
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.hiddenNeurons = hiddenNeurons;
		this.sumOfNeurons += inputNeurons + outputNeurons;
		for (int i : hiddenNeurons) {
			this.sumOfNeurons += i;
		}
		this.activationFunctions = activationFunctions;
		this.eingaben = new ArrayList<Matrix>();
		this.ausgaben = new ArrayList<Matrix>();
		this.weights = new ArrayList<Matrix>();
		this.initializeWeights();
	}

	public NeuralNet(int inputNeurons, int outputNeurons, ArrayList<Integer> hiddenNeurons,
			ArrayList<Activator> activationFunctions, ArrayList<Matrix> weights) {
		if (hiddenNeurons.size() + 1 != activationFunctions.size()) {
			throw new IllegalArgumentException(
					"Activation Function size does not correspond with amount of hidden Layers!");
		}
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.hiddenNeurons = hiddenNeurons;
		this.sumOfNeurons += inputNeurons + outputNeurons;
		for (int i : hiddenNeurons) {
			this.sumOfNeurons += i;
		}
		this.activationFunctions = activationFunctions;
		this.eingaben = new ArrayList<Matrix>();
		this.ausgaben = new ArrayList<Matrix>();
		this.weights = weights;
	}

	public NeuralNet copy() {
		return new NeuralNet(this.inputNeurons, this.outputNeurons, this.hiddenNeurons, this.activationFunctions,
				this.weights);
	}

	private void initializeWeights() {
		// weights von input nach hidden[0]
		double[][] weights = new double[hiddenNeurons.get(0)][inputNeurons + 1];
		for (int i = 0; i < hiddenNeurons.get(0); i++) {
			double[] weightsToNeuron = new double[inputNeurons + 1];
			for (int j = 0; j < weightsToNeuron.length; j++) {
				weightsToNeuron[j] = this.getSamp(inputNeurons + 1);
			}
			weights[i] = weightsToNeuron;
		}
		Matrix m = new Matrix(hiddenNeurons.get(0), inputNeurons + 1);
		m.setMatrix(weights);
		this.weights.add(m);
		for (int i = 0; i + 1 < hiddenNeurons.size(); i++) {
			weights = new double[hiddenNeurons.get(i + 1)][hiddenNeurons.get(i) + 1];
			for (int j = 0; j < hiddenNeurons.get(i + 1); j++) {
				double[] weightsToNeuron = new double[this.hiddenNeurons.get(i) + 1];
				for (int k = 0; k < weightsToNeuron.length; k++) {
					weightsToNeuron[k] = this.getSamp(this.hiddenNeurons.get(i) + 1);
				}
				weights[j] = weightsToNeuron;
			}
			m = new Matrix(hiddenNeurons.get(i + 1), hiddenNeurons.get(i) + 1);
			m.setMatrix(weights);
			this.weights.add(m);
		}
		// Letztes Hiddenlayer-Outputlayer
		weights = new double[outputNeurons][hiddenNeurons.get(hiddenNeurons.size() - 1) + 1];
		for (int i = 0; i < outputNeurons; i++) {
			double[] weightsToNeuron = new double[this.hiddenNeurons.get(hiddenNeurons.size() - 1) + 1];
			for (int j = 0; j < weightsToNeuron.length; j++) {
				weightsToNeuron[j] = this.getSamp(this.hiddenNeurons.get(hiddenNeurons.size() - 1) + 1);
			}
			weights[i] = weightsToNeuron;
		}
		m = new Matrix(outputNeurons, hiddenNeurons.get(hiddenNeurons.size() - 1) + 1);
		m.setMatrix(weights);
		this.weights.add(m);
	}

	private double sum(double[] toBeSummed) {
		double res = 0;
		for (Double d : toBeSummed) {
			res += d;
		}
		return res;
	}

	private double getSamp(int n) {
		Random r = new Random();
		return r.nextGaussian() * (Math.sqrt(2.0 / n));
		// return r.nextGaussian()*(-1/Math.sqrt(n));
	}

	public double[] getFehler(double[] desiredOutput) {
		if (desiredOutput.length != this.outputNeurons) {
			throw new IllegalArgumentException("Desired output does not correspond with amount of output neurons!");
		}
		double[] fehler = new double[this.outputNeurons];
		for (int i = 0; i < fehler.length; i++) {
			double d1 = desiredOutput[i];
			double d2 = this.ausgaben.get(this.ausgaben.size() - 1).getMatrix()[i][0];
			double dfehler = Math.pow((d1 - d2), 2);
			fehler[i] = dfehler;
		}
		return fehler;
	}

	public double evaluate(Matrix testInputs, Matrix testLabels) {
		if (testInputs.hoehe != testLabels.hoehe) {
			throw new IllegalArgumentException();
		}
		double res = 0;
		int size = testInputs.hoehe;
		for (int i = 0; i < testInputs.hoehe; i++) {
			double[] input = testInputs.getMatrix()[i];
			double[] desiredOutput = testLabels.getMatrix()[i];
			this.feedForward(input);
			res += this.sum(getFehler(desiredOutput));
		}
		return res / size;
	}

	public ArrayList<Matrix[]> teileInMiniBatchesEin(Matrix train_inputs, Matrix train_labels, int batch_size) {
		ArrayList<Matrix[]> res = new ArrayList<Matrix[]>();
		int insg_size = train_inputs.hoehe;
		int anzahlBatches = insg_size / batch_size;
		int rest = insg_size % batch_size;
		if (rest != 0) {
			anzahlBatches++;
		}
		// Initialisiere Index ArrayList
		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < train_inputs.hoehe; i++) {
			index.add(i);
		}
		for (int i = 0; i < anzahlBatches; i++) {
			Matrix[] miniBatch = new Matrix[2];
			miniBatch[0] = new Matrix(0, train_inputs.breite);
			miniBatch[1] = new Matrix(0, train_labels.breite);
			int schleifenZaehler = batch_size;
			if (rest != 0 && i == anzahlBatches - 1) {
				schleifenZaehler = rest;
			}
			for (int j = 0; j < schleifenZaehler; j++) {
				int random = (int) (Math.random() * index.size());
				int indexI = index.get(random);
				index.remove(new Integer(indexI));
				miniBatch[0].addZeile(train_inputs.getMatrix()[indexI]);
				miniBatch[1].addZeile(train_labels.getMatrix()[indexI]);
			}
			res.add(miniBatch);
		}
		return res;
	}

	public void update_mini_batch2(Matrix input, Matrix label, double learning_rate, int anzahl_cores) {
		ArrayList<Matrix> deltaWeights = new ArrayList<Matrix>();
		for (int i = 0; i < input.hoehe; i++) {
			double[] inputRow = input.getMatrix()[i];
			double[] labelRow = label.getMatrix()[i];
			this.feedForward(inputRow);
			ArrayList<Matrix> resultingDelta = this.backpropagate(labelRow);
			if (i == 0) {
				deltaWeights = resultingDelta;
			} else {
				ArrayList<Matrix> newDelta = new ArrayList<Matrix>();
				for (int j = 0; j < deltaWeights.size(); j++) {
					newDelta.add(deltaWeights.get(j).add(resultingDelta.get(j)));
				}
				deltaWeights = newDelta;
			}
		}
		ArrayList<Matrix> newDelta = new ArrayList<Matrix>();
		for (int i = 0; i < deltaWeights.size(); i++) {
			newDelta.add(deltaWeights.get(i).multiply(-1 * learning_rate / input.hoehe));
		}
		ArrayList<Matrix> newWeights = new ArrayList<Matrix>();
		for (int i = 0; i < newDelta.size(); i++) {
			newWeights.add(this.weights.get(i).add(newDelta.get(i)));
		}
		this.weights = newWeights;
	}

	public void update_mini_batch(Matrix input, Matrix label, double learning_rate, int anzahl_cores) {
		if (anzahl_cores > input.hoehe) {
			anzahl_cores = input.hoehe;
		}
		ArrayList<Matrix> deltaWeights = new ArrayList<Matrix>();

		// Input in anzahl_cores Teile aufteilen
		int batch_size = input.hoehe;
		ArrayList<Integer> threadSizes = new ArrayList<Integer>();
		int thread_batch_size = batch_size / anzahl_cores;
		int addTo = batch_size % anzahl_cores;
		for (int i = 0; i < anzahl_cores; i++) {
			int tS = thread_batch_size;
			if (i < addTo) {
				tS++;
			}
			threadSizes.add(tS);
		}
		ArrayList<Matrix> threadInputs = new ArrayList<Matrix>();
		ArrayList<Matrix> threadLabels = new ArrayList<Matrix>();
		int startingHeight = 0;
		int endHeight = threadSizes.get(0) - 1;
		for (int i = 0; i < anzahl_cores; i++) {
			threadInputs.add(input.subMatrix(startingHeight, endHeight));
			threadLabels.add(label.subMatrix(startingHeight, endHeight));
			startingHeight = endHeight + 1;
			if (i == anzahl_cores - 1) {
				endHeight = batch_size - 1;
			} else {
				endHeight += threadSizes.get(i + 1);
			}
		}
		ArrayList<MiniBatchThread> threads = new ArrayList<MiniBatchThread>();
		for (int i = 0; i < anzahl_cores; i++) {
			MiniBatchThread t = new MiniBatchThread("Thread " + i, threadInputs.get(i), threadLabels.get(i),
					this.copy());
			t.start();
			threads.add(t);
		}
		for (int i = 0; i < anzahl_cores; i++) {
			try {
				threads.get(i).t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		for (int i = 0; i < anzahl_cores; i++) {
			MiniBatchThread t = threads.get(i);
			if (i == 0) {
				deltaWeights = t.deltaWeights;
			} else {
				ArrayList<Matrix> newDelta = new ArrayList<Matrix>();
				for (int j = 0; j < deltaWeights.size(); j++) {
					newDelta.add(deltaWeights.get(j).add(t.deltaWeights.get(j)));
				}
				deltaWeights = newDelta;
			}
		}

		ArrayList<Matrix> newDelta = new ArrayList<Matrix>();
		for (int i = 0; i < deltaWeights.size(); i++) {
			newDelta.add(deltaWeights.get(i).multiply(-1 * learning_rate / input.hoehe));
		}
		ArrayList<Matrix> newWeights = new ArrayList<Matrix>();
		for (int i = 0; i < newDelta.size(); i++) {
			newWeights.add(this.weights.get(i).add(newDelta.get(i)));
		}
		this.weights = newWeights;
	}

	public void SGD(Matrix train_inputs, Matrix train_labels, Matrix test_inputs, Matrix test_labels, int batch_size,
			int epochs, int test_every, double learning_rate, int anzahl_cores, boolean ausgabe) {
		/*
		 * System.out.println(train_inputs.hoehe != train_labels.hoehe);
		 * System.out.println(train_inputs.breite != this.inputNeurons);
		 * System.out.println(train_inputs.breite != test_inputs.breite);
		 * System.out.println(train_labels.breite != this.outputNeurons);
		 * System.out.println(train_labels.breite != test_labels.breite);
		 * System.out.println(batch_size > train_inputs.hoehe);
		 */
		if (train_inputs.hoehe != train_labels.hoehe || train_inputs.breite != this.inputNeurons
				|| train_inputs.breite != test_inputs.breite || train_labels.breite != this.outputNeurons
				|| train_labels.breite != test_labels.breite || batch_size > train_inputs.hoehe
				|| test_inputs.hoehe != test_labels.hoehe) {
			throw new IllegalArgumentException();
		}
		if (anzahl_cores > Runtime.getRuntime().availableProcessors()) {
			throw new IllegalArgumentException(
					"Amount of Cores may not be greater than the amount of available cores!");
		}
		final long trainingStart = System.currentTimeMillis();
		for (int i = 1; i <= epochs; i++) {
			final long epochStart = System.currentTimeMillis();
			// Train daten in Mini-Batches aufteilen
			ArrayList<Matrix[]> mini_batches = this.teileInMiniBatchesEin(train_inputs, train_labels, batch_size);
			for (Matrix[] batch : mini_batches) {
				Matrix input = batch[0];
				Matrix label = batch[1];
				this.update_mini_batch(input, label, learning_rate, anzahl_cores);
			}
			// Evaluating epoch
			final long epochEnd = System.currentTimeMillis();
			final long epochTime = epochEnd - epochStart;
			if (ausgabe) {
				if (i % test_every == 0) {
					double fehler = this.evaluate(test_inputs, test_labels);
					System.out.println("Epoch " + i + " complete in " + epochTime + " ms with Cost: " + fehler);
				} else {
					System.out.println("Epoch " + i + " complete in " + epochTime + " ms");
				}
			}
		}
		final long trainingStop = System.currentTimeMillis();
		if (ausgabe) {
			System.out.println("Training complete in " + (trainingStop - trainingStart) + "ms");
		}
	}

	public void SGD(Matrix train_inputs, Matrix train_labels, Matrix test_inputs, Matrix test_labels, int batch_size,
			int epochs, int test_every, double learning_rate, int anzahl_cores) {
		this.SGD(train_inputs, train_labels, test_inputs, test_labels, batch_size, epochs, test_every, learning_rate,
				anzahl_cores, true);

	}

	public ArrayList<Matrix> backpropagate(double[] desired_output) {
		if (desired_output.length != this.outputNeurons) {
			throw new IllegalArgumentException("Desired output does not correspont with amount of output neurons!");
		}
		ArrayList<Matrix> res = new ArrayList<Matrix>();
		// outputlayer zu letztem hiddenlayer
		double[][] deltaWeights = new double[outputNeurons][hiddenNeurons.get(hiddenNeurons.size() - 1) + 1];
		double[] previousErrorSum = new double[hiddenNeurons.get(hiddenNeurons.size() - 1)];
		for (int i = 0; i < this.outputNeurons; i++) {
			double deltaW = 2 * (this.ausgaben.get(this.ausgaben.size() - 1).getMatrix()[i][0] - desired_output[i]);
			deltaW *= this.activationFunctions.get(this.activationFunctions.size() - 1)
					.transformDerivative(this.eingaben.get(this.eingaben.size() - 1).getMatrix()[i][0]);
			double[] deltaWeightNeuron = new double[hiddenNeurons.get(hiddenNeurons.size() - 1) + 1];
			for (int j = 0; j < this.hiddenNeurons.get(this.hiddenNeurons.size() - 1); j++) {
				deltaWeightNeuron[j] = deltaW * this.ausgaben.get(this.ausgaben.size() - 2).getMatrix()[j][0];
				previousErrorSum[j] += deltaW * this.weights.get(this.weights.size() - 1).getMatrix()[i][j];
			}
			deltaWeightNeuron[this.hiddenNeurons.get(this.hiddenNeurons.size() - 1)] = deltaW;
			deltaWeights[i] = deltaWeightNeuron;
		}
		Matrix m = new Matrix(outputNeurons, hiddenNeurons.get(hiddenNeurons.size() - 1) + 1);
		m.setMatrix(deltaWeights);
		res.add(0, m);
		// Letztes Hiddenlayer zu vorletztem Hiddenlayer
		for (int i = this.hiddenNeurons.size() - 1; i > 0; i--) {
			deltaWeights = new double[this.hiddenNeurons.get(i)][this.hiddenNeurons.get(i - 1) + 1];
			double[] previousErrorSum2 = new double[hiddenNeurons.get(i - 1)];
			for (int j = 0; j < this.hiddenNeurons.get(i); j++) {
				double deltaW = previousErrorSum[j];
				deltaW *= this.activationFunctions.get(i).transformDerivative(this.eingaben.get(i).getMatrix()[j][0]);
				double[] deltaWeightNeuron = new double[hiddenNeurons.get(i - 1) + 1];
				for (int k = 0; k < this.hiddenNeurons.get(i - 1); k++) {
					deltaWeightNeuron[k] = deltaW * this.ausgaben.get(i).getMatrix()[k][0];
					previousErrorSum2[k] += deltaW * this.weights.get(i).getMatrix()[j][k];
				}
				deltaWeightNeuron[this.hiddenNeurons.get(i - 1)] = deltaW;
				deltaWeights[j] = deltaWeightNeuron;
			}
			m = new Matrix(this.hiddenNeurons.get(i), this.hiddenNeurons.get(i - 1) + 1);
			m.setMatrix(deltaWeights);
			res.add(0, m);
			previousErrorSum = previousErrorSum2;
		}
		// Erstes Hiddenlayer zu Inputlayer
		deltaWeights = new double[this.hiddenNeurons.get(0)][this.inputNeurons + 1];
		for (int i = 0; i < this.hiddenNeurons.get(0); i++) {
			double deltaW = previousErrorSum[i];
			deltaW *= this.activationFunctions.get(0).transformDerivative(this.eingaben.get(0).getMatrix()[i][0]);
			double[] deltaWeightNeuron = new double[this.inputNeurons + 1];
			for (int j = 0; j < this.inputNeurons; j++) {
				deltaWeightNeuron[j] = deltaW * this.ausgaben.get(0).getMatrix()[j][0];
			}
			deltaWeightNeuron[this.inputNeurons] = deltaW;
			deltaWeights[i] = deltaWeightNeuron;
		}
		m = new Matrix(this.hiddenNeurons.get(0), this.inputNeurons + 1);
		m.setMatrix(deltaWeights);
		res.add(0, m);
		return res;
	}

	public Matrix feedForward(double[] input) {
		if (input.length != inputNeurons) {
			throw new IllegalArgumentException("Amount of inputs does not correspond with amount of input neurons!");
		}
		this.eingaben.clear();
		this.ausgaben.clear();
		// Convert to (Input,1) Matrix
		double[][] input2 = new double[input.length + 1][1];
		for (int i = 0; i < input.length; i++) {
			input2[i][0] = input[i];
		}
		input2[input.length][0] = 1;
		Matrix currentAusgaben = new Matrix(input.length + 1, 1);
		currentAusgaben.setMatrix(input2);
		this.ausgaben.add(currentAusgaben);
		Matrix currentEingaben = this.weights.get(0).multiply(currentAusgaben);
		this.eingaben.add(currentEingaben);
		for (int i = 0; i < this.hiddenNeurons.size(); i++) {
			Activator a = this.activationFunctions.get(i);
			currentAusgaben = currentEingaben.applyFunctionOnMatrix(j -> a.transform(j));
			double[] bias = { 1 };
			currentAusgaben.addZeile(bias);
			this.ausgaben.add(currentAusgaben);
			currentEingaben = this.weights.get(i + 1).multiply(currentAusgaben);
			this.eingaben.add(currentEingaben);
		}
		currentAusgaben = currentEingaben
				.applyFunctionOnMatrix(j -> this.activationFunctions.get(this.hiddenNeurons.size()).transform(j));
		this.ausgaben.add(currentAusgaben);
		return currentAusgaben;
	}

	@Override
	public String toString() {
		String s = "(";
		s += this.inputNeurons + ", ";
		for (int i : this.hiddenNeurons) {
			s += i + ", ";
		}
		s += this.outputNeurons + ")\n";
		s += "Activations: (";
		int count = 0;
		for (Activator a : this.activationFunctions) {
			if (count != 0) {
				s += ",";
			}
			if (a instanceof Sigmoid) {
				s += "Sigmoid";
			} else if (a instanceof TanH) {
				s += "Tanh";
			} else if (a instanceof Relu) {
				s += "Relu";
			}
			count++;
		}
		s += ")\n";
		s += "Inputlayer-Hiddenlayer 0: \n";
		s += this.weights.get(0).toString();
		for (int i = 0; i + 1 < this.hiddenNeurons.size(); i++) {
			s += "Hiddenlayer " + i + "-Hiddenlayer " + (i + 1) + ": \n";
			s += this.weights.get(i + 1).toString();
		}
		s += "Hiddenlayer " + (this.hiddenNeurons.size() - 1) + "-Outputlayer: \n";
		s += this.weights.get(this.weights.size() - 1).toString();
		return s;
	}
}

class MiniBatchThread implements Runnable {
	Thread t;
	private String threadName;
	private Matrix input;
	private Matrix labels;
	private NeuralNet nn;
	boolean finished;
	ArrayList<Matrix> deltaWeights;

	MiniBatchThread(String name, Matrix input, Matrix labels, NeuralNet nn) {
		this.threadName = name;
		this.input = input;
		this.labels = labels;
		this.nn = nn;
		this.deltaWeights = new ArrayList<>();
	}

	public void start() {
		if (this.t == null) {
			t = new Thread(this, this.threadName);
			t.start();
		}
	}

	public void run() {
		for (int i = 0; i < input.hoehe; i++) {
			double[] inputRow = input.getMatrix()[i];
			double[] labelRow = labels.getMatrix()[i];
			this.nn.feedForward(inputRow);
			ArrayList<Matrix> resultingDelta = this.nn.backpropagate(labelRow);
			if (i == 0) {
				deltaWeights = resultingDelta;
			} else {
				ArrayList<Matrix> newDelta = new ArrayList<Matrix>();
				for (int j = 0; j < deltaWeights.size(); j++) {
					newDelta.add(deltaWeights.get(j).add(resultingDelta.get(j)));
				}
				deltaWeights = newDelta;
			}
		}
		this.finished = true;
	}
}
