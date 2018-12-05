package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Status message returned by Jarvis apis
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
public class Status implements Serializable
{
	private static final long serialVersionUID = 1L;

	String status;

	public Status()
	{
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

}
