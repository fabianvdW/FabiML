package game.games.connect4;

import java.util.ArrayList;
import java.util.Scanner;

import game.GameEvent;
import neuroevolution.Genome;
import neuroevolution.Neuroevolution;
import processing.core.PApplet;

public class Test2 {

	public static void main(String[] args){
		int rows=6;
		int columns=7;
		String path="./NEAT/gen950.neat";
		Neuroevolution ne = (Neuroevolution) ea.ObjectLoader.loadObject(path);
		Genome best1= ne.fittestGenome;
		Genome best2= ne.fittestGenomes.get(ne.fittestGenomes.size()-2);
		Connect4Neuro c4n= new Connect4Neuro(rows, columns, best1,best2);
		c4n.play();
		ArrayList<Connect4Event> cevents = new ArrayList<Connect4Event>();
		for(GameEvent ge: c4n.ablauf){
			cevents.add((Connect4Event)ge);
		}
		Connect4Visualizer c4v = new Connect4Visualizer(cevents, rows, columns);
		PApplet.runSketch(new String[]{"game.games.connect4.Connect4Visualizer"}, c4v);
		int wins=0;
		int games=1000;
		for(int i=0;i<games;i++){
			Connect4Neuro c4n2 = new Connect4Neuro(rows, columns, best1);
			c4n2.play();
			if(c4n2.winner==1){
				wins+=1;
			}
		}
		System.out.println((wins+0.0)/games*100+"% Winrate");
		Scanner s = new Scanner(System.in);
		String s2= s.nextLine();
		System.exit(0);
	}
}
