package game.games.connect4;

import java.util.ArrayList;
import java.util.Random;

import game.GameEvent;
import neuroevolution.ConnectionGene;
import neuroevolution.FitnessGenome;
import neuroevolution.Genome;
import neuroevolution.GenomeVisualizer;
import neuroevolution.NeuroEvolutionGame;
import neuroevolution.Neuroevolution;
import neuroevolution.NodeGene;
import neuroevolution.Species;
import neuroevolution.NodeGene.Type;
import processing.core.PApplet;

public class Versuch {
	public static void main(String[] args){
		String path="./NEAT/gen";
		int rows=6;
		int columns = 7;
		int cores=4;
		NeuroEvolutionGame g= new Connect4Neuro(rows,columns,null);
		Random r= new Random();
		r.setSeed((long) (Math.random()*Integer.MAX_VALUE));
		Neuroevolution ne =  new Neuroevolution(g, 150, rows*columns, columns,r) {
			private static final long serialVersionUID = 1L;

			@Override
			public void evaluatePopulation() {
				for(Species s: this.population_in_species){
					for(Genome g: s.content){
						double fitness=0;
						ArrayList<Connect4EvaluationThread> threads = new ArrayList<Connect4EvaluationThread>();
						for(int i=0;i< cores;i++){
							Connect4EvaluationThread t = new Connect4EvaluationThread(i+"", g, 100/cores, rows, columns,null);
							t.start();
							threads.add(t);
						}
						for(Connect4EvaluationThread t: threads){
							try{
							t.t.join();
							}catch(Exception e){
								e.printStackTrace();
								System.exit(-1);
							}
						}
						for(int i=0;i< cores;i++){
							Connect4EvaluationThread t = threads.get(i);
							fitness+=t.score;
						}
						this.summedScore+=fitness;
						if(fitness>this.highestScore){
							this.highestScore=fitness;
							this.fittestGenome=g;
						}
						FitnessGenome g2= new FitnessGenome(g,fitness);
						s.evaluatedContent.add(g2);
					}
				}
				
			}
		};
		ne.init();
		for(int i=0;i<50000;i++){
			ne.doGeneration();
			ne.fittestGenomes.add(ne.fittestGenome);
			System.out.println("Generation: "+i);
			System.out.println("Highest Score: "+ne.highestScore);
			System.out.println("Average Score: "+ne.summedScore/ne.population_size);
			System.out.println("Species: "+ne.population_in_species.size());
			System.out.println("Connections of best: "+ne.fittestGenome.connections.size());
			if(i%50==0){
				GenomeVisualizer.printGenome(ne.fittestGenome, path+i+"bestGenome.png");
				ea.ObjectWriter.saveObject(path+i+".genome", ne.fittestGenome);
				ea.ObjectWriter.saveObject(path+i+".neat", ne);
			}
		}
		Genome best1= ne.fittestGenome;
		ne.doGeneration();
		Genome best2= ne.fittestGenome;
		Connect4Neuro c4n= new Connect4Neuro(rows, columns, best1,best2);
		c4n.play();
		ArrayList<Connect4Event> cevents = new ArrayList<Connect4Event>();
		for(GameEvent ge: c4n.ablauf){
			cevents.add((Connect4Event)ge);
		}
		ea.ObjectWriter.saveObject("help.xD", ne);
		ea.ObjectWriter.saveObject("Bestgenom1.genome", best1);
		ea.ObjectWriter.saveObject("Bestgenome2.genome", best2);
		Connect4Visualizer c4v = new Connect4Visualizer(cevents, rows, columns);
		PApplet.runSketch(new String[]{"game.games.connect4.Connect4Visualizer"}, c4v);
	}
	public static void test_compability(){
		Genome parent1 = new Genome();
		ConnectionGene cg1 = new ConnectionGene(null, null, 0, true, 1);
		ConnectionGene cg2 = new ConnectionGene(null, null, 0, true, 2);
		ConnectionGene cg3 = new ConnectionGene(null, null, 0, true, 3);
		ConnectionGene cg4 = new ConnectionGene(null, null, 0, true, 4);
		ConnectionGene cg5 = new ConnectionGene(null, null, 0, true, 5);
		ConnectionGene cg6 = new ConnectionGene(null, null, 0, true, 6);
		ConnectionGene cg7 = new ConnectionGene(null, null, 0, true, 7);
		ConnectionGene cg8 = new ConnectionGene(null, null, 0, true, 9);
		parent1.connections.add(cg1);
		parent1.connections.add(cg2);
		parent1.connections.add(cg3);
		parent1.connections.add(cg4);
		parent1.connections.add(cg5);
		parent1.connections.add(cg6);
		parent1.connections.add(cg7);
		parent1.connections.add(cg8);
		Genome parent2 = new Genome();
		ConnectionGene c_g1= new ConnectionGene(null, null, 0, true, 8);
		ConnectionGene c_g2= new ConnectionGene(null, null, 0, true, 10);
		ConnectionGene c_g3= new ConnectionGene(null, null, 0, true, 11);
		ConnectionGene c_g4= new ConnectionGene(null, null, 0, true, 12);
		ConnectionGene c_g5= new ConnectionGene(null, null, 0, true, 13);
		ConnectionGene c_g6= new ConnectionGene(null, null, 0, true, 14);
		ConnectionGene c_g7= new ConnectionGene(null, null, 0, true, 15);
		ConnectionGene c_g8= new ConnectionGene(null, null, 0, true, 16);
		parent2.connections.add(c_g1);
		parent2.connections.add(c_g2);
		parent2.connections.add(c_g3);
		parent2.connections.add(c_g4);
		parent2.connections.add(c_g5);
		parent2.connections.add(c_g6);
		parent2.connections.add(c_g7);
		parent2.connections.add(c_g8);
		//ConnectionGene c_g1= new ConnectionGene(null, null, 0, true, 1);
		Genome.compatibility_threshold(parent1, parent2);
	}
	public static void test_Mutations(){
		Genome parent1 = new Genome();
		Random r= new Random();
		r.setSeed(42);
		NodeGene ng1= new NodeGene(Type.INPUT,1);
		NodeGene ng2= new NodeGene(Type.INPUT,2);
		NodeGene ng3= new NodeGene(Type.OUTPUT,3);
		ConnectionGene cg1= new ConnectionGene(ng1, ng3, 1, true, Genome.getInnovation_number());
		ConnectionGene cg2 = new ConnectionGene(ng2,ng3,-1,true,Genome.getInnovation_number());
		parent1.addNodeGene(ng1);
		parent1.addNodeGene(ng2);
		parent1.addNodeGene(ng3);
		parent1.connections.add(cg1);
		parent1.connections.add(cg2);
		System.out.println(parent1.toString());
		System.out.println("----------------------------------------------");
		parent1.add_node(r);
		System.out.println(parent1);
		System.out.println("-----------------------------------------------");
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		parent1.add_connection(r);
		System.out.println(parent1);
	}
	public static void testcrossover(){
		Genome parent1 = new Genome();
		Random r= new Random();
		//r.setSeed(402);
		NodeGene ng1= new NodeGene(Type.INPUT, 1);
		NodeGene ng2= new NodeGene(Type.INPUT, 2);
		NodeGene ng3= new NodeGene(Type.INPUT, 3);
		NodeGene ng4= new NodeGene(Type.OUTPUT, 4);
		NodeGene ng5= new NodeGene(Type.HIDDEN, 5);
		parent1.addNodeGene(ng1);
		parent1.addNodeGene(ng2);
		parent1.addNodeGene(ng3);
		parent1.addNodeGene(ng4);
		parent1.addNodeGene(ng5);
		ConnectionGene cg1 = new ConnectionGene(ng1, ng4, r.nextDouble()*2-1, true, 1);
		ConnectionGene cg2 = new ConnectionGene(ng2, ng4, r.nextDouble()*2-1, false, 2);
		ConnectionGene cg3 = new ConnectionGene(ng3, ng4, r.nextDouble()*2-1, true, 3);
		ConnectionGene cg4 = new ConnectionGene(ng2, ng5, r.nextDouble()*2-1, true, 4);
		ConnectionGene cg5 = new ConnectionGene(ng5, ng4, r.nextDouble()*2-1, true, 5);
		ConnectionGene cg8 = new ConnectionGene(ng1, ng5, r.nextDouble()*2-1, true, 8);
		parent1.connections.add(cg1);
		parent1.connections.add(cg2);
		parent1.connections.add(cg3);
		parent1.connections.add(cg4);
		parent1.connections.add(cg5);
		parent1.connections.add(cg8);
		System.out.println("------------------------------------------------------------------");
		System.out.println(parent1.toString());
		System.out.println("------------------------------------------------------------------");
		Genome parent2= new Genome();
		NodeGene n_g1= new NodeGene(Type.INPUT,1);
		NodeGene n_g2= new NodeGene(Type.INPUT,2);
		NodeGene n_g3= new NodeGene(Type.INPUT,3);
		NodeGene n_g4= new NodeGene(Type.OUTPUT,4);
		NodeGene n_g5= new NodeGene(Type.HIDDEN,5);
		NodeGene n_g6= new NodeGene(Type.HIDDEN,6);
		parent2.addNodeGene(n_g1);
		parent2.addNodeGene(n_g2);
		parent2.addNodeGene(n_g3);
		parent2.addNodeGene(n_g4);
		parent2.addNodeGene(n_g5);
		parent2.addNodeGene(n_g6);
		ConnectionGene c_g1 = new ConnectionGene(n_g1,n_g4,r.nextDouble()*2-1,true,1);
		ConnectionGene c_g2 = new ConnectionGene(n_g2,n_g4,r.nextDouble()*2-1,false,2);
		ConnectionGene c_g3 = new ConnectionGene(n_g3,n_g4,r.nextDouble()*2-1,true,3);
		ConnectionGene c_g4 = new ConnectionGene(n_g2,n_g5,r.nextDouble()*2-1,true,4);
		ConnectionGene c_g5 = new ConnectionGene(n_g5,n_g4,r.nextDouble()*2-1,false,5);
		ConnectionGene c_g6 = new ConnectionGene(n_g5,n_g6,r.nextDouble()*2-1,true,6);
		ConnectionGene c_g7 = new ConnectionGene(n_g6,n_g4,r.nextDouble()*2-1,true,7);
		ConnectionGene c_g9 = new ConnectionGene(n_g3,n_g5,r.nextDouble()*2-1,true,9);
		ConnectionGene c_g10 = new ConnectionGene(n_g1,n_g6,r.nextDouble()*2-1,true,10);
		parent2.connections.add(c_g1);
		parent2.connections.add(c_g2);
		parent2.connections.add(c_g3);
		parent2.connections.add(c_g4);
		parent2.connections.add(c_g5);
		parent2.connections.add(c_g6);
		parent2.connections.add(c_g7);
		parent2.connections.add(c_g9);
		parent2.connections.add(c_g10);
		System.out.println(parent2.toString());
		System.out.println("------------------------------------------------------------------");
		Genome child = Genome.cross_over(parent1, 1, parent2, 0, r);
		System.out.println(child.toString());
	}
}
class Connect4EvaluationThread implements Runnable{
	Thread t;
	String threadName;
	Genome g;
	Genome g2;
	int games;
	double score=0;
	int rows,columns;
	public Connect4EvaluationThread(String name, Genome g, int games,int rows, int columns,Genome g2){
		this.threadName=name;
		this.g=g;
		this.games=games;
		this.rows=rows;
		this.columns=columns;
		this.g2=g2;
	}
	public void start(){
		if(this.t==null){
			t= new Thread(this,this.threadName);
			t.start();
		}
	}
	public void run(){
		for(int i=0;i<this.games;i++){
			Connect4Neuro cn = new Connect4Neuro(rows, columns, g,g2);
			cn.play();
			score+=cn.score;
		}
	}
}
