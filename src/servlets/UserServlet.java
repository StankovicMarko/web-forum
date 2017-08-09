package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import model.Reply;
import model.Topic;
import model.User;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
	
		if (request.getParameterMap().containsKey("username")) {
			String username = request.getParameter("username");
			if (loggedInUser == null){
				User user=UserDAO.get(username);
				
//				List<Reply> replies = ReplyDAO.get(topic_id, "guest", starting_point, results_per_page );
//				List<String> details = TopicDAO.getDetails(topic_id);
//				String ownerPicture = UserDAO.getPicture(details.get(3));///send username to get picture
//				details.add(ownerPicture);
//				data.put("replies", replies);
				data.put("user", user);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;
					
				
				
			}
			if(UserDAO.get(loggedInUser) == null){
				data.put("status", status);
				data.put("message", msg);
				data.put("redirect", ".LogoutServlet");
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
			
			if(loggedInUser.getRole().equals("admin")){
				User user=UserDAO.get(username);
				List<Topic> userTopics = TopicDAO.get(username);
				List<Reply> userReplies = ReplyDAO.get(username);
				data.put("user", user);
				data.put("topics", userTopics);
				data.put("replies", userReplies);
				
				if (loggedInUser.getUsername().equals(username)) {
					List<User> allUsers = UserDAO.get();
					data.put("users", allUsers);
				}
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;

			}
			if(loggedInUser.getRole().equals("moderator")){
				User user=UserDAO.get(username);
				List<Topic> userTopics = TopicDAO.get(username);
				List<Reply> userReplies = ReplyDAO.get(username);
				data.put("user", user);
				data.put("topics", userTopics);
				data.put("replies", userReplies);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;
			}
			if(loggedInUser.getRole().equals("registered")){
				User user=UserDAO.get(username);
				List<Topic> userTopics = TopicDAO.get(username);
				List<Reply> userReplies = ReplyDAO.get(username);
				data.put("user", user);
				data.put("topics", userTopics);
				data.put("replies", userReplies);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;
//					
				}else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
				
			}
			
            
            //specificno za poseban forum
            
            
        }
		

