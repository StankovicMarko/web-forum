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
import model.Forum;
import model.Reply;
import model.Topic;
import model.User;

/**
 * Servlet implementation class ReplyServlet
 */
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		HttpSession session = request.getSession();
		User loggedInUser = (User) session.getAttribute("user");
		String msg = "You must be logged in.";
		String status = "failure";	
		
		int page;
		int results_per_page;
		int starting_point;
		
		try {
			page = Integer.parseInt(request.getParameter("reply_page"));
			results_per_page = Integer.parseInt(request.getParameter("reply_results"));
			starting_point = page*results_per_page-results_per_page;
			
			if (starting_point<0 || page<=0 || results_per_page<=0) {
				response.getWriter().write(mapper.writeValueAsString("Parameters not valid"));
				return;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			response.getWriter().write(mapper.writeValueAsString("Parameters not valid"));
			return;
		}
		
		
		
		if (request.getParameterMap().containsKey("topic_id")) {
			int topic_id = Integer.parseInt(request.getParameter("topic_id"));
			int parentForumId = TopicDAO.getParentId(topic_id);
			if (loggedInUser == null){
				if (ForumDAO.hasAccessTo(parentForumId, "guest")) {
					List<Reply> replies = ReplyDAO.get(topic_id, "guest", starting_point, results_per_page );
					List<String> details = TopicDAO.getDetails(topic_id);
					String ownerPicture = UserDAO.getPicture(details.get(3));///send username to get picture
					details.add(ownerPicture);
					data.put("replies", replies);
					data.put("details", details);
					String jsonForums = mapper.writeValueAsString(data);
					response.getWriter().write(jsonForums);
					return;
					
				}else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
				
			}
			if(UserDAO.get(loggedInUser) == null){
				data.put("status", status);
				data.put("message", msg);
				data.put("redirect", ".LogoutServlet");
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
			
			if(loggedInUser.getRole().equals("admin")){
				List<Reply> replies = ReplyDAO.get(topic_id, "guest", starting_point, results_per_page );
				List<String> details = TopicDAO.getDetails(topic_id);
				String ownerPicture = UserDAO.getPicture(details.get(3));///send username to get picture
				details.add(ownerPicture);
				data.put("replies", replies);
				data.put("details", details);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;

			}
			if(loggedInUser.getRole().equals("moderator")){
				List<Reply> replies = ReplyDAO.get(topic_id, loggedInUser.getRole(), starting_point, results_per_page );
				List<String> details = TopicDAO.getDetails(topic_id);
				String ownerPicture = UserDAO.getPicture(details.get(3));///send username to get picture
				details.add(ownerPicture);
				ArrayList<Integer> canEditDelete= ReplyDAO.canEditDelete(loggedInUser.getUsername(), topic_id);
				data.put("canEditDelete", canEditDelete);
				data.put("replies", replies);
				data.put("details", details);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;
			}
			if(loggedInUser.getRole().equals("registered")){
				if (ForumDAO.hasAccessTo(parentForumId, loggedInUser.getRole())) {
					List<Reply> replies = ReplyDAO.get(topic_id, "guest", starting_point, results_per_page );
					List<String> details = TopicDAO.getDetails(topic_id);
					String ownerPicture = UserDAO.getPicture(details.get(3));///send username to get picture
					details.add(ownerPicture);
					ArrayList<Integer> canEditDelete= ReplyDAO.canEditDeleteRegi(loggedInUser.getUsername(), topic_id);
					boolean topicStatus = TopicDAO.getStatus(topic_id);
					data.put("topicStatus", topicStatus);
					data.put("canEditDelete", canEditDelete);
					data.put("replies", replies);
					data.put("details", details);
					String jsonForums = mapper.writeValueAsString(data);
					response.getWriter().write(jsonForums);
					return;
					
				}else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
				
			}
			
            
            //specificno za poseban forum
            
            
        }else if (request.getParameterMap().containsKey("reply_id")) {
        	int reply_id = Integer.parseInt(request.getParameter("reply_id"));
			int topic_id = ReplyDAO.getParentId(reply_id);
			int parentForumId = TopicDAO.getParentId(topic_id);
			if (loggedInUser == null){
				if (ForumDAO.hasAccessTo(parentForumId, "guest")) {
					Reply reply = ReplyDAO.get(reply_id);
					String jsonForums = mapper.writeValueAsString(reply);
					response.getWriter().write(jsonForums);
					return;
					
				}else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
				
			}
			if(UserDAO.get(loggedInUser) == null){
				data.put("status", status);
				data.put("message", msg);
				data.put("redirect", ".LogoutServlet");
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			
			
			if(loggedInUser.getRole().equals("admin")){
				Reply reply = ReplyDAO.get(reply_id);
				String jsonForums = mapper.writeValueAsString(reply);
				response.getWriter().write(jsonForums);
				return;

			}
			if(loggedInUser.getRole().equals("moderator")){
				Reply reply = ReplyDAO.get(reply_id);
				String jsonForums = mapper.writeValueAsString(reply);
				response.getWriter().write(jsonForums);
				return;
			}
			if(loggedInUser.getRole().equals("registered")){
				if (ForumDAO.hasAccessTo(parentForumId, loggedInUser.getRole())) {
					Reply reply = ReplyDAO.get(reply_id);
					String jsonForums = mapper.writeValueAsString(reply);
					response.getWriter().write(jsonForums);
					return;
					
				}else{
					data.put("status", status);
					data.put("message", msg);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
				}
				
			}
			
 
            
            
        }
		

		}


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
		
		String msg = "You must be logged in.";
		String status = "failure";
		
		if (loggedInUser == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", "index.html");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		if(UserDAO.get(loggedInUser) == null){
			data.put("status", status);
			data.put("message", msg);
			data.put("redirect", ".LogoutServlet");
			response.getWriter().write(mapper.writeValueAsString(data));
			return;
		}
		boolean bnd = UserDAO.get(loggedInUser.getUsername()).isBanned();
		if(loggedInUser.getRole().equals("admin") && !bnd){
			
			
			String action = request.getParameter("action");
			if("create".equals(action)){
				try{
					Reply newReply;
							try{
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								String ownerPicture = "";
								String content = request.getParameter("content");
								int parentTopicId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
								
								
								newReply = new Reply(0, content, ownerUsername, 
										ownerPicture, creationDate, parentTopicId, deleted);
//								System.out.println(newTopic);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newReply.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(ReplyDAO.insert(newReply)){
						msg="Reply added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding Reply";
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
			else if ("delete".equals(action)){
				
				try{
					
					int id = Integer.parseInt(request.getParameter("reply_id"));
					if(!ReplyDAO.delete(id)) throw new Exception("Database error");
					
					msg="Reply deleted";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
				
				
			}
			else if("edit".equals(action)){
				int id;
				String content;
				try{
							try{
								id = Integer.parseInt(request.getParameter("reply_id"));
								content = request.getParameter("content");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(content.equals("")) throw new Exception("Parameter content not valid");
					
					if(ReplyDAO.edit(id, content)){
						msg="Reply edited";
						status = "success";
					}else{
						
						msg="Error editing reply";
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
			
		}
		else if(loggedInUser.getRole().equals("registered") && !bnd){
			if(request.getParameterMap().containsKey("parentId")){
			int parentTopicId = Integer.parseInt(request.getParameter("parentId"));
			int parentForumId=TopicDAO.getParentId(parentTopicId);
			if(ForumDAO.hasAccessTo(parentForumId, loggedInUser.getRole())){
			String action = request.getParameter("action");
			if("create".equals(action)){
				try{
					Reply newReply;
							try{
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								String ownerPicture = "";
								String content = request.getParameter("content");
								boolean deleted = false;
								
								
								
								newReply = new Reply(0, content, ownerUsername, 
										ownerPicture, creationDate, parentTopicId, deleted);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newReply.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(ReplyDAO.insert(newReply)){
						msg="Reply added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding Reply";
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
			}
			}
			else if(ForumDAO.hasAccessTo(
						TopicDAO.getParentId(
								ReplyDAO.getParentId(
										Integer.parseInt(
												request.getParameter("reply_id")))), loggedInUser.getRole())){
				String action = request.getParameter("action");
				
				if("edit".equals(action)){
					int id;
					String content;
					try{
								try{
									id = Integer.parseInt(request.getParameter("reply_id"));
									content = request.getParameter("content");
								}
								catch (Exception e){
									throw new Exception("Parameters not valid");
								}
						
						if(content.equals("")) throw new Exception("Parameter content not valid");
						
						if(ReplyDAO.edit(id, content)){
							msg="Reply edited";
							status = "success";
						}else{
							
							msg="Error editing Reply";
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
			
				if("delete".equals(action)){
					int id;
					try{
						try{
							id = Integer.parseInt(request.getParameter("reply_id"));
						}
						catch (Exception e){
								throw new Exception("Parameters not valid");
						}
					
						if(ReplyDAO.delete(id)){
							msg="Reply deleted";
							status = "success";
						}else{
						
							msg="Error deleting Reply";
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
		}
		}
		else if(loggedInUser.getRole().equals("moderator") && !bnd){
			String action = request.getParameter("action");
			if("create".equals(action)){
				try{
					Reply newReply;
							try{
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								String content = request.getParameter("content");
								int parentTopicId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
								
								
								newReply = new Reply(0, content, ownerUsername,
										"", creationDate, parentTopicId, deleted);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newReply.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(ReplyDAO.insert(newReply)){
						msg="Reply added";
						status = "success";
					}else{
						
						msg="Error adding reply";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}else if ("delete".equals(action)){
				
				try{
					
					int id = Integer.parseInt(request.getParameter("reply_id"));
					if(ReplyDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), id)){
					if(!ReplyDAO.delete(id)) throw new Exception("Database error");
					}else{
						throw new Exception("No authorization to edit");
					}
					
					msg="Reply deleted";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
				
				
			}
			else if("edit".equals(action)){
				int id;
				String content;
				try{
							try{
								id = Integer.parseInt(request.getParameter("reply_id"));
								content = request.getParameter("content");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}

					if(content.equals("")) throw new Exception("Parameter content not valid");
					
					if(ReplyDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), id)){
					if(ReplyDAO.edit(id, content)){
						msg="Reply edited";
						status = "success";
					}else{
						
						msg="Error editing reply";
					}}else{
						msg="Error editing reply";
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
			
		}
	}

}
