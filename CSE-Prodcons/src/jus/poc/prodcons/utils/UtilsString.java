package jus.poc.prodcons.utils;

/**
 * @author gattazr
 * 
 */
public class UtilsString {

	public final static String EMPTY = "";

	public final static char SEP_LINE = '\n';

	/**
	 * @param aValue
	 * @param aLen
	 * @param aLeadingChar
	 * @return
	 */
	public static String strAdjustLeft(String aValue, final int aLen,
			final char aLeadingChar) {

		if (aValue == null) {
			aValue = EMPTY;
		}

		int wLen = aValue.length();
		if (wLen < aLen) {
			return aValue + strFromChar(aLeadingChar, aLen - wLen);
		} else if (wLen > aLen) {
			return aValue.substring(0, aLen);
		} else {
			return aValue;
		}
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @return
	 */
	public static String strAdjustRight(long aValue, int aLen) {
		return strAdjustRight(String.valueOf(aValue), aLen, '0');
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @return
	 */
	public static String strAdjustRight(String aValue, int aLen) {

		return strAdjustRight(aValue, aLen, ' ');
	}

	/**
	 * @param aValue
	 * @param aLen
	 * @param aLeadingChar
	 * @return
	 */
	public static String strAdjustRight(String aValue, int aLen,
			char aLeadingChar) {

		if (aValue == null) {
			aValue = EMPTY;
		}
		int wLen = aValue.length();
		if (wLen < aLen)
			return strFromChar(aLeadingChar, aLen - wLen) + aValue;
		else if (wLen > aLen)
			return aValue.substring(aValue.length() - aLen);
		else
			return aValue;
	}

	/**
	 * @param aChar
	 * @param aLen
	 * @return
	 */
	public static String strFromChar(char aChar, int aLen) {
		if (aLen < 1)
			return "";
		if (aLen == 1)
			return String.valueOf(aChar);
		char[] wBuffer = new char[aLen];
		for (int wI = 0; wI < aLen; wI++) {
			wBuffer[wI] = aChar;
		}
		return String.valueOf(wBuffer);
	}
}
