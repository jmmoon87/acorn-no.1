package pack3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbPre1 {
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public DbPre1() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println("로딩실패"+e);
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr","hr");
			
			String sql ="";
			
			//추가
			/*sql = "insert into sangdata values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "100");
			pstmt.setString(2, "연필");
			pstmt.setString(3, "5");
			pstmt.setInt(4, 2000);
			
			if (pstmt.executeUpdate() == 0 )
				System.out.println("추가 실패");*/
			
			//수정
			/*sql = " update sangdata set sang = ?, su = ?, dan = ? where code = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "만년필");
			pstmt.setString(2, "7");
			pstmt.setString(3, "7000");
			pstmt.setString(4, "100");
			if (pstmt.executeUpdate() <= 0 )
				System.out.println("수정 실패");*/
			
			//삭제
			/*sql = " delete from  sangdata where  where code = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "100");
			if (pstmt.executeUpdate() <= 0 )
				System.out.println("삭제 실패");*/
			
			sql = " select * from sangdata";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				System.out.println(rs.getString(1)+ "  "+ 
						rs.getString(2)+ "  "+ 
						rs.getString(3)+ "  "+ 
						rs.getString(4));
			}
			
		/*	System.out.println();
			
			String code = "1";*/
			/*sql = " select * from sangdata where code = "+ code ;
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				System.out.println(rs.getString(1)+ "  "+ 
						rs.getString(2)+ "  "+ 
						rs.getString(3)+ "  "+ 
						rs.getString(4));
			}
			
			System.out.println();*/
			/*
			sql = " select * from sangdata where code = ?" ;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, code);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				System.out.println(rs.getString(1)+ "  "+ 
						rs.getString(2)+ "  "+ 
						rs.getString(3)+ "  "+ 
						rs.getString(4));
			}
			*/
			/*System.out.println();
			String sang = "마";
			//sql = " select * from sangdata where sang like '"+sang +"%'" ;
			sql = " select * from sangdata where sang like?" ;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sang+ "%");
			
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				System.out.println(rs.getString(1)+ "  "+ 
						rs.getString(2)+ "  "+ 
						rs.getString(3)+ "  "+ 
						rs.getString(4));
			}*/
		} catch (Exception e) {
			System.out.println("처리실패 "+ e);
		}try {
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
			if(conn!=null) conn.close();
		} catch (Exception e2) {
			// TODO: handle exception
		} finally {}
	}
	
	public static void main(String[] args) {
		new DbPre1();

	}

}
