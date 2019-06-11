package ea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Class used to write objects to files
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class ObjectWriter {
	/**
	 * Writes any given Object to a file
	 * <p>
	 * Note that the given object has to be serializable, else an error will be
	 * printed to the command line
	 * </p>
	 * 
	 * @param path
	 *            relative path where the object should be written to
	 * @param o
	 *            the to be written object
	 * @return true if saving was successful, false if an error occurred
	 */
	public static boolean saveObject(String path, Object o) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		boolean res = true;
		try {
			fos = new FileOutputStream(path);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
			res = false;
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return res;
	}
}
