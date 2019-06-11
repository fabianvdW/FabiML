package mnist;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PSurface;

public class MnistVisualizer extends PApplet {
	List<double[]> inputs;
	List<Integer> labels;
	List<Integer> prediction;
	List<Double> certainity;
	int currentPos = 0;
	public MnistVisualizer(List<double[]> inputs, List<Integer> labels,List<Integer> prediction,List<Double> certainity){
		this.inputs = inputs;
		this.labels = labels;
		this.prediction=prediction;
		this.certainity=certainity;
	}
	public void settings() {
		size(280, 280);
	}

	public void setup() {
		removeExitEvent(this,getSurface());
	}
	static final void removeExitEvent(MnistVisualizer p, final PSurface surf) {
		  final java.awt.Window win
		    = ((processing.awt.PSurfaceAWT.SmoothCanvas) surf.getNative()).getFrame();
		 
		  for (final java.awt.event.WindowListener evt : win.getWindowListeners()){
		    win.removeWindowListener(evt);
		    win.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
					
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
					
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
					p.dispose();
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					
				}
				
				@Override
				public void windowActivated(WindowEvent e) {
					
				}
			});
		  }
		}
	public void mouseClicked() {
		if ((mouseButton == LEFT)) {
			currentPos += 1;
			if (currentPos == inputs.size()) {
				currentPos = 0;
			}
		} else if ((mouseButton == RIGHT)) {
			currentPos -= 1;
			if (currentPos == -1) {
				currentPos = inputs.size() - 1;
			}
		}
	}

	public void draw() {
		//System.out.println("running");
		PGraphics pg = createGraphics(28, 28);
		pg.beginDraw();
		pg.loadPixels();
		
		// Draw image
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++) {
				pg.pixels[i * 28 + j] = color((int) (inputs.get(currentPos)[i * 28 + j]));

			}
		}
		scale(10,10);
		pg.updatePixels();
		image(pg.get(), 0, 0);
		fill(0,255,0);
		textFont(createFont("Arial",2,true)); 
		double certain= certainity.get(currentPos);
		certain*=100;
		certain-=certain%1;
		certain/=100;
		text("Classified as: "+prediction.get(currentPos)+"("+certain+")",1,2);
		text("Labelled as: "+labels.get(currentPos),1,4);
		fill(255,0,0);
		text(""+currentPos+"/"+(this.prediction.size()-1),18,25);
	}

}
