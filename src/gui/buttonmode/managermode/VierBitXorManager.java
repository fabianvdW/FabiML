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
import gui.mode.VierBitXorGUI;
import gui.monitorpanelmode.VierBitXorMonitorPanel;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.nndomode.VierBitXorNNDO;
import gui.trainermode.VierBitXorTrainer;
import neuralnet.NeuralNet;

/**
 * This class expands button manager logic to the specific case of training a
 * neural network on classifying 4-bit XOR
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class VierBitXorManager extends ButtonManager {

	/**
	 * Calls super constructor
	 * 
	 * @param bp
	 *            the to be managed button panel
	 */
	public VierBitXorManager(ButtonPanel bp) {
		super(bp);
	}

	/**
	 * Listens to button clicks and overwrites some of the existing superclass
	 * button click logic.
	 * 
	 * @param e
	 *            clicked button event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bp.startTraining)) {
			this.inTraining = true;
			this.currentTraining = new VierBitXorTrainer(this, this.nn);
		} else {
			super.actionPerformed(e);
		}
		this.updateButtons();
	}

	/**
	 * Updates the 4-BitXor MonitorPanel
	 * 
	 * @see VierBitXorMonitorPanel
	 */
	@Override
	public void updateMonitorPanel() {
		VierBitXorGUI xg = (VierBitXorGUI) this.bp.modegui;
		xg.mp.update(this.nn, this.currentTraining);
	}

	/**
	 * Loads a 4-BitXOR Train object from file.
	 * 
	 * @return 4-BitXOR Train object
	 * @see VierBitXorNNDO
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
				VierBitXorNNDO nn = (VierBitXorNNDO) ea.ObjectLoader.loadObject(file.getPath());
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
	 * Creates jdialog to edit hyperparameters
	 * 
	 * @param configure
	 *            if this is set to true, then this is called by clicking the
	 *            edit hyper parameters button instead of creating a new neural
	 *            network.
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

		userTrain_Size.setText("16");
		userTrain_Size.setEditable(false);
		userTrain_Size.setBackground(Color.GRAY);

		userTest_Size.setText("16");
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
	 * Creates jdialog to specify path of new neural network.
	 * 
	 * @param title
	 *            title of jdialog
	 * @return 4-BitXOR Train object
	 * @see VierBitXorNNDO
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
			NeuralNet neuralNet = erzeugeNeuralNet("4BitXor", "4", false, "1", false, true, true);
			if (neuralNet == null) {
				return null;
			}
			Hyperparameter hp = erzeugeHyperparameter(false);
			if (hp == null) {
				return null;
			}
			return new VierBitXorNNDO(neuralNet, file.getPath(), hp);

		}
		return null;
	}

}
