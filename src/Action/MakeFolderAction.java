package Action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.*;
import VO.ActionForward;
import VO.PathInfo;

public class MakeFolderAction implements Action{

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		GoogleDrive googleDrive = null;
		Dropbox dropbox			= null;
		ArrayList<PathInfo> pathList = new ArrayList<PathInfo>();
		
		HttpSession session = request.getSession();
		googleDrive  = (GoogleDrive) session.getAttribute("googleDrive");
		dropbox		 = (Dropbox) session.getAttribute("dropbox");
		pathList 	 = (ArrayList<PathInfo>) session.getAttribute("pathList");
		
		String folderName = request.getParameter("folderName");
		String type		  = request.getParameter("type");
		String parentId = null;
		
		if( type.equals("google") ){
			
			if( pathList == null )
				parentId = "root";
			else
				parentId = pathList.get(pathList.size()-1).getId();
			googleDrive.makeFolder_GoogleDrive(folderName, parentId);	
		}else{ // dropbox type
			if( pathList == null )
				parentId = "/" + folderName;
			else{
				parentId = pathList.get(pathList.size()-1).getId();
				parentId += ("/" + folderName);
				System.out.println(parentId);
			}
			dropbox.makeFolder_Dropbox(parentId);
		}
		
		ActionForward forward = new ActionForward();
		forward.setUrl("Cumulus.jsp");
		return forward;
	}

}
