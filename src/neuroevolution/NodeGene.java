package neuroevolution;

import java.io.Serializable;

public class NodeGene implements Serializable {

	private static final long serialVersionUID = 1L;
	public enum Type{
		INPUT,
		HIDDEN,
		OUTPUT;
	}
	Type type;
	int num;
	public NodeGene(Type type, int num){
		this.type=type;
		this.num=num;
	}
	@Override
	  public int hashCode()
	  {
	     return this.num;
	  }
	@Override
	public boolean equals(Object o){
		if(o instanceof NodeGene){
			NodeGene n= (NodeGene) o;
			return(n.num==this.num);
		}
		return false;
	}
}
