package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
	public void testSearchPeriodicos() {
		Page<QualisPeriodico> page = this.qualisService.searchPeriodicos("Test", this.pageable);
		Assertions.assertThat(page.getSize()).isEqualTo(10);
	}

	@Test
	public void testSearchEventos() {
		Page<QualisEvento> page = this.qualisService.searchEventos("Test", this.pageable);
		Assertions.assertThat(page.getSize()).isEqualTo(10);
	}
}
