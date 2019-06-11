package gui.buttonmode.panelmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gui.buttonmode.managermode.ButtonManager;
import gui.mode.ModeGUI;

/**
 * This class creates the ButtonPanel which can be found on the right side of
 * the GUI. It does not however, register and react to button clicks. This is
 * managed by the ButtonManager.
 * 
 * @see ButtonManager
 * @author Fabian von der Warth
 * @version 1.0
 *
 */
public abstract class ButtonPanel extends JPanel {

	/**
	 * Underlying ModeGUI which is put in the JFrame
	 */
	public ModeGUI modegui;

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Horizontal Gap between buttons on the button panel
	 * <p>
	 * Note that this doesn't have any effect at all.
	 * </p>
	 */
	public static final int HGAP = 5;

	/**
	 * Vertical Gap between buttons on the button panel
	 */
	public static final int VGAP = 20;

	/**
	 * JTable which is shown when clicking on visualize testdata
	 */
	public JTable t;

	/**
	 * This JPanel exists so that the buttons get shown slightly more centered.
	 */
	public JPanel west, northWest, middleWest, southWest, east, northEast, middleEast, southEast;

	/**
	 * JButton for creating a NeuralNet
	 * <p>
	 * When this button gets clicked, a prompt will open that will guide you
	 * through creating a new neural network.
	 * </p>
	 */
	public JButton createNeuralNet;

	/**
	 * JButton for creating a NeuralNet
	 * <p>
	 * When this button gets clicked, the training will start with current
	 * training settings. The training settings can be found by clicking on edit
	 * hyperparameters
	 * </p>
	 */
	public JButton startTraining;

	/**
	 * JButton for continuing a Training
	 * <p>
	 * This Button can only be clicked when training is paused. When clicked,
	 * the training will continue.
	 * </p>
	 */
	public JButton continueTraining;

	/**
	 * JButton for saving the NeuralNet
	 * <p>
	 * This button can be clicked to save the NeuralNet. Note that this button
	 * can not be clicked when there are no unsaved changes and that saving the
	 * neural net while training can lead to errors.
	 * </p>
	 */
	public JButton saveNeuralNet;

	/**
	 * JButton for seeing test data
	 * <p>
	 * When this button gets clicked, a window will open where one can see all
	 * the testdata labels and predictions of the current neural network.
	 * </p>
	 */
	public JButton seetestdata;

	/**
	 * JButton for editing hyper parameters
	 * <p>
	 * When this button gets clicked, a window will open where hyper parameters
	 * can be changed.
	 * </p>
	 */
	public JButton configureHyperParameters;

	/**
	 * JButton for stopping the training.
	 * <p>
	 * This button can only be clicked when there is training going on. When
	 * this button gets clicked, the training will stop after finishing its
	 * current epoch.
	 * </p>
	 */
	public JButton stopTraining;

	/**
	 * JButton to pause training
	 * <p>
	 * This button, when clicked, will pause the current training. Note that
	 * this button can only get clicked when there is training going on that is
	 * not paused yet.
	 * </p>
	 */
	public JButton pauseTraining;

	/**
	 * JButton to load a NeuralNet from a file.
	 * <p>
	 * Note that this button can only get clicked when there is no training
	 * going on. When this button gets clicked, a window will open where the
	 * file that should be loaded can be selected.
	 * </p>
	 */
	public JButton loadNeuralNet;

	/**
	 * Class who listens for all clicks on any buttons.
	 */
	public ButtonManager buttonManager;

	/**
	 * Width of the panel
	 */
	public int panelWidth;
	
	/**
	 * Height of the panel
	 */
	public int panelHeight;

	/**
	 * Dimension of one button
	 */
	Dimension buttonDimension;

