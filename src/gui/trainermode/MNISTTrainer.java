package gui.trainermode;

import java.util.HashMap;

import gui.buttonmode.managermode.ButtonManager;
import gui.nndomode.MNISTNNDO;
import gui.nndomode.NeuralNetTrainDataObject;
import matrix.Matrix;

public class MNISTTrainer extends Trainer {

	public MNISTTrainer(ButtonManager bp, NeuralNetTrainDataObject nndo) {
		super(bp, nndo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (this.nndo.epoch < this.endEpoch) {
			long start = System.currentTimeMillis();
			if (bp.pausedTraining) {
				long end = System.currentTimeMillis();
				this.train_time += end - start;
				this.nndo.overall_train_time += end - start;
				return;
			}
			if (!bp.inTraining) {
				long end = System.currentTimeMillis();
				this.train_time += end - start;
				this.nndo.overall_train_time += end - start;
				return;
			}
			// this.nndo.nn.SGD(train_inputs, train_labels, test_inputs,
			// test_labels, batch_size, epochs, test_every, learning_rate,
			// anzahl_cores, ausgabe);
			this.nndo.nn.SGD(nndo.mdl.trainData, nndo.mdl.trainLables, nndo.mdl.testData, nndo.mdl.testLabels,
					nndo.hp.batch_size, 1, 2, nndo.hp.learning_rate, nndo.hp.cores, false);
			double fehler = this.nndo.nn.evaluate(nndo.mdl.testData, nndo.mdl.testLabels);
			if (fehler < this.nndo.best_cost) {
				this.nndo.best_cost = fehler;
				this.nndo.best_epoch = this.nndo.epoch;
			}
			HashMap<Double, Double> bisherigeDaten = this.nndo.data.get("Epochs");
			bisherigeDaten.put(this.nndo.epoch + 0.0, fehler);
			HashMap<Double, Double> accuracy = null;

			accuracy = this.nndo.data.get("Accuracy");
			// Measure Accuarcy
			MNISTNNDO mnndo= (MNISTNNDO)this.nndo;
			int insg = this.nndo.mdl.testData.hoehe;
			mnndo.lastTestData= new Matrix[insg];
			mnndo.lastTestDataCorrect= new boolean[insg];
			int correct = 0;
			for (int j = 0; j < insg; j++) {
				Matrix res = this.nndo.nn.feedForward(this.nndo.mdl.testData.getMatrix()[j]);
				mnndo.lastTestData[j]=res;
				double max = -1;
				int mI = -1;
				for (int m = 0; m < 10; m++) {
					double vergleich = res.getMatrix()[m][0];
					if (vergleich > max) {
						max = vergleich;
						mI = m;
					}
				}
				if (this.nndo.mdl.testLabels.getMatrix()[j][mI] == 1) {
					correct++;
					mnndo.lastTestDataCorrect[j]=true;
				}else{
					mnndo.lastTestDataCorrect[j]=false;
				}
			}
			double acc = (correct + 0.0) / insg;
			accuracy.put(this.nndo.epoch + 0.0, acc);

			HashMap<String, HashMap<Double, Double>> copyData = new HashMap<String, HashMap<Double, Double>>();
			copyData.put("Epochs", (HashMap<Double, Double>) bisherigeDaten.clone());
			this.nndo.data.put("Epochs", bisherigeDaten);
			copyData.put("Accuracy", (HashMap<Double, Double>) accuracy.clone());
			this.nndo.data.put("Accuracy", accuracy);
			this.nndo.epoch += 1;
			this.bp.updateEpoch(copyData);
			this.bp.unSaved();
			long end = System.currentTimeMillis();
			this.train_time += end - start;
			this.nndo.overall_train_time += end - start;

		}
		this.running = false;
		this.bp.finishedTrainingEvent();

	}

}
