package br.ufsc.silq.core.utils.parser;
//package silqcore.app.utils.parser;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import org.apache.pdfbox.cos.COSDocument;
//import org.apache.pdfbox.pdfparser.PDFParser;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.util.PDFTextStripper;
//
//public class PDFExtrator {
//	/**
//	 * Le o conteudo (texto) de um arquivo pdf
//	 * 
//	 */
//
//	public static String pdfExtrator(String fileName) {
//
//		PDFParser parser;
//		String parsedText = null;
//
//		PDFTextStripper pdfStripper = null;
//		PDDocument pdDoc = null;
//		COSDocument cosDoc = null;
//		File file = new File(fileName);
//
//		if (!file.isFile()) {
//			System.err.println("File " + fileName + " does not exist.");
//			return null;
//		}
//
//		try {
//			parser = new PDFParser(new FileInputStream(fileName));
//		} catch (IOException e) {
//			System.err.println("Unable to open PDF Parser. " + e.getMessage());
//			return null;
//		}
//
//		try {
//			parser.parse();
//			cosDoc = parser.getDocument();
//			pdfStripper = new PDFTextStripper();
//			pdDoc = new PDDocument(cosDoc);
//			parsedText = pdfStripper.getText(pdDoc);
//		} catch (Exception e) {
//			System.err.println("An exception occured in parsing the PDF Document." + e.getMessage());
//		} finally {
//			try {
//				if (cosDoc != null)
//					cosDoc.close();
//				if (pdDoc != null)
//					pdDoc.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return parsedText;
//	}
//
//}