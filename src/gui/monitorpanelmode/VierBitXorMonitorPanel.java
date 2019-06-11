package gui.monitorpanelmode;

import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;

public class VierBitXorMonitorPanel extends MonitorPanel {

	private static final long serialVersionUID = 1L;

	public VierBitXorMonitorPanel(int width, int height) {
		super(width, height);
	}

	@Override
	public void update(NeuralNetTrainDataObject nndo, Trainer tr) {
		this.umode.setText("4BitXOR");
		super.update(nndo, tr);
	}
}
