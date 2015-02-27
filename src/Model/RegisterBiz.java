package Model;

import java.sql.*;

import VO.*;
import DAO.RegisterDAO;
import static Util.DBUtil.*;

public class RegisterBiz {
	public int addUser(UserInfoBean user){
		Connection con = getConnection();
		RegisterDAO registerDao = new RegisterDAO(con);
		int result = registerDao.addUser(user);
		close(con);
		
		return result;
	}
}
