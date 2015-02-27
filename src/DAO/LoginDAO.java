package DAO;

import java.sql.*;
import VO.*;
import static Util.DBUtil.*;

public class LoginDAO {
	Connection con;
	public LoginDAO(Connection con){
		super();
		this.con = con;
	}
	
	public UserInfoBean getUserLogin(String id, String passwd){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserInfoBean user = null;
		
		try{
			String sql = "Select * from user_info where member_id = ? and password = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				user = new UserInfoBean();
				user.setId(rs.getString("member_id"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setGoogle(rs.getInt("google"));
				user.setDropbox(rs.getInt("dropbox"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(pstmt);
			close(rs);
		}
		return user;
	}
}
