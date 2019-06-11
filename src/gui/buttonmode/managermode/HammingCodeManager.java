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
import gui.mode.HammingCodeGUI;
import gui.nndomode.HammingCodeNNDO;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.HammingCodeTrainer;
import neuralnet.NeuralNet;

/**
 * This class expands button manager logic to the specific case of training a
 * neural network on the HammingCode
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class HammingCodeManager extends ButtonManager {

	/**
	 * Calls super constructor.
	 * 
	 * @param bp
	 *            the to be managed button panel
	 * @see ButtonManager#ButtonManager(ButtonPanel)
	 */
	public HammingCodeManager(ButtonPanel bp) {
		super(bp);
	}

	/**
	 * Override super method for some button clicks.
	 * 
	 * @param e
	 *            clicked button
	 * @see ButtonManager#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bp.startTraining)) {
			this.inTraining = true;
			this.currentTraining = new HammingCodeTrainer(this, this.nn);
		} else {
			super.actionPerformed(e);
		}
		this.updateButtons();
	}

	/**
	 * Updates the HammingCodeGUI's monitor panel with newest data.
	 * 
	 * @see HammingCodeGUI#mp
	 */
	@Override
	public void updateMonitorPanel() {
		HammingCodeGUI xg = (HammingCodeGUI) this.bp.modegui;
		xg.mp.update(this.nn, this.currentTraining);

	}

	/**
	 * Loads a HammingCode NeuralNetTrainDataObject from a file
	 * 
	 * @return HammingCode NeuralNetTrainDataObject, or null and an error
	 *         message shown as jdialog if the file could not be loaded.
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
				HammingCodeNNDO nn = (HammingCodeNNDO) ea.ObjectLoader.loadObject(file.getPath());
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
	 * Creates the jdialog for editing hyper parameters
	 * 
	 * @param configure
	 *            if this is set to true, this method gets called as an edit of
	 *            already existing hyper parameters.
	 * @return Hyperparameters
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

		this.userTrain_Size.setText("2000");
		this.userTrain_Size.setEditable(false);
		this.userTrain_Size.setBackground(Color.GRAY);

		this.userTest_Size.setText("48");
		this.userTest_Size.setEditable(false);
		this.userTest_Size.setBackground(Color.GRAY);

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
	 * Creates the JFileChooser to save a NeuralNet to a file
	 * 
	 * @param title
	 *            title of jdialog
	 * @return the new neural network
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
			NeuralNet neuralNet = erzeugeNeuralNet("HammingCode", "11", false, "4", false, true, true);
			if (neuralNet == null) {
				return null;
			}
			Hyperparameter hp = erzeugeHyperparameter(false);
			if (hp == null) {
				return null;
			}
			return new HammingCodeNNDO(neuralNet, file.getPath(), hp);

		}
		return null;
	}

}
