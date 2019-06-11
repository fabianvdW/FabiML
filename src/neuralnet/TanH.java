package neuralnet;

public class TanH extends Activator {

	private static final long serialVersionUID = 3L;

	@Override
	public double transform(double x) {
		return 2 * (1 / (1 + Math.exp(-x))) - 1;
	}

	@Override
	public double transformDerivative(double x) {
		return 1 - transform(x) * transform(x);
	}

	@Override
	public String toString() {
		return "TanH";
	}
}