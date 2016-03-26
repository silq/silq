package br.ufsc.silq.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import ch.qos.logback.classic.Logger;
import lombok.Data;

@Data
public class LoggerDTO {

	private String name;

	private String level;

	public LoggerDTO(Logger logger) {
		this.name = logger.getName();
		this.level = logger.getEffectiveLevel().toString();
	}

	@JsonCreator
	public LoggerDTO() {
	}
}
