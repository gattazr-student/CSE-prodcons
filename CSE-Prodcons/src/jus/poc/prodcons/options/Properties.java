package jus.poc.prodcons.options;

final class Properties extends java.util.Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2836322756212808101L;

	public Properties(String aFile) {
		try {
			loadFromXML(ClassLoader.getSystemResourceAsStream(aFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String aKey) {
		return getProperty(aKey);
	}

	public int getInt(String aKey) {
		return Integer.parseInt(getProperty(aKey));
	}

}
