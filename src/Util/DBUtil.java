package Util;
import java.sql.*;

public class DBUtil {
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		Connection con = null;
		
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cumulus", "root", "webclass");
			con.setAutoCommit(false);
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	public static void close(Connection con){
		try{
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs){
		try{
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void close(Statement st){
		try{
			st.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void commit(Connection con){
		try{
			con.commit();
			System.out.println("commit success");
		}catch(Exception e){
			System.out.println("commit fail");
		}
	}
	
	public static void rollback(Connection con){
		try{
			con.rollback();
			System.out.println("rollback success");
		}catch(Exception e){
			System.out.println("rollback fail");
		}
	}
}
