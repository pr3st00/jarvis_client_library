package br.com.fernandoalmeida.jarvis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Holds properties used during client runtime
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class PropertiesHolder
{
	private static final String JARVIS_PROPERTIES = "jarvis.properties";

	private static Properties props = null;
	private static PropertiesHolder instance = null;

	private PropertiesHolder()
	{
		props = new Properties();
	}

	/**
	 * Returns a singleton of this class
	 * 
	 * @return
	 * @throws IOException
	 */
	public static PropertiesHolder getInstance() throws IOException
	{
		if (instance == null)
		{
			instance = new PropertiesHolder();
			instance.load();
		}

		return instance;
	}

	/**
	 * Loads the configuration from the properties file in the classpath.
	 * 
	 * @throws IOException
	 */
	private void load() throws IOException
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(JARVIS_PROPERTIES);
		props.load(in);
	}

	/**
	 * Returns the value of the property named <code> name </code>
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		return props.getProperty(name);
	}
}
