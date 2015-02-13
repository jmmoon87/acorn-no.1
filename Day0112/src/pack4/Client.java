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
    int count=0; //접속 인원수
     
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
        this.setTitle("채팅 프로그램");
        this.setBackground(new Color(198, 214, 255));
        jLabel1.setText("대화명:");
        jLabel1.setBounds(new Rectangle(15, 10, 45, 25));
        txtname.setBounds(new Rectangle(60, 10, 105, 25));
        btnconn.setText("접속");
        btnconn.setBounds(new Rectangle(165, 10, 60, 25));
        jScrollPane1.setBounds(new Rectangle(15, 40, 495, 155));
        txtsend.setBounds(new Rectangle(15, 200, 435, 25));
        btnok.setText("확인");
        btnok.setBounds(new Rectangle(450, 200, 60, 25));
        jLabel2.setText("접속자 목록");
        jLabel2.setBounds(new Rectangle(520, 10, 75, 20));
        jLabel3.setText("인원:");
        jLabel3.setBounds(new Rectangle(530, 170, 35, 25));
        lblinwon.setText("0");
        lblinwon.setBounds(new Rectangle(565, 170, 50, 25));
        lblinwon.setBackground(new Color(198, 198, 200));
        lblinwon.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lblinwon.setHorizontalAlignment(SwingConstants.CENTER);
        lblinwon.setHorizontalTextPosition(SwingConstants.CENTER);
        radio1.setText("귓속말");
        radio1.setBounds(new Rectangle(345, 10, 70, 25));
        radio2.setText("귓속말해제");
        radio2.setBounds(new Rectangle(420, 10, 90, 25));
        btnclose.setText("나가기");
        btnclose.setBounds(new Rectangle(530, 200, 90, 25));
        list.setBounds(new Rectangle(525, 40, 110, 120));
        btnchange.setText("대화명 변경");
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
    		new Thread(this).start(); //소켓을 통해 다른 채팅자의 메시지 얻기
			
		} catch (Exception e) {
			System.out.println("conProcess err "+ e);
		}
    }
    
    void sendProcess() {
    	try {
    		if (radio1.isSelected() == true){ //귓속말
    			String name = list.getSelectedItem();
    			out.write(("/s" +name +"-" +txtsend.getText() +"\n").getBytes());
    			
    			txtarea.append(name + "님께 귀속말이 전달 되었습니다. \n");
    		} else {// 모두 에게
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
			   JOptionPane.showMessageDialog(this, "대화명을 입력하시오");
			   txtname.requestFocus();
			   return;
		   }
		   conProcess();
	   } else if ((e.getSource() == txtsend) || (e.getSource() == btnok)) {
		   sendProcess();
	   } else if (e.getSource() == btnchange) {
		   if(btnchange.getText().equals("대화명 변경")){
			   btnchange.setText("변경확인");
			   txtname.setEnabled(true);
			   txtname.requestFocus();
		   } else {
			   btnchange.setText("대화명 변경");
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
				String  msg = in.readLine(); //채팅서버로 부터 메시지 받기
				if (msg == null) return;
				
				if (msg.charAt(0) == '/'){
					if(msg.charAt(1) == 'c'){
						list.add(msg.substring(2), count);
						count++;
						
						lblinwon.setText(String.valueOf(count));
						
						//입장 메시지
						txtarea.append("***"+ msg.substring(2)+ "님 입장 ! \n");
						txtname.setEnabled(false);//접속명 입력 불가
						btnconn.setEnabled(false);
					} else if (msg.charAt(1) == 'q'){
						txtarea.append("###" + msg.substring(2)+ "님 퇴장 ! \n" );
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
						
						txtarea.append("%%" + oldName + "님의 대화명이 "+ newName +"으로 변경됨 \n");
						
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

