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
import dao.TopicDAO;
import dao.UserDAO;
import model.Forum;
import model.Topic;
import model.User;

/**
 * Servlet implementation class TopicServlet
 */
public class TopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicServlet() {
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
			page = Integer.parseInt(request.getParameter("topic_page"));
			results_per_page = Integer.parseInt(request.getParameter("topic_results"));
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
		
		
		
		if (request.getParameterMap().containsKey("forum_id")) {
			int forum_id = Integer.parseInt(request.getParameter("forum_id"));
			
			if (loggedInUser == null){
				if (ForumDAO.hasAccessTo(forum_id, "guest")) {
					List<Topic> topics = TopicDAO.get(forum_id, starting_point, results_per_page );
					String jsonTopics = mapper.writeValueAsString(topics);
					response.getWriter().write(jsonTopics);
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
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
					List<Topic> topics = TopicDAO.get(forum_id, starting_point, results_per_page );
					String jsonTopics = mapper.writeValueAsString(topics);
					response.getWriter().write(jsonTopics);
					return;
					
				}

			}
			if(loggedInUser.getRole().equals("moderator")){
				///vidi sve ko admin al ne moze da edituje
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
					List<Topic> topics = TopicDAO.get(forum_id, starting_point, results_per_page );
					ArrayList<Integer> canPinLock = TopicDAO.canPinLock(loggedInUser.getUsername(), forum_id);
					data.put("topics", topics);
					data.put("canPinLock", canPinLock);
					response.getWriter().write(mapper.writeValueAsString(data));
					return;
					
				}

			}
			if(loggedInUser.getRole().equals("registered")){
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
					List<Topic> topics = TopicDAO.get(forum_id, starting_point, results_per_page );
					String jsonTopics = mapper.writeValueAsString(topics);
					response.getWriter().write(jsonTopics);
					return;
				
			}else{
				data.put("status", status);
				data.put("message", "Access Denied");
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
				
			}
			
            
            //specificno za posebanu temu
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
					Topic newTopic;
							try{
								String name = request.getParameter("name");
								String description = request.getParameter("description");
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								boolean locked = false;
								boolean pinned = false;
								String content = request.getParameter("content");
								int parentForumId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
								
								
								newTopic = new Topic(0, name, description, content,
										ownerUsername, creationDate, pinned, locked, parentForumId, deleted);
//								System.out.println(newTopic);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newTopic.getName().equals("")) throw new Exception("Parameter name not valid");
					if(newTopic.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(TopicDAO.insert(newTopic)){
						msg="Topic added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding Topic";
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
			else if("lock".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.lock(id)) throw new Exception("Database error");
					
					msg="Topic locked";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if("unlock".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.unlock(id)) throw new Exception("Database error");
					
					msg="Topic unlocked";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}else if("pin".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.pin(id)) throw new Exception("Database error");
					
					msg="Topic pinned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if("unpin".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.unpin(id)) throw new Exception("Database error");
					
					msg="Topic unpinned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}else if ("delete".equals(action)){
				
				try{
					
					int id = Integer.parseInt(request.getParameter("topic_id"));
					if(!TopicDAO.delete(id)) throw new Exception("Database error");
					
					msg="Topic deleted";
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
				String name ;
				String description;
				String content;
				try{
					Topic newTopic;
							try{
								id = Integer.parseInt(request.getParameter("topic_id"));
								name = request.getParameter("name");
								description = request.getParameter("description");
								content = request.getParameter("content");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(id<0) throw new Exception("Parameter id not valid");
					if(name.equals("")) throw new Exception("Parameter name not valid");
					if(content.equals("")) throw new Exception("Parameter content not valid");
					
					if(TopicDAO.edit(id, name, description, content)){
						msg="Topic edited";
						status = "success";
					}else{
						
						msg="Error editing topic";
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
			int parentForumId=Integer.parseInt(request.getParameter("parentId"));
			if(ForumDAO.hasAccessTo(parentForumId, loggedInUser.getRole())){
			String action = request.getParameter("action");
			if("create".equals(action)){
				try{
					Topic newTopic;
							try{
								String name = request.getParameter("name");
								String description = request.getParameter("description");
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								boolean locked = false;
								boolean pinned = false;
								String content = request.getParameter("content");
//								int parentForumId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
								
								
								newTopic = new Topic(0, name, description, content,
										ownerUsername, creationDate, pinned, locked, parentForumId, deleted);
//								System.out.println(newTopic);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newTopic.getName().equals("")) throw new Exception("Parameter name not valid");
					if(newTopic.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(TopicDAO.insert(newTopic)){
						msg="Topic added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding Topic";
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
			else if(ForumDAO.hasAccessTo(TopicDAO.getParentId(Integer.parseInt(request.getParameter("topic_id"))), loggedInUser.getRole())){
				String action = request.getParameter("action");
				if("edit".equals(action)){
					int id;
					String name ;
					String description;
					String content;
					try{
								try{
									id = Integer.parseInt(request.getParameter("topic_id"));
									name = request.getParameter("name");
									description = request.getParameter("description");
									content = request.getParameter("content");
								}
								catch (Exception e){
									throw new Exception("Parameters not valid");
								}
						
						if(id<0) throw new Exception("Parameter id not valid");
						if(name.equals("")) throw new Exception("Parameter name not valid");
						if(content.equals("")) throw new Exception("Parameter content not valid");
						
						if(TopicDAO.edit(id, name, description, content)){
							msg="Topic edited";
							status = "success";
						}else{
							
							msg="Error editing topic";
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
					Topic newTopic;
							try{
								String name = request.getParameter("name");
								String description = request.getParameter("description");
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								boolean locked = false;
								boolean pinned = false;
								String content = request.getParameter("content");
								int parentForumId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
								
								
								newTopic = new Topic(0, name, description, content,
										ownerUsername, creationDate, pinned, locked, parentForumId, deleted);
//								System.out.println(newTopic);
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newTopic.getName().equals("")) throw new Exception("Parameter name not valid");
					if(newTopic.getContent().equals("")) throw new Exception("Parameter content not valid");
					
					if(TopicDAO.insert(newTopic)){
						msg="Topic added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding Topic";
					}
					
					
					
					
				} catch (Exception ex)
				{
					msg=ex.getMessage();
				}
			
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			
			}else if("lock".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.lock(id)) throw new Exception("Database error");
					
					msg="Topic locked";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if("unlock".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.unlock(id)) throw new Exception("Database error");
					
					msg="Topic unlocked";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}else if("pin".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.pin(id)) throw new Exception("Database error");
					
					msg="Topic pinned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
			else if("unpin".equals(action)){
				
				try{
					int id = Integer.parseInt(request.getParameter("topic_id"));
					
					if(!TopicDAO.unpin(id)) throw new Exception("Database error");
					
					msg="Topic unpinned";
					status = "success";
				}catch(Exception e){
					
					msg=e.getMessage();
					
				}
				
				data.put("status", status);
				data.put("message", msg);
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}else if ("delete".equals(action)){
				
				try{
					
					int id = Integer.parseInt(request.getParameter("topic_id"));
					if(TopicDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), id)){
					if(!TopicDAO.delete(id)) throw new Exception("Database error");
					}else{
						throw new Exception("No authorization to edit");
					}
					
					msg="Topic deleted";
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
				String name ;
				String description;
				String content;
				try{
							try{
								id = Integer.parseInt(request.getParameter("topic_id"));
								name = request.getParameter("name");
								description = request.getParameter("description");
								content = request.getParameter("content");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(id<0) throw new Exception("Parameter id not valid");
					if(name.equals("")) throw new Exception("Parameter name not valid");
					if(content.equals("")) throw new Exception("Parameter content not valid");
					
					if(TopicDAO.canEditDelete(loggedInUser.getUsername(), loggedInUser.getRole(), id)){
					if(TopicDAO.edit(id, name, description, content)){
						msg="Topic edited";
						status = "success";
					}else{
						
						msg="Error editing topic";
					}}else{
						msg="Error editing topic";
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

