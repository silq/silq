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

	private final static int PAGE_SIZE = 15;
	private static final String AREA = "Ciência da Computação";

	@Inject
	QualisService qualisService;

	private Pageable pageable;

	@Before
	public void setUp() {
		this.pageable = new PageRequest(0, PAGE_SIZE);
	}

	@Test
	public void testSearchPeriodicosNullQuery() {
		Page<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos(new QualisSearchForm(), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(PAGE_SIZE);
	}

	@Test
	public void testSearchPeriodicosNullQueryAndArea() {
		Page<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos(
				new QualisSearchForm(null, AREA), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(PAGE_SIZE);
		page.forEach(p -> {
			Assertions.assertThat(p.getResultado().getAreaAvaliacao().equals(AREA));
		});
	}

	@Test
	public void testSearchPeriodicos() {
		Page<SimilarityResult<QualisPeriodico>> page = this.qualisService.searchPeriodicos(new QualisSearchForm("Test"), this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}

	@Test
	public void testSearchEventosNullQuery() {
		Page<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos(new QualisSearchForm(), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(PAGE_SIZE);
	}

	@Test
	public void testSearchEventosNullQueryAndArea() {
		Page<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos(
				new QualisSearchForm(null, AREA), this.pageable);
		Assertions.assertThat(page.getNumberOfElements()).isEqualTo(PAGE_SIZE);
		page.forEach(e -> {
			Assertions.assertThat(e.getResultado().getAreaAvaliacao().equals(AREA));
		});
	}

	@Test
	public void testSearchEventos() {
		Page<SimilarityResult<QualisEvento>> page = this.qualisService.searchEventos(new QualisSearchForm("Test"), this.pageable);
		Assertions.assertThat(page.getContent()).isNotEmpty();
	}
}
