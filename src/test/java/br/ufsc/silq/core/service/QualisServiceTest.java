package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.ufsc.silq.core.data.SimilarityResult;
import br.ufsc.silq.core.forms.QualisSearchForm;
import br.ufsc.silq.core.persistence.entities.QualisEvento;
import br.ufsc.silq.core.persistence.entities.QualisPeriodico;
import br.ufsc.silq.test.WebContextTest;

public class QualisServiceTest extends WebContextTest {

	@Inject
	QualisService qualisService;

	private Pageable pageable;

	@Before
	public void setUp() {
		this.pageable = new PageRequest(0, 10);
	}

	@Test
	public void testSearchPeriodicosNullQuery() {
		Page<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos(new QualisSearchForm(), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(0);
	}

	@Test
	public void testSearchPeriodicos() {
		Page<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos(new QualisSearchForm("Test"), this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}

	@Test
	public void testSearchEventosNullQuery() {
		Page<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos(new QualisSearchForm(), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(0);
	}

	@Test
	public void testSearchEventos() {
		Page<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos(new QualisSearchForm("Test"), this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}
}
