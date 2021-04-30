<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>

<%

Date d = new Date();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>한글자르기</title>
<!--
html 페이지 인코딩 utf-8로 되어 있어야 한글이 안깨짐니다.
 -->
<script type="text/javascript" src="./jquery-1.3.2.js"></script>
<style>


.red {
	font-weight: bold;
	color: red;
}
</style>
<script>


	function save(imgUrl) {

		var returnVal;

		var thisUrl = document.location.href;

		var jsData ={
				imgUrl : imgUrl,
				thisUrl : thisUrl
				};

		var jsonData = JSON.stringify(jsData);

		console.log(jsonData);


		$.ajax({
			type : 'post',
			dataType : "json", //json 아닐때 제거. 리턴받는 데이타가 json
			async : false,
			data : {
				data : jsonData
			},
			//url : "http://localhost/bytecut/servlet/TistoryAction",
			//url : "http://gocamp.iptime.org/bytecut/servlet/TistoryAction",
			url : "http://fishfox.cafe24.com/bytecut/servlet/TistoryAction",

			success : function(result) {

				if (result.event == "success") {
					console.log(result.msg);
					returnVal = true;
				} else {
					console.log(result.msg, 'error');
					//alert('fail');
					returnVal = false;
				}
			},
			complete : function(data) {
				//alert('complete');
			},

			error : function(xhr, status, error) {
				//alert('error');
				console.log(error, 'error');
				returnVal = false;
			}

		});

		return returnVal;

	}
</script>
</head>

<body>
	<form id="save_form" class="form-horizontal">
	<input type="button" value="나누기" onclick="save('aaa.jpg')">
	</form>
</body>
</html>

