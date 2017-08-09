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
import model.User;

/**
 * Servlet implementation class UtilServlet
 */
public class UtilServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UtilServlet() {
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
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		HttpSession session = request.getSession();
		User loggedInUser = (User) session.getAttribute("user");
		String msg = "You must be logged in.";
		String status = "failure";	
		
		if (request.getParameterMap().containsKey("forum_id") && request.getParameterMap().containsKey("get_parent_type")) {
			int forum_id = Integer.parseInt(request.getParameter("forum_id"));
			if(loggedInUser.getRole().equals("admin")){
				String type = ForumDAO.getParentType(forum_id);
				response.getWriter().write(mapper.writeValueAsString(type));
				return;
					
			}
			else{
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
	}else if (request.getParameterMap().containsKey("forum_id") && request.getParameterMap().containsKey("get_parent_forum")) {
		int forum_id = Integer.parseInt(request.getParameter("forum_id"));
		if (loggedInUser == null){
			if (ForumDAO.hasAccessTo(forum_id, "guest")) {
				int parentId = ForumDAO.getParentId(forum_id);
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
				
			}else{
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
		}else if(UserDAO.get(loggedInUser) == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", ".LogoutServlet");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
		if(loggedInUser.getRole().equals("admin")){
			if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
				int parentId = ForumDAO.getParentId(forum_id);
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
				
			}
//			else{
//				response.getWriter().write(mapper.writeValueAsString("Access Denied"));
//				return;	
//			}
		}
		if(loggedInUser.getRole().equals("moderator")){
			///vidi sve ko admin al ne moze da edituje
			if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
				int parentId = ForumDAO.getParentId(forum_id);
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
				
			}
//			else{
//				response.getWriter().write(mapper.writeValueAsString("Access Denied"));
//				return;	
//			}
		}
		if(loggedInUser.getRole().equals("registered")){
			///vrati forume ako je roditelj javni/ otvoren
			if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
				int parentId = ForumDAO.getParentId(forum_id);
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
			
		}else{
			data.put("status", status);
			data.put("message", "Access Denied");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
	}
		else{
		data.put("status", status);
		data.put("message", msg);
		response.getWriter().write(mapper.writeValueAsString(data));
		return;
	}
		
		
	}else if (request.getParameterMap().containsKey("forum_id") && request.getParameterMap().containsKey("get_forum_status")) {
		int forum_id = Integer.parseInt(request.getParameter("forum_id"));
		if(loggedInUser.getRole().equals("registered")){
			if(ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())){
			boolean locked = ForumDAO.getStatus(forum_id);
			boolean bnd = UserDAO.get(loggedInUser.getUsername()).isBanned();
			response.getWriter().write(mapper.writeValueAsString(locked || bnd));
			return;
				
		}}
		else{
			data.put("status", status);
			data.put("message", msg);
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
	}else if (request.getParameterMap().containsKey("topic_id") && request.getParameterMap().containsKey("get_parent_forum")) {
		int topic_id = Integer.parseInt(request.getParameter("topic_id"));
		if (loggedInUser == null){
			int parentId = TopicDAO.getParentId(topic_id);
			if (ForumDAO.hasAccessTo(parentId, "guest")) {
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
				
			}else{
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
		}else if(UserDAO.get(loggedInUser) == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", ".LogoutServlet");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
		if(loggedInUser.getRole().equals("admin")){
			int parentId = TopicDAO.getParentId(topic_id);
			String jsonForums = mapper.writeValueAsString(parentId);
			response.getWriter().write(jsonForums);
			return;
				
		}
		if(loggedInUser.getRole().equals("moderator")){
			int parentId = TopicDAO.getParentId(topic_id);
			String jsonForums = mapper.writeValueAsString(parentId);
			response.getWriter().write(jsonForums);
			return;
		}
		if(loggedInUser.getRole().equals("registered")){
			int parentId = TopicDAO.getParentId(topic_id);
			if (ForumDAO.hasAccessTo(parentId, loggedInUser.getRole())) {
				String jsonForums = mapper.writeValueAsString(parentId);
				response.getWriter().write(jsonForums);
				return;
			
		}else{
			data.put("status", status);
			data.put("message", "Access Denied");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
	}
		else{
		data.put("status", status);
		data.put("message", msg);
		response.getWriter().write(mapper.writeValueAsString(data));
		return;
	}
		
		
	}
	else if (request.getParameterMap().containsKey("topic_id") && request.getParameterMap().containsKey("get_canEditDelete")) {
		int topic_id = Integer.parseInt(request.getParameter("topic_id"));
		boolean bnd = UserDAO.get(loggedInUser.getUsername()).isBanned();
		if(loggedInUser.getRole().equals("moderator") && !bnd){
			boolean can = TopicDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), topic_id);
			response.getWriter().write(mapper.writeValueAsString(can));
			return;
				
		}else if (loggedInUser.getRole().equals("registered") && request.getParameterMap().containsKey("parent_forum_id") && !bnd) {
			boolean can = TopicDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), topic_id);
			boolean lockedForum = ForumDAO.getStatus(Integer.parseInt(request.getParameter("parent_forum_id")));
			boolean lockedTopic= TopicDAO.getStatus(topic_id);
			response.getWriter().write(mapper.writeValueAsString(can && !lockedForum && !lockedTopic));
			return;
		}
		else{
			data.put("status", status);
			data.put("message", msg);
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
	}else if (request.getParameterMap().containsKey("topic_id") && request.getParameterMap().containsKey("get_topic_status")) {
		int topic_id = Integer.parseInt(request.getParameter("topic_id"));
		int forum_id = TopicDAO.getParentId(topic_id);
		boolean bnd = UserDAO.get(loggedInUser.getUsername()).isBanned();
		if(loggedInUser.getRole().equals("registered")){
			if(ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())){
			boolean locked = TopicDAO.getStatus(topic_id);
			response.getWriter().write(mapper.writeValueAsString(locked || bnd));
			return;
				
		}}
		else{
			data.put("status", status);
			data.put("message", msg);
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
	}else if (request.getParameterMap().containsKey("reply_id") && request.getParameterMap().containsKey("get_parent_topic")) {
		int reply_id = Integer.parseInt(request.getParameter("reply_id"));
		int parentTopicId = ReplyDAO.getParentId(reply_id);
		int parentId = TopicDAO.getParentId(parentTopicId);
		if (loggedInUser == null){
			if (ForumDAO.hasAccessTo(parentId, "guest")) {
				String jsonForums = mapper.writeValueAsString(parentTopicId);
				response.getWriter().write(jsonForums);
				return;
			}else{
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
		}else if(UserDAO.get(loggedInUser) == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", ".LogoutServlet");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
		if(loggedInUser.getRole().equals("admin")){
			String jsonForums = mapper.writeValueAsString(parentTopicId);
			response.getWriter().write(jsonForums);
			return;
				
		}
		if(loggedInUser.getRole().equals("moderator")){
			String jsonForums = mapper.writeValueAsString(parentTopicId);
			response.getWriter().write(jsonForums);
			return;
		}
		if(loggedInUser.getRole().equals("registered")){
			if (ForumDAO.hasAccessTo(parentId, loggedInUser.getRole())) {
				String jsonForums = mapper.writeValueAsString(parentTopicId);
				response.getWriter().write(jsonForums);
				return;
			
		}else{
			data.put("status", status);
			data.put("message", "Access Denied");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		
	}
		else{
		data.put("status", status);
		data.put("message", msg);
		response.getWriter().write(mapper.writeValueAsString(data));
		return;
	}
		
		
	}else if (request.getParameterMap().containsKey("get_user_status") && request.getParameterMap().containsKey("username")) {
		String usn = request.getParameter("username");	
		User usr = UserDAO.get(usn);
		response.getWriter().write(mapper.writeValueAsString(usr.isBanned()));
		return;
	}
		else{
			data.put("status", status);
			data.put("message", msg);
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
	
		
		
		
	}
		///bice get forum locked? da bi znao da li sme da dodaje il ne 
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
