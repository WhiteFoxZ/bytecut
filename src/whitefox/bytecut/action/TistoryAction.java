package whitefox.bytecut.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ems.common.dbcp.DBManager;
import com.ems.common.dbcp.DataSource;
import com.ems.common.smtp.GMailSender;



/**
 * Servlet implementation class Sb10100Action
 */
public class TistoryAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(this.getClass());

	JSONObject outJson = new JSONObject(); // json 형태 데이터로 화면에 데이터를 전송하기 위해 사용

	DataSource ds;


	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

		ds = (DataSource) this.getServletContext().getAttribute("jdbc/mysql_ds");

		log.debug(ds);


	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}



	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter(); // response 인코딩후 out 객체를 받아야 한다.

		String remoteAddr = getClientIpAddr (request);

		String data = request.getParameter("data")==null?"":request.getParameter("data");

		log.debug("remoteAddr : "+remoteAddr);
		log.debug("data : "+data);

		JSONObject tmp = (JSONObject)JSONValue.parse(data);

		String thisUrl = tmp.get("thisUrl").toString();
		String imgUrl = tmp.get("imgUrl").toString();

		log.debug("thisUrl : "+thisUrl);
		log.debug("imgUrl : "+imgUrl);




		if(ds!=null) {

			DBManager dbm = new DBManager(ds);

			StringBuffer sb = new StringBuffer("insert into tistorylog (HOST_URL,IMG_URL,REMOTE_ADDR) values(?,?,?)");

			Connection con = null;

			try {

				con = dbm.getConnection();

				dbm.insert(dbm.getConnection(), sb.toString(), new String[] {thisUrl,imgUrl,remoteAddr});

				dbm.commitChange(con);

				outJson.put("event", "success");
				outJson.put("msg", "success");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				dbm.rollbackChange(con);

				outJson.put("event", "success");
				outJson.put("msg", "fail");

			}

		}


		outJson.put("event", "success");
		outJson.put("msg", "success");


		out.print(outJson);
		out.flush();
		out.close();

	}

	public static String getClientIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");

	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }

	    return ip;
	}



}
