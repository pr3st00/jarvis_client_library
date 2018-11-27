package br.com.fernandoalmeida.jarvis.exception;

public class JarvisRemoteException extends Exception
{
	private static final long serialVersionUID = 1L;

	public JarvisRemoteException(Exception e)
	{
		super(e);
	}

}
