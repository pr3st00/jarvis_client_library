package br.com.fernandoalmeida.jarvis.client;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.fernandoalmeida.jarvis.PropertiesHolder;
import br.com.fernandoalmeida.jarvis.entities.Action;
import br.com.fernandoalmeida.jarvis.entities.Action.Actions;
import br.com.fernandoalmeida.jarvis.entities.MultipleActions;
import br.com.fernandoalmeida.jarvis.entities.Status;
import br.com.fernandoalmeida.jarvis.exception.JarvisConfigurationException;

/**
 * Jarvis client library
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class JarvisClient
{
	private static final String AVAILABLE_STATUS = "available";
	private static final String WEBCAM_SCRIPT_PROPERTY_NAME = "webcam_script.path";

	private static final Logger logger = LogManager.getLogger(JarvisClient.class);

	private static String JARVIS_URL = null;

	public enum Services
	{
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

	public JarvisClient() throws JarvisConfigurationException
	{
		initialize();
	}

	private void initialize() throws JarvisConfigurationException
	{
		try
		{
			JARVIS_URL = PropertiesHolder.getInstance().getProperty("jarvis.url") + "/api";
		} catch (IOException e)
		{
			logger.error(e);
			throw new JarvisConfigurationException(e);
		}
	}

	public boolean isJarvisAvailable()
	{
		Status status = ClientBuilder.newClient().target(JARVIS_URL).path(Services.STATUS.getUri())
				.request(MediaType.APPLICATION_JSON).get(Status.class);

		return AVAILABLE_STATUS.equals(status.getStatus());
	}

	public boolean takePhoto(String name) throws InterruptedException, IOException
	{
		MultipleActions actions = new MultipleActions();

		String script = PropertiesHolder.getInstance().getProperty(WEBCAM_SCRIPT_PROPERTY_NAME);

		actions.addAction(new Action("execute", Actions.EXECUTE, Arrays.asList(script, name, "OFF"), true));

		logger.info("Say cheese!");

		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		waitForJarvis();

		return response.getStatus() == 200;

	}

	public boolean say(String message) throws InterruptedException
	{
		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("play", Actions.PLAY, Arrays.asList(message), true));

		logger.info("Speak up jarvis.");

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
			logger.info("Waiting for jarvis...");
			Thread.sleep(1000);
		}
	}

}
