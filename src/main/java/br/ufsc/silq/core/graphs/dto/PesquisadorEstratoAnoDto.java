package br.ufsc.silq.core.graphs.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PesquisadorEstratoAnoDto {

	private Map<Integer, ConcurrentMap<String, AtomicInteger>> mapConceitoQuantidade = new HashMap<>();
	private List<String> conceitos;
	private List<String> anos;

}