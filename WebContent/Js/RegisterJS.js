/**
 * 
 */

function OnBtnSignUpClicked() {
	var userId = document.getElementById("usernamesignup");
	var email = document.getElementById("emailsignup");
	var pw1 = document.getElementById("passwordsignup");
	var pw2 = document.getElementById("passwordsignup_confirm");
	
	var eCheck = /^[_a-zA-Z0-9]+([-+.][_a-zA-Z0-9]+)*@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/i;

	if (pw1.value != pw2.value) {
		alert("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
		pw2.value = "";
		pw2.focus();
		return false;
	}
}