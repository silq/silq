package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.ufsc.silq.core.data.SimilarityResult;
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
	public void testSearchPeriodicosNoQuery() {
		PageImpl<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos("", this.pageable);
		Assertions.assertThat(page.getContent()).isEmpty();
	}

	@Test
	public void testSearchPeriodicos() {
		PageImpl<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos("Test", this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}

	@Test
	public void testSearchEventosNoQuery() {
		PageImpl<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos("", this.pageable);
		Assertions.assertThat(page.getContent()).isEmpty();
	}

	@Test
	public void testSearchEventos() {
		PageImpl<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos("Test", this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}
}
