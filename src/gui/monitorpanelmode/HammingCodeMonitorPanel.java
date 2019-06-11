package gui.monitorpanelmode;

import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;

public class HammingCodeMonitorPanel extends MonitorPanel {

	private static final long serialVersionUID = 1L;

	public HammingCodeMonitorPanel(int width, int height) {
		super(width, height);
	}

	@Override
	public void update(NeuralNetTrainDataObject nndo, Trainer tr) {
		this.umode.setText("HammingCode");
		super.update(nndo, tr);
	}
}
