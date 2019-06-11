package gui.mode;

import java.awt.Component;
import java.util.HashMap;

import gui.buttonmode.managermode.ButtonManager;
import gui.buttonmode.panelmode.ButtonPanel;

/**
 * This class builds the GUI for specific training cases.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public abstract class ModeGUI {
	/**
	 * Height of the TABBEDPANE
	 */
	final static int TAB_HEIGHT = 20;

	/**
	 * CenterPane component, this can be a normal jpanel or a tabbed panel
	 */
	public Component centerPane;

	/**
	 * The eastPane is always the buttonpanel
	 */
	public ButtonPanel eastPane;

	/**
	 * Width of the center component
	 */
	int center_width;

	/**
	 * Height of the center component
	 */
	int center_height;

	/**
	 * Width of the button panel
	 */
	int east_width;

	/**
	 * Height of the button panel
	 */
	int east_height;

	/**
	 * Initializes class fields
	 * 
	 * @param center_width
	 *            width of the center component
	 * @param center_height
	 *            height of the center component
	 * @param east_width
	 *            width of the button panel
	 * @param east_height
	 *            height of the button panel
	 */
	public ModeGUI(int center_width, int center_height, int east_width, int east_height) {
		this.center_width = center_width;
		this.center_height = center_height;
		this.east_width = east_width;
		this.east_height = east_height;
	}

	/**
	 * 
	 * @return the button manager of the button panel (east pane)
	 */
	public ButtonManager getManager() {
		return this.eastPane.buttonManager;
	}

	/**
	 * Updates the data of the graph panel
	 * 
	 * @param data
	 *            new data
	 */
	public abstract void updateData(HashMap<String, HashMap<Double, Double>> data);
}
