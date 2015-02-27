package DAO;

import java.sql.*;

import VO.*;
import static Util.DBUtil.*;

public class RegisterDAO {
	Connection con;
	public RegisterDAO(Connection con){
		super();
		this.con = con;
	}
	
	public int addUser(UserInfoBean user){
		PreparedStatement pstmt = null;
		int result = 0;
		
		try{
			String sql = "Insert into user_info values(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			
			int index = 1;
			
			pstmt.setString(index++, user.getId());
			pstmt.setString(index++, user.getPassword());
			pstmt.setString(index++, user.getEmail());
			pstmt.setInt(index++, user.getGoogle());
			pstmt.setInt(index++, user.getDropbox());
			
			result = pstmt.executeUpdate();
			
			// 실행시킨 SQL 확인
			System.out.println("query : " + sql);
			System.out.println("==============================================");

			con.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(pstmt);
		}
		return result;
	}
}
