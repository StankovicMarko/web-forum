package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Forum;
import model.Topic;
import model.User;

public class TopicDAO {

	private TopicDAO(){}
	
	
	public static boolean insert(Topic topic){
		return insert(topic.getName(), topic.getDescription(), topic.getContent(), topic.getOwnerUsername(), 
				topic.getCreationDate(), topic.isPinned(), topic.isLocked(), topic.getParentForumId(), topic.isDeleted());
	}
	
	
	public static boolean insert(String name, String description, String content,
			 String ownerUsername, Date creationDate, boolean pinned, boolean locked,
			 int parentForumId, boolean deleted){
		
		boolean success = false;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			connection = ConnectionManager.getConnection();
			
				String sql = "INSERT INTO topics(name, description, content, ownerUsername, creationDate, "
						+ "pinned, locked, parentForumId, deleted) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, name);
				statement.setString(index++, description);
				statement.setString(index++, content);
				statement.setString(index++, ownerUsername);
				statement.setDate(index++, new java.sql.Date(creationDate.getTime()));
				statement.setBoolean(index++, pinned);
				statement.setBoolean(index++, locked);
				statement.setInt(index++, parentForumId);
				statement.setBoolean(index++, deleted);
				
				statement.executeUpdate();
				success=true;
				

		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}}
		
		
		return success;
	
}
	
	
	public static List<Topic> get(int parenForumId, int starting_point, int results){
		List<Topic> topics = new ArrayList<Topic>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM topics WHERE deleted = 0 AND parentForumId = ? order by pinned desc LIMIT ?, ?";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, parenForumId);
			statement.setInt(index++, starting_point);
			statement.setInt(index++, results);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean pinned = resultSet.getBoolean("pinned");
				boolean locked = resultSet.getBoolean("locked");
				int parentForumId = resultSet.getInt("parentForumId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Topic topic= new Topic(id, name, description, content, 
						ownerUsername, creationDate, pinned, 
						locked, parentForumId, deleted);
				
				topics.add(topic);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return topics;
		
		
		}
		
		

	
	public static boolean lock(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE topics SET locked = 1 WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	public static boolean unlock(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE topics SET locked = 0 WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	public static boolean pin(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE topics SET pinned = 1 WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	public static boolean unpin(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE topics SET pinned = 0 WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	
	public static boolean delete(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
		
			String sql = "UPDATE topics SET deleted = 1 WHERE id = ?";
		
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			statement.close();
			
			sql = "UPDATE replies SET deleted = 1 WHERE parentTopicId = ?";
			
			statement = connection.prepareStatement(sql);
			index= 1;
			statement.setInt(index++, id);
			success= success && statement.executeUpdate()==1;
		
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	

	
	
	
	public static boolean edit(int id, String name, String description, String content){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE topics SET name = ?, description = ?, content = ? WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, name);
			statement.setString(index++, description);
			statement.setString(index++, content);
			statement.setInt(index++, id);
			
			
			success= statement.executeUpdate()==1;
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	public static boolean hasAccessTo(int id, String role){
		boolean has = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "SELECT type FROM forums WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			
			resultSet = statement.executeQuery();
			
			resultSet.next();
			String forumType;
			try {
				forumType= resultSet.getString("type");
			} catch (Exception e) {
				// TODO: handle exception
				forumType="";
			}
			
			
//			System.out.println(forumType);
			
			if(role.equals("guest") && forumType.equals("Public")){
				has = true;
			}
			else if (role.equals("registered") && ( forumType.equals("Public") || forumType.equals("Open"))) {
				has = true;
			}else if (role.equals("moderator") || role.equals("admin")) {
				has = true;
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return has;
		
	}
	
	
	public static String getParentType(int id){
		String type= "";
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "select type from forums where id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			
			resultSet = statement.executeQuery();
			
			resultSet.next();
			try {
				type = resultSet.getString("type");
			} catch (Exception e) {
				// TODO: handle exception
				type="";
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return type;
		
	}
	
	
	public static int getParentId(int id){
		int parentId=0;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "select parentForumId from topics where id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			
			resultSet = statement.executeQuery();
			
			resultSet.next();
			try {
				parentId = resultSet.getInt("parentForumId");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return parentId;
		
	}
	
	
	public static ArrayList<Integer> canPinLock(String username, int parenForumId){
		ArrayList<Integer> topicsIds = new ArrayList<Integer>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "select id from topics where ownerUsername in (select username from users where (username=? or role='registered')) and deleted = 0 and parentForumId = ?";

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, username);
			statement.setInt(index++, parenForumId);
//			statement.setInt(index++, starting_point);
//			statement.setInt(index++, results);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				
				topicsIds.add(id);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return topicsIds;
		
		
		}
	
	
	public static boolean canEditDelete(String username, String role, int topic_id){
		boolean cant = false;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection  = ConnectionManager.getConnection();
			
			String sql = "select username, role from users where username in (select ownerUsername from topics where id = ?)";

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, topic_id);
			resultSet = statement.executeQuery();
			resultSet.next();
			String ownerRole = resultSet.getString("role");
			String ownerUsn = resultSet.getString("username");
			
			if(role.equals("moderator") && ownerRole.equals("registered")){
				return true;
			}else if(username.equals(ownerUsn)){
				return true;
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return cant;
		
		
		}
	
	
	
	public static List<String> getDetails(int id){
		List<String> details = new ArrayList<String>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT name, description, content ,creationDate, ownerUsername FROM topics WHERE id = ?";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			resultSet = statement.executeQuery();
			
			
			resultSet.next();
			
			String name = resultSet.getString("name");
			String description = resultSet.getString("description");
			String content = resultSet.getString("content");
			String ownerUsername = resultSet.getString("ownerUsername");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date cd = resultSet.getDate("creationDate");
			String creationDate = df.format(cd);

			details.add(name);
			details.add(description);
			details.add(content);
			details.add(ownerUsername);
			details.add(creationDate);
				
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		
		return details;
			
		}
	
	public static boolean getStatus(int id){
		boolean locked= false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "select locked from topics where id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			
			resultSet = statement.executeQuery();
			
			resultSet.next();
			try {
				locked = resultSet.getBoolean("locked");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return locked;
		
	}
	
	
	public static List<Topic> get(String username){
		List<Topic> topics = new ArrayList<Topic>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM topics WHERE deleted = 0 AND ownerUsername = ? order by id desc";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, username);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean pinned = resultSet.getBoolean("pinned");
				boolean locked = resultSet.getBoolean("locked");
				int parentForumId = resultSet.getInt("parentForumId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Topic topic= new Topic(id, name, description, content, 
						ownerUsername, creationDate, pinned, 
						locked, parentForumId, deleted);
				
				topics.add(topic);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return topics;
		
		
		}
	
	
	public static List<Topic> get(String term, String role){
		List<Topic> topics = new ArrayList<Topic>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM topics WHERE deleted = 0 AND (name like ? or ownerUsername like ?)";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, "%"+term+"%");
			statement.setString(index++, "%"+term+"%");
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean pinned = resultSet.getBoolean("pinned");
				boolean locked = resultSet.getBoolean("locked");
				int parentForumId = resultSet.getInt("parentForumId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Topic topic= new Topic(id, name, description, content, 
						ownerUsername, creationDate, pinned, 
						locked, parentForumId, deleted);
				
				topics.add(topic);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		
		}else if(role.equals("guest")){
			
			try {
				
				
				connection  = ConnectionManager.getConnection();
				
				String sql = "select * from topics where deleted = 0 and"
						+ "(name like ? or ownerUsername like ? or description like ? or content like ?)"
						+ "and parentForumId in (select id from forums where deleted = 0 and type = 'Public')";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					String content = resultSet.getString("content");
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean pinned = resultSet.getBoolean("pinned");
					boolean locked = resultSet.getBoolean("locked");
					int parentForumId = resultSet.getInt("parentForumId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Topic topic= new Topic(id, name, description, content, 
							ownerUsername, creationDate, pinned, 
							locked, parentForumId, deleted);
					
					topics.add(topic);
					
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
		}else if(role.equals("registered")){
			
			try {
				
				
				connection  = ConnectionManager.getConnection();
				
				String sql = "select * from topics where deleted = 0 and "
						+ "(name like ? or ownerUsername like ? or "
						+ "description like ? or content like ?) "
						+ "and parentForumId in (select id from forums where deleted = 0 and "
						+ "(type = 'Public' or type = 'Open'));";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					String content = resultSet.getString("content");
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean pinned = resultSet.getBoolean("pinned");
					boolean locked = resultSet.getBoolean("locked");
					int parentForumId = resultSet.getInt("parentForumId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Topic topic= new Topic(id, name, description, content, 
							ownerUsername, creationDate, pinned, 
							locked, parentForumId, deleted);
					
					topics.add(topic);
					
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
		}

		
		return topics;
		
	}
	
	
		
	
}
