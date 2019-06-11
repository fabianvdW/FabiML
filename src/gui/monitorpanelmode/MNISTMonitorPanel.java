package gui.monitorpanelmode;

import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;

public class MNISTMonitorPanel extends MonitorPanel {

	private static final long serialVersionUID = 1L;

	public MNISTMonitorPanel(int width, int height) {
		super(width, height);
	}

	@Override
	public void update(NeuralNetTrainDataObject nndo, Trainer tr) {
		this.umode.setText("MNIST");
		super.update(nndo, tr);
	}

}
