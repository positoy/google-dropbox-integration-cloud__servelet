package Controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.*;
import VO.UserInfoBean;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id 		 = request.getParameter("usernamesignup");
		String passwd	 = request.getParameter("passwordsignup");
		String email	 = request.getParameter("emailsignup");
		String googleUse = request.getParameter("registerDriveGoogle");
		String dropboxUse= request.getParameter("registerDriveDropbox");
		
		System.out.println(googleUse + ", " + dropboxUse);
		
		RegisterBiz registerBiz = new RegisterBiz();

		UserInfoBean user		= new UserInfoBean();
		
		user.setId(id); user.setPassword(passwd); user.setEmail(email); 
		
		if( googleUse != null )
			user.setGoogle( Integer.parseInt(googleUse) );
		else
			user.setGoogle(0);
		if( dropboxUse != null )
			user.setDropbox( Integer.parseInt(dropboxUse) );
		else 
			user.setDropbox(0);
		
		int result = registerBiz.addUser(user);
		System.out.println(result);
		RequestDispatcher dispatcher = request.getRequestDispatcher("Login.html");
		dispatcher.forward(request, response);
	}

}
