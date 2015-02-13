package pack3;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DbPre3_crud extends JPanel implements ActionListener {
	JLabel label1, label2, label3;
	JTextField textField1, textField2, textField3;
	JButton btn1, btn2, btn3, btn4, btn5;
	JTextArea textArea;
	JScrollPane jScrollPane;
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String upNo = "0" , delNo = "0";

	public DbPre3_crud() {
		this.setLayout(null);
		label1 = new JLabel("상품명");
		label2 = new JLabel("수량");
		label3 = new JLabel("단가");
		label1.setBounds(new Rectangle(20, 10, 50, 25));
		label2.setBounds(new Rectangle(20, 40, 50, 25));
		label3.setBounds(new Rectangle(20, 70, 50, 25));

		textField1 = new JTextField();
		textField2 = new JTextField();
		textField3 = new JTextField();

		textField1.setBounds(new Rectangle(70, 10, 50, 25));
		textField2.setBounds(new Rectangle(70, 40, 50, 25));
		textField3.setBounds(new Rectangle(70, 70, 50, 25));

		btn1 = new JButton("등록");
		btn2 = new JButton("수정");
		btn3 = new JButton("확인");
		btn3.setEnabled(false);
		btn4 = new JButton("삭제");
		btn5 = new JButton("닫기");

		btn1.setBounds(new Rectangle(150, 10, 70, 25));
		btn2.setBounds(new Rectangle(150, 40, 70, 25));
		btn3.setBounds(new Rectangle(240, 40, 70, 25));
		btn4.setBounds(new Rectangle(150, 70, 70, 25));
		btn5.setBounds(new Rectangle(150, 100, 70, 25));

		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		btn4.addActionListener(this);
		btn5.addActionListener(this);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(textArea);
		jScrollPane.setBounds(new Rectangle(20, 150, 350, 200));

		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(textField1);
		this.add(textField2);
		this.add(textField3);

		this.add(btn1);
		this.add(btn2);
		this.add(btn3);
		this.add(btn4);
		this.add(btn5);
		this.add(jScrollPane);

		dbconnect();
		sangShow(0);
	}

	void dbconnect() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("로딩실패" + e);
			System.exit(0);
		}
	}

	void sangShow(int arg) {
		String sql = "", str = "";

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");

			if (arg == 0) { // 전체 목록
				textArea.setText("");

				sql = " select * from sangdata";

				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				// 컬럼명 얻기
				ResultSetMetaData metaData = rs.getMetaData();
				int cnt = metaData.getColumnCount();

				str = metaData.getColumnName(1);// code
				for (int i = 2; i <= cnt; i++) {
					str += "\t" + metaData.getColumnName(i);
				}
				str += "\n";
				textArea.append(str);

				while (rs.next()) {
					String data = rs.getString("code") + "\t"
							+ rs.getString("sang") + "\t" + rs.getString("su")
							+ "\t" + rs.getString("dan") + "\n";
					textArea.append(data);
				}
			} else { // 부분 목록
				sql = " select * from sangdata where code = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, arg);
				
				rs = pstmt.executeQuery();

				if ( rs.next()){
					textField1.setText(rs.getString(2));
					textField2.setText(rs.getString(3));
					textField3.setText(rs.getString(4));
					btn3.setEnabled(true); //수정확인버튼
				} else {
					JOptionPane.showMessageDialog(this, arg+ "번은 등록되지 않는 번호입니다");
				}
			}
		} catch (Exception e) {
			System.out.println("sangShow err" + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	void insertShow() {
		if (textField1.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "상품 명을 입력하시오");
			textField1.requestFocus();
			return;
		}
		try {
			// 새상품 코드 구하기
			int newcode = 0;

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
			pstmt = conn.prepareStatement(" select max(code) from sangdata ");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				newcode = rs.getInt(1);
			}
			newcode += 1;

			//System.out.println("신상코드 : " + newcode);
			
			String sql = " insert into sangdata values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newcode);
			pstmt.setString(2, textField1.getText());
			pstmt.setString(3, textField2.getText());
			pstmt.setString(4, textField3.getText());
			
			int re = pstmt.executeUpdate();
			if (re == 0)
				JOptionPane.showMessageDialog(this, "관리자에게 문의");
			
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			textField1.requestFocus();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	void updateDate(String upNo) {
		String sql = " update sangdata  set sang = ?, su = ?, dan = ? where code = ?";
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField1.getText());
			pstmt.setString(2, textField2.getText());
			pstmt.setString(3, textField3.getText());
			pstmt.setString(4, upNo);
			
			int re = pstmt.executeUpdate();
			if (re == 0)
				JOptionPane.showMessageDialog(this, "관리자에게 문의");
		} catch (Exception e) {
			// TODO: handle exception
		}  finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("등록")) {
			insertShow();
			sangShow(0);
			return;
		} else if (command.equals("수정")) {
			//수정 대상 자료 읽기
			upNo = JOptionPane.showInputDialog(this, "수정할 상품 코드입력", "", JOptionPane.OK_CANCEL_OPTION);
			if (upNo == null )
				return;
			
			sangShow(Integer.parseInt(upNo));			
		} else if (command.equals("삭제")) {
			//수정 대상 자료 읽기
			upNo = JOptionPane.showInputDialog(this, "삭제할 상품 코드입력", "", JOptionPane.OK_CANCEL_OPTION);
			if (upNo == null )
				return;
			
			//삭제 처리
			int re = JOptionPane.showConfirmDialog(this, "정말로 삭제할까요?","삭제", JOptionPane.YES_NO_OPTION);
			
			if (re == JOptionPane.YES_OPTION){
				String sql = " delete from sangdata where code = ?";
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, upNo);
					
					int re1 = pstmt.executeUpdate();
					if (re1 == 0)
						JOptionPane.showMessageDialog(this, "관리자에게 문의");
				} catch (Exception e1) {
					// TODO: handle exception
				}  finally {
					try {
						if (rs != null)
							rs.close();
						if (pstmt != null)
							pstmt.close();
						if (conn != null)
							conn.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				sangShow(0);
			}
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			textField1.requestFocus();
		} else if (command.equals("확인")) {
			//수정 처리
			int re = JOptionPane.showConfirmDialog(this, "정말로 수정할까요?","수정", JOptionPane.YES_NO_OPTION);
			
			if (re == JOptionPane.YES_OPTION){
				updateDate(upNo);
				sangShow(0);
			}
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			textField1.requestFocus();
			btn3.setEnabled(false);
		} else if (command.equals("닫기")) {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				System.exit(0);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(" 상품 자료 처리 ");
		frame.getContentPane().add(new DbPre3_crud());
		frame.setBounds(200, 200, 400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
