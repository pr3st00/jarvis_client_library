package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Language returned by Jarvis apis
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
public class Language implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String language;

	public Language()
	{
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

}
