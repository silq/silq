package br.ufsc.silq.core.parser.qualis.similarity;
//package silqcore.app.parser.qualis.similarity;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import silqcore.app.utils.parser.PDFExtrator;
//import silqcore.app.utils.parser.TextSeparation;
//import silqcore.app.utils.parser.TextTreatment;
//
//public class Main {
//
//	@SuppressWarnings("unused")
//	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		String caminho = "Consulta_Webqualis.pdf";
//		// String caminho = "QualisConferenciaCienciaDaComputacao.pdf";
//		String text = PDFExtrator.pdfExtrator(caminho);
//		String[] texto = TextTreatment.treatText(text);
//		ArrayList<String[]> result;
//		if (TextTreatment.qualisType) {
//			result = TextSeparation.toArrayListPeriodicos(texto);
//		} else {
//			result = TextSeparation.toArrayListConferencia(texto);
//		}
//
//		Class.forName("org.postgresql.Driver");
//		Connection connection = null;
//		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "mingaudeaveia");
//		// SQLCreator.sqlCreator(connection, result, "TB_QUALIS_GERAL",
//		// "SQ_QUALIS_GERAL");
//		// SQLCreator.sqlCreator(connection, result, "TB_QUALIS_CCO",
//		// "SQ_QUALIS_CCO");
//
//		File file = new File("C:/Users/Felps/Dropbox/Eloisa_Felipe/UFSC/TCC/Elow/ExemploLeitorPDF/Curriculos/0378897709136226.xml");
//		List<File> list = new ArrayList<>();
//		list.add(file);
//		// List<ParseResult> result = new ArrayList<>();
//		try {
//			// result = null;
//			// LattesParser.parseCurricula(list.get(0), null); // MUDEI AQUI;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String email = "bla@bla.com";
//
//		if (result.size() > 0) {
//			// InsertDb.migrate(connection, result.get(0), email);
//			// for (ParseResult rs : result) {
//			// CompareSimilarity.compare(connection, rs);
//			// }
//		}
//
//		connection.close();
//	}
// }
