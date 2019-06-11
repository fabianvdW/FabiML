package gui.mode;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.GraphPanel;
import gui.buttonmode.panelmode.ButtonPanel;
import gui.buttonmode.panelmode.VierBitXorPanel;
import gui.monitorpanelmode.MonitorPanel;
import gui.monitorpanelmode.VierBitXorMonitorPanel;

/**
 * This class defines the appearance of the GUI when training 4-Bit XOR
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class VierBitXorGUI extends ModeGUI {

	/**
	 * Graph Panel showing the cost over the epochs.
	 */
	public GraphPanel gp;

	/**
	 * 4-Bit XOR Button Panel
	 */
	public ButtonPanel bp;

	/**
	 * 4-BIT XOR Monitor Panel
	 */
	public MonitorPanel mp;

	/**
	 * Tabbed center pane
	 */
	public JTabbedPane tabbedCenter;

	/**
	 * Initializes the GUI. The GUi is structures as follows:
	 * <p>
	 * The button panel is always on the right side taking 20% of the space. In
	 * the center there is a tabbed pane, containing two tabs. The first tab
	 * contains one graph panel showing the cost over the epochs. The second tab
	 * contains the monitor panel.
	 * </p>
	 * 
	 * @param center_width
	 *            width of center pane
	 * @param center_height
	 *            height of center pane
	 * @param east_width
	 *            width of button panel
	 * @param east_height
	 *            height of center panel
	 */
	public VierBitXorGUI(int center_width, int center_height, int east_width, int east_height) {
		super(center_width, center_height, east_width, east_height);
		this.gp = new GraphPanel(0, center_width, center_height - TAB_HEIGHT, 0, 5000, 1, "Epochs", "Cost", true);
		this.mp = new VierBitXorMonitorPanel(center_width, center_height - TAB_HEIGHT);
		this.mp.addGraphPanel(gp);
		this.bp = new VierBitXorPanel(east_width, east_height, this);
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
	 * Updates the graph panel
	 * 
	 * @param data
	 *            new data
	 */
	public void updateData(HashMap<String, HashMap<Double, Double>> data) {
		this.gp.setData(data.get("Epochs"));
	}

}
