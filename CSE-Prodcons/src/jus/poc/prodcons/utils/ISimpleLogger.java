package jus.poc.prodcons.utils;

import java.util.logging.Level;

/**
 * @author gattazr
 * 
 */
public interface ISimpleLogger {

	/**
	 * @return
	 */
	public boolean isLogDebugOn();

	/**
	 * @return
	 */
	public boolean isLogSevereOn();

	/**
	 * @param aLevel
	 * @return
	 */
	public boolean isLoggable(Level aLevel);

	/**
	 * @return true if the logger accept
	 */
	public boolean isLogInfoOn();

	/**
	 * @return
	 */
	public boolean isLogWarningOn();

	/**
	 * @param aLevel
	 * @param aWho
	 * @param aWhat
	 * @param aLine
	 */
	public void log(Level aLevel, Object aWho, CharSequence aWhat,
			Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logDebug(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logInfo(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logSevere(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aWho
	 * @param aWhat
	 * @param aInfos
	 */
	public void logWarn(Object aWho, CharSequence aWhat, Object... aInfos);

	/**
	 * @param aLevel
	 */
	public void setLogLevel(Level aLevel);
}
