package jus.poc.prodcons.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gattazr
 */

public class SimpleLogger implements ISimpleLogger {

	static final String DUMMY_SHORT_HASHCODE = "0000";

	static final String EMPTY = "";

	// the width of the thread column
	static final int LENGTH_THREADNAME = 16;

	// the width of the what column
	static final int LENGTH_WHAT = 25;

	// the width of the who column
	static final int LENGTH_WHO = 25;

	private final static Level[] LEVELS = {Level.OFF, Level.INFO, Level.FINE,
			Level.FINER, Level.FINEST, Level.ALL, Level.CONFIG, Level.SEVERE,
			Level.WARNING};

	static final String NULL = "null";

	private final static String LOG_PREFIX_LEVEL = "LEVEL";

	private final static String LOG_PREFIX_LOG = "LOG";

	public final static SimpleLogger out = new SimpleLogger();

	private final static String PATTERN_TIMESTAMP = "yyyy-MM-dd_HH-mm-ss_SSS";

	static final char REPLACE_COLUMN = '_';

	static final char SEP_COLUMN = '|';

	static final String SEP_COLUMN_DELIM = SEP_COLUMN + " ";

	private final static char SEP_LINE = '\n';

	private final static SimpleDateFormat sTimeStampFormater = new SimpleDateFormat(
			PATTERN_TIMESTAMP);

	private final static String THROWABLE_TITLE = "\n---------------- %d -- %s ----------------------";

	/**
	 * @param aLevel
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public static String buildLogLine(final Thread aThread, final Object aWho,
			final CharSequence aWhat, final Object... aInfos) {

		String wLogText = buildLogText(aInfos);

		String wLogWho = buildWhoObjectId(aWho);

		String wLogWhat = (aWhat != null) ? aWhat.toString() : NULL;

		return formatLine(aThread.getName(), wLogWho, wLogWhat, wLogText);
	}

	/**
	 * @param aSB
	 *            a stringbuffer to be appended
	 * @param aObjects
	 *            a table of object
	 * @return the given StringBuffer
	 */
	public static String buildLogText(final Object... aObjects) {

		if ((aObjects == null) || (aObjects.length == 0)) {
			return EMPTY;
		}

		StringBuilder wSB = new StringBuilder(128);

		// converts null of Thowable to strings
		Object wObj;
		for (int wI = 0; wI < aObjects.length; wI++) {
			wObj = aObjects[wI];
			if (wObj == null) {
				aObjects[wI] = NULL;
			} else if (wObj instanceof Throwable) {
				aObjects[wI] = formatThrowable((Throwable) wObj);
			} else if (aObjects[wI].getClass().isArray()) {
				aObjects[wI] = Arrays.toString((Object[]) wObj);
			}
		}

		// if there is only one info
		if (aObjects.length == 1) {
			return wSB.append(String.valueOf(aObjects[0])).toString();
		}

		// if the first object is a format, return the result of the
		// String.format() method
		if (aObjects[0].toString().indexOf('%') > -1) {
			return wSB
					.append(String.format(aObjects[0].toString(),
							UtilsArray.removeOneObject(aObjects, 0)))
					.toString();
		}

		// builds the text by appending the string value of each object.
		boolean wIsId = false;
		boolean wIsValue = false;
		String wStr;
		final int wMax = aObjects.length;
		for (int wI = 0; wI < wMax; wI++) {
			wIsValue = wIsId;
			wStr = String.valueOf(aObjects[wI]);
			wIsId = wStr.endsWith("=");

			if (wIsValue) {
				wSB.append('[');
			}

			wSB.append(wStr);

			if (wIsValue) {
				wSB.append(']');
			}
			if (!wIsId) {
				wSB.append(' ');
			}
		}
		return wSB.toString();
	}

	/**
	 * @param aWho
	 * @return
	 */
	public static String buildWhoObjectId(final Object aWho) {

		if (aWho == null) {
			return NULL;
		}

		if (aWho instanceof Class) {
			return ((Class<?>) aWho).getName() + '_' + DUMMY_SHORT_HASHCODE;
		}

		return new StringBuilder().append(aWho.getClass().getName()).append('_')
				.append(UtilsString.strAdjustRight(aWho.hashCode(), 4))
				.toString();
	}

