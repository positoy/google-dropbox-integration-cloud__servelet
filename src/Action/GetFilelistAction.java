package Action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Model.*;
import VO.ActionForward;
import VO.Item;
import VO.PathInfo;

public class GetFilelistAction implements Action {
	@SuppressWarnings("unchecked")
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("EUC-KR");
		
		ArrayList<Item> items = new ArrayList<Item>();
		
		ArrayList<Item> googleItems  = new ArrayList<Item>();
		ArrayList<Item> dropboxItems = new ArrayList<Item>();
		ArrayList<PathInfo> pathList = new ArrayList<PathInfo>();
		GoogleDrive googleDrive = null;
		Dropbox dropbox = null;
		
		HttpSession session = request.getSession();
		
		googleDrive  = (GoogleDrive) session.getAttribute("googleDrive");
		dropbox 	 = (Dropbox) session.getAttribute("dropbox");
		pathList 	 = (ArrayList<PathInfo>) session.getAttribute("pathList");

		// ��Ʈ���� �� ��� ����Ʈ ����
		if( pathList == null) {
			pathList = new ArrayList<PathInfo>();
		} 

		// depth �Ķ���� ���
		String depthString = (String) request.getParameter("depth");
		Integer depth = null;
		if ( depthString != null )
			depth = Integer.parseInt(depthString);
		
		// ck ; �̹� ����Ǿ� �ִ� pathList�� �����Ͽ� ���� ������ �̵� �� ��� ������ �������� �����ϰ�
		// ck ; ������ �������� �ʴ� ��ζ�� �߰��մϴ�.

		String fileId	= null;
		String fileSrc	= null;
		String fileName	= null;
		
		int destination = -1;
		if ( depth != null ) {
			// ����ڰ� ��θ� �����Ͽ� �̵��ϴ� ���
			
			// pathList���� ���� ������ �����ϱ�, ����Ʈ �� ���� �����Ѵ�.
			destination = depth;
			int endpoint = pathList.size() - 1;
			for( int point = endpoint; point >= destination ; point-- )
				pathList.remove(point);
			
			fileId		= pathList.get(destination-1).getId();
			fileSrc		= new Integer(pathList.get(destination-1).getSrc()).toString();
			fileName	= pathList.get(destination-1).getName();
			
		} else {
			// ����ڰ� ����Ʈ �������� �����Ͽ� �̵��ϴ� ���
			
			fileId   = request.getParameter("id");
			fileSrc  = request.getParameter("type");
			fileName = request.getParameter("name");
			if( fileName != null )
				fileName = new String(fileName.getBytes("8859_1"),"KSC5601");

			PathInfo pathInfo = new PathInfo();
			pathInfo.setId(fileId);
			pathInfo.setSrc(Integer.parseInt(fileSrc));
			pathInfo.setName(fileName);
			pathInfo.setDepth(pathList.size() + 1);
			
			pathList.add(pathInfo);
		}
		
		session.setAttribute("pathList", pathList);
		
		if( fileSrc.equals("0") ){
			
			googleItems  = googleDrive.getListOfFolder(fileId);
			items.addAll(googleItems);
			session.setAttribute("fList", items);
		}else{
			
			dropboxItems = dropbox.getListOfFolder(fileId);
			items.addAll(dropboxItems);
			session.setAttribute("fList", items);
		}
		
		
		ActionForward forward = new ActionForward();
		forward.setUrl("Cumulus.jsp");
		return forward;
	}
}
