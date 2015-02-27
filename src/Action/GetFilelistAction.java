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

		// 루트에서 온 경우 리스트 생성
		if( pathList == null) {
			pathList = new ArrayList<PathInfo>();
		} 

		// depth 파라미터 얻기
		String depthString = (String) request.getParameter("depth");
		Integer depth = null;
		if ( depthString != null )
			depth = Integer.parseInt(depthString);
		
		// ck ; 이미 저장되어 있는 pathList를 조사하여 상위 폴더로 이동 할 경우 나머지 아이템을 삭제하고
		// ck ; 상위에 존재하지 않는 경로라면 추가합니다.

		String fileId	= null;
		String fileSrc	= null;
		String fileName	= null;
		
		int destination = -1;
		if ( depth != null ) {
			// 사용자가 경로를 선택하여 이동하는 경우
			
			// pathList에서 하위 폴더들 삭제하기, 리스트 뒤 부터 삭제한다.
			destination = depth;
			int endpoint = pathList.size() - 1;
			for( int point = endpoint; point >= destination ; point-- )
				pathList.remove(point);
			
			fileId		= pathList.get(destination-1).getId();
			fileSrc		= new Integer(pathList.get(destination-1).getSrc()).toString();
			fileName	= pathList.get(destination-1).getName();
			
		} else {
			// 사용자가 리스트 아이템을 선택하여 이동하는 경우
			
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
