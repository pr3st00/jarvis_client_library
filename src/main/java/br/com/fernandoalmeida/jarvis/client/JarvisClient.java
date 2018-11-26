package br.com.fernandoalmeida.jarvis.client;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.fernandoalmeida.jarvis.PropertiesHolder;
import br.com.fernandoalmeida.jarvis.entities.Action;
import br.com.fernandoalmeida.jarvis.entities.Action.Actions;
import br.com.fernandoalmeida.jarvis.entities.MultipleActions;
import br.com.fernandoalmeida.jarvis.entities.Status;

/**
 * Jarvis client library
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class JarvisClient
{
	private static final String WEBCAM_SCRIPT = "/home/pi/jarvis/jarvis/scripts/webcam.sh";
	private static final String AVAILABLE_STATUS = "available";

	private static String JARVIS_URL = null;

	public enum Services {
		STATUS("/status"), ACTIONS("/actions");

		private final String uri;

		private Services(String value)
		{
			uri = value;
		}

		public String getUri()
		{
			return uri;
		}
	}

	static
	{
		try
		{
			JARVIS_URL = PropertiesHolder.getInstance().getProperty("jarvis.url") + "/api";
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isJarvisAvailable()
	{
		Status status = ClientBuilder.newClient().target(JARVIS_URL).path(Services.STATUS.getUri())
				.request(MediaType.APPLICATION_JSON).get(Status.class);

		return AVAILABLE_STATUS.equals(status.getStatus());
	}

	public boolean takePhoto(String name) throws InterruptedException
	{
		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("execute", Actions.EXECUTE, Arrays.asList(WEBCAM_SCRIPT, name, "OFF"), true));

		System.out.println("Say cheese!");

		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		waitForJarvis();

		return response.getStatus() == 200;

	}

	public boolean say(String message) throws InterruptedException
	{
		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("play", Actions.PLAY, Arrays.asList(message), true));

		System.out.println("Speak up jarvis.");

		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		waitForJarvis();

		return response.getStatus() == 200;
	}

	public boolean say(String... messages) throws InterruptedException
	{
		boolean success = true;

		for (String message : messages)
		{
			success = success && say(message);
		}

		return success;
	}

	private void waitForJarvis() throws InterruptedException
	{
		while (!isJarvisAvailable())
		{
			System.out.println("Waiting for jarvis...");
			Thread.sleep(1000);
		}
	}

}
