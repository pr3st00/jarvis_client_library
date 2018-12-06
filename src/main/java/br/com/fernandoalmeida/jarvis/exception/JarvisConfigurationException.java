package br.com.fernandoalmeida.jarvis.exception;

/**
 * Something is bad configured and/or missing on Jarvis configuration files
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class JarvisConfigurationException extends Exception
{
	private static final long serialVersionUID = 1L;

	public JarvisConfigurationException(Exception e)
	{
		super(e);
	}

}
