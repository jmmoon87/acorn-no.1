package pack4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class Client extends JFrame implements ActionListener, Runnable{
    private JLabel jLabel1 = new JLabel();
    private JTextField txtname = new JTextField();
    private JButton btnconn = new JButton();
    private JTextArea txtarea = new JTextArea();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextField txtsend = new JTextField();
    private JButton btnok = new JButton();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel lblinwon = new JLabel();
    private JRadioButton radio1 = new JRadioButton();
    private JRadioButton radio2 = new JRadioButton();
    private JButton btnclose = new JButton();
    private List list = new List();
    private JButton btnchange = new JButton();

    private BufferedReader in;
    private OutputStream out;
    private Socket soc;
    int count=0; //���� �ο���
     
    public Client() {
        try {
            jbInit();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(652, 264));
        this.setTitle("ä�� ���α׷�");
        this.setBackground(new Color(198, 214, 255));
        jLabel1.setText("��ȭ��:");
        jLabel1.setBounds(new Rectangle(15, 10, 45, 25));
        txtname.setBounds(new Rectangle(60, 10, 105, 25));
        btnconn.setText("����");
        btnconn.setBounds(new Rectangle(165, 10, 60, 25));
        jScrollPane1.setBounds(new Rectangle(15, 40, 495, 155));
        txtsend.setBounds(new Rectangle(15, 200, 435, 25));
        btnok.setText("Ȯ��");
        btnok.setBounds(new Rectangle(450, 200, 60, 25));
        jLabel2.setText("������ ���");
        jLabel2.setBounds(new Rectangle(520, 10, 75, 20));
        jLabel3.setText("�ο�:");
        jLabel3.setBounds(new Rectangle(530, 170, 35, 25));
        lblinwon.setText("0");
        lblinwon.setBounds(new Rectangle(565, 170, 50, 25));
        lblinwon.setBackground(new Color(198, 198, 200));
        lblinwon.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lblinwon.setHorizontalAlignment(SwingConstants.CENTER);
        lblinwon.setHorizontalTextPosition(SwingConstants.CENTER);
        radio1.setText("�ӼӸ�");
        radio1.setBounds(new Rectangle(345, 10, 70, 25));
        radio2.setText("�ӼӸ�����");
        radio2.setBounds(new Rectangle(420, 10, 90, 25));
        btnclose.setText("������");
        btnclose.setBounds(new Rectangle(530, 200, 90, 25));
        list.setBounds(new Rectangle(525, 40, 110, 120));
        btnchange.setText("��ȭ�� ����");
        btnchange.setBounds(new Rectangle(230, 10, 110, 25));
        ButtonGroup group = new ButtonGroup();
        group.add(radio1); 
        group.add(radio2);
        this.getContentPane().add(btnchange, null);
        this.getContentPane().add(list, null);
        this.getContentPane().add(btnclose, null);
        this.getContentPane().add(radio1, null);
        this.getContentPane().add(radio2, null);
        this.getContentPane().add(lblinwon, null);
        this.getContentPane().add(jLabel3, null);
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(btnok, null);
        this.getContentPane().add(txtsend, null);
        jScrollPane1.getViewport().add(txtarea, null);
        this.getContentPane().add(jScrollPane1, null);
        this.getContentPane().add(btnconn, null);
        this.getContentPane().add(txtname, null);
        this.getContentPane().add(jLabel1, null);
    }

    public void addListener(){
        txtname.addActionListener(this);
        txtsend.addActionListener(this);
        btnok.addActionListener(this);
        btnconn.addActionListener(this);
        btnclose.addActionListener(this);
        btnchange.addActionListener(this);
   }
    
    void conProcess() {
    	try {
    		soc = new Socket("localhost", 5555);
    		//soc = new Socket("192.168.0.22", 5555);
    		
    		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
    		out = soc.getOutputStream();
    		
    		out.write((txtname.getText() +"\n").getBytes());
    		new Thread(this).start(); //������ ���� �ٸ� ä������ �޽��� ���
			
		} catch (Exception e) {
			System.out.println("conProcess err "+ e);
		}
    }
    
    void sendProcess() {
    	try {
    		if (radio1.isSelected() == true){ //�ӼӸ�
    			String name = list.getSelectedItem();
    			out.write(("/s" +name +"-" +txtsend.getText() +"\n").getBytes());
    			
    			txtarea.append(name + "�Բ� �ͼӸ��� ���� �Ǿ����ϴ�. \n");
    		} else {// ��� ����
    			out.write((txtsend.getText() +"\n").getBytes());
    		}
			
    		txtsend.setText("");
    		txtsend.requestFocus();
    		
		} catch (Exception e) {
			System.out.println("sendProcess err "+ e);
		}
    }
    
    void changeProcess() {
    	try {
    		out.write(("/n " + txtname.getText() +"\n").getBytes());
		} catch (Exception e) {
			System.out.println("changeProcess err "+ e);
		}
    }
    
    void exitProcess() {
    	try {
    		out.write(("/q\n ").getBytes());
    		if(in != null) in.close();
    		if(out != null) out.close();
    		if(soc != null) soc.close();
		} catch (Exception e) {
			//System.out.println("exitProcess err "+ e);
		}
    	System.exit(0);
    }
    
    public void actionPerformed(ActionEvent e){
	   if((e.getSource() == txtarea) || (e.getSource() == btnconn)) {
		   if (txtname.getText().equals("")){
			   JOptionPane.showMessageDialog(this, "��ȭ���� �Է��Ͻÿ�");
			   txtname.requestFocus();
			   return;
		   }
		   conProcess();
	   } else if ((e.getSource() == txtsend) || (e.getSource() == btnok)) {
		   sendProcess();
	   } else if (e.getSource() == btnchange) {
		   if(btnchange.getText().equals("��ȭ�� ����")){
			   btnchange.setText("����Ȯ��");
			   txtname.setEnabled(true);
			   txtname.requestFocus();
		   } else {
			   btnchange.setText("��ȭ�� ����");
			   txtname.setEnabled(false);
		   }
		   
		   changeProcess();
	   } else if (e.getSource() == btnclose) {
		   exitProcess();
	   }
   }
    
    @Override
    public void run() {
    	while(true){
    		try {
				String  msg = in.readLine(); //ä�ü����� ���� �޽��� �ޱ�
				if (msg == null) return;
				
				if (msg.charAt(0) == '/'){
					if(msg.charAt(1) == 'c'){
						list.add(msg.substring(2), count);
						count++;
						
						lblinwon.setText(String.valueOf(count));
						
						//���� �޽���
						txtarea.append("***"+ msg.substring(2)+ "�� ���� ! \n");
						txtname.setEnabled(false);//���Ӹ� �Է� �Ұ�
						btnconn.setEnabled(false);
					} else if (msg.charAt(1) == 'q'){
						txtarea.append("###" + msg.substring(2)+ "�� ���� ! \n" );
						String str = msg.substring(2);
						
						for (int i = 0; i <count; i++) {
							if(str.equals(list.getItem(i))){
								list.remove(i);
								count--;
								lblinwon.setText(String .valueOf(count));
								break;
							}
						}
					} else if (msg.charAt(1) == 'n'){
						String oldName = msg.substring(msg.indexOf('-'));
						String newName = msg.substring(msg.indexOf('-')+1);
						
						txtarea.append("%%" + oldName + "���� ��ȭ���� "+ newName +"���� ����� \n");
						
						for (int i = 0; i < count; i++) {
							if(oldName.equals(list.getItem(i))){
								list.replaceItem(newName, i);
								break;
							}
						}
					}
				} else {
					txtarea.append(msg + "\n");
				}
			} catch (Exception e) {
				txtarea.append("client run err");
			}
    	}
    }
    
   public static void main(String args[]){
        Client fr=new Client();
        fr.getPreferredSize();
        fr.setLocation(200,200);
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}

