package Action;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import VO.ActionForward;
import VO.PathInfo;

public class GetCurrentPathAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		ArrayList<PathInfo> pathList = (ArrayList<PathInfo>) session.getAttribute("pathList");
		String type = request.getParameter("type");
		String parentId = null;
		PrintWriter out = response.getWriter();
		response.setContentType("text/html;charset=UTF-8");
		
		if( type.equals("google") ){
			if( pathList == null )
				parentId = "root";
			else
				parentId = pathList.get(pathList.size()-1).getId();
		}else{
			if( pathList == null )
				parentId = "/";
			else{
				parentId = pathList.get(pathList.size()-1).getId();
			}
		}
		//System.out.println(parentId);
		out.print(parentId);
		//out.write(parentId);
		
		out.close();
		return null;
	}
}
