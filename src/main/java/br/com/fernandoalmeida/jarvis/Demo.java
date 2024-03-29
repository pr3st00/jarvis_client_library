package br.com.fernandoalmeida.jarvis;

import br.com.fernandoalmeida.jarvis.client.JarvisClient;
import br.com.fernandoalmeida.jarvis.exception.JarvisConfigurationException;

/**
 * Demo application
 * 
 * @author Fernando Costa de Almeida
 *
 */
public class Demo
{

	public static void main(String[] args) throws JarvisConfigurationException
	{
		JarvisClient client = new JarvisClient();

		// Make sure audio is enabled
		client.enableSound();

		// Say something
		client.say("Hi there!", "Smile for the camera!");

		// Take a picture
		client.takePhoto("myphoto.jpg");

		client.say("Check your picture now!");
		
		// Close all client internal resources
		client.close();

	}

}
