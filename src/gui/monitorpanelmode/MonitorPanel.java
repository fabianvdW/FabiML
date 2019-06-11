package gui.monitorpanelmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gui.GraphPanel;
import gui.nndomode.NeuralNetTrainDataObject;
import gui.trainermode.Trainer;

public abstract class MonitorPanel extends JPanel {

	public static final   Font PANEL_FONT = new Font("ARIAL",Font.BOLD,14);
	private static final long serialVersionUID = 1L;
	final int LEFT_MAX_panelHeight = 300;
	final int RIGHT_MAX_panelHeight = 900;
	final int X_BIAS = 100;
	NeuralNetTrainDataObject nndo;
	Trainer tr;
	JPanel left, right;
	JPanel leftContainer, rightContainer;
	JPanel meContainer;
	int panelWidth;
	int panelHeight;
	JTextArea train_time, utrain_time, eta, ueta, epochs_left, uepochs_left;
	JTextArea mode, umode, nnaufbau, unnaufbau, activationfunction, uactivationfunction, overalltraintime,
			uoveralltraintime, learnrate, ulearnrate, minibatchsize, uminibatchsize, nnepochs, unnepochs, cost, ucost,
			bestcost, ubestcost;

	public MonitorPanel(int panelWidth, int panelHeight) {
		this.panelWidth = panelWidth - X_BIAS;
		this.meContainer = new JPanel();
		this.meContainer.setPreferredSize(new Dimension(panelWidth, this.panelHeight - 100));
		this.meContainer.setLayout(new BorderLayout());
		this.panelHeight = panelHeight;
		this.init();
	}

	public void addGraphPanel(GraphPanel gp) {
		// GraphPanel gp2= new GraphPanel(x0, x1, y0, y1, X_MAX, Y_MAX, X_AXIS,
		// Y_AXIS, connect)
		GraphPanel gp2 = new GraphPanel(0, this.panelWidth / 2, this.panelHeight / 2, 0, gp.X_MAX, gp.Y_MAX, gp.X_AXIS, gp.Y_AXIS,
				true);
		gp2.setPreferredSize(new Dimension(this.panelWidth / 2, this.panelHeight / 2));
		this.leftContainer.add(gp2);
		gp.addChild(gp2);
	}