	/**
	 * @param aSourceClassName
	 * @param aSourceMethodName
	 * @param aText
	 * @return
	 */
	public static String formatLine(final String aThreadName,
			final String aSourceClassName, final String aSourceMethodName,
			final String aText) {

		// clean the buffer
		StringBuilder wSB = new StringBuilder();

		wSB.append(formatThreadName(aThreadName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatWho(aSourceClassName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatWhat(aSourceMethodName));

		wSB.append(SEP_COLUMN_DELIM);
		wSB.append(formatText(aText));

		return wSB.toString();
	}

	/**
	 * @param aText
	 * @return
	 */
	public static String formatText(final String aText) {

		return (aText == null) ? NULL : aText;
	}

	/**
	 * @param aSB
	 * @param aThreadName
	 * @return
	 */
	public static String formatThreadName(final String aThreadName) {

		return UtilsString.strAdjustRight(aThreadName, LENGTH_THREADNAME, ' ');
	}

	/**
	 * @param e
	 * @return
	 */
	public static String formatThrowable(Throwable aThrowable) {
		StringBuilder wSB = new StringBuilder();
		int wThrowableLevel = 0;
		while (aThrowable != null) {
			wSB.append(String.format(THROWABLE_TITLE, wThrowableLevel,
					aThrowable.getClass().getName()));
			StringWriter wStack = new StringWriter();
			aThrowable.printStackTrace(new PrintWriter(wStack));
			wSB.append(SEP_LINE).append(wStack.toString());
			aThrowable = aThrowable.getCause();
			wThrowableLevel++;
		}

		return wSB.toString();

	}

	/**
	 * @param aLevel
	 * @return
	 */
	public static String formatWhat(final String aMethod) {

		return UtilsString.strAdjustRight(
				aMethod != null
						? aMethod.replace(SEP_COLUMN, REPLACE_COLUMN)
						: EMPTY,
				LENGTH_WHAT, ' ');

	}

	/**
	 * @param aLevel
	 * @return
	 */
	public static String formatWho(final String aWho) {

		return UtilsString.strAdjustRight(
				aWho != null ? aWho.replace(SEP_COLUMN, REPLACE_COLUMN) : EMPTY,
				LENGTH_WHO, ' ');

	}

	// The default level is OFF.
	private Logger pLogger = null;
	private String plogFolderPath = System.getProperty("java.io.tmpdir");

	/**
	 *
	 */
	private SimpleLogger() {
		super();
	};

	/**
	 * @param aHandlerName
	 * @return
	 */
	private FileHandler buildLoggerFileHandler(String aFhPattern) {
		try {
			FileHandler wFileHandler = new FileHandler(aFhPattern);

			return wFileHandler;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * eg. Error_ExcelFileBuilder_2013-05-31_17-40-23_256.txt
	 *
	 * @param aName
	 * @return
	 */
	private String buildLoggerFileNamePattern(String aHandlerName,
			String aPrefixFileName) {
		String wTimeStamp = sTimeStampFormater.format(new Date());

		String wFhPattern = String.format("%s_%s_%s.log", aPrefixFileName,
				aHandlerName, wTimeStamp);

		wFhPattern = new File(getLogFolderPath(), wFhPattern).getAbsolutePath();

		return wFhPattern;
	}

	private String cleanLevelName(String aLevelName) {
		if (aLevelName != null) {
			aLevelName = aLevelName.toUpperCase();

			// si LOGLEVELINFO => LEVELINFO
			if (aLevelName.startsWith(LOG_PREFIX_LOG)) {
				aLevelName = aLevelName.substring(LOG_PREFIX_LOG.length());
			}
			// si LEVELINFO => INFO
			if (aLevelName.startsWith(LOG_PREFIX_LEVEL)) {
				aLevelName = aLevelName.substring(LOG_PREFIX_LEVEL.length());
			}
		}
		return aLevelName;
	}

	/**
	 * CLose the Logger
	 */
	public void close() {
		if (pLogger != null) {
			Logger wLogger = pLogger;
			pLogger = null;
			wLogger.setLevel(Level.OFF);
			for (Handler wHandler : wLogger.getHandlers()) {
				wHandler.close();
				wLogger.removeHandler(wHandler);
			}
		}
	}

	public String getLogFolderPath() {
		return plogFolderPath;
	}

	@Override
	public boolean isLogDebugOn() {
		return isLoggable(Level.FINE);
	}

	@Override
	public boolean isLoggable(Level aLevel) {
		return (pLogger != null) && pLogger.isLoggable(aLevel);
	}

	@Override
	public boolean isLogInfoOn() {
		return isLoggable(Level.INFO);
	}

	@Override
	public boolean isLogSevereOn() {
		return isLoggable(Level.SEVERE);
	}

	@Override
	public boolean isLogWarningOn() {
		return isLoggable(Level.WARNING);
	}

	/**
	 * @param aFlag
	 * @return
	 */
	public boolean isValidConsoleFlag(String aFlag) {
		return "LOGCONSOLE".equalsIgnoreCase(aFlag);
	}

	/**
	 * @param aFlag
	 * @return
	 */
	public boolean isValidFileFlag(String aFlag) {
		return "LOGFILE".equalsIgnoreCase(aFlag);
	}

	/**
	 * @param aLevelName
	 * @return
	 */
	public boolean isValidLevel(String aLevelName) {
		if (aLevelName != null) {
			aLevelName = cleanLevelName(aLevelName);
			for (Level wLevel : LEVELS) {
				if (wLevel.getName().equalsIgnoreCase(aLevelName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param aLevelName
	 * @return
	 */
	public Level levelNameToLevel(String aLevelName) {
		if (aLevelName != null) {
			aLevelName = cleanLevelName(aLevelName);
			for (Level wLevel : LEVELS) {
				if (wLevel.getName().equalsIgnoreCase(aLevelName)) {
					return wLevel;
				}
			}
		}
		return Level.OFF;
	}

	@Override
	public void log(Level aLevel, Object aWho, CharSequence aWhat,
			Object... aInfos) {

		if (isLoggable(aLevel)) {
			pLogger.log(aLevel,
					buildLogLine(Thread.currentThread(), aWho, aWhat, aInfos));
		}
	}

	@Override
	public void logDebug(Object aWho, CharSequence aWhat, Object... aInfos) {
		log(Level.FINE, aWho, aWhat, aInfos);
	}

	@Override
	public void logInfo(Object aWho, CharSequence aWhat, Object... aInfos) {
		if (isLogInfoOn()) {
			log(Level.INFO, aWho, aWhat, aInfos);
		}
	}

	@Override
	public void logSevere(Object aWho, CharSequence aWhat, Object... aInfos) {
		if (isLogSevereOn()) {
			log(Level.SEVERE, aWho, aWhat, aInfos);
		}
	}

	@Override
	public void logWarn(Object aWho, CharSequence aWhat, Object... aInfos) {
		if (isLogWarningOn()) {
			log(Level.WARNING, aWho, aWhat, aInfos);
		}
	}

	/**
	 * @param aLevel
	 * @param aWithConsole
	 * @param aWithFile
	 */
	public void open(String aPrefixFileName, Level aLevel, boolean aWithConsole,
			boolean aWithFile) {

		close();

		if ((pLogger == null) && !Level.OFF.equals(aLevel)
				&& (aWithConsole || aWithFile)) {

			String wLoggerId = getClass().getSimpleName();
			pLogger = Logger.getLogger(wLoggerId);
			String wFhPattern = buildLoggerFileNamePattern(wLoggerId,
					aPrefixFileName);
			pLogger.setUseParentHandlers(false);

			if (aWithConsole) {
				Handler wHandler = new ConsoleHandler();
				wHandler.setFormatter(SimpleLoggerFormatter.getFormater());
				pLogger.addHandler(wHandler);
			}

			if (aWithFile) {
				FileHandler wHandler = buildLoggerFileHandler(wFhPattern);
				wHandler.setFormatter(SimpleLoggerFormatter.getFormater());
				pLogger.addHandler(wHandler);

			}

			setLogLevel(aLevel);

			logInfo(this, "<init>",
					"Start logger=[%s] Level=[%s] WithConsole=[%b] WithFile=[%b]",
					pLogger.getName(), pLogger.getLevel(), aWithConsole,
					aWithFile);
			if (aWithFile) {
				logInfo(this, "<init>", "FileHandlerPattern=[%s] ", wFhPattern);
			}
		}
	}

	/**
	 * @param aLevelName
	 * @param aWithConsole
	 * @param aWithFile
	 */
	public void open(String aPrefixFileName, String aLevelName,
			boolean aWithConsole, boolean aWithFile) {
		open(aPrefixFileName, levelNameToLevel(aLevelName), aWithConsole,
				aWithFile);
	}

	public void setLogFolderPath(String aLogFolderPath)
			throws FileNotFoundException {
		File f = new File(aLogFolderPath);
		if (f.isDirectory()) {
			this.plogFolderPath = aLogFolderPath;
		} else {
			throw new FileNotFoundException(String
					.format("The folder '%s' doesn't exist", aLogFolderPath));
		}

	}

	@Override
	public void setLogLevel(Level aLevel) {
		pLogger.setLevel(aLevel);
	}
}
