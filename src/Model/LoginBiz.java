package Model;

import java.sql.*;

import VO.*;
import DAO.LoginDAO;
import static Util.DBUtil.*;

public class LoginBiz {
	public UserInfoBean getUserLogin(String id, String passwd) {
		Connection con = getConnection();

		LoginDAO longinDao = new LoginDAO(con);
		UserInfoBean user = longinDao.getUserLogin(id, passwd);

		close(con);

		return user;
	}
}
