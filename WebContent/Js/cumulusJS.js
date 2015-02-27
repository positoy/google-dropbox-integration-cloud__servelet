/**
 * 
 */

$(function() {
	
	var currentFileId = "";
	var currentSrc = "";
	
	 $('input[type="checkbox"]').click(function() {
		// Ŭ�� �̺�Ʈ �߻��� ��Ұ� üũ ������ ���
		if ($(this).prop('checked')) {
			$('input[type="checkbox"]').prop('checked', false);
			//$(this).prop('checked', false);
		}else{
			$(this).prop('checked', true);
		}
	});
	 
	$('.fileInfo').click(function(){
		var isChecked = $(this).find(".selectOne").prop('checked');
		
		if( isChecked ){
			$('input[type="checkbox"]').prop('checked', false);
			currentFileId="";
			currentSrc="";
			//$(this).find(".selectOne").prop('checked', true);
		}else{
			$('input[type="checkbox"]').prop('checked', false);
			$(this).find(".selectOne").prop('checked', true);
			currentFileId = $(this).find(".itemName").attr("id");
			currentSrc = $(this).find(".itemName").attr("title");
		}
	});
	// ���ڵ�� �޴� ����
	$('#leftMain > ul > li > a').click(function() {
		$('#leftMain li').removeClass('active');
		$(this).closest('li').addClass('active');
		var checkElement = $(this).next();
		if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
			$(this).closest('li').removeClass('active');
			checkElement.slideUp('normal');
		}
		if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
			$('#leftMain ul ul:visible').slideUp('normal');
			checkElement.slideDown('normal');
		}
		if ($(this).closest('li').find('ul').children().length == 0) {
			return true;
		} else {
			return false;
		}
	});

	$('#btn-upload').click(function(e) {
		e.preventDefault();
		$('#filePicker').click();
	});
	$('#btn-upload-drop').click(function(e) {
		e.preventDefault();
		$('#uploadDrop').click();
	});
	
	//���� ���� ����� �̺�Ʈ
	$("#spanGoogle").click(function(){
		
		var folderName = window.prompt("Please enter folder name","New Folder");
		
		var url = "MakeFolder.cumulus";
		
		var params="folderName="+ folderName + "&type=google";
		$.ajax({
			 type: "GET",
			 url: url,
			 data: params,
		 }).done(function(){
			 alert("make folder!");
		 });
	 });
	//����ڽ� ���� ����� script
	$("#spanDropbox").click(function(){
		
		var folderName = window.prompt("Please enter folder name","New Folder");
		
		var url = "MakeFolder.cumulus";
		
		var params="folderName="+ folderName + "&type=dropbox";
		$.ajax({
			 type: "GET",
			 url: url,
			 data: params,
		 }).done(function(){
			 alert("make folder!");
		 });
	 });
	
	$("#btnDelete").click(function(){

		if( currentFileId == '' ){
			alert('������ �׸��� �����ϼ���.');
			return false;
		}else{
			if(confirm('������ ������ �����Ͻðڽ��ϱ�?')){
				var url = "DeleteFile.cumulus";
				var params = "fileID=" + currentFileId + "&type=" + currentSrc;
				$.ajax({
					type : "GET",
					url : url,
					data : params,
				}).done(function() {
					alert("Deleted!");
				});
			}
		}
	});
	
});