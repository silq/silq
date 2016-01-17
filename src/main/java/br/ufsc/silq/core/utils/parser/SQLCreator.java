package br.ufsc.silq.core.utils.parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLCreator {

	public static void sqlCreator(Connection connection, ArrayList<String[]> qualis, String tableName, String sequence) throws SQLException {

		PreparedStatement statement = null;
		String aux = "INSERT INTO " + tableName + " VALUES (NEXTVAL(\'" + sequence + "\')";
		String sql = aux;
		try {
			for (String[] entry : qualis) {
				for (String text : entry) {
					if (text.contains("'")) {
						text = text.replaceAll("\'", "''");
					}
					text = text.trim();
					sql = sql + ", " + "\'" + text + "\'";
				}
				sql += ")";
				statement = connection.prepareStatement(sql);
				statement.executeUpdate();
				sql = aux;
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
}
