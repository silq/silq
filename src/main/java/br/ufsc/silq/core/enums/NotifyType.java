package br.ufsc.silq.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotifyType {

	SUCCESS("success"),
	INFO("info"),
	WARNING("warning"),
	DANGER("danger"),
	ERROR("danger");

	private String name;

}
