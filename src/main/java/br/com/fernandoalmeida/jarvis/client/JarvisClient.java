package br.com.fernandoalmeida.jarvis.client;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import br.com.fernandoalmeida.jarvis.PropertiesHolder;
import br.com.fernandoalmeida.jarvis.entities.Action;
import br.com.fernandoalmeida.jarvis.entities.Action.Actions;
import br.com.fernandoalmeida.jarvis.entities.MultipleActions;
import br.com.fernandoalmeida.jarvis.entities.Status;
import br.com.fernandoalmeida.jarvis.exception.JarvisConfigurationException;
import br.com.fernandoalmeida.jarvis.exception.JarvisRemoteException;

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
		STATUS("/status"), ACTIONS("/actions"), ENABLE_SOUND("/enablesound"), DISABLE_SOUND("/disablesound");

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

	/**
	 * Initializes Jarvis client library
	 * 
	 * @throws JarvisConfigurationException
	 */
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

	/**
	 * Returns true if Jarvis is available
	 * 
	 * @return
	 */
	public boolean isJarvisAvailable()
	{
		Status status = ClientBuilder.newClient().target(JARVIS_URL).path(Services.STATUS.getUri())
				.request(MediaType.APPLICATION_JSON).get(Status.class);

		return AVAILABLE_STATUS.equals(status.getStatus());
	}

	/**
	 * Takes a photo and saves it with name <code> name </code>
	 * 
	 * @param name
	 * @return
	 * @throws JarvisConfigurationException
	 */
	public boolean takePhoto(String name) throws JarvisConfigurationException
	{
		String script;

		try
		{
			script = PropertiesHolder.getInstance().getProperty(WEBCAM_SCRIPT_PROPERTY_NAME);
		} catch (IOException e)
		{
			throw new JarvisConfigurationException(e);
		}

		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("execute", Actions.EXECUTE, Arrays.asList(script, name, "OFF"), true));

		logger.info("Say cheese!");

		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		waitForJarvis();

		return response.getStatus() == 200;

	}

	/**
	 * Process the audio contained in <code> file </code>
	 * 
	 * @param file
	 * @return
	 * @throws JarvisRemoteException
	 */
	public boolean processAudio(File file) throws JarvisRemoteException
	{
		Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

		FileDataBodyPart filePart = new FileDataBodyPart("file", file);
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("out.wav", "command")
				.bodyPart(filePart);

		Response response = client.target(JARVIS_URL).path(Services.ACTIONS.getUri()).request()
				.post(Entity.entity(multipart, multipart.getMediaType()));

		try
		{
			formDataMultiPart.close();
			multipart.close();
		} catch (IOException e)
		{
			throw new JarvisRemoteException(e);
		}

		waitForJarvis();

		return response.getStatus() == 200;
	}

	/**
	 * Speak up the message provided
	 * 
	 * @param message
	 * @return
	 */
	public boolean say(String message)
	{
		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("play", Actions.PLAY, Arrays.asList(message), true));

		logger.info("Speak up jarvis.");

		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		waitForJarvis();

		return response.getStatus() == 200;
	}

	/**
	 * Speak up the message(s)
	 * 
	 * @param messages
	 * @return
	 */
	public boolean say(String... messages)
	{
		boolean success = true;

		for (String message : messages)
		{
			success = success && say(message);
		}

		return success;
	}

	/**
	 * Waits for Jarvis to return an available status
	 */
	private void waitForJarvis()
	{
		while (!isJarvisAvailable())
		{
			logger.info("Waiting for jarvis...");

			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				logger.error(e);
			}
		}
	}

	/**
	 * Enables the jarvis sound system
	 * 
	 * @return
	 */
	public boolean enableSound()
	{
		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.ENABLE_SOUND.getUri())
				.request(MediaType.APPLICATION_JSON).get();

		return response.getStatus() == 200;
	}

	/**
	 * Disables the jarvis sound system
	 * 
	 * @return
	 */
	public boolean disableSound()
	{
		Response response = ClientBuilder.newClient().target(JARVIS_URL).path(Services.DISABLE_SOUND.getUri())
				.request(MediaType.APPLICATION_JSON).get();

		return response.getStatus() == 200;
	}

}
