package Model;

import VO.Item;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleDrive implements CloudStorageController {

	private final static String CLIENT_ID		= "179548080181-6e8n90hju2r4imp6m93jb9cgg2llhvtc.apps.googleusercontent.com";
	private final static String CLIENT_SECRET	= "8lOxIde2c3ev1R5WopsRQVRQ";
	private final static String REDIRECT_URI	= "http://localhost:8080/Cumulus/GoogleDrive.Redirect";

	private static GoogleAuthorizationCodeFlow flow;
	private static HttpTransport httpTransport;
	private static JsonFactory jsonFactory;
	
	private static GoogleTokenResponse response;
	private static GoogleCredential credential;
	private static Drive service;

	private static String rootFolderId;		// ROOT�� folderId
	private static long quotaBytesTotal;	// ���۵���̺� ������ �Ҵ� �� ��ü �뷮
	private static long quotaBytesUsed;		// ���۵���̺� ������ ���� �뷮

	@Override
	public void initGoogleDrive_redirect(HttpServletResponse response) {
		
		httpTransport = new NetHttpTransport();
		jsonFactory = new JacksonFactory();

		flow = new GoogleAuthorizationCodeFlow.Builder(
		httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
		Arrays.asList(DriveScopes.DRIVE)).setAccessType("online")
		.setApprovalPrompt("auto").build();						// "auto"�̸� ���� �� �� �α��� ����

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initGoogleDrive_getAccessToken(String authorizationCode) {
		
		try {
			response = flow.newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		credential = new GoogleCredential().setFromTokenResponse(response);

		// Create a new authorized API client
		service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
		
		try {
			About about = service.about().get().execute();

			rootFolderId	= about.getRootFolderId();
			quotaBytesTotal	= about.getQuotaBytesTotal();
			quotaBytesUsed	= about.getQuotaBytesUsed();
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
		}

		System.out.println("���Ա��� LOG : GoogleDrive >> ���۵���̺� ���� �������� ����.");
		System.out.println("���Ա��� LOG : GoogleDrive >> ROOT ID     : " + rootFolderId);
		System.out.println("���Ա��� LOG : GoogleDrive >> TOTAL QUOTA : " + quotaBytesTotal + " bytes");
		System.out.println("���Ա��� LOG : GoogleDrive >> USED QUOTA  : " + quotaBytesUsed + " bytes");
		System.out.println();
	}

	@Override
	public ArrayList<Item> getListOfRoot() {
		// TODO Auto-generated method stub

		return getListOfFolder(rootFolderId);
	}

	@Override
	public ArrayList<Item> getListOfFolder(String folderId) {
		
		ArrayList<Item> items = new ArrayList<Item>();

		List<File> result = new ArrayList<File>();
		Files.List request;
		try {
			request = service.files().list();

			request.setQ("'" + folderId + "' in parents and trashed = false");

			do {
				try {
					FileList files = request.execute();

					result.addAll(files.getItems());
					request.setPageToken(files.getNextPageToken());
				} catch (IOException e) {
					System.out.println("An error occurred: " + e);
					request.setPageToken(null);
				}
			} while (request.getPageToken() != null
					&& request.getPageToken().length() > 0);

		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
		}
		
		
		int count = 1;
		for (File f : result) {
			Item item = new Item();

			item.setSrc(Item.googleDrive);
			item.setId(f.getId());
			item.setName(f.getTitle());
			
			if(f.getMimeType().equalsIgnoreCase("application/vnd.google-apps.folder")) {
				item.setIsFolder(true);
			} else {
				item.setIsFolder(false);
				
				int index = f.getTitle().lastIndexOf(".") + 1;
				item.setExtension(index == 0 ? "" : f.getTitle().substring(index));
			}

			items.add(item);
			
			
			
			System.out.println("���Ա��� LOG : GoogleDrive >> #" + count++);
			if( item.isFolder )
				System.out.println("���Ա��� LOG : GoogleDrive >> (folder) " + item.getName());
			else
				System.out.println("���Ա��� LOG : GoogleDrive >> (file) " + item.getName() + " / " + item.getExtension());
		}
		
		System.out.println("���Ա��� LOG : GoogleDrive >> " + folderId + " ���� ���� ����Ʈ �������� ����.");
		
		return items;
	}

	@Override
	public void upload(String fileName, String parentId) {
		
	    File body = new File();
	    

	    if(fileName.contains("\\")) {
		    // filename�� �������� ��� ��� ���ڿ����� ���ϸ� �����ϱ�
	    	StringTokenizer strtok = new StringTokenizer(fileName, "\\");
	    	String realFilename = null;
	    	while(strtok.hasMoreTokens()) {
	    		realFilename = strtok.nextToken();
	    	}
	    	
	    	body.setTitle(realFilename);
	    	
	    } else {
		    // filename�� �����ΰ� �ƴѰ��
		    body.setTitle(fileName);
	    }

	    // Set the parent folder.
	    if (parentId != null && parentId.length() > 0) {
	    	if( parentId == "root" ) {
	    		parentId = rootFolderId;
	    	}
	      
	    	body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
	    }

	    // File's content.
	    java.io.File fileContent = new java.io.File(fileName);
	    FileContent mediaContent = new FileContent(null, fileContent);
	    try {
	      File file = service.files().insert(body, mediaContent).execute();

	      System.out.println("���Ա��� LOG : GoogleDrive >> " + fileName + " ���ε� ����.");
	      System.out.println("���Ա��� LOG : GoogleDrive >> FILE ID : " + file.getId());

	    } catch (IOException e) {
	      System.out.println("An error occured: " + e);
	    }
	}
	
	@Override
	public String download(String fileId) {
		// TODO Auto-generated method stub

		// �ٿ�ε� URL ���ϱ�
		File file;
		try {
			file = service.files().get(fileId).execute();
		} catch (IOException e) {
			System.out.println("An error occured: " + e);
			return null;
		}
		
		GenericUrl genericUrl = new GenericUrl(file.getDownloadUrl());
		
		System.out.println("���Ա��� LOG : GoogleDrive >> " + file.getTitle() + "�� Download URL ��� ����.");
	    System.out.println("���Ա��� LOG : GoogleDrive >> Download URL : " + genericUrl.toString());

	    System.out.println(file.getDownloadUrl());
	    System.out.println(genericUrl.toString());
	    
	    return genericUrl.toString();
	}
	
	public String downloadDirect(String fileId) {

		// �ٿ�ε� URL ���ϱ�
		File file = null;

		try {
			file = service.files().get(fileId).execute();
		} catch (IOException e) {
			System.out.println("An error occured: " + e);
		}

		return file.getWebContentLink();
	}

	@Override
	public void makeFolder_GoogleDrive(String folderName, String parentId) {
		// TODO Auto-generated method stub
		
		// File's metadata.
	    File body = new File();
	    body.setTitle(folderName);
	    body.setMimeType("application/vnd.google-apps.folder");

	    // Set the parent folder.
	    if (parentId != null && parentId.length() > 0) {
	    	if(parentId == "root") {
	    		parentId = rootFolderId;
	    	}

	    	body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
	    }

	    try {
	      File file = service.files().insert(body).execute();

	      System.out.println("���Ա��� LOG : GoogleDrive >> " + file.getTitle() + " ���� ���� ����.");
	      return;
	      
	    } catch (IOException e) {
	      System.out.println("An error occured: " + e);
	      return;
	    }
	}

	@Override
	public void delete(String fileId) {
		// TODO Auto-generated method stub
		
		try {
			File file = service.files().trash(fileId).execute();
			System.out.println("���Ա��� LOG : GoogleDrive >> " + file.getTitle() + "�� ����(���������� �ű��) ����.");
			return;
			
		} catch (IOException e) {
			System.out.println("An error occured: " + e);
			return;
		}
	}

	@Override
	public void rename(String id, String newName) {
		// TODO Auto-generated method stub

		try {
			File file = service.files().get(id).execute();
			
			// file�� ��Ÿ������ ����
			file.setTitle(newName);
			
			File updatedFile = service.files().update(id, file).execute();
			System.out.println("���Ա��� LOG : GoogleDrive >> " + file.getTitle() + "�� " + updatedFile.getTitle() + "�� ���� ����.");
			return;
			
		} catch (IOException e) {
			System.out.println("An error occured: " + e);
			return;
		}
		
	}

	@Override
	public void initDropbox_redirect(HttpServletRequest request, HttpServletResponse response) { }
	@Override
	public void initDropbox_getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException { }
	@Override
	public void makeFolder_Dropbox(String path) { }


}