	private void init() {

		this.setLayout(new BorderLayout());
		// LeftAbteil
		this.leftContainer = new JPanel();
		this.leftContainer.setPreferredSize(new Dimension((int) (this.panelWidth / 2), this.panelHeight));
		this.leftContainer.setLayout(new FlowLayout());
		this.left = new JPanel();
		if (this.panelHeight > LEFT_MAX_panelHeight) {
			this.left.setPreferredSize(new Dimension(this.panelWidth / 2, LEFT_MAX_panelHeight));
		} else {
			this.left.setPreferredSize(new Dimension(this.panelWidth / 2, this.panelHeight));
		}
		this.left.setLayout(new GridLayout(0, 2, 10, 10));
		this.rightContainer = new JPanel();
		this.rightContainer.setPreferredSize(new Dimension((int) (this.panelWidth / 2), this.panelHeight));
		this.right = new JPanel();
		if (this.panelHeight > RIGHT_MAX_panelHeight) {
			this.right.setPreferredSize(new Dimension(this.panelWidth / 2, RIGHT_MAX_panelHeight));
		} else {
			this.right.setPreferredSize(new Dimension(this.panelWidth / 2, this.panelHeight));
		}
		this.right.setLayout(new GridLayout(0, 2, 10, 10));

		// LEFT
		this.train_time = new JTextArea("Time spent training:");
		this.train_time.setFont(MonitorPanel.PANEL_FONT);
		this.train_time.setEditable(false);
		this.train_time.setBackground(this.left.getBackground());
		this.train_time.setWrapStyleWord(true);
		this.train_time.setLineWrap(true);

		this.utrain_time = new JTextArea();
		this.utrain_time.setFont(MonitorPanel.PANEL_FONT);
		this.utrain_time.setEditable(false);
		this.utrain_time.setBackground(Color.WHITE);
		this.utrain_time.setWrapStyleWord(true);
		this.utrain_time.setLineWrap(true);

		this.eta = new JTextArea("Estimated time training will finish in:");
		this.eta.setFont(MonitorPanel.PANEL_FONT);
		this.eta.setEditable(false);
		this.eta.setBackground(this.left.getBackground());
		this.eta.setWrapStyleWord(true);
		this.eta.setLineWrap(true);

		this.ueta = new JTextArea();
		this.ueta.setFont(MonitorPanel.PANEL_FONT);
		this.ueta.setEditable(false);
		this.ueta.setBackground(Color.WHITE);
		this.ueta.setWrapStyleWord(true);
		this.ueta.setLineWrap(true);

		this.epochs_left = new JTextArea("Epochs left in training:");
		this.epochs_left.setFont(MonitorPanel.PANEL_FONT);
		this.epochs_left.setEditable(false);
		this.epochs_left.setBackground(this.left.getBackground());
		this.epochs_left.setWrapStyleWord(true);
		this.epochs_left.setLineWrap(true);

		this.uepochs_left = new JTextArea();
		this.uepochs_left.setFont(MonitorPanel.PANEL_FONT);
		this.uepochs_left.setEditable(false);
		this.uepochs_left.setBackground(Color.WHITE);
		this.uepochs_left.setWrapStyleWord(true);
		this.uepochs_left.setLineWrap(true);

		this.left.add(this.train_time);
		this.left.add(this.utrain_time);
		this.left.add(this.eta);
		this.left.add(this.ueta);
		this.left.add(this.epochs_left);
		this.left.add(this.uepochs_left);

		this.mode = new JTextArea("Mode:");
		this.mode.setFont(MonitorPanel.PANEL_FONT);
		this.mode.setEditable(false);
		this.mode.setBackground(this.left.getBackground());
		this.mode.setWrapStyleWord(true);
		this.mode.setLineWrap(true);

		this.umode = new JTextArea();
		this.umode.setFont(MonitorPanel.PANEL_FONT);
		this.umode.setEditable(false);
		this.umode.setBackground(Color.WHITE);
		this.umode.setWrapStyleWord(true);
		this.umode.setLineWrap(true);

		this.nnaufbau = new JTextArea("Layer-Structure:");
		this.nnaufbau.setFont(MonitorPanel.PANEL_FONT);
		this.nnaufbau.setEditable(false);
		this.nnaufbau.setBackground(this.left.getBackground());
		this.nnaufbau.setWrapStyleWord(true);
		this.nnaufbau.setLineWrap(true);

		this.unnaufbau = new JTextArea();
		this.unnaufbau.setFont(MonitorPanel.PANEL_FONT);
		this.unnaufbau.setEditable(false);
		this.unnaufbau.setBackground(Color.WHITE);
		this.unnaufbau.setWrapStyleWord(true);
		this.unnaufbau.setLineWrap(true);

		this.activationfunction = new JTextArea("Activationfunctions:");
		this.activationfunction.setFont(MonitorPanel.PANEL_FONT);
		this.activationfunction.setEditable(false);
		this.activationfunction.setBackground(this.left.getBackground());
		this.activationfunction.setWrapStyleWord(true);
		this.activationfunction.setLineWrap(true);

		this.uactivationfunction = new JTextArea();
		this.uactivationfunction.setFont(MonitorPanel.PANEL_FONT);
		this.uactivationfunction.setEditable(false);
		this.uactivationfunction.setBackground(Color.WHITE);
		this.uactivationfunction.setWrapStyleWord(true);
		this.uactivationfunction.setLineWrap(true);

		this.overalltraintime = new JTextArea("Overall training time:");
		this.overalltraintime.setFont(MonitorPanel.PANEL_FONT);
		this.overalltraintime.setEditable(false);
		this.overalltraintime.setBackground(this.left.getBackground());
		this.overalltraintime.setWrapStyleWord(true);
		this.overalltraintime.setLineWrap(true);

		this.uoveralltraintime = new JTextArea();
		this.uoveralltraintime.setFont(MonitorPanel.PANEL_FONT);
		this.uoveralltraintime.setEditable(false);
		this.uoveralltraintime.setBackground(Color.WHITE);
		this.uoveralltraintime.setWrapStyleWord(true);
		this.uoveralltraintime.setLineWrap(true);

		this.learnrate = new JTextArea("Learning Rate:");
		this.learnrate.setFont(MonitorPanel.PANEL_FONT);
		this.learnrate.setEditable(false);
		this.learnrate.setBackground(this.left.getBackground());
		this.learnrate.setWrapStyleWord(true);
		this.learnrate.setLineWrap(true);

		this.ulearnrate = new JTextArea();
		this.ulearnrate.setFont(MonitorPanel.PANEL_FONT);
		this.ulearnrate.setEditable(false);
		this.ulearnrate.setBackground(Color.WHITE);
		this.ulearnrate.setWrapStyleWord(true);
		this.ulearnrate.setLineWrap(true);

		this.minibatchsize = new JTextArea("Mini batch size:");
		this.minibatchsize.setFont(MonitorPanel.PANEL_FONT);
		this.minibatchsize.setEditable(false);
		this.minibatchsize.setBackground(this.left.getBackground());
		this.minibatchsize.setWrapStyleWord(true);
		this.minibatchsize.setLineWrap(true);

		this.uminibatchsize = new JTextArea();
		this.uminibatchsize.setFont(MonitorPanel.PANEL_FONT);
		this.uminibatchsize.setEditable(false);
		this.uminibatchsize.setBackground(Color.WHITE);
		this.uminibatchsize.setWrapStyleWord(true);
		this.uminibatchsize.setLineWrap(true);

		this.nnepochs = new JTextArea("Epochs trained:");
		this.nnepochs.setFont(MonitorPanel.PANEL_FONT);
		this.nnepochs.setEditable(false);
		this.nnepochs.setBackground(this.left.getBackground());
		this.nnepochs.setWrapStyleWord(true);
		this.nnepochs.setLineWrap(true);

		this.unnepochs = new JTextArea();
		this.unnepochs.setFont(MonitorPanel.PANEL_FONT);
		this.unnepochs.setEditable(false);
		this.unnepochs.setBackground(Color.WHITE);
		this.unnepochs.setWrapStyleWord(true);
		this.unnepochs.setLineWrap(true);

		this.cost = new JTextArea("Current cost:");
		this.cost.setFont(MonitorPanel.PANEL_FONT);
		this.cost.setEditable(false);
		this.cost.setBackground(this.left.getBackground());
		this.cost.setWrapStyleWord(true);
		this.cost.setLineWrap(true);

		this.ucost = new JTextArea();
		this.ucost.setFont(MonitorPanel.PANEL_FONT);
		this.ucost.setEditable(false);
		this.ucost.setBackground(Color.WHITE);
		this.ucost.setWrapStyleWord(true);
		this.ucost.setLineWrap(true);

		this.bestcost = new JTextArea("Best cost(Epoch):");
		this.bestcost.setFont(MonitorPanel.PANEL_FONT);
		this.bestcost.setEditable(false);
		this.bestcost.setBackground(this.left.getBackground());
		this.bestcost.setWrapStyleWord(true);
		this.bestcost.setLineWrap(true);

		this.ubestcost = new JTextArea();
		this.ubestcost.setFont(MonitorPanel.PANEL_FONT);
		this.ubestcost.setEditable(false);
		this.ubestcost.setBackground(Color.WHITE);
		this.ubestcost.setWrapStyleWord(true);
		this.ubestcost.setLineWrap(true);

		this.right.add(mode);
		this.right.add(umode);
		this.right.add(nnaufbau);
		this.right.add(unnaufbau);
		this.right.add(activationfunction);
		this.right.add(uactivationfunction);
		this.right.add(overalltraintime);
		this.right.add(uoveralltraintime);
		this.right.add(learnrate);
		this.right.add(ulearnrate);
		this.right.add(minibatchsize);
		this.right.add(uminibatchsize);
		this.right.add(nnepochs);
		this.right.add(unnepochs);
		this.right.add(cost);
		this.right.add(ucost);
		this.right.add(bestcost);
		this.right.add(ubestcost);

		this.leftContainer.add(this.left);
		this.rightContainer.add(this.right);
		this.meContainer.add(this.leftContainer, BorderLayout.WEST);
		this.meContainer.add(this.rightContainer, BorderLayout.CENTER);
		JPanel north = new JPanel();
		north.setPreferredSize(new Dimension(this.panelWidth, 100));
		north.setLayout(new BorderLayout());
		JPanel northWest = new JPanel();
		northWest.setLayout(new BorderLayout());
		northWest.setPreferredSize(new Dimension(this.panelWidth / 2, 100));
		JTextField training = new JTextField("Training stats");
		training.setBorder(null);
		training.setFont(new Font("ARIAL", Font.BOLD, 24));
		training.setEditable(false);
		training.setBackground(this.getBackground());
		training.setHorizontalAlignment(JTextField.CENTER);
		northWest.add(training, BorderLayout.CENTER);
		JPanel northEast = new JPanel();
		northEast.setLayout(new BorderLayout());
		northEast.setPreferredSize(new Dimension(this.panelWidth / 2, 100));
		JTextField neuralnet = new JTextField("Neuralnet stats");
		neuralnet.setBorder(null);
		neuralnet.setFont(new Font("ARIAL", Font.BOLD, 24));
		neuralnet.setEditable(false);
		neuralnet.setBackground(this.getBackground());
		neuralnet.setHorizontalAlignment(JTextField.CENTER);
		northEast.add(neuralnet, BorderLayout.CENTER);
		north.add(northWest, BorderLayout.WEST);
		north.add(northEast, BorderLayout.EAST);
		this.add(north, BorderLayout.NORTH);
		this.add(this.meContainer, BorderLayout.CENTER);
		this.update(null, null);
	}
	
