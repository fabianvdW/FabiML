package neuroevolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Species implements Serializable{

	private static final long serialVersionUID = 1L;
	public ArrayList<Genome> content;
	public ArrayList<FitnessGenome> evaluatedContent;
	public Genome representative;
	public double adjustedFitness=0;
	public int stagnation_period=0;
	public Species(Genome g){
		this.representative=g;
		this.content= new ArrayList<Genome>();
		this.content.add(this.representative);
		this.evaluatedContent= new ArrayList<FitnessGenome>();
	}
	public boolean belongsToSpecies(Genome g){
		return Genome.compatibility_threshold(this.representative, g)<Neuroevolution.COMPATIBILITYTHRESHOLD;
	}
	public void evaluateAdjustedFitness(){
		double newFitness=0;
		for(FitnessGenome g: this.evaluatedContent){
			newFitness+=g.fitness;
		}
		newFitness/=this.evaluatedContent.size();
		if(newFitness-adjustedFitness>Neuroevolution.STAGNATION_DIFFERENCE){
			stagnation_period=0;
		}else{
			stagnation_period++;
		}
		this.adjustedFitness=newFitness;
	}
	public void reset(Random r){
		this.representative= this.content.get(r.nextInt(this.content.size()));
		this.content.clear();
		this.evaluatedContent.clear();
	}
}
