package game.games.connect4;

import neuroevolution.FitnessGenome;
import neuroevolution.Genome;
import neuroevolution.GenomeVisualizer;
import neuroevolution.Neuroevolution;
import neuroevolution.Species;

public class Test3 {
	public static void main(String[] args){
		String path="NEAT/aXOR";

		Neuroevolution ne = new Neuroevolution(null,150,2,1) {
			private static final long serialVersionUID = 1L;

			@Override
			public void evaluatePopulation() {
				for(Species s: this.population_in_species){
					for(Genome g: s.content){
						double[] input1 = {0,0};
						double[] input2 = {0,1};
						double[] input3 = {1,0};
						double[] input4={1,1};
						double[] label1={0};
						double[] label2={1};
						double[] label3={1};
						double[] label4={0};
						double[] output1 =g.feedForward(input1);
						double[] output2 = g.feedForward(input2);
						double[] output3 = g.feedForward(input3);
						double[] output4 = g.feedForward(input4);
						//System.out.println(output1[0]+", "+output2[0]+", "+output3[0]+", "+output4[0]);
						double fitness= (output1[0]-label1[0])*(output1[0]-label1[0]);
						fitness+= (output2[0]-label2[0])*(output2[0]-label2[0]);
						fitness+= (output3[0]-label3[0])*(output3[0]-label3[0]);
						fitness+= (output4[0]-label4[0])*(output4[0]-label4[0]);
						fitness=4-fitness;
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
			if(i%10==0){
				GenomeVisualizer.printGenome(ne.fittestGenome, path+i+"bestGenome.png");
				ea.ObjectWriter.saveObject(path+i+".genome", ne.fittestGenome);
				ea.ObjectWriter.saveObject(path+i+".neat", ne);
			}
		}
	}

}
