package br.ufsc.silq.core.utils.files;

public class OldFileCleaner {

	public static void cleanUp() {
		// TODO (bonetti)
		/*
		 * String uploadPath =
		 * Play.application().configuration().getString("uploadPath");
		 * System.out.println("Iniciando limpeza do diretorio: " + uploadPath);
		 * 
		 * File folder = new File(uploadPath);
		 * 
		 * File[] listFiles = folder.listFiles();
		 * 
		 * // 30 minutos de limite para deletar os velhos arquivos; Long
		 * limitToDeletion = System.currentTimeMillis() - (30 * 60 * 999L); for
		 * (File file : listFiles) { if (file.lastModified() < limitToDeletion)
		 * { System.out.println("Deletando arquivo: " + file.getName()); if
		 * (!file.delete()) { System.out.println(
		 * "Não foi possível deletar o arquivo " + file.getAbsolutePath()); } }
		 * }
		 */
	}
}
