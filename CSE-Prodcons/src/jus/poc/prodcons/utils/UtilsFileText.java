package jus.poc.prodcons.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * 
 * @author gattazr
 * 
 */
public class UtilsFileText {

	public final static String ENCODING_UTF8 = "UTF-8";

	/**
	 * @param aFile
	 *            the file to read
	 * @param aEncoding
	 *            the char encoding. eg. "UTF-8"
	 * @return
	 * @throws Exception
	 */
	public static String readAll(File aFile, String aEncoding)
			throws Exception {
		return new String(readAllBytes(aFile), aEncoding);
	}

	/**
	 * @param aFile
	 *            the file to read
	 * @return the byte array
	 * @throws Exception
	 */
	public static byte[] readAllBytes(File aFile) throws Exception {
		FileInputStream wStream = new FileInputStream(aFile);
		int wSize = wStream.available();
		byte[] wData = new byte[wSize];
		wStream.read(wData);
		wStream.close();
		return wData;
	}

	/**
	 * @param aFile
	 *            the file to read
	 * @return
	 * @throws Exception
	 */
	public static String readAllUtf8(File aFile) throws Exception {
		return readAll(aFile, ENCODING_UTF8);
	}

	/**
	 * @param aFile
	 * @param aString
	 * @param aEncoding
	 * @throws Exception
	 */
	public static void writeAll(File aFile, String aString, String aEncoding)
			throws Exception {
		// you don't need to create a File object, FileWriter takes a string for
		// the filepath as well

		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(aFile), aEncoding));
		writer.write(aString);

		writer.close();
	}

	/**
	 * @param aFile
	 * @param aString
	 * @throws Exception
	 */
	public static void writeAllUtf8(File aFile, String aString)
			throws Exception {
		writeAll(aFile, aString, ENCODING_UTF8);
	}
}
