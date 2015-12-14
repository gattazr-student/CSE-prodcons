package jus.poc.prodcons.options;

/**
 * Classe permettant la lecture de fichier XML properties.
 */
public final class Properties extends java.util.Properties {

	private static final long serialVersionUID = 2836322756212808101L;

	/**
	 * Chargement d'un fichier
	 * 
	 * @param aFile
	 *            Fichier XML à lire
	 */
	public Properties(String aFile) {
		try {
			loadFromXML(ClassLoader.getSystemResourceAsStream(aFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Récupérer la valeur dans la property avec la clé aKey
	 * 
	 * @param aKey
	 *            clé de la propoerty a récupérer
	 * @return String property
	 */
	public String get(String aKey) {
		return getProperty(aKey);
	}

	/**
	 * Récupérer la valeur (int) dans la property avec la clé aKey
	 * 
	 * @param aKey
	 *            clé de la propoerty a récupérer
	 * @return int property
	 */
	public int getInt(String aKey) {
		return Integer.parseInt(getProperty(aKey));
	}

}
