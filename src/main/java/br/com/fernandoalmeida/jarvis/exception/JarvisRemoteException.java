package br.com.fernandoalmeida.jarvis.exception;

/**
 * Something went wrong when communicating with Jarvis (like network failure)
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class JarvisRemoteException extends Exception
{
	private static final long serialVersionUID = 1L;

	public JarvisRemoteException(Exception e)
	{
		super(e);
	}

}
