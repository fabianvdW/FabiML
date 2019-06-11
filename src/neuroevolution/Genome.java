package neuroevolution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import neuroevolution.NodeGene.Type;

public class Genome implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static double WEIGHT_PERTURBE_CHANCE=0.9;//USED
	public final static double WEIGHT_NEW_VALUE_CHANCE=1-WEIGHT_PERTURBE_CHANCE;//USED
	public final static double C1=1.0;//USED
	public final static double C2=1.0;//USED
	public final static double C3=.4;//USED

	private static int innovation_number=1;
	public ArrayList<ConnectionGene> connections;
	public ArrayList<NodeGene> nodegenes;
	public ArrayList<NodeGene> notInputs;
	public static double sigmoidal_transfer(double x){
		return 1/(1+Math.exp(-5*x));
	}
	public Genome(){
		this.nodegenes= new ArrayList<NodeGene>();
		this.connections= new ArrayList<ConnectionGene>();
		this.notInputs= new ArrayList<NodeGene>();
	}
	public Genome copy(){
		Genome g = new Genome();
		for(ConnectionGene cg: connections){
			g.connections.add(cg.copy());
		}
		for(NodeGene ng: this.nodegenes){
			g.addNodeGene(ng);
		}
		return g;
	}
	public void addNodeGene(NodeGene d){
		nodegenes.add(d);
		if(d.type!=Type.INPUT){
			notInputs.add(d);
		}
	}
	public ArrayList<NodeGene> getNodeGenes(){
		return this.nodegenes;
	}
	public double[] feedForward(double[] input){
		ArrayList<NodeGene> inputs= new ArrayList<NodeGene>();
		ArrayList<NodeGene> hiddens= new ArrayList<NodeGene>();
		ArrayList<NodeGene> output= new ArrayList<NodeGene>();
		for(NodeGene g: this.nodegenes){
			if(g.type==Type.INPUT){
				inputs.add(g);
			}else if(g.type==Type.HIDDEN){
				hiddens.add(g);
			}else{
				output.add(g);
			}
		}
		if(inputs.size()!=input.length){
			throw new RuntimeException();
		}
		Genome g2= this.copy();
		//Remove all ConnectionGenes which are not enabledf
		ArrayList<ConnectionGene> notEnabled = new ArrayList<ConnectionGene>();
		for(ConnectionGene cg: g2.connections){
			if(!cg.enabled){
				notEnabled.add(cg);
			}
		}
		for(ConnectionGene cg: notEnabled){
			g2.connections.remove(cg);
		}
		//Remove hidden Neurons wiht 0 inputs
		ArrayList<NodeGene> tobeRemoved= new ArrayList<NodeGene>();
		for(NodeGene ng: hiddens){
			boolean thereIsAnInput=false;
			ArrayList<ConnectionGene> toBeRemoved= new ArrayList<ConnectionGene>();
			for(ConnectionGene cg: g2.connections){
					if(cg.out.equals(ng)){
						thereIsAnInput=true;
						break;
					}else if(cg.in.equals(ng)){
						toBeRemoved.add(cg);
					}
				
			}
			if(!thereIsAnInput){
				tobeRemoved.add(ng);
				for(ConnectionGene cg: toBeRemoved){
					g2.connections.remove(cg);
				}
			}
		}
		for(NodeGene ng: tobeRemoved){
			hiddens.remove(ng);
		}
		HashMap<NodeGene,NodeOutput> bisherBerechneteOutputs = new HashMap<NodeGene,NodeOutput>();
		//Inputs hinzufügen
		for(int i=0;i<inputs.size();i++){
			NodeGene ng = inputs.get(i);
			double d= input[i];
			bisherBerechneteOutputs.put(ng, new NodeOutput(ng, d,false));
		}
		//Input zu hidden
		while(hiddens.size()>0){
			//System.out.println(hiddens.size());
			boolean foundOne=false;
			NodeGene del=null;
			for(NodeGene hidden : hiddens){
				boolean alleInfoVorhanden=true;
				for(ConnectionGene cg: g2.connections){
					if(cg.out.equals(hidden)){
						if(!bisherBerechneteOutputs.containsKey(cg.in)){
							alleInfoVorhanden=false;
							break;
						}
					}
				}
				if(alleInfoVorhanden){
					foundOne=true;
					double inputSum=0;
					ArrayList<ConnectionGene> toBeRemoved= new ArrayList<ConnectionGene>();
					for(ConnectionGene cg: g2.connections){
						if(cg.out.equals(hidden)){
							inputSum+=cg.weight* bisherBerechneteOutputs.get(cg.in).output;
							toBeRemoved.add(cg);
						}
					}
					for(ConnectionGene cg: toBeRemoved){
						g2.connections.remove(cg);
					}
					del=hidden;
					bisherBerechneteOutputs.put(hidden, new NodeOutput(hidden, inputSum,true));
					break;
				}
			}
			if(foundOne){
				foundOne=false;
				hiddens.remove(del);
			}else{
				System.out.println(this.toString());
				System.out.println(g2.toString());
				System.out.println("---------------------------------");
				for(NodeGene ng: hiddens){
					System.out.println(ng.num);
				}
				for(NodeGene ng: bisherBerechneteOutputs.keySet()){
					NodeOutput no = bisherBerechneteOutputs.get(ng);
					System.out.println("NodeGene: "+ng.num+", Output: "+no.output);
					System.out.println("Contains: "+bisherBerechneteOutputs.containsKey(new NodeGene(ng.type, ng.num)));
				}
				GenomeVisualizer.printGenome(this, "fail.png");
				throw new RuntimeException();
			}
		}
		double[] outputs= new double[output.size()];
		for(int i=0;i<output.size();i++){
			NodeGene ng = output.get(i);
			double inputSum= 0;
			for(ConnectionGene cg: g2.connections){
				if(cg.out.equals(ng)){
					NodeOutput no= bisherBerechneteOutputs.get(cg.in);
					if(no==null){
						throw new RuntimeException();
					}
					inputSum+=cg.weight* no.output;
				}
			}
			outputs[i]= Genome.sigmoidal_transfer(inputSum);
		}
		return outputs;
	}
	public void mutate(Random r){
		for(ConnectionGene g: this.connections){
			if(r.nextDouble()<Neuroevolution.MUTATION_WEIGHT_CHANCE){
				if(r.nextDouble()<WEIGHT_PERTURBE_CHANCE){
					g.weight=g.weight*(r.nextDouble()*0.2+0.9);
				}else{
					g.weight=r.nextDouble()*4-2;
				}
			}
		}
	}
	public void add_connection(Random r){
		NodeGene n1 = nodegenes.get(r.nextInt(nodegenes.size()));
		NodeGene n2= nodegenes.get(r.nextInt(nodegenes.size()));
		//System.out.println("Attempted Connection: "+n1.num+" - "+n2.num);
		if(n1.equals(n2)){
			//System.out.println("Gleiche Node");
			return;
		}
		if(n1.type==Type.INPUT&& n2.type==Type.INPUT||n1.type==Type.OUTPUT&& n2.type==Type.OUTPUT){
			//System.out.println("Gleicher Typ");
			return;
		}
		if(n1.type==Type.HIDDEN&& n2.type==Type.INPUT|| n1.type==Type.OUTPUT&& n2.type==Type.INPUT || n1.type==Type.OUTPUT&& n2.type==Type.HIDDEN){
			//System.out.println("Reversed");
			NodeGene local = n1;
			n1=n2;
			n2=local;
		}
		//Überprüfe ob es Connection bereits gibt
		for(ConnectionGene con: this.connections){
			if(con.in.equals(n1)&&con.out.equals(n2)||con.in.equals(n2)&&con.out.equals(n1)){
				if(!con.enabled){
					con.enabled=true;
				}else{
				}
				return;
			}
		}
		ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(this,n1);
		if(verhindereKreis.contains(n2)){
			//System.out.println("Kreis verhindert");
			return;
		}
		ConnectionGene newConnection = new ConnectionGene(n1, n2, r.nextDouble()*2-1, true, Genome.getInnovation_number(),"Mutation in Generation: "+Neuroevolution.generation);
		this.connections.add(newConnection);
	}
	public static ArrayList<NodeGene> recursive_all_input_nodes(Genome g, NodeGene n){
		ArrayList<NodeGene> nodes= new ArrayList<NodeGene>();
		if(n.type==Type.INPUT){
			return nodes;
		}
		for(ConnectionGene cn: g.connections){
			if(cn.out.equals(n)){
				nodes.add(cn.in);
				nodes.addAll(recursive_all_input_nodes(g,cn.in));
			}
		}
		return nodes;
	}
	public void add_node(Random r){
		if(this.connections.size()==0){
			return;
		}
		ConnectionGene g = this.connections.get(r.nextInt(this.connections.size()));
		g.disable();
		NodeGene in = g.in;
		NodeGene out= g.out;
		NodeGene n = new NodeGene(Type.HIDDEN, nodegenes.size()+1);
		//Von in zu n
		ConnectionGene inn = new ConnectionGene(in, n, 1, true, Genome.getInnovation_number(),"Mutation in Generation: "+Neuroevolution.generation);
		ConnectionGene nout = new ConnectionGene(n,out,g.weight,true,Genome.getInnovation_number(),"Mutation in Generation: "+Neuroevolution.generation);
		this.addNodeGene(n);
		connections.add(inn);
		connections.add(nout);
	}
	public static int getInnovation_number(){
		return innovation_number++;
	}
	public static Genome cross_over(Genome g_1, double fitness_g1, Genome g_2, double fitness_g2,Random r){
		Genome g1= g_1.copy();
		Genome g2= g_2.copy();
		Genome child = new Genome();
		if(fitness_g1==fitness_g2){
			for(NodeGene ng: g1.nodegenes){
				child.addNodeGene(ng);
			}
			for(NodeGene g: g2.nodegenes){
				if(!child.nodegenes.contains(g)){
					child.addNodeGene(g);
				}
			}
		}else if(fitness_g1>fitness_g2){
			for(NodeGene ng: g1.nodegenes){
				child.addNodeGene(ng);
			}
		}else{
			for(NodeGene ng: g2.nodegenes){
				child.addNodeGene(ng);
			}
		}
		String added= g_1.equals(g_2)? "TRUE ":"FALSE";
		
		//Lining up connections
		if(fitness_g1==fitness_g2){
			int g2index=0;
			for(ConnectionGene conGene: g1.connections){
				boolean addedGene=false;
				if(g2index!=g2.connections.size()){
					while(conGene.innovation_number>g2.connections.get(g2index).innovation_number){
						g2.connections.get(g2index).genetical_info=added+"EQUALFITNESS-DIsJOINT-Inherited through Cross-over in Generation: "+Neuroevolution.generation;
						//Kreis verhindern
						ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,g2.connections.get(g2index).in);
						if(verhindereKreis.contains(g2.connections.get(g2index).out)){
							
						}else{
							child.connections.add(g2.connections.get(g2index));
						}
						g2index+=1;
						if(g2index==g2.connections.size()){
							break;
						}
					}
					if(g2index!=g2.connections.size()){
						if(conGene.innovation_number==g2.connections.get(g2index).innovation_number){
							addedGene=true;
							if(r.nextBoolean()){
								conGene.genetical_info=added+"EQUALFITNESS-MATCHING-Inherited through Cross-over in Generation: "+Neuroevolution.generation;
								child.connections.add(conGene);
							}else{
								g2.connections.get(g2index).genetical_info=added+"EQUALFITNESS-MATCHING-Inherited through Cross-over in Generation: "+Neuroevolution.generation;
								child.connections.add(g2.connections.get(g2index));
							}
							g2index++;
						}
					}
				}
				if(!addedGene){
					conGene.genetical_info=added+"EQUALFITNESS -DISJOINT-Inherited through Cross-over in Generation: "+Neuroevolution.generation;
					ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,conGene.in);
					if(verhindereKreis.contains(conGene.out)){
						
					}else{
						child.connections.add(conGene);
					}
				}
			}
			while(g2index<g2.connections.size()){
				g2.connections.get(g2index).genetical_info=added+"EQUALFITNESS-EXCESS-Inherited through Cross-over in Generation: "+Neuroevolution.generation;
				ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,g2.connections.get(g2index).in);
				if(verhindereKreis.contains(g2.connections.get(g2index).out)){
					
				}else{
					child.connections.add(g2.connections.get(g2index));
				}
				g2index+=1;
			}
		}else{
			if(fitness_g2>fitness_g1){
				Genome local = g1;
				g1=g2;
				g2=local;
			}
			for(ConnectionGene conGene: g1.connections){
				if(g2.connections.contains(conGene)){
					if(r.nextBoolean()){
						ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,conGene.in);
						if(verhindereKreis.contains(conGene.out)){
							
						}else{
							conGene.genetical_info= "MATCHING-PARENT1 Inherited through Cross-over in Generation: "+Neuroevolution.generation;
							child.connections.add(conGene);
						}
					}else{
						ConnectionGene newCon=null;
						for(ConnectionGene conGene2:g2.connections){
							if(conGene2.equals(conGene)){
								newCon=conGene2;
								break;
							}
						}
						if(newCon==null){
							throw new RuntimeException();
						}
						newCon.genetical_info="MATCHING-PARENT2 Inherited through Cross-over in Generation: "+Neuroevolution.generation;
						ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,newCon.in);
						if(verhindereKreis.contains(newCon.out)){
							
						}else{
							child.connections.add(newCon);
						}
					}
				}else{//Disjoint or excess
					conGene.genetical_info="DISJOINT OR EXCESS- Inherited through Cross-over in Generation: "+Neuroevolution.generation;
					ArrayList<NodeGene> verhindereKreis = recursive_all_input_nodes(child,conGene.in);
					if(verhindereKreis.contains(conGene.out)){
						
					}else{
						child.connections.add(conGene);
					}
				}
			}
		}
		Collections.sort(child.connections);
		return child;
	}
	public static double compatibility_threshold(Genome g1, Genome g2){
		int maxInnovationg1= g1.connections.get(g1.connections.size()-1).innovation_number;
		int maxInnovationg2= g2.connections.get(g2.connections.size()-1).innovation_number;
		if(maxInnovationg2>maxInnovationg1){
			Genome local =g1;
			g1=g2;
			g2=local;
		}
		int excess=0;
		int disjoint=0;
		int matching=0;
		double weight_diff=0;
		int g1Index=0;
		for(ConnectionGene conGene: g2.connections){
			if(g1Index!=g1.connections.size()){
				ConnectionGene conGene2= g1.connections.get(g1Index);
				while(conGene2.innovation_number<conGene.innovation_number){
					disjoint++;
					g1Index++;
					if(g1Index==g1.connections.size()){
						break;
					}else{
						conGene2= g1.connections.get(g1Index);
					}
					
				}
				if(g1Index!=g1.connections.size()){
					if(conGene2.innovation_number==conGene.innovation_number){
						g1Index++;
						matching++;
						weight_diff+=conGene.weight-conGene2.weight;
					}else{
						disjoint++;
					}
				}else{
					//throw new RuntimeException();
				}
			}else{
				disjoint++;
			}
		}
		excess=g1.connections.size()-g1Index;
		//double genomes= g1.connections.size()>g2.connections.size()? g1.connections.size(): g2.connections.size();
		//System.out.println("Excess: "+excess+", Matching: "+matching+", Disjoint: "+disjoint);
		double ergebnis=0;
		if(matching==0){
			ergebnis= C1*excess+C2*disjoint;
		}else{
			ergebnis= C1*excess+C2*disjoint+C3*weight_diff/(matching+0.0);
		}
		return ergebnis ;
	}
	@Override
	public String toString(){
		String s="";
		for(NodeGene n: this.nodegenes){
			s+="NodeGene: "+n.type+", Num: "+n.num+"\n";
		}
		for(ConnectionGene g: this.connections){
			s+="ConnectionGene: In( "+g.in.num+" ), Out( "+g.out.num+" ), Weight: "+g.weight+" ,Enabled: "+g.enabled+", Innovation Number: "+g.innovation_number+", "+g.genetical_info+"\n";
		}
		return s;
	}
}
class NodeOutput{
	NodeGene g;
	double input;
	double output;
	public NodeOutput(NodeGene g, double input,boolean transfer){
		this.input=input;
		this.g=g;
		if(transfer){
		this.output=Genome.sigmoidal_transfer(this.input);
		}else{
			this.output=this.input;
		}
	}
}
class NodeFeedForwardObject{
	NodeGene g;
	ArrayList<ConnectionGene> in;
	ArrayList<ConnectionGene> out;
	public NodeFeedForwardObject(NodeGene g,ArrayList<ConnectionGene> in, ArrayList<ConnectionGene> out){
		this.g=g;
		this.in=in;
		this.out=out;
	}
}
