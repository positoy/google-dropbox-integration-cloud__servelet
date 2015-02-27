package Controller;

import java.io.IOException;

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
 * Servlet implementation class GoogleRedirect
 */

@WebServlet("*.Redirect")
public class Redirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static GoogleDrive googleDrive = null;
	private static Dropbox dropbox = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Redirect() {
		super();
	}

	protected void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("π∂∞‘±∏∏ß:Redirect LOG >> ªÁøÎ¿⁄ ¡¢º”");
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());

		HttpSession session = request.getSession();

		UserInfoBean user = (UserInfoBean) session.getAttribute("user");
		session.setAttribute("pathList", null);

		if ( command.equals("/GoogleDrive.Redirect") ) {
			System.out.println(command);
			String authorizationCode = request.getParameter("code");
			System.out.println("π∂∞‘±∏∏ß:Redirect LOG >> GoogleDrive Authorzation code µµ¬¯, " + authorizationCode);
			
			googleDrive.initGoogleDrive_getAccessToken(authorizationCode);
			session.setAttribute("googleDrive", googleDrive);
			
		} else if ( command.equals("/Dropbox.Redirect") ) {
			System.out.println(command);
			String authorizationCode = request.getParameter("code");
			System.out.println("π∂∞‘±∏∏ß:Redirect LOG >> Dropbox Authorzation code µµ¬¯, " + authorizationCode);
			
			dropbox.initDropbox_getAccessToken(request, response);
			session.setAttribute("dropbox", dropbox);
		}
		
		if ( googleDrive == null && user.getGoogle() == 1 ) {
			
			// First time visiting comes to here
			System.out.println("π∂∞‘±∏∏ß:Redirect LOG >> googleDrive ¿Œ¡ıΩ√µµ");
			session.setAttribute("googleDrive", null);

			googleDrive = new GoogleDrive();
			googleDrive.initGoogleDrive_redirect(response);

			return;
	
		} else if ( dropbox == null && user.getDropbox() == 1 ) {
			
			// First time visiting comes to here
			System.out.println("π∂∞‘±∏∏ß:Redirect LOG >> Dropbox ¿Œ¡ıΩ√µµ");
			session.setAttribute("dropbox", null);

			dropbox = new Dropbox();
			dropbox.initDropbox_redirect(request, response);

			return;
		}

		System.out.println("googledrive: " + user.getGoogle());
		System.out.println("dropbox: " + user.getDropbox());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("GetRootlist.cumulus");
		dispatcher.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doProcess(request, response);
	}

}