package br.ufsc.silq.core.data;

import br.ufsc.silq.core.forms.AvaliarForm;
import lombok.Getter;

import java.util.ArrayList;

public class ClassificacaoCollectionResult {

    @Getter
    private final AvaliarForm form;

    @Getter
    private ArrayList<AvaliacaoResult> avaliacoes;

    public ClassificacaoCollectionResult(AvaliarForm form) {
        this.form = form;
        this.avaliacoes = new ArrayList<>();
    }

    public void putAvaliacaoResult(AvaliacaoResult avaliacaoStats){
        this.avaliacoes.add(avaliacaoStats);
    }

}