	/**
	 * Initializes the panel
	 * 
	 * @param width
	 *            width of panel
	 * @param height
	 *            height of panel
	 * @param mg
	 *            underlying modegui
	 */
	public ButtonPanel(int width, int height, ModeGUI mg) {
		this.modegui = mg;
		this.panelWidth = width;
		this.panelHeight = height;
		int buttonMaxWidth = width / 2;
		int buttonMaxHeight = buttonMaxWidth / 3;
		this.buttonDimension = new Dimension(buttonMaxWidth * 4 / 5, buttonMaxHeight);
		this.setLayout(new BorderLayout());
		// West
		this.west = new JPanel();
		this.west.setLayout(new BorderLayout());
		this.northWest = new JPanel();
		this.northWest.setBackground(new Color(217, 217, 217));
		this.middleWest = new JPanel();
		this.middleWest.setBackground(new Color(217, 217, 217));
		this.middleWest.setLayout(new FlowLayout(FlowLayout.CENTER, HGAP, VGAP));
		this.southWest = new JPanel();
		this.southWest.setBackground(new Color(217, 217, 217));

		this.createNeuralNet = new JButton("Create Neural Net");
		this.startTraining = new JButton("Start Training");
		this.continueTraining = new JButton("Continue Training");
		this.saveNeuralNet = new JButton("Save Neural Net");
		this.seetestdata = new JButton("Testdata");

		this.createNeuralNet.setPreferredSize(buttonDimension);
		this.createNeuralNet.setEnabled(true);
		this.middleWest.add(this.createNeuralNet);

		this.startTraining.setPreferredSize(buttonDimension);
		this.startTraining.setEnabled(false);
		this.middleWest.add(this.startTraining);

		this.continueTraining.setPreferredSize(buttonDimension);
		this.continueTraining.setEnabled(false);
		this.middleWest.add(this.continueTraining);

		this.saveNeuralNet.setPreferredSize(buttonDimension);
		this.saveNeuralNet.setEnabled(false);
		this.middleWest.add(this.saveNeuralNet);

		this.seetestdata.setPreferredSize(buttonDimension);
		this.seetestdata.setEnabled(false);
		this.middleWest.add(this.seetestdata);

		this.northWest.setPreferredSize(new Dimension(width / 2, height / 10));
		this.middleWest.setPreferredSize(new Dimension(width / 2, 8 * height / 10));
		this.southWest.setPreferredSize(new Dimension(width / 2, height / 10));
		this.west.add(this.northWest, BorderLayout.NORTH);
		this.west.add(this.middleWest, BorderLayout.CENTER);
		this.west.add(this.southWest, BorderLayout.SOUTH);

		// East
		this.east = new JPanel();
		this.east.setLayout(new BorderLayout());
		this.northEast = new JPanel();
		this.northEast.setBackground(new Color(217, 217, 217));
		this.middleEast = new JPanel();
		this.middleEast.setBackground(new Color(217, 217, 217));
		this.middleEast.setLayout(new FlowLayout(FlowLayout.CENTER, HGAP, VGAP));
		this.southEast = new JPanel();
		this.southEast.setBackground(new Color(217, 217, 217));

		this.configureHyperParameters = new JButton("Change Hyperparams");
		this.stopTraining = new JButton("Stop Training");
		this.pauseTraining = new JButton("Pause Training");
		this.loadNeuralNet = new JButton("Load Neural Net");

		this.configureHyperParameters.setPreferredSize(buttonDimension);
		this.configureHyperParameters.setEnabled(false);
		this.middleEast.add(this.configureHyperParameters);

		this.stopTraining.setPreferredSize(buttonDimension);
		this.stopTraining.setEnabled(false);
		this.middleEast.add(this.stopTraining);

		this.pauseTraining.setPreferredSize(buttonDimension);
		this.pauseTraining.setEnabled(false);
		this.middleEast.add(this.pauseTraining);

		this.loadNeuralNet.setPreferredSize(buttonDimension);
		this.loadNeuralNet.setEnabled(true);
		this.middleEast.add(this.loadNeuralNet);

		this.northEast.setPreferredSize(new Dimension(width / 2, height / 10));
		this.middleEast.setPreferredSize(new Dimension(width / 2, 8 * height / 10));
		this.southEast.setPreferredSize(new Dimension(width / 2, height / 10));
		this.east.add(this.northEast, BorderLayout.NORTH);
		this.east.add(this.middleEast, BorderLayout.CENTER);
		this.east.add(this.southEast, BorderLayout.SOUTH);
		this.west.setPreferredSize(new Dimension(width / 2, height));
		this.east.setPreferredSize(new Dimension(width / 2, height));
		this.add(this.west, BorderLayout.WEST);
		this.add(this.east, BorderLayout.EAST);
	}

	/**
	 * Adds the buttonmanager as actionListener to the buttons.
	 */
	public void addActionListeners() {
		this.createNeuralNet.addActionListener(buttonManager);
		this.startTraining.addActionListener(buttonManager);
		this.continueTraining.addActionListener(buttonManager);
		this.saveNeuralNet.addActionListener(buttonManager);
		this.seetestdata.addActionListener(buttonManager);
		this.configureHyperParameters.addActionListener(buttonManager);
		this.stopTraining.addActionListener(buttonManager);
		this.pauseTraining.addActionListener(buttonManager);
		this.loadNeuralNet.addActionListener(buttonManager);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * Shows the table of test data
	 * 
	 * @param labels
	 *            label array of the test data
	 * @param data
	 *            input array of the test data
	 * @param title
	 *            title of jframe
	 */
	public void showTestTable(String[] labels, String[][] data, String title) {
		int cellHeight = 20;
		if (this.t != null)
			if (labels.length != data[0].length) {
				throw new RuntimeException();
			}
		this.t = new JTable(data, labels) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int r, int c) {
				if (r >= data.length && (c == 0 || c == 2)) {
					return true;
				} else {
					return false;
				}
			}
		};
		this.t.setRowHeight(cellHeight);
		JFrame tableContainer = new JFrame();
		tableContainer.setLocationRelativeTo(this.modegui.centerPane);
		tableContainer.setTitle(title);
		tableContainer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tableContainer.setSize(new Dimension(this.panelWidth / 2, this.panelHeight / 2));
		tableContainer.getContentPane().add(new JScrollPane(this.t), BorderLayout.CENTER);
		tableContainer.pack();
		tableContainer.setVisible(true);

	}
}
