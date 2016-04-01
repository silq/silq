package br.ufsc.silq.core.business.service;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;

import br.ufsc.silq.Fixtures;
import br.ufsc.silq.WebContextTest;
import br.ufsc.silq.core.exception.SilqUploadException;

public class DocumentManagerTest extends WebContextTest {

	@Inject
	private DocumentManager documentManager;

	@Test
	public void testUploadCurriculumXml() throws SilqUploadException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RONALDO_XML_UPLOAD);
		Assertions.assertThat(document).isNotNull();
	}

	@Test
	public void testIsZipUpload() throws SilqUploadException {
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.CHRISTIANE_XML_UPLOAD)).isFalse();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.EMPTY_XML_UPLOAD)).isFalse();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.ERROR_XML_UPLOAD)).isFalse();

		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.RAUL_ZIP_UPLOAD)).isTrue();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.EMPTY_ZIP_UPLOAD)).isTrue();
		Assertions.assertThat(this.documentManager.isZipUpload(Fixtures.NO_XML_ZIP_UPLOAD)).isTrue();
	}

	@Test
	public void testUploadCurriculumZip() throws SilqUploadException {
		Document document = this.documentManager.extractXmlDocumentFromUpload(Fixtures.RAUL_ZIP_UPLOAD);
		Assertions.assertThat(document).isNotNull();
	}
}
