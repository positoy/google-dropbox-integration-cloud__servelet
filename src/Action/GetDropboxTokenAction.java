package Action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.Dropbox;
import VO.ActionForward;

public class GetDropboxTokenAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Dropbox dopbox = (Dropbox)session.getAttribute("dropbox");
		String dToken = dopbox.getAccessToken();
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
		System.out.println(dToken);
		out.print(dToken);
		out.close();
		return null;
	}
}