//		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		HttpSession session = request.getSession();
		User loggedInUser = (User) session.getAttribute("user");
		
		String msg = "";
		String status = "failure";
		
		if (loggedInUser == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", "index.html");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		
		boolean bnd = UserDAO.get(loggedInUser.getUsername()).isBanned();
		
		if (request.getParameterMap().containsKey("action") && loggedInUser.getRole().equals("admin") && !bnd) {
			String action = request.getParameter("action");
			if("create".equals(action)){
			try{
			User newUser;
					try{
						String username = request.getParameter("username");
						String password = request.getParameter("password");
						String mail = request.getParameter("mail");
						Date dateReg = new Date();
						String role = request.getParameter("role");
						boolean banned = false;
						String picture = "images/default-avatar.png";
						boolean deleted = false;
						
						newUser = new User(0, username, password, " ", 
								" ", mail, dateReg, role, banned, picture, deleted);
						
							
					}
					catch (Exception e){
						throw new Exception("Parameters not valid");
					}
					
					if(newUser.getUsername().equals("")) throw new Exception("Parameter username not valid");
					if(newUser.getPassword().equals("")) throw new Exception("Parameter password not valid");
					if(newUser.getMail().equals("")) throw new Exception("Parameter email not valid");
					
					
					if(UserDAO.insert(newUser)){
						msg="User added successfully";
						status = "success";
					}else{
						
						msg="Adding user failed (Username already in use)";
					}
					
					
					
					
				}catch (Exception ex){msg=ex.getMessage();}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				
			}else if ("ban".equals(action)) {
				try{
					int id = Integer.parseInt(request.getParameter("user_id"));
					
					if(!UserDAO.ban(id)) throw new Exception("Database error");
					
					msg="User banned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
				
				
			}else if ("unban".equals(action)) {
				try{
					int id = Integer.parseInt(request.getParameter("user_id"));
					
					if(!UserDAO.unban(id)) throw new Exception("Database error");
					
					msg="User unbanned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if ("delete".equals(action)) {
				try{
					int id = Integer.parseInt(request.getParameter("user_id"));
					
					if(!UserDAO.delete(id)) throw new Exception("Database error");
					
					msg="User deleted";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if ("edit".equals(action)) {
				int id;
				String psw ;
				String mail;
				String role;
				try{
							try{
								id = Integer.parseInt(request.getParameter("user_id"));
								psw = request.getParameter("password");
								mail = request.getParameter("mail");
								role = request.getParameter("role");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(id<0) throw new Exception("Parameter id not valid");
					if(mail.equals("")) throw new Exception("Parameter name not valid");
					
					if(UserDAO.edit(id, psw, mail, role)){
						msg="User edited";
						status = "success";
					}else{
						
						msg="Error editing user";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}else if ("updateName".equals(action) && request.getParameterMap().containsKey("username")) {
				String usn = request.getParameter("username");
				if(loggedInUser.getUsername().equals(usn)){
				String new_name;
				String new_surName;
				try{
							try{
								new_name = request.getParameter("new_name");
								new_surName = request.getParameter("new_surName");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(usn.equals("")) throw new Exception("Parameter usn not valid");
					if(new_name.equals("")) throw new Exception("Parameter name not valid");
					if(new_surName.equals("")) throw new Exception("Parameter surname not valid");
					
					
					if(UserDAO.updateName(usn, new_name, new_surName)){
						msg="User updated";
						status = "success";
					}else{
						
						msg="Error updating user";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}
				else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
			}
			else if ("updatePass".equals(action) 
					&& request.getParameterMap().containsKey("username") 
					&& request.getParameterMap().containsKey("old_pass")) {
				String usn = request.getParameter("username");
				String old_pass = request.getParameter("old_pass");
				if(loggedInUser.getUsername().equals(usn) && loggedInUser.getPassword().equals(old_pass)){
				String new_pass;
				try{
							try{
								new_pass = request.getParameter("new_pass");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(old_pass.equals("")) throw new Exception("Parameter name not valid");
					if(new_pass.equals("")) throw new Exception("Parameter surname not valid");
					
					
					if(UserDAO.updatePass(usn, new_pass)){
						msg="User updated";
						status = "success";
					}else{
						
						msg="Error updating user";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}
				else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
			}
			
		}else if (request.getParameterMap().containsKey("action") && (loggedInUser.getRole().equals("registered") || loggedInUser.getRole().equals("moderator"))) {
			String action = request.getParameter("action");
			if ("updateName".equals(action) && request.getParameterMap().containsKey("username")) {
				String usn = request.getParameter("username");
				if(loggedInUser.getUsername().equals(usn)){
				String new_name;
				String new_surName;
				try{
							try{
								new_name = request.getParameter("new_name");
								new_surName = request.getParameter("new_surName");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(usn.equals("")) throw new Exception("Parameter usn not valid");
					if(new_name.equals("")) throw new Exception("Parameter name not valid");
					if(new_surName.equals("")) throw new Exception("Parameter surname not valid");
					
					
					if(UserDAO.updateName(usn, new_name, new_surName)){
						msg="User updated";
						status = "success";
					}else{
						
						msg="Error updating user";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}
				else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
			}
			else if ("updatePass".equals(action) 
					&& request.getParameterMap().containsKey("username") 
					&& request.getParameterMap().containsKey("old_pass")) {
				String usn = request.getParameter("username");
				String old_pass = request.getParameter("old_pass");
				if(loggedInUser.getUsername().equals(usn) && loggedInUser.getPassword().equals(old_pass)){
				String new_pass;
				try{
							try{
								new_pass = request.getParameter("new_pass");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(old_pass.equals("")) throw new Exception("Parameter name not valid");
					if(new_pass.equals("")) throw new Exception("Parameter surname not valid");
					
					
					if(UserDAO.updatePass(usn, new_pass)){
						msg="User updated";
						status = "success";
					}else{
						
						msg="Error updating user";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}
				else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
			}
			
		}
		
		
		
		
		else{
	
		try{
//				String jsonUser = request.getParameter("user");
			
			User newUser;
					try{
						String username = request.getParameter("username");
						String password = request.getParameter("password");
						String mail = request.getParameter("email");
						String name = request.getParameter("fname");
						String surName = request.getParameter("lname");
						Date dateReg = new Date();
						String role = "registered";
						boolean banned = false;
						String picture = "images/default-avatar.png";
						boolean deleted = false;
						
						newUser = new User(0, username, password, name, 
								surName, mail, dateReg, role, banned, picture, deleted);
						
//							newUser= mapper.readValue(jsonUser, User.class);
					
					}
					catch (Exception e){
						throw new Exception("Parameters not valid");
					}
			
			if(newUser.getUsername().equals("")) throw new Exception("Parameter username not valid");
			if(newUser.getPassword().equals("")) throw new Exception("Parameter password not valid");
			if(newUser.getMail().equals("")) throw new Exception("Parameter email not valid");
			
			
			if(UserDAO.insert(newUser)){
				msg="Registration successful";
				status = "success";
			}else{
				
				msg="Registration failed (Username already in use)";
			}
			
			
			
			
		} catch (Exception ex)
		{
			msg=ex.getMessage();
		}
	
		
		data.put("status", status);
		data.put("message", msg);
		response.getWriter().write(mapper.writeValueAsString(data));

	
		}
		
	
	}
	
}
