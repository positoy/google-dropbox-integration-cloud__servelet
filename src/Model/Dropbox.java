 package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import VO.Item;

import com.dropbox.core.*;

public class Dropbox implements CloudStorageController {

	private final static String APP_KEY		= "45q26bnvhwyjvmu";
	private final static String APP_SECRET	= "cvwgqxg6vvcj0qo";
	
	private final static String REDIRECT_URI	= "http://localhost:8080/Cumulus/Dropbox.Redirect";
	
	private DbxAppInfo appInfo;
	private DbxRequestConfig config;
	private static DbxWebAuth webAuth;
	
	private String accessToken;
	public static DbxClient dbxClient;
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	@Override
	public void initDropbox_redirect(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		String sessionKey = "dropbox-auth-csrf-token";
		DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);

		appInfo	= new DbxAppInfo(APP_KEY, APP_SECRET);
		config		= new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
		webAuth	= new DbxWebAuth(config, appInfo, REDIRECT_URI, csrfTokenStore);
				 
		String authorizeUrl = webAuth.start();
	
		System.out.println("dropbox authorizeUrl : " + authorizeUrl);

		try {
			response.sendRedirect(authorizeUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initDropbox_getAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

		DbxAuthFinish authFinish = null;
		
		 try {
		     authFinish = webAuth.finish(request.getParameterMap());
		 }
		 catch (DbxWebAuth.BadRequestException ex) {
		     System.out.println("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
		     response.sendError(400);
		     return;
		 }
		 catch (DbxWebAuth.BadStateException ex) {
		     // Send them back to the start of the auth flow.
		     response.sendRedirect("http://my-server.com/dropbox-auth-start");
		     return;
		 }
		 catch (DbxWebAuth.CsrfException ex) {
		     System.out.println("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
		     return;
		 }
		 catch (DbxWebAuth.NotApprovedException ex) {
		     // When Dropbox asked "Do you want to allow this app to access your
		     // Dropbox account?", the user clicked "No".
		     return;
		 }
		 catch (DbxWebAuth.ProviderException ex) {
		     System.out.println("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
		     response.sendError(503, "Error communicating with Dropbox.");
		     return;
		 }
		 catch (DbxException ex) {
		     System.out.println("On /dropbox-auth-finish: Error getting token: " + ex.getMessage());
		     response.sendError(503, "Error communicating with Dropbox.");
		     return;
		 }
		 
		 accessToken = authFinish.accessToken;
		 dbxClient = new DbxClient(config, accessToken);

		 try {
			 System.out.println("���Ա���:Dropbox LOG >> Linked account : " + dbxClient.getAccountInfo().displayName);
		 } catch (DbxException e) {
			 e.printStackTrace();
		 }
	}


	@Override
	public ArrayList<Item> getListOfRoot() {
		
		getListOfFolder("/");
		return null;
	}


	@Override
	public ArrayList<Item> getListOfFolder(String path) {

		ArrayList<Item> items = new ArrayList<Item>();
		
		DbxEntry.WithChildren listing = null;
		try {
			listing = dbxClient.getMetadataWithChildren(path);
		} catch (DbxException e) {
			e.printStackTrace();
		}


		int count = 1;
		for (DbxEntry child : listing.children) {

			Item item = new Item();
			
			item.setSrc(Item.dropbox);
			item.setId(child.path);
			item.setName(child.name);
			
			if ( child.isFolder() ) {
				item.setIsFolder(true);
			} else {
				item.setIsFolder(false);

				int index = child.name.lastIndexOf(".") + 1;
				item.setExtension(index == 0 ? "" : child.name.substring(index));
			}
			
			items.add(item);
			
			System.out.println("���Ա��� LOG : Dropbox >> #" + count++);
			if( item.isFolder )
				System.out.println("���Ա��� LOG : Dropbox >> (folder) " + item.getName());
			else
				System.out.println("���Ա��� LOG : Dropbox >> (file) " + item.getName() + " / " + item.getExtension());
		}

		System.out.println("���Ա��� LOG : Dropbox >> " + path + " ���� ���� ����Ʈ �������� ����.");
		
		return items;
	}


	@Override
	public void upload(String fileName, String targetPath) {
		
		File inputFile = new File(fileName);
		targetPath = targetPath + (targetPath.endsWith("/") ? "" : "/") + fileName;
		DbxEntry.File uploadedFile = null;
		
		try {
			uploadedFile = dbxClient.uploadFile(targetPath,  DbxWriteMode.add(), inputFile.length(), new FileInputStream(inputFile));
			System.out.println("���Ա��� LOG : Dropbox >> " + uploadedFile.name + " ���ε� ����.");
		} catch (DbxException e) {
			System.out.println("An error occured: ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occured: ");
			e.printStackTrace();
		}
	}


	@Override
	public String download(String fileId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void makeFolder_Dropbox(String path) {
		try {
			dbxClient.createFolder(path);
			System.out.println("���Ա��� LOG : Dropbox >> " + path + " ���� ���� ����.");
		} catch (DbxException e) {
		      System.out.println("An error occured: ");
		      e.printStackTrace();
		}
	}


	@Override
	public void delete(String path) {
		
		try {
			System.out.println(path);
			dbxClient.delete(path);
			System.out.println("���Ա��� LOG : Dropbox >> " + path + "�� ����(���������� �ű��) ����.");
		
		} catch (DbxException e) {
			System.out.println("An error occured: ");
			e.printStackTrace();
		
		}
		
	}


	@Override
	public void rename(String fromPath, String toPath) {

		try {
			dbxClient.move(fromPath, toPath);
			System.out.println("���Ա��� LOG : Dropbox >> " + fromPath + "�� " + toPath + "�� ���� ����.");
			return;
			
		} catch (DbxException e) {
			System.out.println("An error occured: " + e);
			e.printStackTrace();
			return;
			
		}
	}

	@Override
	public void initGoogleDrive_redirect(HttpServletResponse response) { }
	@Override
	public void initGoogleDrive_getAccessToken(String authorizationCode) { }
	@Override
	public void makeFolder_GoogleDrive(String folderName, String parentId) { }


}
