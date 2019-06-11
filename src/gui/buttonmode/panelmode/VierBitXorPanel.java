package gui.buttonmode.panelmode;

import gui.buttonmode.managermode.VierBitXorManager;
import gui.mode.ModeGUI;

/**
 * This class expands the ButtonPanel to the specific case of training 4-Bit XOR
 * <p>
 * This class does not provide any extras in this version.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class VierBitXorPanel extends ButtonPanel {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Sets up panel
	 * @param width width of panel
	 * @param height height of panel
	 * @param mg underlying modegui
	 */
	public VierBitXorPanel(int width, int height, ModeGUI mg) {
		super(width, height, mg);
		this.buttonManager = new VierBitXorManager(this);
		this.addActionListeners();
	}

}
