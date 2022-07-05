import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBSample1 {
	static String dbServerAddr = "jdbc:mysql://20.41.98.141:3306/";
	static String dbName = "stdt086"; // 여러분 DB 이름으로 수정
	static String user = "stdt086"; // 여러분 계정 이름으로 수정
	static String pswd = "!stdt086"; // 여러분 비밀번호로 수정

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	DBSample1() {
	}

	/*
	 * [실습1] STUDENT 테이블에서 ID가 20116745인 학생의 ID, 이름 전화전호를 검색하도록 SelectTest 함수를
	 * 수정하시오.
	 */
	public void SelectTest() {
		try {
			// DB 연결
			conn = DriverManager.getConnection(dbServerAddr + dbName, user, pswd);

			// SQL 실행
			stmt = conn.prepareStatement("select * from 병원;");
			rs = stmt.executeQuery();

			// Result Set 처리
			System.out.println("이름\t\t\t우편번호\t도로명 주소\t영업상태\t진료과목정보\t전화번호");
			System.out.println("-------------------------------------");
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t\t\t||" + rs.getString(2) + "\t||" + rs.getString(3)+ "\t||" + rs.getString(4)+ "\t||" + rs.getString(5)+ "\t||" + rs.getString(6));
			}
		} catch (SQLException e) {
			System.err.println("DB에 접근할 수 없거나 SQL을 실행할 수 없습니다.");
			e.printStackTrace();
		}
		// 리소스 변환
		finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	/*
	 * [실습2] 다음과 같은 방식으로 사용자로부터 ID를 입력받아 해당 ID 학생의 이름과 전화번호를 검색하도록 InputSelectTest
	 * 함수를 수정하시오. 함수 수정 후 main 함수의 커멘트를 해제ㅏ여 실행하시오.
	 */
	public void InputSelectTest() {
		// 사용자로부터 ID를 입력받는 것은 아래코드를 활용할 것.
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("검색할 이름를 입력하세요. 이름: ");
		String str = reader.readLine();
		
			// DB 연결
			conn = DriverManager.getConnection(dbServerAddr + dbName, user, pswd);

			// SQL 실행
			stmt = conn.prepareStatement("select * from 병원 where 이름 = ?;");
			
			rs = stmt.executeQuery();

			// Result Set 처리
			System.out.println("이름\t\t\t우편번호\t도로명 주소\t영업상태\t진료과목정보\t전화번호");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3)+ "\t" + rs.getString(4)+ "\t" + rs.getString(5)+ "\t" + rs.getString(6));
			}
		} catch (SQLException e) {
			System.err.println("DB에 접근할 수 없거나 SQL을 실행할 수 없습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("오류!");
			e.printStackTrace();
		}
		
		// 리소스 변환
		finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}

	}

	public static void main(String[] args) {
		DBSample1 sample = new DBSample1();
		sample.SelectTest();
		sample.InputSelectTest();
	}
}
