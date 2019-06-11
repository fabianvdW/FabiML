package neuralnet;

public class Relu extends Activator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	@Override
	public double transform(double x) {
		return Math.max(0, x);
	}

	@Override
	public double transformDerivative(double x) {
		if (x < 0) {
			return 0.0;
		}
		return 1.0;
	}

	@Override
	public String toString() {
		return "Relu";
	}
}
