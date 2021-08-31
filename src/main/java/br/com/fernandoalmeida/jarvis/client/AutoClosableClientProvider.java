package br.com.fernandoalmeida.jarvis.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AutoClosableClientProvider implements java.lang.AutoCloseable
{
	@Getter private Client client = null;

	public AutoClosableClientProvider(boolean multiPart)
	{
		log.trace("Instantiating a new jax-rs client");
		
		if (multiPart)
		{
			client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		}
		else
		{
			client = ClientBuilder.newClient();
		}
	}
	
	@Override
	public void close()
	{
		if (client != null)
		{
			log.trace("Closing jax-rs client");
			client.close();
		}
	}

}
