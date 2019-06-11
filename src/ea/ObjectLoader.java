package ea;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Class used to load objects from files
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class ObjectLoader {
	/**
	 * Loads Objects from a path
	 * <p>
	 * Note that this method does not throw errors, it just prints any to the
	 * console. If an error occurs, null will be returned
	 * </p>
	 * 
	 * @param path
	 *            relative path where the object that is to be loaded is stored
	 * @return the given object at the path, or null if an error occurs
	 */
	public static Object loadObject(String path) {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
