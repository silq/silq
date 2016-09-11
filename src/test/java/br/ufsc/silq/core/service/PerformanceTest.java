package br.ufsc.silq.core.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import br.ufsc.silq.core.exception.SilqException;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.persistence.entities.CurriculumLattes;
import br.ufsc.silq.test.Fixtures;
import br.ufsc.silq.test.WebContextTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerformanceTest extends WebContextTest {

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private CurriculumLattesService curriculumService;

	private CurriculumLattes lattesChristiane;

	private CurriculumLattes lattesRaul;

	private CurriculumLattes lattesRonaldo;

	@Before
	public void setup() throws SilqException {
		this.lattesChristiane = this.curriculumService.saveFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		this.lattesRaul = this.curriculumService.saveFromUpload(Fixtures.RAUL_XML_UPLOAD);
		this.lattesRonaldo = this.curriculumService.saveFromUpload(Fixtures.RONALDO_ZIP_UPLOAD);
	}

	@Test
	public void testNothing() {
		// Dummy assertion só para o teste passar caso todos os testes abaixo
		// estejam comentados
		Assertions.assertThat(true).isTrue();
	}

	/**
	 * Calcula uma média do tempo de execução do parsing + cálculo de
	 * similaridade de um currículo qualquer. Utilizado para fine-tuning dos
	 * algoritmos de comparação e parsing.
	 *
	 * @throws SilqLattesException
	 */
	// @Test
	public void testCompareExecutionMeanTime() throws SilqLattesException {
		// Últimas médias calculadas:
		// 478ms (30 iterações)

		int iterations = 30;
		double executionTotalTime = 0;

		for (int i = 0; i < iterations; i++) {
			log.debug("Running iteration #" + i + " of performance test");
			double iterationTotal = 0;
			iterationTotal += this.compareAndGetExecutionTime(this.lattesChristiane);
			iterationTotal += this.compareAndGetExecutionTime(this.lattesRaul);
			iterationTotal += this.compareAndGetExecutionTime(this.lattesRonaldo);
			executionTotalTime += iterationTotal;
		}

		double mean = executionTotalTime / (iterations * 3);
		log.info("Parsing + Comparing execution mean time: " + mean + " ms");
	}

	/**
	 * Calcula a similaridade e retorna o tempo de execução em milissegundos.
	 *
	 * @param lattes
	 * @return
	 * @throws SilqLattesException
	 */
	private double compareAndGetExecutionTime(CurriculumLattes lattes) throws SilqLattesException {
		AvaliarForm form = new AvaliarForm();
		form.setArea("Ciência da Computação");

		StopWatch watch = new StopWatch();
		watch.start();
		this.avaliacaoService.avaliar(lattes, form, null);
		watch.stop();

		// log.debug("Result in " + watch.getTotalTimeMillis() + "ms");
		return watch.getTotalTimeMillis();
	}

}
