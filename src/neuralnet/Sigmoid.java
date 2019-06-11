package neuralnet;

public class Sigmoid extends Activator {

	private static final long serialVersionUID = 2L;

	@Override
	public double transform(double x) {
		return (1 / (1 + Math.exp(-x)));
	}

	@Override
	public double transformDerivative(double x) {
		return transform(x) * (1 - transform(x));
	}

	@Override
	public String toString() {
		return "Sigmoid";
	}

}
