/**
 * 
 */
	var CLIENT_ID = '179548080181-6e8n90hju2r4imp6m93jb9cgg2llhvtc.apps.googleusercontent.com';
	var SCOPES = 'https://www.googleapis.com/auth/drive';

	/**
	 * Called when the client library is loaded to start the auth flow.
	 */
	function handleClientLoad() {
		window.setTimeout(checkAuth, 1);
	}

	/**
	 * Check if the current user has authorized the application.
	 */
	function checkAuth() {
		gapi.auth.authorize({
			'client_id' : CLIENT_ID,
			'scope' : SCOPES,
			'immediate' : true
		}, handleAuthResult);
	}

	/**
	 * Called when authorization server replies.
	 *
	 * @param {Object} authResult Authorization result.
	 */
	function handleAuthResult(authResult) {
		//var authButton = document.getElementById('authorizeButton');
		var filePicker = document.getElementById('filePicker');
		//authButton.style.display = 'none';
		//filePicker.style.display = 'none';
		if (authResult && !authResult.error) {
			// Access token has been successfully retrieved, requests can be sent to the API.
			//filePicker.style.display = 'block';
			filePicker.onchange = uploadFile;
		} //else {
			// No access token could be retrieved, show the button to start the authorization flow.
// 			authButton.style.display = 'block';
// 			authButton.onclick = function() {
// 				gapi.auth.authorize({
// 					'client_id' : CLIENT_ID,
// 					'scope' : SCOPES,
// 					'immediate' : false
// 				}, handleAuthResult);
// 			};
//		}
	}

	/**
	 * Start the file upload.
	 *
	 * @param {Object} evt Arguments from the file selector.
	 */
	function uploadFile(evt) {
		gapi.client.load('drive', 'v2', function() {
			var file = evt.target.files[0];
			insertFile(file);
		});
	}

	/**
	 * Insert new file.
	 *
	 * @param {File} fileData File object to read data from.
	 * @param {Function} callback Function to call when the request is complete.
	 */
	function insertFile(fileData, callback) {
		const boundary = '-------314159265358979323846';
		const delimiter = "\r\n--" + boundary + "\r\n";
		const close_delim = "\r\n--" + boundary + "--";

		var path = "";
		var url = "GetCurrentPath.cumulus";
		var params="type=google";
		path = $.ajax({
			 type: "GET",
			 async: false,
			 url: url,
			 data: params,
			 success: function(data){
				 //path = data;
			 }
		 }).responseText;
		
		var reader = new FileReader();
		reader.readAsBinaryString(fileData);
		reader.onload = function(e) {
			var contentType = fileData.type || 'application/octet-stream';
			var metadata = {
				'title' : fileData.name,
				'mimeType' : contentType,
				'parents': [{"id": path}],
			};

			var base64Data = btoa(reader.result);
			var multipartRequestBody = delimiter 
					+ 'Content-Type: application/json\r\n\r\n'
					+ JSON.stringify(metadata) + delimiter + 'Content-Type: '
					+ contentType + '\r\n'
					+ 'Content-Transfer-Encoding: base64\r\n' + '\r\n'
					+ base64Data + close_delim;

			var request = gapi.client.request({
				'path' : '/upload/drive/v2/files',
				'method' : 'POST',
				'params' : {
					'uploadType' : 'multipart'
				},
				'headers' : {
					'Content-Type' : 'multipart/mixed; boundary="' + boundary + '"'
				},
				'body' : multipartRequestBody
			});
			if (!callback) {
				callback = function(file) {
					console.log(file);
				};
			}
			request.execute(callback);
		}
	}
	 
	 /////////////////Dropbox test/////////////////////
	 
	
	function uploadDropbox() {
		//--------------------------------------------------------------------------------------------------------- 
		// Instantiate the in-browser file reader
		//--------------------------------------------------------------------------------------------------------- 
		var file = document.getElementById("uploadDrop").files[0];
		var fileReader = new FileReader({
			'blob' : true
		});
		
		var path = "";
		var url = "GetCurrentPath.cumulus";
		var params="type=dropbox";
		path = $.ajax({
			 type: "GET",
			 async: false,
			 url: url,
			 data: params,
			 success: function(data){
				 //path = data;
			 }
		 }).responseText;
		
		var atk = "";
		$.ajax({
			type: "GET",
			async: false,
			url: "GetDropboxToken.cumulus",
			success: function(data){
				atk = data;
			}
		});
		
		
		//--------------------------------------------------------------------------------------------------------- 
		// Once the file reader has read the file into memory, 
		// do the dropbox upload 
		//--------------------------------------------------------------------------------------------------------- 
		fileReader.onload = function(e) {
			// Get the raw file to PUT 
			var rawBytes = e.target.result;
			
			// This would be handled by oAuth - hardcoding like this is unsecure 
			var uid = Math.random().toString(36).substr(2, 9);
			var oauth_token = atk;

			// Raw XMLHttpRequest 
			var xmlhttp = new XMLHttpRequest();

			// Handle the httprequest completion 
			xmlhttp.onreadystatechange = function() {

				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					alert('Uploaded!');
				}
			}

			// Open the connection 
			
			
			xmlhttp.open("PUT", "https://api-content.dropbox.com/1/files_put/dropbox" + path + "/" + file.name, true);

			// Set the headers so the transfer works
			xmlhttp.setRequestHeader("Authorization", 'Bearer ' + atk);
			
			xmlhttp.setRequestHeader("Accept", '"text/plain; charset=iso-8859-1", "Content-Type": "text/plain; charset=iso-8859-1"');

			// Send the data 
			xmlhttp.send(rawBytes);
		};

		//--------------------------------------------------------------------------------------------------------- 
		// Start by loading the file into memory 
		//--------------------------------------------------------------------------------------------------------- 
		fileReader.readAsArrayBuffer(file);
	}
	 
	 //Dropbox download script
	function downloadDropbox(fullPath, item) {
		// Raw XMLHttpRequest 
		var atk = "";
		$.ajax({
			type: "GET",
			async: false,
			url: "GetDropboxToken.cumulus",
			success: function(data){
				atk = data;
			}
		});
		var xmlhttp = new XMLHttpRequest();
        
        // Handle the httprequest completion 
        xmlhttp.onreadystatechange = function() {
           
           if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
             var responseFile = xmlhttp.response;
             get_file(responseFile, item);
           }
        }
        
        
		
        // Open the connection 
        xmlhttp.open("GET", "https://api-content.dropbox.com/1/files/dropbox" + fullPath);//+ "/test/" + "test.txt", true);
        
        // Set the headers so the transfer works
        xmlhttp.responseType="blob";
        
        xmlhttp.setRequestHeader("Authorization", 'Bearer ' + atk);
        xmlhttp.setRequestHeader("Accept", '"text/plain; charset=iso-8859-1", "Content-Type": "text/plain; charset=iso-8859-1"');

        xmlhttp.send();
	}
	
	function get_file(buf, filename){
	      var textFileAsBlob = new Blob([buf], { type : 'text/plain' });

	      var fileNameToSaveAs = filename;

	      var downloadLink = document.createElement("a");
	      downloadLink.download = fileNameToSaveAs;
	      downloadLink.innerHTML = "Download File";
	      if (window.webkitURL != null) {
	         downloadLink.href = window.webkitURL.createObjectURL(textFileAsBlob);
	      } else {
	         downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
	         downloadLink.onclick = destroyClickedElement;
	         downloadLink.style.display = "none";
	         document.body.appendChild(downloadLink);
	      }
	      downloadLink.click();
	}