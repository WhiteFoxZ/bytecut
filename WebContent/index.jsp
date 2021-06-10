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
	var email = "";
	var subject = "";
	var html="";
	var sessionId ="<%=d.toGMTString()%>";

	var calByte = {
		getByteLength : function(s) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;

			for (var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
			}

			return size;
		},

		cutByteLength : function(s, len) {
			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;
			var rIndex = s.length;
			for (var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));
				if (size == len) {
					rIndex = i + 1;
					break;

				} else if (size > len) {
					rIndex = i;
					break;
				}
			}

			return s.substring(0, rIndex);
		},

		cutByteLengthArr : function(tmp, s, len) {

			if (s == null || s.length == 0) {
				return 0;
			}
			var size = 0;
			var rIndex = s.length;

			for (var i = 0; i < s.length; i++) {
				size += this.charByteSize(s.charAt(i));

				if (size == len) { //현재 byte가 len byte 이면 rIndex 값세팅
					rIndex = i + 1;
					break;
				} else if (size > len) {
					rIndex = i;
					break;
				}
			}

			if (rIndex != s.length) {
				tmp.push(s.substring(0, rIndex));
				this.cutByteLengthArr(tmp, s.substring(rIndex), len);
			} else {
				tmp.push(s.substring(0, rIndex));
				return tmp;
			}

		},

		charByteSize : function(ch) {

			if (ch == null || ch.length == 0) {
				return 0;
			}

			var charCode = ch.charCodeAt(0);

			if (charCode <= 0x00007F) {
				return 1;
			} else if (charCode <= 0x0007FF) {
				return 2;
			} else if (charCode <= 0x00FFFF) {
				return 3;
			} else {
				return 4;
			}
		}
	};

	function delay(gap) { /* gap is in millisecs */

		var then, now;

		then = new Date().getTime();

		now = then;

		while ((now - then) < gap) {

			now = new Date().getTime(); // 현재시간을 읽어 함수를 불러들인 시간과의 차를 이용하여 처리

		}

	}

	function splitButton() {

		var tmp = new Array();

		var txt = $('[name="contents"]').val();

		var inByte = $('[name="inByte"]').val();
		var inDelay = $('[name="inDelay"]').val();

		if(inDelay<500)
		{
			inDelay=500;
		}

		if(inByte<50)
		{
			inByte=50;
		}



		email = $('[name="email"]').val();
		subject = $('[name="subject"]').val();
		html= $('[name="html"]').attr('checked');

		if (email == "") {
			alert('email 을 넣어주세요.');
			return false;

		}

		$(".viewByte").html(calByte.getByteLength(txt));

		calByte.cutByteLengthArr(tmp, txt, inByte);

		delay(100);

		var rv = save("start");

		var byteCnt = 0;
		var byteTotal = 0;

		for (var i = 0; i < tmp.length; i++) {

			byteCnt = calByte.getByteLength(tmp[i]);
			byteTotal += byteCnt;

			$(".view20Byte").append(tmp[i] + ':[' + byteCnt + ']<br>');
			console.log(tmp[i]);

			var rv = save(tmp[i]);

			delay(inDelay);

		}

		$(".view20Byte").append('총:[' + byteTotal + ']<p>');

		var rv = save("end");

	}

	function save(msg) {

		var returnVal;

		$.ajax({
			type : 'post',
			dataType : "json", //json 아닐때 제거
			async : false,
			data : {
				data : msg,
				email : email,
				subject : subject,
				html : html,
				sessionId : sessionId
			},
			url : "./servlet/ByteCutAction",

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
		<table width="500px;" >
			<tr>
				<td>delay(밀리세컨드)</td>
				<td><input type="text" name="inDelay" value="500" /></td>
			</tr>
			<tr>
				<td>Byte (글자나누기)</td>
				<td><input type="text" name="inByte" value="50" /></td>
			</tr>


			<tr>
				<td>받는사람</td>

				<td><input type="text" name="email" value="fmjj007@naver.com" /></td>
				<!--
				<td><input type="text" name="email" value="" /></td> -->

			</tr>

			<tr>
				<td>제목</td>
				<td><input type="text" name="subject" value="5분 느린메일" /></td>
			</tr>

			<tr>
				<td>HTML</td>
				<td><input type="checkbox" name="html" value="1"></td>
			</tr>

			<tr>
				<td colspan="2"><textarea name="contents" 	style="width: 99%; height: 100px;" ondblclick="this.value=''">
한미 미사일 지침 개정을 통해 우리 미사일의 탄두 중량을 늘리려는 것은 북한의 미사일 기술이 상대적으로 급진전하고 있기 때문입니다.
현재 사거리 800㎞에 탄두 중량 500㎏으로는 미 본토까지 타격 가능한 ICBM급 미사일에 매달리는 북한의 도발 의지를 꺾기에는 역부족이라는 게 우리 정부의 판단입니다.
</textarea></td>

			</tr>

		</table>
		<p>
			<span class="viewByte red"></span>byte입니다.
		</p>
		<p>
			<span class="view20Byte red"></span>
		</p>
		<p>
			<input type="button" value="나누기" onclick="splitButton()">
		</p>
	</form>
</body>
</html>

