package gui.buttonmode.managermode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gui.Hyperparameter;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;
import neuralnet.Activator;
import neuralnet.NeuralNet;
import neuralnet.Relu;
import neuralnet.Sigmoid;
import neuralnet.TanH;

/**
 * This class is responsible for managing the normal button panel, which is to
 * be found on the right.
 * <p>
 * Note that managing here means registering and reacting to button clicks.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public abstract class ButtonManager implements ActionListener {

	/**
	 * Used Font
	 */
	public static final   Font font = new Font("ARIAL",Font.BOLD,14);
	
	/**
	 * The to be managed button panel.
	 */
	public ButtonPanel bp;

	/**
	 * This object is the current neural network which is getting trained.
	 */
	public NeuralNetTrainDataObject nn;

	/**
	 * If this is set to true, there have not been any changes made on the
	 * {@link #nn}.
	 */
	public boolean saved = true;

	/**
	 * If this is set to true, {@link #nn} is not equal to null.
	 */
	public boolean hasNeuralNet = false;

	/**
	 * If this is set to true, the neural network is currently getting trained.
	 */
	public boolean inTraining = false;

	/**
	 * If this is set to true, there still is training going on, but it is
	 * currently paused.
	 */
	public boolean pausedTraining = false;

	/**
	 * If this is set to true, there user decided to continue (somewhere)
	 * without saving the {@link #nn}
	 */
	public boolean continuedWithoutSaving = false;

	/**
	 * The current Training, if any.
	 */
	public Trainer currentTraining;

	/**
	 * The current save dialog, so the dialog that gets called when the user
	 * wanted to go somewhere while there were unsaveds changes.
	 */
	private JDialog saveDialog;

	/**
	 * This button can be found in the {@link #saveDialog}. If this button gets
	 * pressed, the user will continue without saving thereby dumping all
	 * unsaved changes.
	 */
	private JButton continuewithoutsaving;

	/**
	 * This button can be found in the {@link #saveDialog}. If this button gets
	 * pressed, the user will continue while the current neural network will be
	 * saved.
	 */
	private JButton continueandsave;

	/**
	 * This will continue the program where it stopped after a saving dialog.
	 */
	public ActionEvent saveIntervened;

	/**
	 * Text field used when creating a neural network or when changing it's
	 * hyperparameters.
	 */
	JTextField inputNeurons, userInputNeurons, outputNeurons, userOutputNeurons, userHiddenNeurons, userActivations,
			train_size, userTrain_Size, test_size, userTest_Size, userBatch_Size, epochs, userEpochs_Size,
			userCore_Size, learning_rate, userLearning_rate;

	/**
	 * Text area used when creating a neural network or when changing it's
	 * hyperparameters
	 */
	JTextArea hiddenNeurons, activations, batchSize, anzahlCores;

	/**
	 * Sets the button panel
	 * 
	 * @param bp
	 *            the to be managed button panel
	 */
	public ButtonManager(ButtonPanel bp) {
		this.bp = bp;
	}

	/**
	 * This method waits for a button click. Then it manages the resulting
	 * actions.
	 * 
	 * @param e
	 *            clicked button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.continuewithoutsaving)) {
			this.continuedWithoutSaving = true;
			this.saveDialog.dispose();
			this.actionPerformed(this.saveIntervened);
		} else if (e.getSource().equals(this.continueandsave)) {
			this.saved = true;
			this.saveDialog.dispose();
			saveNN();
			this.actionPerformed(this.saveIntervened);
		} else
		// Neuronales Netz einlesen
		if (e.getSource().equals(bp.createNeuralNet)) {
			if (this.inTraining) {
				this.erzeugeError("Can not create neural net during training!", this.bp);
				return;
			}
			if (saved || continuedWithoutSaving) {
				if (continuedWithoutSaving) {
					continuedWithoutSaving = false;
				}
				NeuralNetTrainDataObject nn2 = erzeugeNeuralNetJFileChooser("Select File NeuralNet will be saved in");
				if (nn2 != null) {
					this.nn = nn2;
					saveNN();
					this.hasNeuralNet = true;
					this.currentTraining = null;
					this.bp.modegui.updateData(this.nn.data);
					this.updateMonitorPanel();
				}

			} else {
				erzeugeSaveDialog(e);
			}
		} else if (e.getSource().equals(bp.loadNeuralNet)) {
			if (this.inTraining) {
				this.erzeugeError("Can not load neural net during training!", this.bp);
				return;
			}
			if (saved || continuedWithoutSaving) {
				if (continuedWithoutSaving) {
					continuedWithoutSaving = false;
				}
				NeuralNetTrainDataObject nn2 = ladeNeuralNet();
				if (nn2 != null) {
					this.nn = nn2;
					this.hasNeuralNet = true;
					this.currentTraining = null;
					this.saved = true;
					this.bp.modegui.updateData(nn2.data);
					this.updateMonitorPanel();
				}
			} else {
				erzeugeSaveDialog(e);
			}
		} else if (e.getSource().equals(bp.configureHyperParameters)) {
			Hyperparameter hp = erzeugeHyperparameter(true);
			if (hp != null) {
				this.nn.hp = hp;
				this.unSaved();
				this.updateMonitorPanel();
			}
		} else if (e.getSource().equals(bp.saveNeuralNet)) {
			this.saveNN();
		} else if (e.getSource().equals(bp.stopTraining)) {
			this.finishedTrainingEvent();
		} else if (e.getSource().equals(bp.pauseTraining)) {
			this.pausedTraining = true;

		} else if (e.getSource().equals(bp.continueTraining)) {
			this.pausedTraining = false;
			this.currentTraining.start();
		} else if (e.getSource().equals(bp.seetestdata)) {
			String[] labels = { "Input", "Output", "Desired Output" };
			String[][] data = new String[this.nn.mdl.testData.hoehe][3];
			for (int i = 0; i < this.nn.mdl.testData.hoehe; i++) {
				double[] input = this.nn.mdl.testData.getMatrix()[i];
				double[] output = this.nn.nn.feedForward(input).transpose().getMatrix()[0];
				double[] label = this.nn.mdl.testLabels.getMatrix()[i];
				data[i][0] = doubleArrToString(input);
				data[i][1] = doubleArrToString(output);
				data[i][2] = doubleArrToString(label);
			}
			bp.showTestTable(labels, data, "Testdata");
		} else {
			throw new RuntimeException("This error should not exist since every case should be handled in subclasses.");
		}
		this.updateButtons();

	}

	/**
	 * This method manages whether the buttons are clickable or not, since not
	 * all of them should be clickable all the time.
	 */
	public void updateButtons() {
		this.bp.saveNeuralNet.setEnabled(!this.saved);
		this.bp.stopTraining.setEnabled(this.inTraining);
		this.bp.startTraining.setEnabled(this.hasNeuralNet && !this.inTraining);
		if (this.inTraining) {
			this.bp.continueTraining.setEnabled(this.pausedTraining);
			this.bp.pauseTraining.setEnabled(!this.pausedTraining);
		}
		if (this.hasNeuralNet && !this.inTraining && !this.pausedTraining) {
			this.bp.startTraining.setEnabled(true);
			this.bp.stopTraining.setEnabled(false);
			this.bp.pauseTraining.setEnabled(false);
			this.bp.continueTraining.setEnabled(false);
		}
		if (this.hasNeuralNet) {
			bp.configureHyperParameters.setEnabled(true);
			bp.seetestdata.setEnabled(true);
		}
	}

	/**
	 * Formats an double array to a string. TODO move in format class
	 * 
	 * @param arr
	 *            the to be formatted array
	 * @return formatted array as string
	 */
	public static String doubleArrToString(double[] arr) {
		String s = "";
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				s += ", ";
			}
			double d = arr[i];
			d *= 100;
			d -= d % 1;
			d /= 100;
			s += d;
		}
		return s;
	}

	/**
	 * Updates the monitor and graph panel after each epoch.
	 * 
	 * @param data
	 *            new data
	 */
	public void updateEpoch(HashMap<String, HashMap<Double, Double>> data) {
		this.bp.modegui.updateData(data);
		updateMonitorPanel();
	}

	/**
	 * Updates the monitor panel, which depends on the type of training.
	 */
	public abstract void updateMonitorPanel();

	/**
	 * After a training has been finished, buttons need to change.
	 */
	public void finishedTrainingEvent() {
		this.inTraining = false;
		this.pausedTraining = false;
		this.updateButtons();
	}

	/**
	 * Saves the neural net to its path.
	 */
	public void saveNN() {
		this.saved = true;
		if (this.nn != null) {
			ea.ObjectWriter.saveObject(this.nn.path, this.nn);
		}
		this.updateButtons();

	}

	/**
	 * Internally changes the state to an unsaved state, or in other words:
	 * there have been unsaved changes
	 */
	public void unSaved() {
		this.saved = false;
		this.updateButtons();
	}

	/**
	 * Loads a NeuralNet, it's type depends on the type of training
	 * 
	 * @return the loaded neural net
	 */
	public abstract NeuralNetTrainDataObject ladeNeuralNet();

	/**
	 * Adds all the basic fields to the NeuralNet creation or configuration
	 * dialog.
	 * 
	 * @param jd
	 *            underlying jdialog
	 * @param configure
	 *            if this is set to true, then this jdialog got called by
	 *            clicking on the edit hyperparameters button.
	 */
	public void addFieldsToJDialog(JDialog jd, boolean configure) {
		if (configure) {
			jd.add(this.inputNeurons);
			jd.add(this.userInputNeurons);
			jd.add(this.outputNeurons);
			jd.add(this.userOutputNeurons);
			jd.add(this.hiddenNeurons);
			jd.add(this.userHiddenNeurons);
			jd.add(this.activations);
			jd.add(this.userActivations);
		}
		jd.add(train_size);
		jd.add(userTrain_Size);
		jd.add(test_size);
		jd.add(userTest_Size);
		jd.add(batchSize);
		jd.add(userBatch_Size);
		jd.add(epochs);
		jd.add(userEpochs_Size);
		jd.add(anzahlCores);
		jd.add(userCore_Size);
		jd.add(learning_rate);
		jd.add(userLearning_rate);
	}

	/**
	 * Finishes creating the hyper parameters jdialog and returns the final
	 * hyper parameters.
	 * 
	 * @param jd
	 *            hyper params jdialog
	 * @return finished hyper parameters
	 */
	public Hyperparameter finishHyperparamDialog(JDialog jd) {
		JButton okay = new JButton("Okay");
		JButton cancel = new JButton("Cancel");
		okay.setFont(font);
		cancel.setFont(font);
		HyperparameterErzeuger hpe = new HyperparameterErzeuger(jd, this, userTrain_Size, userTest_Size, userBatch_Size,
				userEpochs_Size, userCore_Size, userLearning_rate);
		okay.addActionListener(hpe);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});

		jd.add(okay);
		jd.add(cancel);
		jd.setLocationRelativeTo(bp);
		jd.setModalityType(ModalityType.APPLICATION_MODAL);
		jd.setVisible(true);
		return hpe.hp;
	}

	/**
	 * Starts creating the hyper parameters jdialog.
	 * 
	 * @param configure
	 *            configure is set to true if the edit hyper parameters button
	 *            gets clicked as edit of the neural network.
	 * @return finished hyper parameters
	 */
	public abstract Hyperparameter erzeugeHyperparameter(boolean configure);

	/**
	 * Creates the basic Jdialog needed when wanting retrieve the new hyper
	 * parameters.
	 * 
	 * @param configure
	 *            configure is set to true if the edit hyper parameters button
	 *            gets clicked as edit of the neural network
	 */
	public void setupHyperparameter(boolean configure) {

		if (configure) {
			if (this.nn == null) {
				// yikes
				erzeugeError(
						"OOPSIE WOOPSIE!! Uwu We made a fucky wucky!! A wittle fucko boingo! The code monkeys at our headquarters are working VEWY HAWD to fix this!",
						this.bp);
			}
			this.inputNeurons = new JTextField("Input Neurons:");
			this.inputNeurons.setFont(font);
			this.inputNeurons.setBackground(this.bp.getBackground());
			this.inputNeurons.setEditable(false);

			this.userInputNeurons = new JTextField("" + this.nn.nn.inputNeurons);
			this.userInputNeurons.setFont(font);
			this.userInputNeurons.setEditable(false);
			this.userInputNeurons.setBackground(Color.GRAY);

			// Outputs

			this.outputNeurons = new JTextField("Output Neurons: ");
			this.outputNeurons.setFont(font);
			this.outputNeurons.setBackground(this.bp.getBackground());
			this.outputNeurons.setEditable(false);

			this.userOutputNeurons = new JTextField("" + this.nn.nn.outputNeurons);
			this.userOutputNeurons.setFont(font);
			this.userOutputNeurons.setEditable(false);
			this.userOutputNeurons.setBackground(Color.GRAY);

			this.hiddenNeurons = new JTextArea("Hidden Neurons (Seperate with comma): ");
			this.hiddenNeurons.setBorder(userOutputNeurons.getBorder());
			this.hiddenNeurons.setFont(font);
			this.hiddenNeurons.setEditable(false);
			this.hiddenNeurons.setBackground(this.bp.getBackground());
			this.hiddenNeurons.setWrapStyleWord(true);
			this.hiddenNeurons.setLineWrap(true);

			this.userHiddenNeurons = new JTextField();
			this.userHiddenNeurons.setFont(font);
			String s = "";
			int counter = 0;

			for (int i : this.nn.nn.hiddenNeurons) {
				if (counter != 0) {
					s += ",";
				}
				s += i;
				counter++;
			}
			this.userHiddenNeurons.setText(s);
			this.userHiddenNeurons.setEditable(false);
			this.userHiddenNeurons.setBackground(Color.GRAY);

			this.activations = new JTextArea("Activations (Available: Sigmoid, Tanh,Relu)(Seperate with comma): ");
			this.activations.setBorder(userHiddenNeurons.getBorder());
			this.activations.setFont(font);
			this.activations.setEditable(false);
			this.activations.setBackground(this.bp.getBackground());
			this.activations.setWrapStyleWord(true);
			this.activations.setLineWrap(true);

			this.userActivations = new JTextField();
			s = "";
			counter = 0;
			for (Activator a : this.nn.nn.activationFunctions) {
				if (counter != 0) {
					s += ",";
				}
				if (a instanceof Sigmoid) {
					s += "Sigmoid";
				} else if (a instanceof TanH) {
					s += "TanH";
				} else if (a instanceof Relu) {
					s += "Relu";
				}
				counter++;

			}
			this.userActivations.setText(s);
			this.userActivations.setFont(font);
			this.userActivations.setEditable(false);
			this.userActivations.setBackground(Color.GRAY);

		}
		this.train_size = new JTextField("Amount of train data: ");
		this.train_size.setBackground(this.bp.getBackground());
		this.train_size.setEditable(false);
		this.train_size.setFont(font);

		this.userTrain_Size = new JTextField();
		this.userTrain_Size.setFont(font);

		this.test_size = new JTextField("Amount of test data: ");
		this.test_size.setBackground(this.bp.getBackground());
		this.test_size.setEditable(false);
		this.test_size.setFont(font);

		this.userTest_Size = new JTextField();
		this.userTest_Size.setFont(font);

		this.batchSize = new JTextArea("Mini batch size(Can not be bigger than train_size):");
		this.batchSize.setBorder(test_size.getBorder());
		this.batchSize.setBackground(this.bp.getBackground());
		this.batchSize.setEditable(false);
		this.batchSize.setFont(font);
		this.batchSize.setWrapStyleWord(true);
		this.batchSize.setLineWrap(true);

		this.userBatch_Size = new JTextField();
		this.userBatch_Size.setFont(font);

		this.epochs = new JTextField("Amount of epochs: ");
		this.epochs.setBackground(this.bp.getBackground());
		this.epochs.setEditable(false);
		this.epochs.setFont(font);

		this.userEpochs_Size = new JTextField();
		this.userEpochs_Size.setFont(font);

		this.anzahlCores = new JTextArea("Amount of cores that should be used during training: ");
		this.anzahlCores.setBorder(userEpochs_Size.getBorder());
		this.anzahlCores.setLineWrap(true);
		this.anzahlCores.setWrapStyleWord(true);
		this.anzahlCores.setBackground(this.bp.getBackground());
		this.anzahlCores.setEditable(false);
		this.anzahlCores.setFont(font);

		this.userCore_Size = new JTextField();
		this.userCore_Size.setFont(font);

		this.learning_rate = new JTextField("Learning Rate: ");
		this.learning_rate.setBackground(this.bp.getBackground());
		this.learning_rate.setEditable(false);
		this.learning_rate.setFont(font);

		this.userLearning_rate = new JTextField();
		this.userLearning_rate.setFont(font);

	}

	/**
	 * Creates an error window with custom message and custom return component.
	 * 
	 * @param message
	 *            custom error message
	 * @param c
	 *            custom return component
	 */
	public void erzeugeError(String message, Component c) {
		JDialog jd = new JDialog();
		jd.setTitle("Error");
		jd.setSize(new Dimension(300, 200));
		jd.setLayout(new BorderLayout());
		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		JTextArea jta = new JTextArea(message);
		jta.setPreferredSize(new Dimension(270, 150));
		jta.setFont(new Font("ARIAL", Font.BOLD, 14));
		jta.setEditable(false);
		jta.setBackground(jd.getBackground());
		jta.setWrapStyleWord(true);
		jta.setLineWrap(true);
		center.add(jta);
		center.setPreferredSize(new Dimension(300, 150));
		JPanel south = new JPanel();
		JButton okay = new JButton("Okay");
		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		south.add(okay);
		south.setPreferredSize(new Dimension(200, 50));
		jd.add(center, BorderLayout.CENTER);
		jd.add(south, BorderLayout.SOUTH);
		jd.setLocationRelativeTo(c);
		jd.setModalityType(ModalityType.APPLICATION_MODAL);
		jd.setVisible(true);
	}

	/**
	 * Starts creating the jdialog for creating a neural network
	 * 
	 * @param mode
	 *            which training type is set, for example "XOR" or "MNIST"
	 * @param inputs
	 *            standard input neurons for training type
	 * @param inputEditable
	 *            if the input neurons are editable
	 * @param outputs
	 *            standard output neurons for training type
	 * @param outputEditable
	 *            if the output neurons are editable
	 * @param hiddenEditable
	 *            if the hidden neurons are editable
	 * @param activationEditable
	 *            if the activation functions are editable
	 * @return finishedf neural network
	 */
	public NeuralNet erzeugeNeuralNet(String mode, String inputs, boolean inputEditable, String outputs,
			boolean outputEditable, boolean hiddenEditable, boolean activationEditable) {
		JDialog jd = new JDialog();
		jd.setTitle("Creating " + mode + " Neural Net");
		jd.setSize(new Dimension(600, 500));
		jd.setLayout(new GridLayout(0, 2, 10, 10));

		// Inputs

		Font font = new Font("ARIAL", Font.BOLD, 14);

		JTextField inputNeurons = new JTextField("Input Neurons:");
		inputNeurons.setFont(font);
		inputNeurons.setBackground(jd.getBackground());
		inputNeurons.setEditable(false);

		JTextField userInputNeurons = new JTextField("" + inputs);
		userInputNeurons.setFont(font);
		if (!inputEditable) {
			userInputNeurons.setEditable(false);
		} else {
			userInputNeurons.setEditable(true);
		}
		if (userInputNeurons.isEditable()) {
			userInputNeurons.setBackground(Color.WHITE);
		} else {
			userInputNeurons.setBackground(Color.GRAY);
		}

		jd.add(inputNeurons);
		jd.add(userInputNeurons);

		// Outputs

		JTextField outputNeurons = new JTextField("Output Neurons: ");
		outputNeurons.setFont(font);
		outputNeurons.setBackground(jd.getBackground());
		outputNeurons.setEditable(false);

		JTextField userOutputNeurons = new JTextField("" + outputs);
		userOutputNeurons.setFont(font);
		if (!outputEditable) {
			userOutputNeurons.setEditable(false);
		} else {
			userOutputNeurons.setEditable(true);
		}
		if (userOutputNeurons.isEditable()) {
			userOutputNeurons.setBackground(Color.WHITE);
		} else {
			userOutputNeurons.setBackground(Color.GRAY);
		}

		jd.add(outputNeurons);
		jd.add(userOutputNeurons);

		JTextArea hiddenNeurons = new JTextArea("Hidden Neurons (Seperate with comma): ");
		hiddenNeurons.setBorder(userOutputNeurons.getBorder());
		hiddenNeurons.setFont(font);
		hiddenNeurons.setEditable(false);
		hiddenNeurons.setBackground(jd.getBackground());
		hiddenNeurons.setWrapStyleWord(true);
		hiddenNeurons.setLineWrap(true);

		JTextField userHiddenNeurons = new JTextField();
		userHiddenNeurons.setFont(font);
		if (hiddenEditable) {
			userHiddenNeurons.setEditable(true);

		} else {
			userHiddenNeurons.setEditable(false);
		}
		if (userHiddenNeurons.isEditable()) {
			userHiddenNeurons.setBackground(Color.WHITE);
		} else {
			userHiddenNeurons.setBackground(Color.GRAY);
		}
		jd.add(hiddenNeurons);
		jd.add(userHiddenNeurons);

		JTextArea activations = new JTextArea("Activations (Available: Sigmoid, Tanh,Relu)(Seperate with comma): ");
		activations.setBorder(userHiddenNeurons.getBorder());
		activations.setFont(font);
		activations.setEditable(false);
		activations.setBackground(jd.getBackground());
		activations.setWrapStyleWord(true);
		activations.setLineWrap(true);

		JTextField userActivations = new JTextField("Sigmoid,Tanh,Relu");
		userActivations.setFont(font);
		if (activationEditable) {
			userActivations.setEditable(true);
		} else {
			userActivations.setEditable(false);
		}
		if (userActivations.isEditable()) {
			userActivations.setBackground(Color.WHITE);
		} else {
			userActivations.setBackground(Color.GRAY);
		}
		jd.add(activations);
		jd.add(userActivations);

		JButton okay = new JButton("Okay");
		okay.setFont(font);
		NeuralNetErzeuger nne = new NeuralNetErzeuger(this, jd, userInputNeurons, userOutputNeurons, userHiddenNeurons,
				userActivations);
		okay.addActionListener(nne);
		JButton cancel = new JButton("Cancel");
		cancel.setFont(font);
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});
		jd.add(okay);
		jd.add(cancel);
		jd.setLocationRelativeTo(bp);
		jd.setModalityType(ModalityType.APPLICATION_MODAL);
		jd.setVisible(true);

		return nne.nn;
	}

	/**
	 * This method gets called when you have to specify the path of your new
	 * neural network.
	 * 
	 * @param title
	 *            dialog title
	 * @return loaded neural network and its training history
	 */
	public abstract NeuralNetTrainDataObject erzeugeNeuralNetJFileChooser(String title);

	/**
	 * Creates the unsaved changes j dialog
	 * 
	 * @param e
	 *            underlying event, so that the program can be continued there
	 *            after this dialog.
	 */
	public void erzeugeSaveDialog(ActionEvent e) {
		this.saveIntervened = e;
		JDialog meinJDialog = new JDialog();
		this.saveDialog = meinJDialog;
		meinJDialog.setTitle("Unsaved Changes");
		meinJDialog.setSize(new Dimension(500, 150));
		meinJDialog.setLayout(new BorderLayout());
		JPanel north = new JPanel();
		north.setLayout(new FlowLayout());
		JTextArea jtf = new JTextArea(
				"There are unsaved changes. Do you still want to continue? Unsaved progess will be lost.", 3, 3);
		jtf.setEditable(false);
		jtf.setBackground(north.getBackground());
		jtf.setPreferredSize(new Dimension(400, 50));
		jtf.setFont(new Font("Arial", Font.BOLD, 14));
		jtf.setWrapStyleWord(true);
		jtf.setLineWrap(true);

		north.add(jtf);
		north.setPreferredSize(new Dimension(500, 50));
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());

		JButton saveandcontinue = new JButton("Save and continue");
		this.continueandsave = saveandcontinue;
		saveandcontinue.addActionListener(this);

		JButton continuewithoutsaving = new JButton("Continue without saving");
		this.continuewithoutsaving = continuewithoutsaving;
		continuewithoutsaving.addActionListener(this);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				meinJDialog.dispose();

			}
		});

		south.add(saveandcontinue);
		south.add(continuewithoutsaving);
		south.add(cancel);

		south.setPreferredSize(new Dimension(500, 50));
		meinJDialog.add(north, BorderLayout.NORTH);
		meinJDialog.add(south, BorderLayout.SOUTH);

		meinJDialog.setLocationRelativeTo(bp);
		meinJDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		meinJDialog.setVisible(true);
	}
}

