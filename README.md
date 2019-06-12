<h1 align="center">Jarvis Java client library</h1>

<p>
A client library for Jarvis written in JAVA, useful for home automation using Jarvis backend.
</p>

## Configuration
<p>
	Fill in the jarvis url in jarvis.properties
</p>

## Sample code
<p>

package br.com.fernandoalmeida.jarvis;

import br.com.fernandoalmeida.jarvis.client.JarvisClient;
import br.com.fernandoalmeida.jarvis.exception.JarvisConfigurationException;

public class Demo
{

	public static void main(String[] args) throws JarvisConfigurationException
	{
		JarvisClient client = new JarvisClient();

		// Say something
		client.say("Hi there!", "Smile for the camera!");

		// Take a picture
		client.takePhoto("myphoto.jpg");

		client.say("Check your picture now!");

	}

}

</p>

## Current Release: v0.0.2

## Current Feature Requests/Suggestions
Pending..

## License
MIT

## Author
[Fernando Almeida] (fernando.c.almeida@gmail.com)

