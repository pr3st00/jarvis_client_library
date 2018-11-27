package br.com.fernandoalmeida.jarvis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHolder
{
	private static final String JARVIS_PROPERTIES = "jarvis.properties";

	private static Properties props = null;
	private static PropertiesHolder instance = null;

	private PropertiesHolder()
	{
		props = new Properties();
	}

	public static PropertiesHolder getInstance() throws IOException
	{
		if (instance == null)
		{
			instance = new PropertiesHolder();
			instance.load();
		}

		return instance;
	}

	private void load() throws IOException
	{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(JARVIS_PROPERTIES);
		props.load(in);
	}

	public String getProperty(String name)
	{
		return props.getProperty(name);
	}
}
