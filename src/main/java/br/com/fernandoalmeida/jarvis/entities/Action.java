package br.com.fernandoalmeida.jarvis.entities;

import java.util.List;

public class Action
{
	public enum Actions
	{
		PLAY, EXECUTE
	};

	private String code;
	private Actions action;
	private List<String> parameters;
	private boolean synchronous;

	public Action(String code, Actions action, List<String> parameters, boolean syncrhonous)
	{
		super();
		this.code = code;
		this.action = action;
		this.parameters = parameters;
		this.synchronous = syncrhonous;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Actions getAction()
	{
		return action;
	}

	public void setAction(Actions action)
	{
		this.action = action;
	}

	public List<String> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<String> parameters)
	{
		this.parameters = parameters;
	}

	public boolean isSynchronous()
	{
		return synchronous;
	}

	public void setSynchronous(boolean synchronous)
	{
		this.synchronous = synchronous;
	}

}
