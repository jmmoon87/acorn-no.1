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
		label1 = new JLabel("��ǰ��");
		label2 = new JLabel("����");
		label3 = new JLabel("�ܰ�");
		label1.setBounds(new Rectangle(20, 10, 50, 25));
		label2.setBounds(new Rectangle(20, 40, 50, 25));
		label3.setBounds(new Rectangle(20, 70, 50, 25));

		textField1 = new JTextField();
		textField2 = new JTextField();
		textField3 = new JTextField();

		textField1.setBounds(new Rectangle(70, 10, 50, 25));
		textField2.setBounds(new Rectangle(70, 40, 50, 25));
		textField3.setBounds(new Rectangle(70, 70, 50, 25));

		btn1 = new JButton("���");
		btn2 = new JButton("����");
		btn3 = new JButton("Ȯ��");
		btn3.setEnabled(false);
		btn4 = new JButton("����");
		btn5 = new JButton("�ݱ�");

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
			System.out.println("�ε�����" + e);
			System.exit(0);
		}
	}

	void sangShow(int arg) {
		String sql = "", str = "";

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");

			if (arg == 0) { // ��ü ���
				textArea.setText("");

				sql = " select * from sangdata";

				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				// �÷��� ���
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
			} else { // �κ� ���
				sql = " select * from sangdata where code = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, arg);
				
				rs = pstmt.executeQuery();

				if ( rs.next()){
					textField1.setText(rs.getString(2));
					textField2.setText(rs.getString(3));
					textField3.setText(rs.getString(4));
					btn3.setEnabled(true); //����Ȯ�ι�ư
				} else {
					JOptionPane.showMessageDialog(this, arg+ "���� ��ϵ��� �ʴ� ��ȣ�Դϴ�");
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
			JOptionPane.showMessageDialog(this, "��ǰ ���� �Է��Ͻÿ�");
			textField1.requestFocus();
			return;
		}
		try {
			// ����ǰ �ڵ� ���ϱ�
			int newcode = 0;

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
			pstmt = conn.prepareStatement(" select max(code) from sangdata ");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				newcode = rs.getInt(1);
			}
			newcode += 1;

			//System.out.println("�Ż��ڵ� : " + newcode);
			
			String sql = " insert into sangdata values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newcode);
			pstmt.setString(2, textField1.getText());
			pstmt.setString(3, textField2.getText());
			pstmt.setString(4, textField3.getText());
			
			int re = pstmt.executeUpdate();
			if (re == 0)
				JOptionPane.showMessageDialog(this, "�����ڿ��� ����");
			
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
				JOptionPane.showMessageDialog(this, "�����ڿ��� ����");
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

		if (command.equals("���")) {
			insertShow();
			sangShow(0);
			return;
		} else if (command.equals("����")) {
			//���� ��� �ڷ� �б�
			upNo = JOptionPane.showInputDialog(this, "������ ��ǰ �ڵ��Է�", "", JOptionPane.OK_CANCEL_OPTION);
			if (upNo == null )
				return;
			
			sangShow(Integer.parseInt(upNo));			
		} else if (command.equals("����")) {
			//���� ��� �ڷ� �б�
			upNo = JOptionPane.showInputDialog(this, "������ ��ǰ �ڵ��Է�", "", JOptionPane.OK_CANCEL_OPTION);
			if (upNo == null )
				return;
			
			//���� ó��
			int re = JOptionPane.showConfirmDialog(this, "������ �����ұ��?","����", JOptionPane.YES_NO_OPTION);
			
			if (re == JOptionPane.YES_OPTION){
				String sql = " delete from sangdata where code = ?";
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123");
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, upNo);
					
					int re1 = pstmt.executeUpdate();
					if (re1 == 0)
						JOptionPane.showMessageDialog(this, "�����ڿ��� ����");
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
		} else if (command.equals("Ȯ��")) {
			//���� ó��
			int re = JOptionPane.showConfirmDialog(this, "������ �����ұ��?","����", JOptionPane.YES_NO_OPTION);
			
			if (re == JOptionPane.YES_OPTION){
				updateDate(upNo);
				sangShow(0);
			}
			textField1.setText("");
			textField2.setText("");
			textField3.setText("");
			textField1.requestFocus();
			btn3.setEnabled(false);
		} else if (command.equals("�ݱ�")) {
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
		JFrame frame = new JFrame(" ��ǰ �ڷ� ó�� ");
		frame.getContentPane().add(new DbPre3_crud());
		frame.setBounds(200, 200, 400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
