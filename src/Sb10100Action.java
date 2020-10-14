import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.ems.common.smtp.GMailSender;


/**
 * Servlet implementation class Sb10100Action
 */
public class Sb10100Action extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(this.getClass());

	JSONObject outJson = new JSONObject(); // json 형태 데이터로 화면에 데이터를 전송하기 위해 사용

	GMailSender gmail = new GMailSender();

	static String FROM_NAME = "느린메일";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Sb10100Action() {
		super();
		// TODO Auto-generated constructor stub
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

	StringBuffer sb = new StringBuffer("");

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter(); // response 인코딩후 out 객체를 받아야 한다.

		String data = request.getParameter("data");
		String to_email = request.getParameter("email");
		String subject = request.getParameter("subject");


		if (data.equals("시작")) {
			sb.setLength(0);
		} else if (data.equals("끝")) {

			log.info("[" + sb.toString() + "]");

			gmail.mailSender(FROM_NAME, subject, to_email, sb.toString());

		} else {
			sb.append(data);
			log.debug(data);
		}

		outJson.put("event", "success");
		outJson.put("msg", "success");


		out.print(outJson);
		out.flush();

	}

}
