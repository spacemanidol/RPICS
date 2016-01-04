package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class StreamHelper {
	public static String getString(InputStream stream, int length) {
		byte[] buffer = new byte[length];
		try {
			stream.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buffer);
	}

	public static String getContents(File file) {
		StringBuffer buffer = new StringBuffer();
		FileReader fw;
		try {
			fw = new FileReader(file);
			while (fw.ready()) {
				buffer.append((char) fw.read());
			}
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
