package neuroevolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import neuroevolution.NodeGene.Type;

public abstract class Neuroevolution implements Serializable{
	
	public static final double STAGNATION_DIFFERENCE=0;//USED
	public final static double MUTATION_CHANCE=0.8;
	public final static double MUTATION_WEIGHT_CHANCE=1;
	public final static int SPECIES_STAGNATION=15;//USED
	public final static double OFFSPRING_MUTATION_WITHOUT_CROSSOVER=.25;
	public final static double ADD_NODE_MUTATION=.03;
	public final static double ADD_CONNECTION_MUTATION=.05;
	public final static double COMPATIBILITYTHRESHOLD=5;//USED
	public final static double KEEP_BEST_OF_SPECIES=10;
	private static final long serialVersionUID = 1L;

	protected NeuroEvolutionGame g;
	
	public int population_size;
	
	public ArrayList<Species> population_in_species;
	public ArrayList<Genome> population;
	public Random r;
	public double highestScore;
	public double summedScore;
	public Genome fittestGenome;
	public ArrayList<Genome> fittestGenomes;
	public int inputs;
	public int outputs;
	public static int generation=0;
	public Neuroevolution(NeuroEvolutionGame g,int pop_size,int inputs,int outputs){
		this.g=g;
		this.inputs=inputs;
		this.outputs=outputs;
		this.population_size=pop_size;
		this.fittestGenomes= new ArrayList<Genome>();
		this.population= new ArrayList<Genome>();
		population_in_species= new ArrayList<Species>();
		this.r= new Random();
		
	}
	public Neuroevolution(NeuroEvolutionGame g, int pop_size,int inputs, int outputs,Random r){
		this(g,pop_size,inputs,outputs);
		this.r=r;
	}
	public void init(){
		for(int i=0;i<this.population_size;i++){
			Genome g= new Genome();
			for(int j=0;j<this.inputs;j++){
				g.addNodeGene(new NodeGene(Type.INPUT, g.getNodeGenes().size()));
			}
			for(int j=0;j<this.outputs;j++){
				g.addNodeGene(new NodeGene(Type.OUTPUT,g.getNodeGenes().size()));
			}
			for(int j=0;j<this.inputs;j++){
				for(int k=0;k<this.outputs;k++){
					g.connections.add(new ConnectionGene(g.nodegenes.get(j), g.nodegenes.get(this.inputs+k), r.nextDouble()*2-2, true, Genome.getInnovation_number()));
				}
			}
			while(g.connections.size()==0){
				g.add_connection(r);
			}
			g.add_node(this.r);
			this.population.add(g);
		}
	}
	public void doGeneration(){
		summedScore=0;
		highestScore=Double.MIN_VALUE;
		fittestGenome=null;
		//Split genomes in species
		splitGenomesInSpecies();
		//Evaluate pop
		this.evaluatePopulation();
		for(Species s: this.population_in_species){
			s.evaluateAdjustedFitness();
		}
		//Best Genome from each species into next gen
		ArrayList<Genome> newGenomes= new ArrayList<Genome>();
		for(Species s: this.population_in_species){
			if(s.content.size()<KEEP_BEST_OF_SPECIES){
				continue;
			}
			if(s.stagnation_period>Neuroevolution.SPECIES_STAGNATION){
				continue;
			}
			Collections.sort(s.evaluatedContent,new FitnessGenomeComparator());
			newGenomes.add(s.evaluatedContent.get(0).g);
		}
		if(!newGenomes.contains(this.fittestGenome)){
			newGenomes.add(this.fittestGenome);
		}
		//Breed the rest of the genomes
		ArrayList<Species> workingSpecies= new ArrayList<Species>();
		double totalAdjFitness=0;
		for(Species s: this.population_in_species){
			if(s.stagnation_period>Neuroevolution.SPECIES_STAGNATION){
				continue;
			}
			workingSpecies.add(s);
			totalAdjFitness+=s.adjustedFitness;
		}
		//System.out.println("Working Species: "+workingSpecies.size());
		//System.out.println("Total Adj.Fitness: "+totalAdjFitness);
		ArrayList<Double> propability= new ArrayList<Double>();
		double summedP=0;
		for(Species s: workingSpecies){
			summedP+=s.adjustedFitness/totalAdjFitness;
			propability.add(summedP);
		}
		/*System.out.println("propability: ");
		for(Double d: propability){
			System.out.println(d);
		}*/
		while(newGenomes.size()<population_size){
			double rand= r.nextDouble();
			int index=0;
			while(rand>propability.get(index)){
				index++;
			}
			Species s= workingSpecies.get(index);
			if(r.nextDouble()<Neuroevolution.OFFSPRING_MUTATION_WITHOUT_CROSSOVER){
				FitnessGenome g= getRandomGenome(s);
				Genome fg = g.g.copy();
				doMutation(fg);
				newGenomes.add(fg);
			}else{
				FitnessGenome g1= getRandomGenome(s);
				FitnessGenome g2= getRandomGenome(s);
				if(g1.fitness==g2.fitness){
					//System.out.println(g1.equals(g2));
				}
				Genome child =Genome.cross_over(g1.g, g1.fitness, g2.g, g2.fitness, this.r);
				doMutation(child);
				newGenomes.add(child);
			}
			
		}
		
		this.population= newGenomes;
		for(Species s: this.population_in_species){
			s.reset(this.r);
		}
		generation++;
	}
	public void doMutation(Genome g){
		if(this.r.nextDouble()<Neuroevolution.MUTATION_CHANCE){
			g.mutate(this.r);
		}
		if(this.r.nextDouble()<Neuroevolution.ADD_CONNECTION_MUTATION){
			g.add_connection(this.r);
		}
		if(this.r.nextDouble()<Neuroevolution.ADD_NODE_MUTATION){
			g.add_node(this.r);
		}
	}
	public FitnessGenome getRandomGenome(Species s){
		double totalFitness= s.adjustedFitness* s.content.size();
		ArrayList<Double> pro= new ArrayList<Double>();
		double summedP=0;
		for(FitnessGenome g: s.evaluatedContent){
			summedP+= g.fitness/totalFitness;
			pro.add(summedP);
		}
		double rand= r.nextDouble();
		int index=0;
		while(rand>pro.get(index)){
			index++;
		}
		return s.evaluatedContent.get(index);
	}
	public void splitGenomesInSpecies(){
		A: for(Genome g: population){
			for(Species s: population_in_species){
				if(s.belongsToSpecies(g)){
					s.content.add(g);
					continue A;
				}
			}
			Species s= new Species(g);
			population_in_species.add(s);
		}
		//Remove species
		Iterator<Species> iter= this.population_in_species.iterator();
		while(iter.hasNext()){
			Species s= iter.next();
			if(s.content.size()==0){
				iter.remove();
			}
		}
	}
	/*public void evaluatePopulation(){
		for(Species s: this.population_in_species){
			for(Genome g: s.content){
				double score= this.evaluateGenome(g);
				if(score>this.highestScore){
					this.highestScore=score;
					this.fittestGenome=g;
				}
				FitnessGenome g2= new FitnessGenome(g, score);
				s.evaluatedContent.add(g2);
			}
		}
	}*/
	public abstract void evaluatePopulation();
	
	
}
class FitnessGenomeComparator implements Comparator<FitnessGenome>{

	@Override
	public int compare(FitnessGenome o1, FitnessGenome o2) {
		if( o1.fitness> o2.fitness){
			return -1;
		}else if(o1.fitness<o2.fitness){
			return 1;
		}
		return 0;
	}
	
}