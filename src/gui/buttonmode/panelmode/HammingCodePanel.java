package gui.buttonmode.panelmode;

import gui.buttonmode.managermode.HammingCodeManager;
import gui.mode.ModeGUI;

/**
 * This class expands the ButtonPanel to the specific case of training the
 * HammingCode
 * <p>
 * This class does not provide any extras in this version.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class HammingCodePanel extends ButtonPanel {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializing class fields
	 * 
	 * @param width
	 *            width of panel
	 * @param height
	 *            height of panel
	 * @param mg
	 *            underlying modegui
	 */
	public HammingCodePanel(int width, int height, ModeGUI mg) {
		super(width, height, mg);
		this.buttonManager = new HammingCodeManager(this);
		this.addActionListeners();
	}

}
