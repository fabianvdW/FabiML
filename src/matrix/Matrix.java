package matrix;

import java.io.Serializable;
import java.util.function.DoubleFunction;;

public class Matrix implements Serializable {

	private static final long serialVersionUID = 10L;
	private double[][] matrix;
	public int hoehe;
	public int breite;

	public Matrix(int hoehe, int breite) {
		this.hoehe = hoehe;
		this.breite = breite;
	}

	public Matrix transpose() {
		Matrix res = new Matrix(this.breite, this.hoehe);
		double[][] data = new double[this.breite][this.hoehe];
		for (int i = 0; i < this.breite; i++) {
			for (int j = 0; j < this.hoehe; j++) {
				data[i][j] = this.matrix[j][i];
			}
		}
		res.setMatrix(data);
		return res;
	}

	public void addRow(double[] row) {
		if (row.length != this.breite) {
			throw new IllegalArgumentException("Row does not correspond with dimension of Matrix");
		}
		this.hoehe += 1;
		double[][] newM = new double[this.hoehe][this.breite];
		for (int i = 0; i < this.hoehe - 1; i++) {
			newM[i] = this.matrix[i];
		}
		newM[this.hoehe - 1] = row;
		this.matrix = newM;
	}

	public Matrix add(Matrix m) {
		if (m.breite != this.breite || m.hoehe != this.hoehe) {
			throw new IllegalArgumentException("Given Matrix does not have corresponding dimensions!");
		}
		Matrix res = new Matrix(this.hoehe, this.breite);
		double[][] resM = new double[this.hoehe][this.breite];
		for (int i = 0; i < this.hoehe; i++) {
			double[] resRow = new double[this.breite];
			for (int j = 0; j < this.breite; j++) {
				resRow[j] = this.matrix[i][j] + m.getMatrix()[i][j];
			}
			resM[i] = resRow;
		}
		res.setMatrix(resM);
		return res;
	}

	public Matrix multiply(double d) {
		Matrix res = new Matrix(this.hoehe, this.breite);
		double[][] resM = new double[this.hoehe][this.breite];
		for (int i = 0; i < this.hoehe; i++) {
			double[] resRow = new double[this.breite];
			for (int j = 0; j < this.breite; j++) {
				resRow[j] = this.matrix[i][j] * d;
			}
			resM[i] = resRow;
		}
		res.setMatrix(resM);
		return res;
	}

	public Matrix subMatrix(int startingHeight, int endHeight) {
		if (startingHeight < 0 || endHeight > this.hoehe || startingHeight > endHeight) {
			throw new IllegalArgumentException();
		}
		Matrix res = new Matrix(endHeight + 1 - startingHeight, this.breite);
		double[][] newM = new double[endHeight + 1 - startingHeight][this.breite];
		for (int i = 0; i < newM.length; i++) {
			double[] newMRow = this.matrix[startingHeight + i];
			newM[i] = newMRow;
		}
		res.setMatrix(newM);
		return res;
	}

	public Matrix multiply(Matrix m) {
		Matrix res = new Matrix(this.hoehe, m.breite);
		if (this.breite == m.hoehe) {
			double[][] resM = new double[this.hoehe][m.breite];
			for (int i = 0; i < this.hoehe; i++) {
				double[] arr1 = this.matrix[i];
				resM[i] = new double[m.breite];
				for (int k = 0; k < m.breite; k++) {
					double sum = 0;
					for (int j = 0; j < arr1.length; j++) {
						sum += arr1[j] * m.getMatrix()[j][k];
					}
					resM[i][k] = sum;
				}
			}
			res.setMatrix(resM);
			return res;
		} else {
			throw new MatrixDimensionException("The given Matrix does not have the given dimensions (" + this.breite
					+ ",X" + ") to be multiplied");
		}
	}

