import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSample2 {

	Connection conn = null;

	//������
	DBSample2() {}
	
	//�Ҹ���
	protected void finalize() {
		closeDBConnection();
	}
	
	//DB ������ ��ȿ���� Ȯ��, ��ȿ���� ������ ���ο� ���� ����
	void validateDBConnection() {
		try {
			if (conn == null) {
				conn = DBConnManager.getConnection();
				System.err.println("DB�� ����Ǿ����ϴ�.");
			}
			else if (!conn.isValid(15)) { //15�� �̳��� �������� ������ ������,
				conn.close();
				conn = DBConnManager.getConnection();
				System.err.println("DB�� �翬��Ǿ����ϴ� .");
			}
		}
		catch (SQLException e) {
			System.err.println("DB�� ������ �� �����ϴ�..");
			e.printStackTrace();
		}
	}
	
	//DB ��������
	void closeDBConnection() {
		try {
			DBConnManager.closeConnection(conn);
			System.err.println("DB ������ �����Ǿ����ϴ�.");
		}
		catch (SQLException e) {
			System.err.println("DB ���� ���� �� ����!");
			e.printStackTrace();
		}
	}
	
	public void SelectTest() {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// DB ����Ȯ��
		validateDBConnection();
		
		try {
			// SQL ����
			//stmt = conn.prepareStatement("select ID, NAME, PHONE from STUDENT where ID = 20116745;");
			stmt = conn.prepareStatement("select * from ����;");
			rs = stmt.executeQuery();
			
			// Result Set ó��
			System.out.println("�̸�\t\t\t�����ȣ\t���θ� �ּ�\t��������\t�����������\t��ȭ��ȣ");
			System.out.println("--------------------------------------------------------------");
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3)+ "\t" + rs.getString(4)+ "\t" + rs.getString(5)+ "\t" + rs.getString(6));
			}
		}
		catch (SQLException e) {
			System.err.println("DB�� ������ �� ���ų� SQL�� ������ �� �����ϴ�.");
			e.printStackTrace();
		}
		// ���ҽ� ��ȯ
		finally {
			if (rs != null)		try { rs.close(); }		catch (Exception e) {}
			if (stmt != null)	try { stmt.close(); }	catch (Exception e) {}
		}		
	}

	
	public void InputSelectTest() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// 1. ����ڷκ��� �˻��� ID�� �Է¹���
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("�˻��� �̸��� �Է��ϼ���. �̸�: ");
			String str = reader.readLine();
			
			// 2. DB ����Ȯ��
			validateDBConnection();
			
			// 3. SQL ����
			stmt = conn.prepareStatement("select * from ? ");
			
			rs = stmt.executeQuery();
			
			// 4. Result Set ó��
			System.out.println("�� ��\t��ȭ��ȣ");
			System.out.println("----------------------");
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2));
			}
		}
		catch (IOException e) {
			System.out.println("�Է¿���!");
		}
		catch (SQLException e) {
			System.out.println("DB�� ������ �� ���ų� SQL�� ������ �� �����ϴ�.");
			e.printStackTrace();
		}
		// 5. ���ҽ� ��ȯ
		finally {
			if (rs != null)		try { rs.close(); }		catch (Exception e) {}
			if (stmt != null)	try { stmt.close(); }	catch (Exception e) {}
		}		
	}

	
	public static void main(String[] args) {
		DBSample2 sample = new DBSample2();
		
		sample.SelectTest();
		sample.InputSelectTest();
		
		sample.closeDBConnection();
	}
}
