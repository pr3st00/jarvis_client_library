package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Holds multiple actions
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class MultipleActions implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NonNull private List<Action> actions;
	
	public void addAction(Action action)
	{
		if (actions == null)
		{
			actions = new ArrayList<>();
		}

		actions.add(action);
	}

}
