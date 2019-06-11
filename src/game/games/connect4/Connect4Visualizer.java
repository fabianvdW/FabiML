package game.games.connect4;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import processing.core.PApplet;
import processing.core.PSurface;

/**
 * This class visualizes a Connect4GameEvent
 * <p>
 * Note that this is done asynchronously.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class Connect4Visualizer extends PApplet {
	/**
	 * Course of the game saved in this array
	 */
	public List<Connect4Event> events;

	/**
	 * Amount of rows
	 */
	int rows;

	/**
	 * Amount of columns
	 */
	int columns;

	/**
	 * Current position in the course of the game, being 0 at the start of the
	 * game, 1 after 1 move has been shown, etc.
	 */
	int pos;

	/**
	 * Initializes all the class fields
	 * 
	 * @param events
	 *            course of the game
	 * @param rows
	 *            amount of rows
	 * @param columns
	 *            amount of columns
	 */
	public Connect4Visualizer(List<Connect4Event> events, int rows, int columns) {
		this.events = events;
		this.rows = rows;
		this.columns = columns;
		this.pos = 0;
	}

	/**
	 * Sets the settings of the PApplet
	 * <p>
	 * Here only the size is set. It is calculated as follows: size x= 80*
	 * columns +50 size y = 80* rows + 50
	 * </p>
	 */
	@Override
	public void settings() {
		size(80 * columns + 50, 80 * rows + 50);
	}

	/**
	 * Does nothing yet.
	 */
	@Override
	public void setup() {
		// removeExitEvent(this,getSurface());
	}

	/**
	 * Mouse clicked listener, if it is a left click, the game will advance to
	 * show one more move, if it is a right click, the game will recede back one
	 * move
	 */
	@Override
	public void mouseClicked() {
		if ((mouseButton == LEFT)) {
			pos += 1;
			if (pos > this.events.size()) {
				pos = 0;
			}
		} else if ((mouseButton == RIGHT)) {
			pos -= 1;
			if (pos == -1) {
				pos = events.size();
			}
		}
	}

	/**
	 * Board is drawn to the screen
	 */
	@Override
	public void draw() {
		background(209);
		fill(0, 0, 255);
		rect(25, 25, (float)(80 * columns),(float)( 80 * rows));
		int x0 = 25;
		int y0 = 25;
		fill(177, 184, 186);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				ellipse((float)(x0 + 80.0 * j + 40.0), (float)(y0 + 80.0 * i + 40.0), 70.0f, 70.0f);
			}
		}
		for (int i = 0; i < pos; i++) {
			Connect4Event e = this.events.get(i);
			if (e.player == 1) {
				if (!e.random) {
					fill(242, 255, 99);
				} else {
					fill(109, 112, 2);
				}
			} else {
				if (!e.random) {
					fill(255, 0, 0);
				} else {
					fill(128, 0, 0);
				}
			}
			ellipse((float)(x0 + 80.0 * e.x + 40.0), (float)(y0 + 80.0 * e.y + 40.0), 70.0f, 70.0f);
		}
	}

	/**
	 * Here the exit event of the PApplet is removed. That means, that not the
	 * whole program will exit after the PApplet window has been closed. Now
	 * only all resources of the PApplet will be disposed
	 * 
	 * @param p
	 *            affected Visualizer
	 * @param surf
	 *            surfaced of affected Visualizer
	 */
	static final void removeExitEvent(Connect4Visualizer p, final PSurface surf) {
		final java.awt.Window win = ((processing.awt.PSurfaceAWT.SmoothCanvas) surf.getNative()).getFrame();

		for (final java.awt.event.WindowListener evt : win.getWindowListeners()) {
			win.removeWindowListener(evt);
			win.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					//only window closing needs to be checked
				}

				@Override
				public void windowIconified(WindowEvent e) {
					//only window closing needs to be checked
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					//only window closing needs to be checked
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					//only window closing needs to be checked
				}

				@Override
				public void windowClosing(WindowEvent e) {
					p.dispose();
				}

				@Override
				public void windowClosed(WindowEvent e) {
					//only window closing needs to be checked
				}

				@Override
				public void windowActivated(WindowEvent e) {
					//only window closing needs to be checked
				}
			});
		}
	}
}
