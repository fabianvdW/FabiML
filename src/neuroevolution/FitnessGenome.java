package neuroevolution;

import java.io.Serializable;

public class FitnessGenome implements Serializable{

	private static final long serialVersionUID = 1L;
	double fitness;
	Genome g;
	public FitnessGenome(Genome g, double fitness){
		this.g=g;
		this.fitness=fitness;
	}
	
}
