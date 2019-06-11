package neuralnet;

import java.io.Serializable;

public abstract class Activator implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract double transform(double x);

	public abstract double transformDerivative(double x);
}
