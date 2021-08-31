package br.com.fernandoalmeida.jarvis.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Status message returned by Jarvis apis
 * 
 * @author Fernando Costa de Almeida
 *
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class Status implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NonNull @XmlElement(name = "status") String stat;
}
