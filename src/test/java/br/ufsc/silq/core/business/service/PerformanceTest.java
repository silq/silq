package br.ufsc.silq.core.business.service;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.exception.SilqLattesException;
import br.ufsc.silq.core.forms.AvaliarForm;
import br.ufsc.silq.core.parser.LattesParser;

public class PerformanceTest extends WebContextTest {

	@Inject
	private LattesParser lattesParser;

	@Inject
	private DocumentManager documentManager;

	private Document documentXmlChristiane;

	private Document documentXmlRaul;

	private Document documentXmlRonaldo;

	@Before
	public void setup() throws SilqLattesException {
		this.documentXmlChristiane = this.documentManager.extractXmlDocumentFromUpload(Fixtures.CHRISTIANE_XML_UPLOAD);
		this.documentXmlRaul = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RAUL_XML_UPLOAD);
		this.documentXmlRonaldo = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RONALDO_XML_UPLOAD);
	}

	/**
	 * Calcula uma média do tempo de execução do parsing + cálculo de
	 * similaridade de um currículo qualquer. Utilizado para fine-tuning dos
	 * algoritmos de comparação e parsing.
	 *
	 * @throws SilqLattesException
	 */
	@Test
	public void testCompareExecutionMeanTime() throws SilqLattesException {

		int iterations = 5;
		double executionTotalTime = 0;

		for (int i = 0; i < iterations; i++) {
			double iterationTotal = 0;
			iterationTotal += this.compareAndGetExecutionTime(this.documentXmlChristiane);
			iterationTotal += this.compareAndGetExecutionTime(this.documentXmlRaul);
			iterationTotal += this.compareAndGetExecutionTime(this.documentXmlRonaldo);
			executionTotalTime += iterationTotal / 3;
		}

		System.out.println("Parsing + Comparing execution mean time: " + executionTotalTime / iterations + " seconds");
	}

	/**
	 * Calcula a similaridade e retorna o tempo de execução.
	 *
	 * @param document
	 * @return
	 * @throws SilqLattesException
	 */
	private double compareAndGetExecutionTime(Document document) throws SilqLattesException {
		AvaliarForm form = new AvaliarForm();
		form.setNivelSimilaridade("0.6");
		form.setArea("Ciência da Computação");

		long tStart = System.currentTimeMillis();
		this.lattesParser.parseCurriculum(document, form);
		long tEnd = System.currentTimeMillis();
		double elapsedSeconds = (tEnd - tStart) / 1000.0;

		// System.out.println(result);
		// System.out.println("Result in " + elapsedSeconds + "s");
		return elapsedSeconds;
	}

}
