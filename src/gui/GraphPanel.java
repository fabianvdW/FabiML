package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	public final static int OVAL_RADIUS = 2;
	public final static int CHAR_HEIGHT = 7;
	public final static int CHAR_WIDTH = 7;
	public final static int X_AXIS_NUMBER_Y_DIST = 25;
	public final static int Y_AXIS_NUMBER_X_DIST = -25;
	public final static int X_AXIS_HORIZONTAL_SEPERATOR_HEIGHT = 10;
	public final static int Y_AXIS_VERTICAL_SEPERATOR_WIDTH = 10;
	public final static int ARROW_X = 5;
	public final static int ARROW_Y = 12;
	public final static int DATA_STROKE = 1;
	public final static int STROKE = 2;
	public final static int X_BIAS = 80;
	public final static int Y_BIAS = -80;
	public final static int X_DIST = 50;
	public final static int Y_DIST = 50;
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private int x0, x1, y0, y1;

	public double X_MAX, Y_MAX;
	public double X_INTERVAL, Y_INTERVAL;
	public String X_AXIS, Y_AXIS;
	private HashMap<Double, Double> data;
	private SortedSet<Double> sorted_data;
	boolean connect;
	public ArrayList<GraphPanel> childs;

	public GraphPanel(int x0, int x1, int y0, int y1, double X_MAX, double Y_MAX, String X_AXIS, String Y_AXIS,
			boolean connect) {
		if (this.width < 0 || this.height < 0) {
			throw new RuntimeException();
		}
		this.x0 = x0 + X_BIAS;
		this.y0 = y0 + Y_BIAS;
		this.x1 = x1 - X_BIAS;
		this.y1 = y1 - Y_BIAS;
		this.width = this.x1 - this.x0;
		this.height = this.y0 - this.y1;
		this.X_MAX = X_MAX;
		this.Y_MAX = Y_MAX;
		this.X_AXIS = X_AXIS;
		this.Y_AXIS = Y_AXIS;
		this.connect = connect;
		this.X_INTERVAL = this.X_MAX / ((this.width - X_DIST) / X_DIST);
		this.Y_INTERVAL = this.Y_MAX / ((this.height - Y_DIST) / Y_DIST);
		this.data = new HashMap<Double, Double>();
		this.sorted_data = new TreeSet<Double>();
		this.childs = new ArrayList<GraphPanel>();
	}

	public void addChild(GraphPanel gp) {
		this.childs.add(gp);
	}

	public void setData(HashMap<Double, Double> data) {
		this.data = data;
		if (this.data == null) {
			System.out.println("GraphPanel, data is null");
			System.exit(-1);
		}
		this.sorted_data = new TreeSet<>(data.keySet());
		if (sorted_data.size() > 0) {
			this.X_MAX = sorted_data.last();
			this.X_INTERVAL = this.X_MAX / ((this.width - X_DIST) / X_DIST);
		}
		for (GraphPanel gp : this.childs) {
			gp.setData(data);
		}
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// System.out.println(this.x0+" "+this.x1+" "+this.y0+" "+this.y1);
		g2.setColor(Color.WHITE);
		g2.fillRect(this.x0, this.y1, this.width, this.height);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(STROKE));

		// X-Achse malen
		g2.drawLine(this.x0, this.y0, this.x1, this.y0);
		// Pfeil
		g2.drawLine(this.x1, this.y0, this.x1 - ARROW_Y, this.y0 - ARROW_X);
		g2.drawLine(this.x1, this.y0, this.x1 - ARROW_Y, this.y0 + ARROW_X);

		// X-Achsen Beschriftung
		int length = this.X_AXIS.length() / 2;
		g2.drawString(this.X_AXIS, this.x1 - CHAR_WIDTH * length, this.y0 + X_AXIS_NUMBER_Y_DIST);

		// Seperatoren einzeichnen
		int real_width = this.width - X_DIST;
		int seperators = real_width / X_DIST + 1;
		for (int i = 0; i < seperators; i++) {
			// Seperator
			g2.drawLine(this.x0 + (i) * X_DIST, this.y0 - X_AXIS_HORIZONTAL_SEPERATOR_HEIGHT / 2,
					this.x0 + (i) * X_DIST, this.y0 + X_AXIS_HORIZONTAL_SEPERATOR_HEIGHT / 2);

			// Beschriftung
			double x = i * this.X_INTERVAL;
			x *= 100;
			x -= x % 1;
			x /= 100;
			String str = "" + x;
			int len = str.length() / 2;
			g2.drawString(str, this.x0 + (i) * X_DIST - CHAR_WIDTH * len, this.y0 + X_AXIS_NUMBER_Y_DIST);
		}

		// Y-Achse
		g2.drawLine(this.x0, this.y0, this.x0, this.y1);
		// Pfeil
		g2.drawLine(this.x0, this.y1, this.x0 - ARROW_X, this.y1 + ARROW_Y);
		g2.drawLine(this.x0, this.y1, this.x0 + ARROW_X, this.y1 + ARROW_Y);

		// Y-Achsen Beschriftung
		length = this.Y_AXIS.length() / 2;
		g2.drawString(this.Y_AXIS, this.x0 - CHAR_WIDTH * length + Y_AXIS_NUMBER_X_DIST, this.y1 + CHAR_HEIGHT);

		// Seperatoren einzeichnen
		int real_height = this.height - Y_DIST;
		seperators = real_height / Y_DIST + 1;
		for (int i = 0; i < seperators; i++) {
			// Seperator
			g2.drawLine(this.x0 + Y_AXIS_VERTICAL_SEPERATOR_WIDTH / 2, y0 - i * Y_DIST,
					this.x0 - Y_AXIS_VERTICAL_SEPERATOR_WIDTH / 2, y0 - i * Y_DIST);
			// Beschriftung
			double y = i * this.Y_INTERVAL;
			y *= 100;
			y -= y % 1;
			y /= 100;
			String str = "" + y;
			int len = str.length() / 2;
			g2.drawString(str, this.x0 + Y_AXIS_NUMBER_X_DIST - len * CHAR_WIDTH,
					this.y0 - i * Y_DIST + CHAR_HEIGHT / 2);
		}
		this.drawData(g);
	}

	public void drawData(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(DATA_STROKE));
		Iterator<Double> it = this.sorted_data.iterator();
		double lastD = -1;
		double lastx = -1;
		double lasty = -1;
		while (it.hasNext()) {
			double d = it.next();
			double y = this.data.get(d);
			if (d > this.X_MAX || y > this.Y_MAX) {
				System.out.println(d);
				System.out.println(this.X_MAX);
				System.out.println(y);
				System.out.println(this.Y_MAX);
				throw new RuntimeException("Data has exceeded maximum X or Y");
			}
			double xcoord = this.x0 + d / (this.X_MAX) * (X_DIST * (this.width / X_DIST - 1));
			double ycoord = this.y0 - y / (this.Y_MAX) * (Y_DIST * (this.height / Y_DIST - 1));
			if (connect && lastD != -1) {
				g2.drawLine((int) lastx, (int) lasty, (int) xcoord, (int) ycoord);
			}
			lastx = xcoord;
			lasty = ycoord;
			g2.setColor(Color.RED);
			g2.fillOval((int) (xcoord), (int) (ycoord), OVAL_RADIUS, OVAL_RADIUS);
			lastD = d;
		}
	}
}
