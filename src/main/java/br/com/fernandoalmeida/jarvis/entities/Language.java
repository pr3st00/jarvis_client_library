package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Language returned by Jarvis apis
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class Language implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NonNull private String lang;
}
