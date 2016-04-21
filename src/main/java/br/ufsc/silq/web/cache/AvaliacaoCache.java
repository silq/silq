package br.ufsc.silq.web.cache;

import org.springframework.stereotype.Service;

import br.ufsc.silq.core.data.AvaliacaoResult;

/**
 * Cache utilizado para guardar resultados de avaliação ({@link AvaliacaoResult}) de
 * currículos. Na avaliação livre, por exemplo, os currículos são enviados e
 * salvos em um {@link CurriculumCache} com um ID representando a avaliação. Ao
 * realizar a avaliação é criada uma nova entrada em {@link AvaliacaoCache} com
 * o mesmo ID do cache dos currículos, contendo os resultados das avaliações.
 */
@Service
public class AvaliacaoCache extends AbstractExpirableCache<AvaliacaoResult> {

}
