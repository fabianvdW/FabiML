package mnist;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import matrix.Matrix;

public class MnistDataReader {
	Matrix trainD;
	Matrix trainL;
	Matrix testD;
	Matrix testL;

	public MnistDataReader(String relative_path) {
		this.trainD = this.readData(relative_path + "train-images.idx3-ubyte");
		this.trainL = this.readLabel(relative_path + "train-labels.idx1-ubyte");
		this.testD = this.readData(relative_path + "t10k-images.idx3-ubyte");
		this.testL = this.readLabel(relative_path + "t10k-labels.idx1-ubyte");
	}

	public Matrix readLabel(String path) {
		DataInputStream diS = null;
		try {
			File file = new File(path);
			diS = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			int magicNumber = diS.readInt();
			if (magicNumber != 2049) {
				throw new IOException("magic number not 2049");
			}
			int labels = diS.readInt();
			double[][] labelM = new double[labels][10];
			for (int i = 0; i < labels; i++) {
				double[] labelRow = new double[10];
				int label = (int) (diS.readUnsignedByte());
				for (int j = 0; j < 10; j++) {
					labelRow[j] = 0;
				}
				labelRow[label] = 1;
				labelM[i] = labelRow;

			}
			Matrix res = new Matrix(labels, 10);
			res.setMatrix(labelM);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				diS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Matrix readData(String path) {
		DataInputStream diS = null;
		try {
			File file = new File(path);
			diS = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			int magicNumber = diS.readInt();
			if (magicNumber != 2051) {
				throw new IOException("magic number not 2049");
			}
			int numImages = diS.readInt();
			int height = diS.readInt();
			int width = diS.readInt();
			if (height != 28 || width != 28) {
				throw new IOException("Width or height does not match");
			}
			double[][] data = new double[numImages][width * height];
			for (int i = 0; i < numImages; i++) {
				double[] dataRow = new double[width * height];
				for (int j = 0; j < height; j++) {
					for (int k = 0; k < width; k++) {
						dataRow[j * height + k] = diS.readUnsignedByte();
					}
				}

				data[i] = dataRow;
			}
			Matrix res = new Matrix(numImages, height * width);
			res.setMatrix(data);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				diS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
