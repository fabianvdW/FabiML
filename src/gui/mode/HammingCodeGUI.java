package gui.mode;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.GraphPanel;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.buttonmode.panelmode.HammingCodePanel;
import gui.monitorpanelmode.HammingCodeMonitorPanel;
import gui.monitorpanelmode.MonitorPanel;

/**
 * This class defines the appearance of the GUI when training HammingCode
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class HammingCodeGUI extends ModeGUI {

	/**
	 * GraphPanel showing the cost over the epochs.
	 */
	public GraphPanel gp;

	/**
	 * ButtonPanel of GUI
	 */
	public ButtonPanel bp;

	/**
	 * MonitorPanel of GUI
	 */
	public MonitorPanel mp;

	/**
	 * Tabbed Center of GUI
	 */
	public JTabbedPane tabbedCenter;

	/**
	 * Initializes the GUI. The GUI is structured as follows:
	 * <p>
	 * The button panel is always on the right side and has 20% of the width.
	 * The center is a tabbed pane with two tabs, one for the monitor panel and
	 * one for one graph panel showing the average cost of the neural network.
	 * </p>
	 * 
	 * @param center_width
	 *            width of the center
	 * @param center_height
	 *            height of the center
	 * @param east_width
	 *            width of the button panel
	 * @param east_height
	 *            height of the button panel
	 */
	public HammingCodeGUI(int center_width, int center_height, int east_width, int east_height) {
		super(center_width, center_height, east_width, east_height);
		this.gp = new GraphPanel(0, center_width, center_height - TAB_HEIGHT, 0, 5000, 4, "Epochs", "Cost", true);
		this.mp = new HammingCodeMonitorPanel(center_width, center_height - TAB_HEIGHT);
		this.mp.addGraphPanel(gp);
		this.bp = new HammingCodePanel(east_width, east_height, this);
		this.tabbedCenter = new JTabbedPane();
		tabbedCenter.addTab("GraphPanel", gp);
		tabbedCenter.addTab("MonitorPanel", mp);
		tabbedCenter.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mp.update(bp.buttonManager.nn, bp.buttonManager.currentTraining);

			}
		});
		this.centerPane = tabbedCenter;
		this.centerPane.setPreferredSize(new Dimension(center_width, center_height));
		this.eastPane = bp;
		this.eastPane.setPreferredSize(new Dimension(east_width, east_height));
	}

	/**
	 * Updates the GraphPanel
	 * 
	 * @param data
	 *            new data
	 */
	public void updateData(HashMap<String, HashMap<Double, Double>> data) {
		this.gp.setData(data.get("Epochs"));
	}

}
