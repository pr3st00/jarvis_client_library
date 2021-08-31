package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents an action to be processed by Jarvis
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Actions
	{
		PLAY, EXECUTE
	}
	
	@NonNull private String code;
	@NonNull private Actions action;
	@NonNull private List<String> parameters;
	
	private boolean synchronous;

}
