package servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.UserDAO;
import model.User;


/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		Map<String, Object> data = new LinkedHashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		HttpSession session = request.getSession();
		User loggedInUser = (User) session.getAttribute("user");
		
		if(loggedInUser==null){
			data.put("status", "no");
		}else
		{
			data.put("status", "yes");
			data.put("username", loggedInUser.getUsername());
			data.put("role", loggedInUser.getRole());
		}
		
		String jsonData = mapper.writeValueAsString(data);
		response.getWriter().write(jsonData);
		
//		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("application/json");
		Map<String, Object> data = new LinkedHashMap<>(); //zadrzava redosled elemenata
		ObjectMapper mapper = new ObjectMapper();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = UserDAO.get(username, password);
		if (user == null){
			data.put("status", "failure");
			data.put("message", "Login unsuccessful!");
			
			String jsonData = mapper.writeValueAsString(data);
			response.getWriter().write(jsonData);
			
			return;
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
//		getCookieByName(request, username);
		
		data.put("status", "success");
		data.put("message", "Login succesful! Welcome");
		data.put("username", user.getUsername());
		data.put("role", user.getRole());
		String jsonData = mapper.writeValueAsString(data);
		response.getWriter().write(jsonData);
		
		
	}
	
//	protected Cookie getCookieByName(HttpServletRequest request, String username) {
//	    if (request.getCookies() == null) {
//	        return null;
//	    }
//	    for (int i = 0; i < request.getCookies().length; i++) {
////	        if (
//	        		System.out.println(request.getCookies()[i].getValue());
////	        		) {
////	            return request.getCookies()[i];
//	        }
////	    }
//	    return null;
//	}

}