/**
 * This class checks the users inputs when creating a neural network.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
class NeuralNetErzeuger implements ActionListener {
	/**
	 * Button manger, so error messages can be created
	 */
	private ButtonManager bm;

	/**
	 * Underlying jdialog component
	 */
	private JDialog jd;

	/**
	 * Field that needs to be checked
	 */
	private JTextField userInputNeurons, userOutputNeurons, userHiddenNeurons, userActivations;

	/**
	 * NeuralNet that will be finished if all the inputs are correct
	 */
	NeuralNet nn;

	/**
	 * Initializes class fields
	 * 
	 * @param bm
	 *            {@link #bm}
	 * @param jd
	 *            {@link #jd}
	 * @param userInputNeurons
	 *            {@link #userInputNeurons}
	 * @param userOutputNeurons
	 *            {@link #userOutputNeurons}
	 * @param userHiddenNeurons
	 *            {@link #userHiddenNeurons}
	 * @param userActivations
	 *            {@link #userActivations}
	 */
	public NeuralNetErzeuger(ButtonManager bm, JDialog jd, JTextField userInputNeurons, JTextField userOutputNeurons,
			JTextField userHiddenNeurons, JTextField userActivations) {
		this.bm = bm;
		this.jd = jd;
		this.userInputNeurons = userInputNeurons;
		this.userOutputNeurons = userOutputNeurons;
		this.userHiddenNeurons = userHiddenNeurons;
		this.userActivations = userActivations;
	}

	/**
	 * This method listens to clicks on the okay button on the underlying
	 * jdialog component
	 * <p>
	 * When the okay button has been pressed, the fields get checked if they are
	 * filled out with the correct format. If not, error messages will be
	 * created.
	 * </p>
	 * 
	 * @param e
	 *            okay button was pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int fehler = 0;
		int inputs = -1;
		try {
			inputs = Integer.parseInt(userInputNeurons.getText());
			if (inputs < 0) {
				throw new RuntimeException();
			}
			userInputNeurons.setBackground(Color.GREEN);
		} catch (Exception excp) {
			fehler++;
			userInputNeurons.setBackground(Color.RED);
			bm.erzeugeError("Input Neurons value is not in the correct Format!", jd);
		}
		int outputs = -1;
		try {
			outputs = Integer.parseInt(userOutputNeurons.getText());
			if (outputs < 0) {
				throw new RuntimeException();
			}
			userOutputNeurons.setBackground(Color.GREEN);
		} catch (Exception excp) {
			fehler++;
			userOutputNeurons.setBackground(Color.RED);
			bm.erzeugeError("Output Neurons value is not in the correct format!", jd);
		}
		ArrayList<Integer> hiddens = new ArrayList<Integer>();
		try {
			String t = userHiddenNeurons.getText();
			String[] h = t.split(",");
			for (String s : h) {
				hiddens.add(Integer.parseInt(s));
				if (Integer.parseInt(s) < 0) {
					throw new RuntimeException();
				}
			}
			userHiddenNeurons.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			userHiddenNeurons.setBackground(Color.RED);
			bm.erzeugeError("Hidden Neurons values are not in the correct format!", jd);
		}
		ArrayList<Activator> activators = new ArrayList<Activator>();
		try {
			String t = userActivations.getText();
			String[] h = t.split(",");
			for (String s : h) {
				if (s.toLowerCase().equals("sigmoid")) {
					activators.add(new Sigmoid());
				} else if (s.toLowerCase().equals("tanh")) {
					activators.add(new TanH());
				} else if (s.toLowerCase().equals("relu")) {
					activators.add(new Relu());
				} else {
					throw new RuntimeException("format");
				}
			}
			if (activators.size() != hiddens.size() + 1) {
				throw new RuntimeException("amount");
			}
			userActivations.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			userActivations.setBackground(Color.RED);
			if (excep.getMessage().equals("amount")) {
				bm.erzeugeError("The amount of activation functions does not correspond to the amount of hidden layers",
						jd);
			} else if (excep.getMessage().equals("format")) {
				bm.erzeugeError("The activation functions are not in the correct format!", jd);
			}
		}
		if (fehler == 0) {
			this.nn = new NeuralNet(inputs, outputs, hiddens, activators);
			jd.dispose();
		}
	}
}

/**
 * This class checks the users inputs when editing hyper parameters.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
class HyperparameterErzeuger implements ActionListener {

	/**
	 * Button manger, so error messages can be created
	 */
	private ButtonManager bm;

	/**
	 * Underlying jdialog component
	 */
	private JDialog jd;

	/**
	 * Text field that needs to be checked.
	 */
	private JTextField userTrain_data, userTest_data, userMiniBatch_size, userEpochs, userCores, userLearning_rate;

	/**
	 * Hyperparameter object that will be finished if all the inputs are
	 * correct.
	 */
	Hyperparameter hp;

	/**
	 * Initializing class fields
	 * 
	 * @param jd
	 *            {@link #jd}
	 * @param bm
	 *            {@link #bm}
	 * @param userTrain_data
	 *            {@link #userTrain_data}
	 * @param userTest_data
	 *            {@link #userTest_data}
	 * @param userMiniBatch_size
	 *            {@link #userMiniBatch_size}
	 * @param userEpochs
	 *            {@link #userEpochs}
	 * @param userCores
	 *            {@link #userCores}
	 * @param userLearning_rate
	 *            {@link #userLearning_rate}
	 */
	public HyperparameterErzeuger(JDialog jd, ButtonManager bm, JTextField userTrain_data, JTextField userTest_data,
			JTextField userMiniBatch_size, JTextField userEpochs, JTextField userCores, JTextField userLearning_rate) {
		this.bm = bm;
		this.jd = jd;
		this.userTrain_data = userTrain_data;
		this.userTest_data = userTest_data;
		this.userMiniBatch_size = userMiniBatch_size;
		this.userEpochs = userEpochs;
		this.userCores = userCores;
		this.userLearning_rate = userLearning_rate;
	}

	/**
	 * This method listens to clicks on the okay button on the underlying
	 * jdialog component
	 * <p>
	 * When the okay button has been pressed, the fields get checked if they are
	 * filled out with the correct format. If not, error messages will be
	 * created.
	 * </p>
	 * 
	 * @param e
	 *            okay button was clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int fehler = 0;
		int train_data = -1;
		try {
			train_data = Integer.parseInt(this.userTrain_data.getText());
			if (train_data < 0) {
				throw new RuntimeException();
			}
			this.userTrain_data.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userTrain_data.setBackground(Color.RED);
			bm.erzeugeError("Train data was not given in the correct format!", jd);
		}
		int test_data = -1;
		try {
			test_data = Integer.parseInt(this.userTest_data.getText());
			if (test_data < 0) {
				throw new RuntimeException();
			}
			this.userTest_data.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userTest_data.setBackground(Color.RED);
			this.bm.erzeugeError("Test data was not given in the correct format!", jd);
		}
		int batch_size = -1;
		try {
			batch_size = Integer.parseInt(this.userMiniBatch_size.getText());
			if (batch_size < 0 || batch_size > train_data) {
				throw new RuntimeException();
			}
			this.userMiniBatch_size.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userMiniBatch_size.setBackground(Color.RED);
			this.bm.erzeugeError("Mini batch size was not given in the correct format!", jd);
		}
		int epochs = -1;
		try {
			epochs = Integer.parseInt(this.userEpochs.getText());
			if (epochs < 0) {
				throw new RuntimeException();
			}
			this.userEpochs.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userEpochs.setBackground(Color.RED);
			this.bm.erzeugeError("Amount of epochs was not given in the correct format!", jd);
		}
		int cores = -1;
		try {
			cores = Integer.parseInt(this.userCores.getText());
			if (cores < 0 || cores > Runtime.getRuntime().availableProcessors()) {
				throw new RuntimeException();
			}
			this.userCores.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userCores.setBackground(Color.RED);
			this.bm.erzeugeError("Amount of cores do not correspond to amount of available cores!", jd);
		}
		double learning_rate = -1;
		try {
			learning_rate = Double.parseDouble(this.userLearning_rate.getText());
			if (learning_rate < 0) {
				throw new RuntimeException();
			}
			this.userLearning_rate.setBackground(Color.GREEN);
		} catch (Exception excep) {
			fehler++;
			this.userLearning_rate.setBackground(Color.RED);
			this.bm.erzeugeError("Learning rate was not given in the correct format!", jd);
		}
		if (fehler == 0) {
			this.hp = new Hyperparameter(train_data, test_data, batch_size, epochs, cores, learning_rate);
			jd.dispose();
		}

	}

}