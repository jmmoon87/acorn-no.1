package pack4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	ServerSocket serverSocket = null;
	Service service;
	ArrayList<Service> list = new ArrayList<Server.Service>();

	public Server() {
		try {
			serverSocket = new ServerSocket(5555);
			System.out.println("서버 서비스 중 ");
			new Thread(this).start();
		} catch (Exception e) {
			System.out.println("서버 소켓 생성 오류 "+ e);
		}
	}
	
	@Override
	public void run() {
		while (true){
			try {
				Socket socket = serverSocket.accept(); //client 요청 대기 중....
				
				service = new Service(socket);
				service.start();
				service.chatName = service.in.readLine();
				System.out.println("대화명 : "+ service.chatName);
				service.messageAll("/c" + service.chatName);
				
				list.add(service); //모든 클라이언트 객체 담기
				
				for (int i = 0; i < list.size(); i++) {
					Service cs = (Service)list.get(i);
					service.message("/c" + cs.chatName);
				}
				
				
			} catch (Exception e) {
				System.out.println("socket 생성 오류 "+e);
				return;
			}
		}
	}
	
	//내부 클래스
	class Service extends Thread {
		String chatName;
		BufferedReader in = null;
		OutputStream out = null;
		Socket socket = null;
		
		public Service(Socket soc) {
			try {
				socket = soc;
				
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = socket.getOutputStream();
			} catch (Exception e) {
				System.out.println("서비스 생성자 요류 "+ e);
			}
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					String msg = in.readLine();
					if(msg == null) return;
					
					if (msg.charAt(0) == '/') {
						if (msg.charAt(1) == 'n' ) {
							if(msg.charAt(2)== ' ') {
								messageAll("/n" + chatName + "-" + msg.substring(3).trim());
								this.chatName = msg.substring(3).trim();
							}
						} else if (msg.charAt(1) == 's' ) {
							String name = msg.substring(2, msg.indexOf('-')).trim();
							for (int i = 0; i < list.size(); i++) {
								Service cs2 = (Service)list.get(i);
								if(name.equals(cs2.chatName)){
									cs2.message(chatName + "> (귓속말)" + msg.substring(msg.indexOf('-')+1));
									//예) 대화명1> (귓속말)
									break;
								}
							}
						} else if (msg.charAt(1) == 'q' ) {
							try {
								message("/q" + chatName);
								list.remove(service);
								socket.close();
								return;
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					} else { //일반 메시지
						messageAll(chatName+ " > " + msg);
					}
				} catch (Exception e) {
					System.out.println("server run err "+ e);
					
				}
			} //while
		} //end run
		
		void messageAll(String msg) throws Exception{
			for (int i = 0; i < list.size(); i++) {
				Service sc = (Service)list.get(i);
				service.message(msg);
			}
		}
		
		void message(String msg) throws Exception {
			//client로 메시지 출력
			out.write((msg + "\n").getBytes());
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
