package br.com.fernandoalmeida.jarvis.entities;

import java.util.ArrayList;
import java.util.List;

public class MultipleActions
{
	List<Action> actions;

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
