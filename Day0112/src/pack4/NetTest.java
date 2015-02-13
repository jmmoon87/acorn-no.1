package pack4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NetTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InetAddress ia = null;
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			ia = InetAddress.getByName("www.naver.com");
			//System.out.println(ia.getHostAddress());
			socket = new Socket(ia, 80);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			out.println("get http://www.naver.com");
			out.flush();
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				String  str = in.readLine();
				if (str == null) break;
				System.out.println(str);
			}
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("err " + e);
		}
	}

}
