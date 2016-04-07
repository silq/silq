package br.ufsc.silq.core.business.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.exception.SilqLattesException;

public class DocumentManagerTest extends WebContextTest {

	@Inject
	private DocumentManager documentManager;

	@Test
	public void testUploadCurriculumXml() throws SilqLattesException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RONALDO_XML_UPLOAD);
		Assertions.assertThat(document).isNotNull();
	}

	@Test
	public void testIsZipUpload() throws SilqLattesException {
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.RAUL_XML_UPLOAD)).isFalse();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.EMPTY_XML_UPLOAD)).isFalse();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.ERROR_XML_UPLOAD)).isFalse();

		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.RAUL_ZIP_UPLOAD)).isTrue();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.EMPTY_ZIP_UPLOAD)).isTrue();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.NO_XML_ZIP_UPLOAD)).isTrue();
	}

	@Test
	public void testUploadCurriculumZip() throws SilqLattesException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(Fixtures.CHRISTIANE_ZIP_UPLOAD);
		Assertions.assertThat(document).isNotNull();
	}

	@Test
	public void testUploadCurriculumXmlWithError() throws SilqLattesException {
		Assertions.assertThatThrownBy(() -> {
			this.documentManager.extractXmlDocumentFromUpload(Fixtures.ERROR_XML_UPLOAD);
		}).isInstanceOf(SilqLattesException.class);

		Assertions.assertThatThrownBy(() -> {
			this.documentManager.extractXmlDocumentFromUpload(Fixtures.EMPTY_XML_UPLOAD);
		}).isInstanceOf(SilqLattesException.class);
	}

	@Test
	public void testUploadCurriculumZipWithError() {
		Assertions.assertThatThrownBy(() -> {
			this.documentManager.extractXmlDocumentFromUpload(Fixtures.EMPTY_ZIP_UPLOAD);
		}).isInstanceOf(SilqLattesException.class);

		Assertions.assertThatThrownBy(() -> {
			this.documentManager.extractXmlDocumentFromUpload(Fixtures.NO_XML_ZIP_UPLOAD);
		}).isInstanceOf(SilqLattesException.class);
	}
}
