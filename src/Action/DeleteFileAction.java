package Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.*;
import VO.ActionForward;
import VO.Item;

public class DeleteFileAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		GoogleDrive googleDrive = null;
		Dropbox dropbox = null;
		
		HttpSession session = request.getSession();
		
		googleDrive  = (GoogleDrive) session.getAttribute("googleDrive");
		dropbox 	 = (Dropbox) session.getAttribute("dropbox");
		
		String fileId = request.getParameter("fileID");
		String type = request.getParameter("type");
		
		fileId = new String(fileId.getBytes("8859_1"),"KSC5601");
		
		System.out.println(fileId + ", " + type);
		
		if( Integer.parseInt(type) == Item.googleDrive ){
			googleDrive.delete(fileId);
			System.out.println("备臂 昏力");
		}else{
			System.out.println(fileId);
			dropbox.delete(fileId);
			System.out.println("靛而 昏力");
		}
		
		ActionForward forward = new ActionForward();
		forward.setUrl("Cumulus.jsp");
		return forward;
	}

}
