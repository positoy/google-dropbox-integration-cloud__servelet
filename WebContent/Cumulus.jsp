<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="Model.*"%>
<%@ page import="VO.*"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="CSS/NewFile.css">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="Js/cumulusJS.js"></script>
<script type="text/javascript" src="Js/UploadJS.js"></script>
<script type="text/javascript" src="https://apis.google.com/js/client.js?onload=handleClientLoad"></script>
</head>
<body>
	<%!
		// current PathInfo
		ArrayList<PathInfo> path;
		
		String name;
		int src;
		String id;
		int depth;
		
		String currentPath;
	%>
	<%
		ArrayList<Item> items = (ArrayList<Item>)session.getAttribute("fList");
		ArrayList<PathInfo> pathList = (ArrayList<PathInfo>) session.getAttribute("pathList");
		//Dropbox dropbox = (Dropbox)session.getAttribute("dropbox");
	%>
	<form id="fileForm" onsubmit="return false;">
		<div>
			<div class="header">
				home
			</div>
			<h1 style="background-image: url('Images/beige_paper.png');">Cumulus Service <span>Google-Drive &amp; Dropbox</span></h1>
			<div>
				<input id="btnDelete" type="button" value="Delete">
			</div>
			<div id="pathInfo">
				<ul>
					<li>
						<a href="GetRootlist.cumulus">/</a>
					</li>
					<%
						if(pathList != null ) {
							int pathSize = pathList.size();
							for( int i = 0; i < pathSize; ++i ){
					%>
					<li>
						<a href="GetFilelist.cumulus?depth=<%= pathList.get(i).getDepth() %>"><%= pathList.get(i).getName() %>/</a>
					</li>
					<%
							}
						}
					%>
				</ul>
			</div>
			<div id="leftMain">
				<ul class="ca-menu">
					<li class='has-sub'><a href='#'><span>GoogleDrive</span></a>
						<ul>
							<li>
								<input type="file" id="filePicker" name="filePicker" />
								<button type="button" id="btn-upload">Upload</button>
							</li>
							<li><a href='#'><span id="spanGoogle">Make a folder</span></a></li>
						</ul></li>

					<li class='has-sub'><a href='#'><span>Dropbox</span></a>
						<ul>
							<li>
								<input type="file" id="uploadDrop" name="uploadDrop" onchange="uploadDropbox()" />
								<button type="button" id="btn-upload-drop">Upload</button>
							</li>
							<li><a href='#'><span id="spanDropbox">Make a folder</span></a></li>
						</ul></li>
				</ul>
			</div>
			<div id="rightMain">
				<div>
					<table style="padding-left: 1em;">
						<tr>
							<td style="width: 440px;">제목</td>
							<td style="width: 150px;">유형</td>
							<td style="width: 150px;">아이디</td>
						</tr>
					</table>					
					<ul class="contents">
						<%
							String fileId = request.getParameter("id");
							if(fileId == null){
								fileId = "root";
							}else{
								fileId = request.getParameter("id");
							}
							int size = items.size();
							for (int i = 0; i < size; ++i) {
								//String folderId = "root";
								Item item = items.get(i);
						%>
						<li class="fileInfo">
							<table>
								<tr>
									<td>
										<input type="checkbox" class="selectOne" name="selectOne">
									</td>
									<td>
										<%
											String imgSrc = null;
											if( item.getSrc() == 0 )
												imgSrc = "Images/img_google.PNG";
											else
												imgSrc = "Images/img_dropbox.PNG";
										%>
										<img id="apiImg" alt="google" src="<%=imgSrc%>" width="40px" height="40px">
									</td>
									<td style="width: 400px;">
										<%
										if (item.getIsFolder()) {
										%>
											<a class="itemName" style="color: black;" title="<%=item.getSrc()%>" id="<%=item.getId()%>" href="GetFilelist.cumulus?id=<%=item.getId()%>&type=<%=item.getSrc()%>&name=<%=item.getName()%>"><%=item.getName()%></a>
										<%
										}else{
										%>
											<%
												if( item.getSrc() == Item.googleDrive ){
											%>
													<a class="itemName" style="color: black;" title="<%=item.getSrc()%>" id="<%=item.getId()%>" href="DownloadGoogle.cumulus?id=<%=item.getId()%>&type=<%=item.getSrc()%>&name=<%=item.getName()%>"><%=item.getName()%></a>
											<%
												}else{
											%>
													<a class="itemName" style="color: black;" title="<%=item.getSrc()%>" id="<%=item.getId()%>" href="javascript:downloadDropbox('<%=item.getId()%>', '<%=item.getName()%>');"><%=item.getName()%></a>
													<%
												}
													%>
										<%		
										}
										%>
									</td>
									<td style="width: 150px;">
										<%
											if (item.getIsFolder()) {
													session.setAttribute("dir", item.getId());
										%> Folder <%
											} else {
										%> File <%
											}
										%>
									</td>
									<td style="width: 300px;">
										<%=item.getId() %>
									</td>
								</tr>
							</table>
						</li>
							
						<%
							}
						%>
						
					</ul>
				</div>
			</div>
		</div>
	</form>
</body>
</html>