package servlets;

import java.io.IOException;
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
import dao.UserDAO;
import model.Forum;
import model.User;


/**
 * Servlet implementation class ForumServlet
 */
public class ForumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForumServlet() {
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
		
		int page;
		int results_per_page;
		int starting_point;
		
		try {
			page = Integer.parseInt(request.getParameter("forum_page"));
			results_per_page = Integer.parseInt(request.getParameter("forum_results"));
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
					List<Forum> forums = ForumDAO.get(forum_id, "guest", starting_point, results_per_page );
					List<String> details = ForumDAO.getDetails(forum_id);
					data.put("forums", forums);
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
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
					List<Forum> forums = ForumDAO.get(forum_id, loggedInUser.getRole(), starting_point, results_per_page );
					List<String> details = ForumDAO.getDetails(forum_id);
					data.put("forums", forums);
					data.put("details", details);
					String jsonForums = mapper.writeValueAsString(data);
					response.getWriter().write(jsonForums);
					return;
					
				}
//				else{
//					response.getWriter().write(mapper.writeValueAsString("Access Denied"));
//					return;	
//				}
			}
			if(loggedInUser.getRole().equals("moderator")){
				///vidi sve ko admin al ne moze da edituje
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
					List<Forum> forums = ForumDAO.get(forum_id, loggedInUser.getRole(), starting_point, results_per_page );
					List<String> details = ForumDAO.getDetails(forum_id);
					data.put("forums", forums);
					data.put("details", details);
					String jsonForums = mapper.writeValueAsString(data);
					response.getWriter().write(jsonForums);
					return;
					
				}
//				else{
//					response.getWriter().write(mapper.writeValueAsString("Access Denied"));
//					return;	
//				}
			}
			if(loggedInUser.getRole().equals("registered")){
				///vrati forume ako je roditelj javni/ otvoren
				if (ForumDAO.hasAccessTo(forum_id, loggedInUser.getRole())) {
				List<Forum> forums = ForumDAO.get(forum_id, loggedInUser.getRole(), starting_point, results_per_page );
				List<String> details = ForumDAO.getDetails(forum_id);
				data.put("forums", forums);
				data.put("details", details);
				String jsonForums = mapper.writeValueAsString(data);
				response.getWriter().write(jsonForums);
				return;
				
			}else{
				data.put("status", status);
				data.put("message", "Access Denied");
				response.getWriter().write(mapper.writeValueAsString(data));
				return;
			}
				
			}
			
            
            //specificno za poseban forum
            
            
            ///bez id-a poziva se sa index.html
        }else{
		
		
		if (loggedInUser == null){
			List<Forum> forums = ForumDAO.get("guest", starting_point, results_per_page);
			String jsonForums = mapper.writeValueAsString(forums);
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
		
		////uzmi id parametar i onda vrati za njega mogu ovaj servlet da iskoristim za pojedinacni prikaz foruma
		
		if(loggedInUser.getRole().equals("admin")){
			List<Forum> forums = ForumDAO.get(loggedInUser.getRole(), starting_point, results_per_page );
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
		}
		if(loggedInUser.getRole().equals("registered")){
			///vrati forume ako je javni/ otvoren
			List<Forum> forums = ForumDAO.get(loggedInUser.getRole(), starting_point, results_per_page );
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
		}
		if(loggedInUser.getRole().equals("moderator")){
			///vrati forume ako je javni/ otvoren / zatvoren
			List<Forum> forums = ForumDAO.get(loggedInUser.getRole(), starting_point, results_per_page );
			String jsonForums = mapper.writeValueAsString(forums);
			response.getWriter().write(jsonForums);
			return;
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
					Forum newForum;
							try{
								String name = request.getParameter("name");
								String description = request.getParameter("description");
								String ownerUsername = request.getParameter("owner");
								Date creationDate = new Date();
								boolean locked = false;
								String type = request.getParameter("type");
								int parentForumId=Integer.parseInt(request.getParameter("parentId"));
								boolean deleted = false;
								
//								System.out.println(parentForumId); 0 kad je main page, u bazi je null jer nema ref na fkey
								newForum = new Forum(0, name, description, ownerUsername, 
										creationDate, locked, type, parentForumId, deleted);
								
							
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(newForum.getName().equals("")) throw new Exception("Parameter name not valid");
					if(newForum.getType().equals("")) throw new Exception("Parameter type not valid");
					if(newForum.getOwnerUsername().equals("")) throw new Exception("Parameter owner not valid");
					
					if(ForumDAO.insert(newForum)){
						msg="Forum added";
						status = "success";
//						data.put("forum", newForum);
					}else{
						
						msg="Error adding forum";
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
					int id = Integer.parseInt(request.getParameter("forum_id"));
					
					if(!ForumDAO.lock(id)) throw new Exception("Database error");
					
					msg="Forum locked";
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
					int id = Integer.parseInt(request.getParameter("forum_id"));
					
					if(!ForumDAO.unlock(id)) throw new Exception("Database error");
					
					msg="Forum unlocked";
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
					
					int id = Integer.parseInt(request.getParameter("forum_id"));
					
					if(!ForumDAO.delete(id)) throw new Exception("Database error");
					
					msg="Forum deleted";
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
				String type;
				try{
					Forum newForum;
							try{
								id = Integer.parseInt(request.getParameter("forum_id"));
								name = request.getParameter("name");
								description = request.getParameter("description");
								type = request.getParameter("type");
							}
							catch (Exception e){
								throw new Exception("Parameters not valid");
							}
					
					if(id<0) throw new Exception("Parameter id not valid");
					if(name.equals("")) throw new Exception("Parameter name not valid");
					if(type.equals("")) throw new Exception("Parameter type not valid");
					
					if(ForumDAO.edit(id, name, description, type)){
						msg="Forum edited";
						status = "success";
					}else{
						
						msg="Error editing forum";
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
