package gui.monitorpanelmode;

import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;

public class XORMonitorPanel extends MonitorPanel {

	private static final long serialVersionUID = 1L;

	public XORMonitorPanel(int width, int height) {
		super(width, height);
	}

	@Override
	public void update(NeuralNetTrainDataObject nndo, Trainer tr) {
		this.umode.setText("XOR");
		super.update(nndo, tr);
	}
}
