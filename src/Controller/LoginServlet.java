package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.*;
import VO.UserInfoBean;
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		
		System.out.println(id + ", " + passwd);
		LoginBiz loginBiz = new LoginBiz();
		
		UserInfoBean user = loginBiz.getUserLogin(id, passwd);
	
		//System.out.println( randomCategory + ", " + category.get(randomCategory).getCategory());
		
		if( user != null ){
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			RequestDispatcher dispatcher = request.getRequestDispatcher("*.Redirect");
			dispatcher.forward(request, response);
		}else{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('login error');");
			out.println("history.back();");
			out.println("</script>");
		}
		
	}
}
