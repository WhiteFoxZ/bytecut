package whitefox.bytecut.action;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class HttpConnectionUtil {

	public static String postRequest(String pURL, HashMap pList) throws IOException {

        System.out.println("HttpConnectionUtil, pURL: "+pURL);


        String json = "";

        JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("imgUrl", pList.get("imgUrl"));

			jsonObject.put("thisUrl", pList.get("thisUrl"));

			json = jsonObject.toString();

//			System.out.println("postRequest: " + json);

		} catch (JSONException e) {
			e.printStackTrace();
		}


        String myResult = "";

        HttpURLConnection http = null;

        try {
// URL 설정하고 접속하기
            URL url = new URL(pURL); // URL 설정

            http = (HttpURLConnection) url.openConnection(); // 접속
// --------------------------
// 전송 모드 설정 - 기본적인 설정
// --------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST


// --------------------------
// 헤더 세팅
// --------------------------
// 서버로 보내는 처리방식설정
 http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
// http.setRequestProperty("Content-type", "application/json");	//send the request content in JSON form

 //서버로부터 수신받는 처리방식
http.setRequestProperty("Accept", "application/json");

        	StringBuffer buffer = new StringBuffer();
			buffer.append("data").append("=").append(json);


 			OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            writer.close();

// --------------------------
// Response Code
// --------------------------
// http.getResponseCode();

// --------------------------
// 서버에서 전송받기
// --------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
            myResult = builder.toString();

            reader.close();

            return myResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            http.disconnect();
        }

        return myResult;
    }

    public static void main(String[] args) {

        String pURL = "http://localhost/bytecut/servlet/TistoryAction";
//        String pURL = "http://fishfox.cafe24.com/bytecut/servlet/TistoryAction";
        HashMap pList = new HashMap();
        pList.put("imgUrl", "bbb.jsp");
        pList.put("thisUrl", "naver.com");

        System.out.println("**********************");


        String resp = null;
        try {
            resp = HttpConnectionUtil.postRequest(pURL, pList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("result : "+resp);

    }

}