	public void update(NeuralNetTrainDataObject nndo, Trainer tr) {
		this.nndo = nndo;
		this.tr = tr;
		if (this.nndo == null) {
			this.unnaufbau.setText("-");
			this.uactivationfunction.setText("-");
			this.uoveralltraintime.setText("-");
			this.ulearnrate.setText("-");
			this.uminibatchsize.setText("-");
			this.unnepochs.setText("-");
			this.ucost.setText("-");
			this.ubestcost.setText("-");
		} else {
			String aufbau = "";
			aufbau += nndo.nn.inputNeurons;
			for (int i = 0; i < nndo.nn.hiddenNeurons.size(); i++) {
				aufbau += "," + nndo.nn.hiddenNeurons.get(i);
			}
			aufbau += "," + nndo.nn.outputNeurons;
			this.unnaufbau.setText(aufbau);
			aufbau = nndo.nn.activationFunctions.get(0).toString();
			for (int i = 1; i < nndo.nn.activationFunctions.size(); i++) {
				aufbau += "," + nndo.nn.activationFunctions.get(i).toString();
			}
			this.uactivationfunction.setText(aufbau);
			// Zeit ausrechnen
			int sekunden = (int) (this.nndo.overall_train_time / 1000);
			int minuten = sekunden / 60;
			int stunden = minuten / 60;
			minuten -= stunden * 60;
			sekunden -= stunden * 3600 + minuten * 60;
			String time = "";
			String stund = stunden + "";
			String minut = minuten + "";
			String sekund = sekunden + "";
			if (stunden < 10) {
				stund = "0" + stund;
			}
			if (minuten < 10) {
				minut = "0" + minut;
			}
			if (sekunden < 10) {
				sekund = "0" + sekund;
			}
			time += stund + "h";
			time += minut + "m";
			time += sekund + "s";
			this.uoveralltraintime.setText(time);
			this.ulearnrate.setText("" + nndo.hp.learning_rate);
			this.uminibatchsize.setText("" + nndo.hp.batch_size);
			this.unnepochs.setText("" + nndo.epoch);
			if (this.nndo.data.containsKey("Epochs")
					&& this.nndo.data.get("Epochs").containsKey(nndo.epoch - 1 + 0.0)) {
				if (this.nndo.data.containsKey("Accuracy")) {
					this.ucost.setText("" + nndo.data.get("Epochs").get(nndo.epoch - 1 + 0.0) + "(Acc:"
							+ 100 * this.nndo.data.get("Accuracy").get(nndo.epoch - 1 + 0.0) + "%)");
				} else {
					this.ucost.setText("" + nndo.data.get("Epochs").get(nndo.epoch - 1 + 0.0));
				}
			} else {
				this.ucost.setText("-");
			}
			if (this.nndo.best_epoch != -1) {
				this.ubestcost.setText("" + nndo.best_cost + "(" + (nndo.best_epoch + 1) + ")");
			} else {
				this.ubestcost.setText("-");
			}
		}
		if (this.tr == null) {
			this.ueta.setText("-");
			this.utrain_time.setText("-");
			if (this.nndo == null) {
				this.uepochs_left.setText("-");
			} else {
				this.uepochs_left.setText("" + this.nndo.hp.epochs + "/" + this.nndo.hp.epochs);
			}
		} else {
			int sekunden = (int) (this.tr.train_time / 1000);
			int minuten = sekunden / 60;
			int stunden = minuten / 60;
			minuten -= stunden * 60;
			sekunden -= stunden * 3600 + minuten * 60;
			String time = "";
			String stund = stunden + "";
			String minut = minuten + "";
			String sekund = sekunden + "";
			if (stunden < 10) {
				stund = "0" + stund;
			}
			if (minuten < 10) {
				minut = "0" + minut;
			}
			if (sekunden < 10) {
				sekund = "0" + sekund;
			}
			time += stund + "h";
			time += minut + "m";
			time += sekund + "s";
			this.utrain_time.setText(time);
			try {
				double epochRatio = 1 / ((this.nndo.hp.epochs + 0.0 - (this.tr.endEpoch - this.nndo.epoch))
						/ (this.nndo.hp.epochs + 0.0));
				int timeleft = (int) (this.tr.train_time * epochRatio - this.tr.train_time);
				sekunden = (int) (timeleft / 1000.0);
				minuten = sekunden / 60;
				stunden = minuten / 60;
				minuten -= stunden * 60;
				sekunden -= stunden * 3600 + minuten * 60;
				time = "";
				stund = stunden + "";
				minut = minuten + "";
				sekund = sekunden + "";
				if (stunden < 10) {
					stund = "0" + stund;
				}
				if (minuten < 10) {
					minut = "0" + minut;
				}
				if (sekunden < 10) {
					sekund = "0" + sekund;
				}
				time += stund + "h";
				time += minut + "m";
				time += sekund + "s";
				this.ueta.setText(time);
			} catch (Exception e) {
				e.printStackTrace();
				this.ueta.setText("-");
			}
			this.uepochs_left.setText("" + (this.tr.endEpoch - this.nndo.epoch) + "/" + this.nndo.hp.epochs);
		}
	}
}