	public Matrix applyFunctionOnMatrix(DoubleFunction<Double> aMethod) {
		Matrix m = new Matrix(this.hoehe, this.breite);
		double[][] mAfter = new double[this.hoehe][this.breite];
		for (int i = 0; i < this.matrix.length; i++) {
			double[] mArr = new double[this.breite];
			for (int j = 0; j < this.breite; j++) {
				mArr[j] = aMethod.apply(this.matrix[i][j]);
			}
			mAfter[i] = mArr;
		}
		m.setMatrix(mAfter);
		return m;
	}

	public Matrix copy() {
		Matrix newMe = new Matrix(this.hoehe, this.breite);
		// Kopieren der Arrays
		double[][] newMatrix = new double[this.hoehe][this.breite];
		for (int i = 0; i < this.matrix.length; i++) {
			double[] newArr = new double[this.breite];
			for (int j = 0; j < this.matrix[i].length; j++) {
				newArr[j] = this.matrix[i][j];
			}
			newMatrix[i] = newArr;
		}
		newMe.setMatrix(newMatrix);
		return newMe;
	}

	public boolean istGueltigeMatrix(double[][] matrix) {
		if (matrix == null)
			return false;
		if (matrix.length == this.hoehe) {
			for (double[] arr : matrix) {
				if (arr.length != this.breite) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void setMatrix(double[][] matrix) {
		if (istGueltigeMatrix(matrix)) {
			this.matrix = matrix;
		} else {
			throw new MatrixDimensionException(
					"Invalid Dimensions: The given matrix does not match with the given dimensions " + this.hoehe + ","
							+ this.breite);
		}
	}

	public double[][] getMatrix() {
		return this.matrix;
	}

	public String toExactString() {
		String s = "";
		int maxLength = -1;
		int[][] lengths = new int[this.hoehe][this.breite];
		for (int i = 0; i < this.matrix.length; i++) {
			double[] reihe = this.matrix[i];
			for (int j = 0; j < reihe.length; j++) {
				double d = reihe[j];
				double rounded = d;
				int length = String.valueOf(rounded).length();
				lengths[i][j] = length;
				if (length > maxLength) {
					maxLength = length;
				}
			}
		}
		s += "[";
		for (int i = 0; i < this.matrix.length; i++) {
			if (i == 0) {
				s += "[";
			} else {
				s += " [";
			}
			for (int j = 0; j < this.matrix[i].length; j++) {
				double rounded = this.matrix[i][j];
				s += rounded;
				for (int m = 0; m < maxLength - lengths[i][j]; m++) {
					s += " ";
				}
				if (j != this.matrix[i].length - 1) {
					s += ", ";
				}
			}
			if (i != this.matrix.length - 1) {
				s += "]\n";
			} else {
				s += "]]\n";
			}
		}
		return s;
	}

	@Override
	public String toString() {
		String s = "";
		int maxLength = -1;
		int[][] lengths = new int[this.hoehe][this.breite];
		for (int i = 0; i < this.matrix.length; i++) {
			double[] reihe = this.matrix[i];
			for (int j = 0; j < reihe.length; j++) {
				double d = reihe[j];
				double rounded = d * 100;
				rounded -= rounded % 1;
				rounded /= 100;
				int length = String.valueOf(rounded).length();
				lengths[i][j] = length;
				if (length > maxLength) {
					maxLength = length;
				}
			}
		}
		s += "[";
		for (int i = 0; i < this.matrix.length; i++) {
			if (i == 0) {
				s += "[";
			} else {
				s += " [";
			}
			for (int j = 0; j < this.matrix[i].length; j++) {
				double rounded = this.matrix[i][j] * 100;
				rounded -= rounded % 1;
				rounded /= 100;
				s += rounded;
				for (int m = 0; m < maxLength - lengths[i][j]; m++) {
					s += " ";
				}
				if (j != this.matrix[i].length - 1) {
					s += ", ";
				}
			}
			if (i != this.matrix.length - 1) {
				s += "]\n";
			} else {
				s += "]]\n";
			}
		}
		return s;
	}
}

class MatrixDimensionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatrixDimensionException() {
		super();
	}

	public MatrixDimensionException(String s) {
		super(s);
	}

	public MatrixDimensionException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public MatrixDimensionException(Throwable throwable) {
		super(throwable);
	}
}
