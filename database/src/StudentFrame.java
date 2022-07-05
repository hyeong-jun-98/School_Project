import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class StudentFrame extends JFrame implements ActionListener {
	void createTabbedPane() {
		JTabbedPane tPane = new JTabbedPane();
		add(tPane);

		JLabel mainLabel = new JLabel("첫번째", SwingConstants.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.add(mainLabel);
		tPane.addTab("1", mainPanel);
	}

	static String sqlSelectAll = "select * from 병원;";
	static String sqlInsert = "insert 병원(이름, 우편번호, 도로명주소, 영업상태, 진료과목정보, 전화번호 ) values(?, ?, ?, ?, ?, ?);";
	static String sqlDelete = "delete from 병원 where 이름 = ?;";
	static String sqlUpdate = "update 병원 set 이름 where 이름 = ?;"; // 수정해야할 부분
	static String sqlSearch = "select * from 병원  where 우편번호 = ?;";
	static String sqlSearchAll = "select * from 병원;";

	Connection conn = null;

	JTable table;
	JTextField 이름Text, 우편번호Text, 도로명주소Text, 영업상태Text, 진료과목정보Text, 전화번호Text;
	JButton insButton, delButton, updateButton, searchButton;
	JPanel panel;
	JLabel status;

	StudentFrame() {
		setTitle("주변 정보 알리미");

		// 창 닫을 때 DB 연결 해제하도록 설정
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeDBConnection();

				System.exit(0);
			}
		});

		Container contentPane = getContentPane();

		// table
		String colNames[] = { "이름", "우편번호", "도로명주소", "영업상태", "진료과목정보", "전화번호" };
		DefaultTableModel model = new DefaultTableModel(colNames, 0) {
			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				else
					return true;
			}
		};
		table = new JTable(model);
		status = new JLabel();

		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

		// panel
		JPanel inputPanel = new JPanel();
		이름Text = new JTextField(5);
		우편번호Text = new JTextField(5);
		도로명주소Text = new JTextField(5);
		영업상태Text = new JTextField(5);
		진료과목정보Text = new JTextField(5);
		전화번호Text = new JTextField(5);
		insButton = new JButton("삽입");
		delButton = new JButton("삭제");
		updateButton = new JButton("수정");
		searchButton = new JButton("검색");
		inputPanel.add(new JLabel("이름"));
		inputPanel.add(이름Text);
		inputPanel.add(new JLabel("우편번호"));
		inputPanel.add(우편번호Text);
		inputPanel.add(new JLabel("도로명주소 "));
		inputPanel.add(도로명주소Text);
		inputPanel.add(new JLabel("영업상태 "));
		inputPanel.add(영업상태Text);
		inputPanel.add(new JLabel("진료과목정보 "));
		inputPanel.add(진료과목정보Text);
		inputPanel.add(new JLabel("전화번호 "));
		inputPanel.add(전화번호Text);
		inputPanel.add(insButton);
		inputPanel.add(delButton);
		inputPanel.add(updateButton);
		inputPanel.add(searchButton);

		panel = new JPanel(new BorderLayout());
		panel.add(inputPanel, BorderLayout.CENTER);
		panel.add(status, BorderLayout.SOUTH);

		contentPane.add(panel, BorderLayout.SOUTH);

		// action listener
		insButton.addActionListener(this);
		delButton.addActionListener(this);
		updateButton.addActionListener(this);
		searchButton.addActionListener(this);
	}

	// 소멸자
	protected void finalize() {
		closeDBConnection();
	}

	// DB 연결이 유효헌지 확인, 유효하지 않으면 새로운 연결 수립
	void validateDBConnection() {
		try {
			if (conn == null) {
				conn = DBConnManager.getConnection();
				System.err.println("DB가 연결되었습니다.");
			} else if (!conn.isValid(15)) { // 15초 이내에 정상적인 응답이 없으면,
				conn.close();
				conn = DBConnManager.getConnection();
				System.err.println("DB가 재연결되었습니다.");
			}
		} catch (SQLException e) {
			System.err.println("DB에 접근할 수 없습니다.");
			e.printStackTrace();
		}
	}

	// DB 연결해제
	void closeDBConnection() {
		try {
			DBConnManager.closeConnection(conn);
			System.err.println("DB 연결이 해제되었습니다.");
		} catch (SQLException e) {
			System.err.println("DB 연결 해제 중 에러!");
			e.printStackTrace();
		}
	}

	// 테이블 초기화(모든 레코드)
	public int initTableWithDB() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			// DB 연결
			status.setText("DB 연결중...");
			validateDBConnection();
			status.setText("DB 연결완료");

			// statement 준비
			stmt = conn.prepareStatement(sqlSelectAll);

			// sql 실행
			rs = stmt.executeQuery();

			// 테이블에서 세팅
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String arr[] = new String[6];

			while (rs.next()) {
				arr[0] = rs.getString(1);
				arr[1] = rs.getString(2);
				arr[2] = rs.getString(3);
				arr[3] = rs.getString(4);
				arr[4] = rs.getString(5);
				arr[5] = rs.getString(6);
				model.addRow(arr);
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 리소스 반환
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

			setStatus(count + "개의 병원정보가 DB에 존재합니다.");
		}

		return count;
	}

	// 삽입/삭제/수정/검색 처리
	public void actionPerformed(ActionEvent ae) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			// DB 연결확인
			validateDBConnection();

			Object src = ae.getSource();
			if (src == insButton) { // 삽입
				stmt = conn.prepareStatement(sqlInsert);

				String nameStr = 이름Text.getText().trim();
				String postnumStr = 우편번호Text.getText().trim();
				String addressStr = 도로명주소Text.getText().trim();
				String activeStr = 영업상태Text.getText().trim();
				String subjectStr = 진료과목정보Text.getText().trim();
				String phoneStr = 전화번호Text.getText().trim();

				stmt.setString(1, nameStr);
				stmt.setString(2, postnumStr);
				stmt.setString(3, addressStr);
				stmt.setString(4, activeStr);
				stmt.setString(5, subjectStr);
				stmt.setString(6, phoneStr);

				setStatus("DB에 추가중...");
				int num = stmt.executeUpdate();

				if (num > 0) {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					String arr[] = new String[6];
					arr[0] = nameStr;
					arr[1] = postnumStr;
					arr[2] = addressStr;
					arr[3] = activeStr;
					arr[4] = subjectStr;
					arr[5] = phoneStr;
					model.addRow(arr);

					setStatus(num + "개의 병원 정보가 추가되었습니다.");
				} else
					setStatus("병원 정보가 추가되지 않았습니다.");
			} else if (src == delButton) { // 삭제
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "삭제할 행을 먼저 선택한 후 삭제버튼을 클릭하세요.");
				} else {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					stmt = conn.prepareStatement(sqlDelete);
					stmt.setString(1, model.getValueAt(row, 0).toString());

					setStatus("DB에서 삭제중 ...");
					int num = stmt.executeUpdate();

					if (num > 0) {
						model.removeRow(row);
						setStatus(num + "개의 병원 정보가 삭제되었습니다.");
					} else
						setStatus(" 개의 병원 정보가 삭제되지 않았습니다.");

				}
			} else if (src == updateButton) { // 수정
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "수정할 행을  선택하여 직접 값을 수정한 후 수정 버튼을 클릭하세요.");
				} else {
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					stmt = conn.prepareStatement(sqlUpdate);
					stmt.setString(1, model.getValueAt(row, 1).toString().trim());
					stmt.setString(2, model.getValueAt(row, 2).toString().trim());
					stmt.setString(3, model.getValueAt(row, 3).toString().trim());
					stmt.setString(4, model.getValueAt(row, 4).toString().trim());
					stmt.setString(5, model.getValueAt(row, 5).toString().trim());
					stmt.setString(6, model.getValueAt(row, 6).toString().trim());

					setStatus("DB 수정중...");
					int num = stmt.executeUpdate();

					if (num > 0) {
						model.fireTableDataChanged();
						setStatus(num + "개의 병원 정보가 수정되었습니다.");
					} else
						setStatus("개의 병원 정보가 수정되지 않았습니다.");
				}
			} else if (src == searchButton) { // 검색

				String postnum = 우편번호Text.getText().trim();
				if (postnum.compareTo("") == 0)
					stmt = conn.prepareStatement(sqlSearchAll);
				else {
					stmt = conn.prepareStatement(sqlSearch);
					stmt.setString(1, postnum);
				}

				setStatus("DB 검색중...");
				rs = stmt.executeQuery();

				// 테이블에 세팅
				int count = 0;
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int rowNum = model.getRowCount();
				for (int i = rowNum - 1; i >= 0; i--)
					model.removeRow(i);
				String arr[] = new String[6];
				while (rs.next()) {
					arr[0] = rs.getString(1);
					arr[1] = rs.getString(2);
					arr[2] = rs.getString(3);
					arr[3] = rs.getString(4);
					arr[4] = rs.getString(5);
					arr[5] = rs.getString(6);
					model.addRow(arr);
					count++;
				}

				setStatus(count + "개의 병원이 검색되었습니다.");
			}
		} catch (SQLException e) {
			setStatus("DB에 접근할 수 없거나 SQL을 실행할 수 없습니다.");
			e.printStackTrace();
		} finally {
			// 리소스 반환
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
		}
	}

	public void setStatus(String s) {
		status.setText(s);
		this.validate();
	}

	public static void main(String[] args) {
		StudentFrame frame = new StudentFrame();
		frame.setPreferredSize(new Dimension(1000, 600));
		frame.setLocation(500, 300);
		frame.pack();
		frame.setVisible(true);
		frame.initTableWithDB();
	}

}
