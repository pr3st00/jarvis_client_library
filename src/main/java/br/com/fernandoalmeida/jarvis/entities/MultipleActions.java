package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds multiple actions
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
public class MultipleActions implements Serializable
{
	private static final long serialVersionUID = 1L;

	List<Action> actions;

	public MultipleActions()
	{
	}

	public List<Action> getActions()
	{
		return actions;
	}

	public void setActions(List<Action> actions)
	{
		this.actions = actions;
	}

	public void addAction(Action action)
	{
		if (actions == null)
		{
			actions = new ArrayList<>();
		}

		actions.add(action);
	}

}
