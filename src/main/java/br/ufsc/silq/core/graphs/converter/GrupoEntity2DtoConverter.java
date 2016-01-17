package br.ufsc.silq.core.graphs.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.ufsc.silq.core.dto.commondto.GroupDto;
import br.ufsc.silq.core.dto.commondto.PesquisadorDto;
import br.ufsc.silq.core.entities.Grupo;
import br.ufsc.silq.core.entities.Pesquisador;

public class GrupoEntity2DtoConverter {

	public static GroupDto convertGrupo(Grupo grupo) {
		GroupDto groupDto = new GroupDto();
		groupDto.setInstituicao(grupo.getNomeInstituicao());
		groupDto.setNome(grupo.getNomeGrupo());
		groupDto.setId(grupo.getId());
		groupDto.setNomeArea(grupo.getNomeArea());

		List<PesquisadorDto> pesquisadores = GrupoEntity2DtoConverter.convertPesquisadores(grupo.getPesquisadores());
		groupDto.setPesquisadores(pesquisadores);

		return groupDto;
	}

	public static List<GroupDto> convertGrupos(List<Grupo> grupos) {
		List<GroupDto> groupList = new ArrayList<>();

		for (Grupo grupo : grupos) {
			GroupDto groupDto = GrupoEntity2DtoConverter.convertGrupo(grupo);
			groupList.add(groupDto);
		}

		return groupList;
	}

	public static List<PesquisadorDto> convertPesquisadores(Set<Pesquisador> pesquisadores) {
		List<PesquisadorDto> pesquisadorList = new ArrayList<>();

		for (Pesquisador pesquisador : pesquisadores) {
			PesquisadorDto pesquisadorDto = new PesquisadorDto();

			pesquisadorDto.setId(pesquisador.getId());
			pesquisadorDto.setCurriculoXml(new String(pesquisador.getCurriculoXml()));
			pesquisadorDto.setDataAtualizacaoCurriculo(pesquisador.getDataAtualizacaoCurriculo());
			pesquisadorDto.setDataAtualizacaoUsuario(pesquisador.getDataAtualizacaoUsuario());
			pesquisadorDto.setIdCurriculo(pesquisador.getIdCurriculo());
			pesquisadorDto.setNome(pesquisador.getNome());
			pesquisadorDto.setIdGrupo(pesquisador.getGrupo().getId());

			pesquisadorList.add(pesquisadorDto);
		}

		return pesquisadorList;
	}

}
