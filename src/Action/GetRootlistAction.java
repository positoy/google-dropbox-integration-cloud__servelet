package Action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.*;
import VO.ActionForward;
import VO.Item;

public class GetRootlistAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Item> items = new ArrayList<Item>();
		
		ArrayList<Item> googleItems = new ArrayList<Item>();
		ArrayList<Item> dropboxItems = new ArrayList<Item>();
		
		GoogleDrive googleDrive = null;
		Dropbox dropbox = null;
		
		googleDrive  = (GoogleDrive)request.getSession().getAttribute("googleDrive");
		dropbox = (Dropbox)request.getSession().getAttribute("dropbox");
		
		if( googleDrive != null ){
			googleItems = googleDrive.getListOfFolder("root");
			items.addAll(googleItems);
		}
			
		if( dropbox != null ){
			dropboxItems = dropbox.getListOfFolder("/");
			items.addAll(dropboxItems);
		}
		
		HttpSession session = request.getSession();
		
		session.setAttribute("fList", items);
		session.setAttribute("pathList", null);
		
		ActionForward forward = new ActionForward();
		forward.setUrl("Cumulus.jsp");
		
		return forward;
	}
}
