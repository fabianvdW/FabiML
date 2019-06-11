package gui.mode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gui.GraphPanel;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.buttonmode.panelmode.MNISTPanel;
import gui.monitorpanelmode.MNISTMonitorPanel;
import gui.monitorpanelmode.MonitorPanel;

/**
 * This class defines the appearance of the GUI when training the MNIST dataset
 * for handwritten digits
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class MNISTGUI extends ModeGUI {

	/**
	 * GraphPanel showing the cost over the epochs
	 */
	public GraphPanel epochs;

	/**
	 * GraphPanel showing the classification rate over the epochs
	 */
	public GraphPanel classified;

	/**
	 * MNIST button panel
	 */
	public ButtonPanel bp;

	/**
	 * MNIST MonitorPanel
	 */
	public MonitorPanel mp;

	/**
	 * TabbedCenter Pane
	 */
	public JTabbedPane tabbedCenter;

	/**
	 * Initializes the GUI. The GUI is structures as follows:
	 * <p>
	 * The button panel is on the right and takes 20% of the width. In the
	 * center there is a tabbed center pane with two tabs. The first tab shows
	 * two graph panels, from which one is showing the cost over the epochs
	 * while the other is showing the classification rate over the epochs. The
	 * second tab contains the monitor panel which shows the graph panel showing
	 * the classification rate.
	 * </p>
	 * 
	 * @param center_width
	 *            width of center pane
	 * @param center_height
	 *            height of center pane
	 * @param east_width
	 *            width of button panel
	 * @param east_height
	 *            height of button panel
	 */
	public MNISTGUI(int center_width, int center_height, int east_width, int east_height) {
		super(center_width, center_height, east_width, east_height);
		this.tabbedCenter = new JTabbedPane();
		JPanel graphPanelContainer = new JPanel();
		graphPanelContainer.setLayout(new BorderLayout());
		this.epochs = new GraphPanel(0, center_width / 2, center_height - TAB_HEIGHT, 0, 5000, 10, "Epochs", "Cost",
				true);
		this.classified = new GraphPanel(0, center_width / 2, center_height - TAB_HEIGHT, 0, 5000, 1, "Epochs",
				"Accuracy", true);
		this.epochs.setPreferredSize(new Dimension(center_width / 2, center_height));
		this.classified.setPreferredSize(new Dimension(center_width / 2, center_height));
		graphPanelContainer.add(this.epochs, BorderLayout.WEST);
		graphPanelContainer.add(this.classified, BorderLayout.EAST);
		this.tabbedCenter.add("GraphPanel", graphPanelContainer);
		this.mp = new MNISTMonitorPanel(center_width, center_height - TAB_HEIGHT);
		this.mp.addGraphPanel(this.classified);
		this.tabbedCenter.add("MonitorPanel", mp);
		this.centerPane = tabbedCenter;
		this.centerPane.setPreferredSize(new Dimension(center_width, center_height));
		this.bp = new MNISTPanel(east_width, east_height, this);
		this.eastPane = bp;
		this.eastPane.setPreferredSize(new Dimension(east_width, east_height));
	}

	/**
	 * Updates data in both of the graph panels
	 * 
	 * @param data
	 *            new data
	 */
	@Override
	public void updateData(HashMap<String, HashMap<Double, Double>> data) {
		this.epochs.setData(data.get("Epochs"));
		this.classified.setData(data.get("Accuracy"));
	}

}
