package Controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Action.Action;
import Action.DeleteFileAction;
import Action.DownloadGoogleAction;
import Action.GetCurrentPathAction;
import Action.GetDropboxTokenAction;
import Action.GetFilelistAction;
import Action.GetRootlistAction;
import Action.MakeFolderAction;
import VO.ActionForward;

/**
 * Servlet implementation class FilelistFrontController
 */
@WebServlet("*.cumulus")
public class FilelistFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilelistFrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
		Action action = null;
		ActionForward forward = null;
		
		if ( command.equals("/GetFilelist.cumulus") ) {
			action = new GetFilelistAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if( command.equals("/GetRootlist.cumulus") ){
			action = new GetRootlistAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if( command.equals("/DownloadGoogle.cumulus") ){
			action = new DownloadGoogleAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if( command.equals("/MakeFolder.cumulus") ){
			action = new MakeFolderAction();
			try {
				forward = action.execute(request, response);
				//System.out.println("make folder 咀记 角青");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if( command.equals("/GetCurrentPath.cumulus") ){
			action = new GetCurrentPathAction();
			try {
				forward = action.execute(request, response);
				//System.out.println("current path 角青");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if( command.equals("/DeleteFile.cumulus") ){
			action = new DeleteFileAction();
			try {
				forward = action.execute(request, response);
				System.out.println("昏力 action");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if( command.equals("/GetDropboxToken.cumulus") ){
			action = new GetDropboxTokenAction();
			try {
				forward = action.execute(request, response);
				System.out.println("dropbox get token action");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getUrl());
				//System.out.println(forward.getUrl());
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getUrl());
				dispatcher.forward(request, response);
				//System.out.println(forward.getUrl());
			}
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

}
