package gui.trainermode;

import gui.buttonmode.managermode.ButtonManager;
import gui.nndomode.NeuralNetTrainDataObject;

public abstract class Trainer implements Runnable {

	public boolean running;
	public ButtonManager bp;
	public NeuralNetTrainDataObject nndo;
	public Thread t;
	public int endEpoch;
	public int insgesamtEpochs;
	public long train_time;

	public Trainer(ButtonManager bp, NeuralNetTrainDataObject nndo) {
		this.bp = bp;
		this.nndo = nndo;
		this.endEpoch = this.nndo.hp.epochs + this.nndo.epoch;
		this.insgesamtEpochs = this.nndo.hp.epochs;
		this.start();
	}

	public void start() {
		this.t = new Thread(this, "train");
		t.start();
	}

	@Override
	public abstract void run();

}