package gui.buttonmode.managermode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.Hyperparameter;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.buttonmode.panelmode.MNISTPanel;
import gui.mode.MNISTGUI;
import gui.nndomode.MNISTNNDO;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.MNISTTrainer;
import mnist.MnistVisualizer;
import neuralnet.NeuralNet;
import processing.core.PApplet;

/**
 * This class expands button manager logic to the specific case of training a
 * neural network on the MNIST handwritten digit data.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class MNISTManager extends ButtonManager {

	/**
	 * Specific MNIST Button Panel
	 * 
	 * @see MNISTPanel
	 */
	MNISTPanel mp;

	/**
	 * Calling super constructor
	 * 
	 * @param bp
	 *            To be managed button panel
	 */
	public MNISTManager(ButtonPanel bp) {
		super(bp);
		mp = (MNISTPanel) this.bp;
	}

	/**
	 * This method listens to button clicks and overwrites some of the
	 * functionality of the super class, for example for the visualize correct
	 * and false buttons
	 * 
	 * @param e
	 *            action event e that got called
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(bp.seetestdata)) {
			String[] labels = { "Input", "Output", "Desired Output" };
			String[][] data = new String[this.nn.mdl.testData.hoehe][3];
			boolean[] correct = new boolean[this.nn.mdl.testData.hoehe];
			for (int i = 0; i < this.nn.mdl.testData.hoehe; i++) {
				double[] input = this.nn.mdl.testData.getMatrix()[i];
				MNISTNNDO mm = (MNISTNNDO) this.nn;
				double[] output;
				if (mm.lastTestData.equals(null)) {
					output = this.nn.nn.feedForward(input).transpose().getMatrix()[0];
				} else {
					output = mm.lastTestData[i].transpose().getMatrix()[0];
				}

				double[] label = this.nn.mdl.testLabels.getMatrix()[i];
				data[i][0] = doubleArrToString(input);
				data[i][1] = doubleArrToString(output);
				data[i][2] = doubleArrToString(label);
				int index = -1;
				for (int j = 0; j < 10; j++) {
					double l = label[j];
					if (l == 1) {
						index = j;
					}
				}
				if (index == -1) {
					throw new RuntimeException();
				}
				int mIndex = -1;
				double max = -1;
				for (int j = 0; j < 10; j++) {
					double l = output[j];
					if (l > max) {
						max = l;
						mIndex = j;
					}
				}
				if (mIndex == -1) {
					throw new RuntimeException();
				}
				if (mIndex == index) {
					correct[i] = true;
				} else {
					correct[i] = false;
				}

			}
			mp.showTestTable(labels, data, "Testdata", correct);
		} else if (e.getSource().equals(bp.startTraining)) {
			this.inTraining = true;
			this.currentTraining = new MNISTTrainer(this, this.nn);
		} else if (e.getSource().equals(mp.visualizeCorrects)) {
			MNISTNNDO mm = (MNISTNNDO) this.nn;
			ArrayList<double[]> corrects = new ArrayList<double[]>();
			ArrayList<Integer> labels = new ArrayList<Integer>();
			ArrayList<Integer> prediction = new ArrayList<Integer>();
			ArrayList<Double> certainity = new ArrayList<Double>();
			for (int i = 0; i < mm.lastTestData.length; i++) {
				if (mm.lastTestDataCorrect[i]) {
					corrects.add(mm.mdl.testData.getMatrix()[i]);
					double[] label = mm.mdl.testLabels.getMatrix()[i];
					int index = 0;
					for (; index < label.length; index++) {
						if (label[index] == 1) {
							break;
						}
					}
					labels.add(index);
					double[] output = mm.lastTestData[i].transpose().getMatrix()[0];
					index = 0;
					int maxIndex = -1;
					double maxpred = -1;
					for (; index < output.length; index++) {
						if (output[index] > maxpred) {
							maxpred = output[index];
							maxIndex = index;
						}
					}
					prediction.add(maxIndex);
					certainity.add(maxpred);
				}
			}
			// new MnistVisualizer(inputs, labels, prediction, certainity)
			MnistVisualizer mnv = new MnistVisualizer(corrects, labels, prediction, certainity);
			PApplet.runSketch(new String[] { "Mnist.MnistVisualizer" }, mnv);
		} else if (e.getSource().equals(mp.visualizeFalse)) {
			MNISTNNDO mm = (MNISTNNDO) this.nn;
			ArrayList<double[]> corrects = new ArrayList<double[]>();
			ArrayList<Integer> labels = new ArrayList<Integer>();
			ArrayList<Integer> prediction = new ArrayList<Integer>();
			ArrayList<Double> certainity = new ArrayList<Double>();
			for (int i = 0; i < mm.lastTestData.length; i++) {
				if (!mm.lastTestDataCorrect[i]) {
					corrects.add(mm.mdl.testData.getMatrix()[i]);
					double[] label = mm.mdl.testLabels.getMatrix()[i];
					int index = 0;
					for (; index < label.length; index++) {
						if (label[index] == 1) {
							break;
						}
					}
					labels.add(index);
					double[] output = mm.lastTestData[i].transpose().getMatrix()[0];
					index = 0;
					int maxIndex = -1;
					double maxpred = -1;
					for (; index < output.length; index++) {
						if (output[index] > maxpred) {
							maxpred = output[index];
							maxIndex = index;
						}
					}
					prediction.add(maxIndex);
					certainity.add(maxpred);
				}
			}
			// new MnistVisualizer(inputs, labels, prediction, certainity)
			MnistVisualizer mnv = new MnistVisualizer(corrects, labels, prediction, certainity);
			PApplet.runSketch(new String[] { "Mnist.MnistVisualizer" }, mnv);
		} else {
			super.actionPerformed(e);
		}
		this.updateButtons();
	}

	/**
	 * Updates the buttons to make them clickable or not
	 */
	@Override
	public void updateButtons() {
		super.updateButtons();
		if (this.hasNeuralNet) {
			mp.visualizeCorrects.setEnabled(true);
			mp.visualizeFalse.setEnabled(true);
		}
	}

	/**
	 * Updates the MNIST Monitor panel
	 */
	@Override
	public void updateMonitorPanel() {
		MNISTGUI mg = (MNISTGUI) this.bp.modegui;
		mg.mp.update(this.nn, this.currentTraining);
	}

	/**
	 * Loads a MNIST NeuralNetTrainDataObject from a file. Returns null if the
	 * file was invalid.
	 * 
	 * @see MNISTNNDO
	 */
	@Override
	public NeuralNetTrainDataObject ladeNeuralNet() {
		JFileChooser fc = new JFileChooser(new File("."));
		FileNameExtensionFilter nntFilter = new FileNameExtensionFilter("nnt files (*.nnt)", "nnt");
		fc.addChoosableFileFilter(nntFilter);
		fc.setFileFilter(nntFilter);
		fc.setDialogTitle("Selct NeuralNet to load");
		int returnVal = fc.showOpenDialog(bp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getPath().endsWith("nnt")) {
				file = new File(file.toString() + ".nnt");
			}
			try {
				MNISTNNDO nn = (MNISTNNDO) ea.ObjectLoader.loadObject(file.getPath());
				return nn;
			} catch (ClassCastException e) {
				this.erzeugeError("The NeuralNet is not in the correct mode!", this.bp);
			} catch (Exception e) {
				this.erzeugeError("There was an error loading the file!", this.bp);
			}

		}
		return null;
	}

	/**
	 * Creates the jdialog to edit hyper parameters.
	 */
	@Override
	public Hyperparameter erzeugeHyperparameter(boolean configure) {
		super.setupHyperparameter(configure);
		Font font = new Font("ARIAL", Font.BOLD, 14);
		JDialog jd = new JDialog();
		jd.setFont(font);
		jd.setTitle("Configure Hyperparameters");
		if (configure) {
			jd.setSize(new Dimension(600, this.bp.getHeight()));
		} else {
			jd.setSize(new Dimension(600, 700));
		}
		jd.setLayout(new GridLayout(0, 2, 10, 10));

		userTrain_Size.setText("60000");
		userTrain_Size.setEditable(false);
		userTrain_Size.setBackground(Color.GRAY);

		userTest_Size.setText("10000");
		userTest_Size.setEditable(false);
		userTest_Size.setBackground(Color.GRAY);

		if (configure) {
			this.userBatch_Size.setText(this.nn.hp.batch_size + "");
			this.userBatch_Size.setEditable(true);
		} else {
			this.userBatch_Size.setText("");
			this.userBatch_Size.setEditable(true);
		}
		this.userBatch_Size.setBackground(Color.WHITE);

		if (configure) {
			this.userEpochs_Size.setText(this.nn.hp.epochs + "");
			this.userEpochs_Size.setEditable(true);
		} else {
			this.userEpochs_Size.setText("");
			this.userEpochs_Size.setEditable(true);
		}
		this.userEpochs_Size.setBackground(Color.WHITE);

		if (configure) {
			this.userCore_Size.setText(this.nn.hp.cores + "");
			this.userCore_Size.setEditable(true);
		} else {
			this.userCore_Size.setText("");
			this.userCore_Size.setEditable(true);
		}
		this.userCore_Size.setBackground(Color.WHITE);

		if (configure) {
			userLearning_rate.setText(this.nn.hp.learning_rate + "");
			userLearning_rate.setEditable(true);
		} else {
			userLearning_rate.setText("");
			userLearning_rate.setEditable(true);
		}
		userLearning_rate.setBackground(Color.WHITE);

		addFieldsToJDialog(jd, configure);
		return finishHyperparamDialog(jd);
	}

	/**
	 * Creates the JDialog where you can specify the path of your new neural
	 * network.
	 * 
	 * @param title
	 *            title of jdialog
	 * @return new MNISTNNDO Train object
	 */
	@Override
	public NeuralNetTrainDataObject erzeugeNeuralNetJFileChooser(String title) {
		JFileChooser fc = new JFileChooser(new File("."));
		FileNameExtensionFilter nntFilter = new FileNameExtensionFilter("nnt files (*.nnt)", "nnt");
		fc.addChoosableFileFilter(nntFilter);
		fc.setFileFilter(nntFilter);
		fc.setDialogTitle(title);
		int returnVal = fc.showSaveDialog(bp);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getPath().endsWith("nnt")) {
				file = new File(file.toString() + ".nnt");
			}
			NeuralNet neuralNet = erzeugeNeuralNet("MNIST", "784", false, "10", false, true, true);
			if (neuralNet == null) {
				return null;
			}
			Hyperparameter hp = erzeugeHyperparameter(false);
			if (hp == null) {
				return null;
			}
			return new MNISTNNDO(neuralNet, file.getPath(), hp);

		}
		return null;
	}
}
