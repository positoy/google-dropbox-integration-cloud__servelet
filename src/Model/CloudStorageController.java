package Model;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import VO.Item;

public interface CloudStorageController {
	
	

	/****************************************************

		*
		* ROOT ���丮�� ����� ������ ID = "root"
		*
		
		<< Mandatory Operations >>
		
		* ��������
		* 
		*  1) �������� initOAuth_redirect(response)�� ȣ���ϸ�.
		*  �츮�� ����ڸ� Ŭ��L �α����������� �����̷�Ʈ.
		*  Ŭ������ ����ڸ� �츮 �������� �����̷�Ʈ. (authorization code�� �Բ�)
		*  
		*  2) Redirect �������� initOAuth_getCredential(authorzationCode)�� ȣ���ϸ�.
		*  Ŭ���忡�� access token�� ���ͼ� ��ü ���� ����.
		*  ; GoogleDrive ��ü�� credential, service ����
		*  
		*  3) Ŭ������ API ȣ�� ����. 
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
	 * ������� �������� Ŭ���� ���� �α��� �������� �����̷�Ʈ �Ѵ�.
	 * 
	 * @param response
	 * ������ ������ ����ڸ� �����̷�Ʈ �ϱ� ���� http response
	 */
	public void initGoogleDrive_redirect(HttpServletResponse response);
	public void initDropbox_redirect(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Ŭ���� ���񽺿� ����Ͽ� controller��ü�� access token�� �����Ѵ�.
	 * 
	 * @param authorizationCode
	 * ������ ������ ����� request���� ���� authorization code�� �ʿ�� �Ѵ�.
	 * 
	 */
	public void initGoogleDrive_getAccessToken(String authorizationCode);
	public void initDropbox_getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	
	// Common functions.
	/**
	 * ����� Ŭ���� ���丮�� ��Ʈ���� ���丮�� ������ ��� ���� �����´�. 
	 * 
	 * @return Item(boolean isFolder, String name, String id)�� ����Ʈ
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
