package gui.buttonmode.managermode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.Hyperparameter;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.mode.XORGUI;
import gui.monitorpanelmode.XORMonitorPanel;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.nndomode.XORNNDO;
import gui.trainermode.XORTrainer;
import neuralnet.NeuralNet;
/**
 * This class expands button manager logic to the specific case of training a
 * neural network on XOR
 * @author Fabian von der Warth
 * @version 1.0
 */
public class XORManager extends ButtonManager {

	/**
	 * @param bp the to be managed button panel
	 */
	public XORManager(ButtonPanel bp) {
		super(bp);
	}

	/**
	 * Overwrites some of the superclass logic for clicking some buttons
	 * @param e clicked button event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bp.startTraining)) {
			this.inTraining = true;
			this.currentTraining = new XORTrainer(this, this.nn);
		} else {
			super.actionPerformed(e);
		}
		this.updateButtons();
	}

	/**
	 * Updates the XOR Monitor Panel
	 * @see XORMonitorPanel
	 */
	@Override
	public void updateMonitorPanel() {
		XORGUI xg = (XORGUI) this.bp.modegui;
		xg.mp.update(this.nn, this.currentTraining);

	}

	/**
	 * Loads XOR NeuralNet Train object from file
	 * @see XORNNDO
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
				XORNNDO nn = (XORNNDO) ea.ObjectLoader.loadObject(file.getPath());
				return nn;
			}catch(ClassCastException e){
				this.erzeugeError("The NeuralNet is not in the correct mode!", this.bp);
			}
			catch (Exception e) {
				this.erzeugeError("There was an error loading the file!", this.bp);
			}

		}
		return null;
	}

	/**
	 * Creates jdialog to change hyper parameters.
	 * @param configure if this is set to true, then this method got called by clicking the edit hyper parameters button.
	 * @return new hyper parameters
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

		userTrain_Size.setText("4");
		userTrain_Size.setEditable(false);
		userTrain_Size.setBackground(Color.GRAY);

		userTest_Size.setText("4");
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
	 * Creates jdialog where you can specify where to save new neural net in.
	 * @param title title of jdialog
	 * @return new XOR Train object
	 * @see XORNNDO
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
			NeuralNet neuralNet = erzeugeNeuralNet("XOR","2",false,"1",false,true,true);
			if (neuralNet == null) {
				return null;
			}
			Hyperparameter hp = erzeugeHyperparameter(false);
			if (hp == null) {
				return null;
			}
			return new XORNNDO(neuralNet, file.getPath(), hp);

		}
		return null;
	}
}
