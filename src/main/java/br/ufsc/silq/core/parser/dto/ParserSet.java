package br.ufsc.silq.core.parser.dto;

import java.util.ArrayList;
import java.util.List;

public class ParserSet {

	private List<String> nodeNames = new ArrayList<>();
	private List<String> attributeNames = new ArrayList<>();
	private List<String> setName = new ArrayList<>();

	public ParserSet(List<String> nodeNames, List<String> attributeNames, List<String> setName) {
		this.nodeNames = nodeNames;
		this.attributeNames = attributeNames;
		this.setName = setName;
	}

	@Override
	public String toString() {
		String setInfo = "Set name: " + setName;

		return setInfo;
	}

	public List<String> getNodeNames() {
		return nodeNames;
	}

	public List<String> getSetName() {
		return setName;
	}

	public void setSetName(List<String> setName) {
		this.setName.addAll(setName);
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}

}
