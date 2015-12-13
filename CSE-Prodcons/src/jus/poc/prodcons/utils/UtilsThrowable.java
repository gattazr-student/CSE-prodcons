package jus.poc.prodcons.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsThrowable {

	private final static String PATTERN_TIMESTAMP = "yyyy-MM-dd_HH-mm-ss_SSS";

	private final static SimpleDateFormat sTimeStampFormater = new SimpleDateFormat(
			PATTERN_TIMESTAMP);

	private final static String THROWABLE_MESSAGE = "(%s | %s)";

	private final static String THROWABLE_TITLE = "\n---------------- %d -- %s ----------------------";

	/**
	 * eg. ExcelFileBuilder_Error_2013-05-31_17-40-23_256.txt
	 * 
	 * @param aFileNamePrefix
	 * @return
	 */
	private static String buildErrorDumpFileName(String aFileNamePrefix) {
		return String.format("%s_Error_%s.log", aFileNamePrefix,
				sTimeStampFormater.format(new Date()));
	}

	/**
	 * @param aThrowable
	 * @return
	 */
	public static String dumpError(Throwable aThrowable) {
		StringBuilder wSB = new StringBuilder();
		int wThrowableLevel = 0;
		while (aThrowable != null) {

			wSB.append(String.format(THROWABLE_TITLE, wThrowableLevel,
					aThrowable.getClass().getName()));
			StringWriter wStack = new StringWriter();
			aThrowable.printStackTrace(new PrintWriter(wStack));
			wSB.append(UtilsString.SEP_LINE).append(wStack.toString());
			aThrowable = aThrowable.getCause();
			wThrowableLevel++;
		}

		return wSB.toString();
	}

	/**
	 * @param aThrowable
	 * @return
	 */
	public static String dumpErrorMessages(Throwable aThrowable) {
		StringBuilder wSB = new StringBuilder();
		while (aThrowable != null) {

			wSB.append(String.format(THROWABLE_MESSAGE, aThrowable.getClass()
					.getSimpleName(), aThrowable.getLocalizedMessage()));

			aThrowable = aThrowable.getCause();
		}

		return wSB.toString();
	}

	/**
	 * 
	 * <pre>
	 * /tmp/ExcelFileBuilder_Error_2013-06-01_17-43-15_560.log
	 * /var/folders/sw/7b8ph_9j3g994751npv1g7k80000gp/T/ExcelFileBuilder_Error_2013-06-01_17-47-00_657.log
	 * </pre>
	 * 
	 * @param aFileNamePrefix
	 * @param aThrowable
	 * @return the absoutepath of the created error file
	 */
	public static String storeErrorDumpInTmp(String aFileNamePrefix,
			Throwable aThrowable) {

		File wTempDirFile = new File(System.getProperty("java.io.tmpdir"));

		File wErrorDumpFile = new File(wTempDirFile,
				buildErrorDumpFileName(aFileNamePrefix));

		String wDump = dumpError(aThrowable);
		try {
			UtilsFileText.writeAllUtf8(wErrorDumpFile, wDump);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(wDump);
		}
		return wErrorDumpFile.getAbsolutePath();

	}
}
