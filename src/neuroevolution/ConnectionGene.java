package neuroevolution;

import java.io.Serializable;

public class ConnectionGene implements Serializable,Comparable<ConnectionGene>{

	private static final long serialVersionUID = 1L;
	NodeGene in;
	NodeGene out;
	double weight;
	boolean enabled;
	int innovation_number;
	String genetical_info;
	public ConnectionGene(NodeGene in, NodeGene out,double weight, boolean enabled, int innovation_number,String info){
		this.in=in;
		this.out=out;
		this.weight=weight;
		this.enabled=enabled;
		this.innovation_number=innovation_number;
		this.genetical_info=info;
	}
	public ConnectionGene(NodeGene in, NodeGene out, double weight, boolean enabled, int innovation_number){
		this(in,out,weight,enabled,innovation_number,"");
	}
	public ConnectionGene copy(){
		ConnectionGene cg = new ConnectionGene(this.in, this.out, this.weight, this.enabled, this.innovation_number,this.genetical_info);
		return cg;
	}
	public void disable(){
		this.enabled=false;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof ConnectionGene){
			ConnectionGene cg = (ConnectionGene) o;
			if(cg.innovation_number==this.innovation_number){
				return true;
			}else if(cg.in.equals(this.in)&& cg.out.equals(this.out)|| cg.in.equals(this.out)&&cg.out.equals(this.in)){
				return true;
			}
			
		}
		return false;
	}
	@Override
	public int compareTo(ConnectionGene o) {
		ConnectionGene c = (ConnectionGene)o;
		if(c.innovation_number>this.innovation_number){
			return -1;
		}else if(this.innovation_number>c.innovation_number){
			return 1;
		}else{
			return 0;
		}
	}
}
