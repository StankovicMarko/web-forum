package servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.ForumDAO;
import dao.ReplyDAO;
import dao.TopicDAO;
import dao.UserDAO;
import model.Forum;
import model.Reply;
import model.Topic;
import model.User;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		HttpSession session = request.getSession();
		User loggedInUser = (User) session.getAttribute("user");
		
		if(request.getParameterMap().containsKey("term") && request.getParameterMap().containsKey("search_forums")){
			String term = request.getParameter("term");
		
			if ("".equals(term)) {
				response.sendRedirect("search.html");
				return;
			}
		if (loggedInUser == null){
			List<Forum> forums = ForumDAO.get(term, "guest");
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
		
		}
		if(UserDAO.get(loggedInUser) == null){
			response.sendRedirect("index.html");
			return;
		}
		
		if(loggedInUser.getRole().equals("admin")){
				List<Forum> forums = ForumDAO.get(term, loggedInUser.getRole() );
				String jsonForums = mapper.writeValueAsString(forums);
				response.getWriter().write(jsonForums);
				return;
				
			}
		else if(loggedInUser.getRole().equals("moderator")){
			List<Forum> forums = ForumDAO.get(term, loggedInUser.getRole() );
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
			
		}
		else if(loggedInUser.getRole().equals("registered")){
			List<Forum> forums = ForumDAO.get(term, loggedInUser.getRole() );
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
			
		}
		
		
		}else if(request.getParameterMap().containsKey("term") && request.getParameterMap().containsKey("search_topics")){
			String term = request.getParameter("term");
		if (loggedInUser == null){
			List<Topic> topics = TopicDAO.get(term, "guest");
			String jsonTopics = mapper.writeValueAsString(topics);
			response.getWriter().write(jsonTopics);
			return;
		
		}
		if(UserDAO.get(loggedInUser) == null){
			response.sendRedirect("index.html");
			return;
		}
		
		if(loggedInUser.getRole().equals("admin")){
			List<Topic> topics = TopicDAO.get(term, loggedInUser.getRole());
			String jsonTopics = mapper.writeValueAsString(topics);
			response.getWriter().write(jsonTopics);
			return;
				
			}
		else if(loggedInUser.getRole().equals("moderator")){
			List<Topic> topics = TopicDAO.get(term, loggedInUser.getRole());
			String jsonTopics = mapper.writeValueAsString(topics);
			response.getWriter().write(jsonTopics);
			return;
			
		}
		else if(loggedInUser.getRole().equals("registered")){
			List<Topic> topics = TopicDAO.get(term, loggedInUser.getRole());
			String jsonTopics = mapper.writeValueAsString(topics);
			response.getWriter().write(jsonTopics);
			return;
			
		}
		
		
		}else if(request.getParameterMap().containsKey("term") && request.getParameterMap().containsKey("search_replies")){
			String term = request.getParameter("term");
		if (loggedInUser == null){
			List<Reply> replies = ReplyDAO.get(term, "guest");
			String jsonReplies = mapper.writeValueAsString(replies);
			response.getWriter().write(jsonReplies);
			return;
		
		}
		if(UserDAO.get(loggedInUser) == null){
			response.sendRedirect("index.html");
			return;
		}
		
		if(loggedInUser.getRole().equals("admin")){
			List<Reply> replies = ReplyDAO.get(term, loggedInUser.getRole());
			String jsonReplies = mapper.writeValueAsString(replies);
			response.getWriter().write(jsonReplies);
			return;
				
			}
		else if(loggedInUser.getRole().equals("moderator")){
			List<Reply> replies = ReplyDAO.get(term, loggedInUser.getRole());
			String jsonReplies = mapper.writeValueAsString(replies);
			response.getWriter().write(jsonReplies);
			return;
			
		}
		else if(loggedInUser.getRole().equals("registered")){
			List<Reply> replies = ReplyDAO.get(term, loggedInUser.getRole());
			String jsonReplies = mapper.writeValueAsString(replies);
			response.getWriter().write(jsonReplies);
			return;
			
		}
		
		
		}else if(request.getParameterMap().containsKey("term") && request.getParameterMap().containsKey("search_users")){
			String term = request.getParameter("term");
			boolean search = true;
		if (loggedInUser == null || loggedInUser.getRole().equals("moderator") || loggedInUser.getRole().equals("admin")
				|| loggedInUser.getRole().equals("registered")){
			List<User> users = UserDAO.get(term, search);
			String jsonUsers = mapper.writeValueAsString(users);
			response.getWriter().write(jsonUsers);
			return;
		
		}
		
		
		
		
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
