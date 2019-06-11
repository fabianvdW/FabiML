package game.games.flappybird;

import java.util.ArrayList;

import game.GameEvent;
import neuroevolution.Genome;
import neuroevolution.Neuroevolution;
import processing.core.PApplet;

public class FlappyBirdTest2 {
	public static void main(String[] args){
		String path="./NEAT/flappy250.neat";
		Neuroevolution ne = (Neuroevolution) ea.ObjectLoader.loadObject(path);
		Genome best1= ne.fittestGenome;
		FlappyBirdNeuro fbn = new FlappyBirdNeuro(best1);
		fbn.play();
		ArrayList<FlappyBirdMove> events = new ArrayList<FlappyBirdMove>();
		for(GameEvent g:  fbn.b.ablauf){
			FlappyBirdMove fbm= (FlappyBirdMove) g;
			events.add(fbm);
		}
		FlappyBirdVisualizer fbv = new FlappyBirdVisualizer(events);
		PApplet.runSketch(new String[]{"game.games.flappybird.FlappyBirdVisualizer"}, fbv);
	}
}
