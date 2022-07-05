import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//DB ���� ���� �� ����
public class DBConnManager {
	static String dbServerAddr = "jdbc:mysql://20.41.98.141:3306/";
	static String dbName = "stdt086"; // ������ DB �̸����� ����
	static String user = "stdt086"; // ������ ���� �̸����� ����
	static String pswd = "!stdt086"; // ������ ��й�ȣ�� ����

	// DB ���� ����
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbServerAddr + dbName, user, pswd);
	}

	public static Connection getConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(dbServerAddr + dbName, user, pswd);
	}

	// DB ���� ���� 
	public static void closeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
}
