package game.games.flappybird;


import neuroevolution.FitnessGenome;
import neuroevolution.Genome;
import neuroevolution.GenomeVisualizer;
import neuroevolution.NeuroEvolutionGame;
import neuroevolution.Neuroevolution;
import neuroevolution.Species;

public class FlappyBirdVersuch{
	public static void main(String[] args){
		String path="./NEAT/flappy";
		NeuroEvolutionGame g= new FlappyBird();
		Neuroevolution ne = new Neuroevolution(g,150,6,1) {
			private static final long serialVersionUID = 1L;
			@Override
			public void evaluatePopulation() {
				for(Species s: this.population_in_species){
					for(Genome g: s.content){
						double fitness=0;
						FlappyBirdNeuro fbn = new FlappyBirdNeuro(g);
						fbn.play();
						fitness= fbn.b.score;
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
		for(int i=0;i<10000;i++){
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
	}
}
