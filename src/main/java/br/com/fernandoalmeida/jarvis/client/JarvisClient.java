package br.com.fernandoalmeida.jarvis.client;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import br.com.fernandoalmeida.jarvis.PropertiesHolder;
import br.com.fernandoalmeida.jarvis.entities.Action;
import br.com.fernandoalmeida.jarvis.entities.Action.Actions;
import br.com.fernandoalmeida.jarvis.entities.Language;
import br.com.fernandoalmeida.jarvis.entities.MultipleActions;
import br.com.fernandoalmeida.jarvis.entities.Status;
import br.com.fernandoalmeida.jarvis.exception.JarvisConfigurationException;
import br.com.fernandoalmeida.jarvis.exception.JarvisRemoteException;
import lombok.extern.log4j.Log4j2;

/**
 * Jarvis client library
 * 
 * @author Fernando Costa de Almeida
 *
 */
@Log4j2
public class JarvisClient
{
	private static final String JARVIS_URL_PROPERTY_NAME = "jarvis.url";
	private static final String DEFAULT_SESSION_ID_PROPERTY_NAME = "default.session.id";

	private static final String AVAILABLE_STATUS = "available";
	private static final String WEBCAM_SCRIPT_PROPERTY_NAME = "webcam_script.path";
	private static final String SESSION_ID_HEADER_NAME = "sessionId";

	private String jarvisUrl = null;
	private String sessionId = null;

	private Client internalClient = null;

	/**
	 * All Jarvis services
	 */
	public enum Services
	{
		STATUS("/status"), ACTIONS("/actions"), ENABLE_SOUND("/enablesound"), DISABLE_SOUND("/disablesound"), LANGUAGE(
				"/language");

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

	public JarvisClient(String sessionId) throws JarvisConfigurationException
	{
		initialize();
		this.sessionId = sessionId;
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
			log.trace("Initializing java client api");

			this.jarvisUrl = PropertiesHolder.getInstance().getProperty(JARVIS_URL_PROPERTY_NAME) != null
					? PropertiesHolder.getInstance().getProperty(JARVIS_URL_PROPERTY_NAME) + "/api"
					: null;
			this.sessionId = PropertiesHolder.getInstance().getProperty(DEFAULT_SESSION_ID_PROPERTY_NAME) != null
					? PropertiesHolder.getInstance().getProperty(DEFAULT_SESSION_ID_PROPERTY_NAME)
					: null;

			internalClient = ClientBuilder.newClient();
		}
		catch (IOException e)
		{
			log.error(e);
			throw new JarvisConfigurationException(e);
		}

		if (this.jarvisUrl == null)
		{
			throw new JarvisConfigurationException("Jarvis url cannot be empty.");
		}
	}

	/**
	 * Returns true if Jarvis is available
	 * 
	 * @return
	 */
	public boolean isJarvisAvailable()
	{
		Status status = performGet(Services.STATUS, Status.class);

		return AVAILABLE_STATUS.equals(status.getStat());
	}

	/**
	 * Returns the current language being used by Jarvis
	 * 
	 * @return
	 */
	public String getLanguage()
	{
		Language lang = performGet(Services.LANGUAGE, Language.class);
		
		return lang != null ? lang.getLang() : "undefined";
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
		}
		catch (IOException e)
		{
			throw new JarvisConfigurationException(e);
		}

		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("execute", Actions.EXECUTE, Arrays.asList(script, name, "OFF"), true));

		log.info("Say cheese!");

		Response response = internalClient.target(jarvisUrl).path(Services.ACTIONS.getUri())
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
		try (AutoClosableClientProvider clientProvider = new AutoClosableClientProvider(true))
		{
			FileDataBodyPart filePart = new FileDataBodyPart("file", file);
			FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
			FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("out.wav", "command")
					.bodyPart(filePart);

			Response response = clientProvider.getClient().target(jarvisUrl).path(Services.ACTIONS.getUri()).request()
					.header(SESSION_ID_HEADER_NAME, this.sessionId)
					.post(Entity.entity(multipart, multipart.getMediaType()));

			formDataMultiPart.close();
			multipart.close();

			waitForJarvis();

			return response.getStatus() == 200;
		}
		catch (IOException e)
		{
			throw new JarvisRemoteException(e);
		}
	}

	/**
	 * Speak up the message provided
	 * 
	 * @param message
	 * @return
	 */
	public boolean say(String message)
	{
		return say(message, true);
	}

	/**
	 * Speak up the message provided. Wait for Jarvis if <code> wait </code> is true
	 * 
	 * @param message
	 * @param wait
	 * @return
	 */
	public boolean say(String message, boolean wait)
	{
		MultipleActions actions = new MultipleActions();

		actions.addAction(new Action("play", Actions.PLAY, Arrays.asList(message), true));

		log.info("Speak up Jarvis.");

		Response response = internalClient.target(jarvisUrl).path(Services.ACTIONS.getUri())
				.request(MediaType.APPLICATION_JSON).header(SESSION_ID_HEADER_NAME, this.sessionId)
				.post(Entity.entity(actions, MediaType.APPLICATION_JSON));

		if (wait)
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
		return say(true, messages);
	}

	/**
	 * Speak up all the message(s). Wait for Jarvis if <code> wait </code> is true.
	 * 
	 * @param wait
	 * @param messages
	 * @return
	 */
	public boolean say(boolean wait, String... messages)
	{
		boolean success = true;

		for (String message : messages)
		{
			success = success && say(message, wait);
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
			log.info("Waiting for Jarvis...");

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				log.error(e);
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Enables the Jarvis sound system
	 * 
	 * @return
	 */
	public boolean enableSound()
	{
		log.info("Enabling sound system");

		Response response = performGet(Services.ENABLE_SOUND);

		return response.getStatus() == 200;
	}

	/**
	 * Disables the Jarvis sound system
	 * 
	 * @return
	 */
	public boolean disableSound()
	{
		log.info("Disabling sound system");

		Response response = performGet(Services.DISABLE_SOUND);

		return response.getStatus() == 200;
	}

	public void close()
	{
		if (this.internalClient != null)
		{
			log.trace("Closing all internal resources");
			internalClient.close();
		}
	}

	private Response performGet(Services service)
	{
		return performGet(service, Response.class);
	}

	private <T> T performGet(Services service, Class<T> returnClass)
	{
		log.trace("Calling service: " + service.getUri());

		return internalClient.target(jarvisUrl).path(service.getUri()).request(MediaType.APPLICATION_JSON)
				.get(returnClass);
	}

}
