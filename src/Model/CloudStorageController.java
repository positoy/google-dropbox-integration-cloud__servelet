package Model;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import VO.Item;

public interface CloudStorageController {
	
	

	/****************************************************

		*
		* ROOT 디렉토리를 명시할 때에는 ID = "root"
		*
		
		<< Mandatory Operations >>
		
		* 인증과정
		* 
		*  1) 서블릿에서 initOAuth_redirect(response)를 호출하면.
		*  우리가 사용자를 클라욷 로그인페이지로 리다이렉트.
		*  클라우드이 사용자를 우리 페이지로 리다이렉트. (authorization code와 함께)
		*  
		*  2) Redirect 서블릿에서 initOAuth_getCredential(authorzationCode)를 호출하면.
		*  클라우드에서 access token을 얻어와서 객체 내에 저장.
		*  ; GoogleDrive 객체의 credential, service 지정
		*  
		*  3) 클라우드의 API 호출 가능. 
		*  
		
		public void initOAuth_redirect(HttpServletResponse response);
		public void initOAuth_getAccessToken(String authorizationCode);
	
		public ArrayList<Item> getListOfRoot();
		public ArrayList<Item> getListOfFolder(String folderId);
	
		public void upload(String fileName, String parentId); 
		public String download(String fileId);
		public void makeFolder(String folderName, String parentId);
	
		<< Optional Operations >>
		
		public void delete(String fileId);
		public void rename(String id, String newName);
	
	 ****************************************************/
	
	// Authorization
	/**
	 * 사용자의 브라우저를 클라우드 서비스 로그인 페이지로 리다이렉트 한다.
	 * 
	 * @param response
	 * 서블릿에 접속한 사용자를 리다이렉트 하기 위한 http response
	 */
	public void initGoogleDrive_redirect(HttpServletResponse response);
	public void initDropbox_redirect(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 클라우드 서비스와 통신하여 controller객체에 access token을 저장한다.
	 * 
	 * @param authorizationCode
	 * 서블릿에 접속한 사용자 request에서 얻은 authorization code를 필요로 한다.
	 * 
	 */
	public void initGoogleDrive_getAccessToken(String authorizationCode);
	public void initDropbox_getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	
	// Common functions.
	/**
	 * 사용자 클라우드 스토리지 루트에서 디렉토리를 포함한 모든 파일 가져온다. 
	 * 
	 * @return Item(boolean isFolder, String name, String id)의 리스트
	 */
	public ArrayList<Item> getListOfRoot();
	public ArrayList<Item> getListOfFolder(String folderId);
	
	public void upload(String fileName, String parentId); 
	public String download(String fileId);
	public void makeFolder_GoogleDrive(String folderName, String parentId);
	public void makeFolder_Dropbox(String path);
	
	public void delete(String fileId);
	public void rename(String id, String newName);

}
