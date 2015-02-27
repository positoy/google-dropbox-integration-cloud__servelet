package Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.*;
import VO.ActionForward;

public class DownloadGoogleAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		GoogleDrive googleDrive = null;
		
		googleDrive  = (GoogleDrive)request.getSession().getAttribute("googleDrive");
		
		String fileId   = request.getParameter("id");
		String downloadURL = googleDrive.downloadDirect(fileId);
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(true);
		forward.setUrl(downloadURL);
		return forward;
	}

}
