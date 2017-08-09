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
import model.User;

public class ForumDAO {

	private ForumDAO(){}
	
	
	public static boolean insert(Forum forum){
		return insert(forum.getName(), forum.getDescription(), forum.getOwnerUsername(), forum.getCreationDate()
				, forum.isLocked(), forum.getType(), forum.getParentForumId(), forum.isDeleted());
	}
	
	
	public static boolean insert(String name, String description, 
			 String ownerUsername, Date creationDate, boolean locked, String type,
			 int parentForumId, boolean deleted){
		
		boolean success = false;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			connection = ConnectionManager.getConnection();
			
			if(parentForumId==0){
				String sql = "INSERT INTO forums(name, description, ownerUsername, creationDate, "
						+ "locked, type, deleted) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
				
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, name);
				statement.setString(index++, description);
				statement.setString(index++, ownerUsername);
				statement.setDate(index++, new java.sql.Date(creationDate.getTime()));
				statement.setBoolean(index++, locked);
				statement.setString(index++, type);
				statement.setBoolean(index++, deleted);
				
				statement.executeUpdate();
				success=true;
				
				
			}else{
			
			String sql = "INSERT INTO forums(name, description, ownerUsername, creationDate, "
					+ "locked, type, parentForumId, deleted) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, name);
			statement.setString(index++, description);
			statement.setString(index++, ownerUsername);
			statement.setDate(index++, new java.sql.Date(creationDate.getTime()));
			statement.setBoolean(index++, locked);
			statement.setString(index++, type);
			statement.setInt(index++, parentForumId);
			statement.setBoolean(index++, deleted);
			
			statement.executeUpdate();
			success=true;
			}

		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}}
		
		
		return success;
	
}
	
	public static List<Forum> get(String role, int starting_point, int results){
		List<Forum> forums = new ArrayList<Forum>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId is null LIMIT ?, ?";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, starting_point);
			statement.setInt(index++, results);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean locked = resultSet.getBoolean("locked");
				String type = resultSet.getString("type");
				int parentForumId = 0;
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Forum forum= new Forum(id, name, description, ownerUsername, 
						creationDate, locked, type, parentForumId, deleted);
				
				forums.add(forum);
				
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId is null AND type = 'Public' LIMIT ?, ?";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setInt(index++, starting_point);
				statement.setInt(index++, results);
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = 0;
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId is null AND (type = 'Public' OR type = 'Open') LIMIT ?, ?";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setInt(index++, starting_point);
				statement.setInt(index++, results);
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String name = resultSet.getString("name");
					String description = resultSet.getString("description");
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = 0;
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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
//		else if(role.equals("moderator")){
//			
//			try {
//				
//				
//				connection  = ConnectionManager.getConnection();
//				
//				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId is null AND (type = 'Public' OR type = 'Open' OR type = 'Closed') LIMIT ?, ?";
//				
//
//				//sql injection protection 
//				statement = connection.prepareStatement(sql);
//				int index= 1;
//				statement.setInt(index++, starting_point);
//				statement.setInt(index++, results);
//				
//				resultSet = statement.executeQuery();
//				
//				while(resultSet.next()){
//					int id  = Integer.parseInt(resultSet.getString("id"));
//					String name = resultSet.getString("name");
//					String description = resultSet.getString("description");
//					String ownerUsername = resultSet.getString("ownerUsername");
//					Date creationDate = resultSet.getDate("creationDate");
//					boolean locked = resultSet.getBoolean("locked");
//					String type = resultSet.getString("type");
//					int parentForumId = 0;
//					boolean deleted = resultSet.getBoolean("deleted");
//				
//							
//					Forum forum= new Forum(id, name, description, ownerUsername, 
//							creationDate, locked, type, parentForumId, deleted);
//					
//					forums.add(forum);
//					
//				}
//				
//				
//				
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			finally{
//				try {statement.close();} catch (Exception e) {e.printStackTrace();}
//				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
//				try {connection.close();} catch (Exception e) {e.printStackTrace();}
//			}
//			
//		}
		
		
		return forums;
		
	}
	
	
	public static List<Forum> get(int parenForumId, String role, int starting_point, int results){
		List<Forum> forums = new ArrayList<Forum>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId = ? LIMIT ?, ?";
			

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
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean locked = resultSet.getBoolean("locked");
				String type = resultSet.getString("type");
				int parentForumId = resultSet.getInt("parentForumId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Forum forum= new Forum(id, name, description, ownerUsername, 
						creationDate, locked, type, parentForumId, deleted);
				
				forums.add(forum);
				
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId = ? AND type = 'Public' LIMIT ?, ?";
				

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
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = resultSet.getInt("parentForumId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId = ? AND (type = 'Public' OR type = 'Open') LIMIT ?, ?";
				

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
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = resultSet.getInt("parentForumId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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
//		else if(role.equals("moderator")){
//			
//			try {
//				
//				
//				connection  = ConnectionManager.getConnection();
//				
//				String sql = "SELECT * FROM forums WHERE deleted = 0 AND parentForumId = ? AND (type = 'Public' OR type = 'Open' OR type = 'Closed') LIMIT ?, ?";
//				
//
//				//sql injection protection 
//				statement = connection.prepareStatement(sql);
//				int index= 1;
//				statement.setInt(index++, parenForumId);
//				statement.setInt(index++, starting_point);
//				statement.setInt(index++, results);
//				
//				resultSet = statement.executeQuery();
//				
//				while(resultSet.next()){
//					int id  = Integer.parseInt(resultSet.getString("id"));
//					String name = resultSet.getString("name");
//					String description = resultSet.getString("description");
//					String ownerUsername = resultSet.getString("ownerUsername");
//					Date creationDate = resultSet.getDate("creationDate");
//					boolean locked = resultSet.getBoolean("locked");
//					String type = resultSet.getString("type");
//					int parentForumId = resultSet.getInt("parentForumId");
//					boolean deleted = resultSet.getBoolean("deleted");
//				
//							
//					Forum forum= new Forum(id, name, description, ownerUsername, 
//							creationDate, locked, type, parentForumId, deleted);
//					
//					forums.add(forum);
//					
//				}
//				
//				
//				
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			finally{
//				try {statement.close();} catch (Exception e) {e.printStackTrace();}
//				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
//				try {connection.close();} catch (Exception e) {e.printStackTrace();}
//			}
//			
//		}
//		
		
		return forums;
		
	}
	
	
	
	public static boolean lock(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE forums SET locked = 1 WHERE id = ?";
			
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
			
			String sql = "UPDATE forums SET locked = 0 WHERE id = ?";
			
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
		
//		PreparedStatement statement = null;
//		Connection connection = null;
		try {
//			connection = ConnectionManager.getConnection();
//			
//			String sql = "UPDATE forums SET deleted = 1 WHERE id = ?";
//			
//			statement = connection.prepareStatement(sql);
//			int index= 1;
//			statement.setInt(index++, id);
//			success= statement.executeUpdate()==1;
		
		deleteChildren(id);
		deleteTopics(id);
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
//			try {statement.close();} catch (Exception e) {e.printStackTrace();}
//			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return true;
		
	}
	

	public static boolean deleteChildren(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		ArrayList<Integer> childrenIds = new ArrayList<Integer>();
		
		try {
			connection = ConnectionManager.getConnection();
			
			
			String sql = "UPDATE forums SET deleted = 1 WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			statement.close();
			
			if(success){
			sql = "select id from forums WHERE parentForumId = ?";
			
			
			statement = connection.prepareStatement(sql);
			index= 1;
			statement.setInt(index++, id);
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int childId  = resultSet.getInt("id");
				childrenIds.add(childId);
				
			}
			resultSet.close();
			statement.close();
			
			for (Integer childId : childrenIds) {
				sql = "UPDATE forums SET deleted = 1 WHERE id = ?";
				
				statement = connection.prepareStatement(sql);
				index= 1;
				statement.setInt(index++, childId);
				success= statement.executeUpdate()==1;
				if(success){
					deleteChildren(childId);
				}else{
					break;
				}
			}
			}
			else{
				throw new Exception("couldnt delete parent");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception\

			e.printStackTrace();
		}
		finally {
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			}
		
		
		return success;
		
	}
	
	public static boolean deleteTopics(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		ArrayList<Integer> topicsIds = new ArrayList<>();
		try {
			connection = ConnectionManager.getConnection();
		
			String sql = "select id from topics WHERE parentForumId = ?";
		
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int topic_id =resultSet.getInt("id");
				topicsIds.add(topic_id);
				
			}
			statement.close();
			resultSet.close();
			success=true;
			
			sql = "update topics SET deleted = 1 WHERE parentForumId = ?";
			statement = connection.prepareStatement(sql);
			index= 1;
			statement.setInt(index++, id);
			success= statement.executeUpdate()==1;
			statement.close();
			resultSet.close();
			
			for (Integer i : topicsIds) {
				sql = "UPDATE replies SET deleted = 1 WHERE parentTopicId = ?";
				statement = connection.prepareStatement(sql);
				index= 1;
				statement.setInt(index++, id);
				success= statement.executeUpdate()==1;
				statement.close();
				resultSet.close();
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
		
		
		return success;
		
	}
	
	
	public static boolean edit(int id, String name, String description, String type){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE forums SET name = ?, description = ?, type = ? WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, name);
			statement.setString(index++, description);
			statement.setString(index++, type);
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
			
			String sql = "select parentForumId from forums where id = ?";
			
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
	
	public static boolean getStatus(int id){
		boolean locked= false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "select locked from forums where id = ?";
			
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
	
	public static List<String> getDetails(int id){
		List<String> details = new ArrayList<String>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT name, description, creationDate, ownerUsername FROM forums WHERE id = ?";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			resultSet = statement.executeQuery();
			
			
			resultSet.next();
			
			String name = resultSet.getString("name");
			String description = resultSet.getString("description");
			String ownerUsername = resultSet.getString("ownerUsername");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date cd = resultSet.getDate("creationDate");
			String creationDate = df.format(cd);

			details.add(name);
			details.add(description);
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
	
	
	
	public static List<Forum> get(String term, String role){
		List<Forum> forums = new ArrayList<Forum>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT * FROM forums WHERE deleted = 0 AND (name like ? or ownerUsername like ?)";
			

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
				String ownerUsername = resultSet.getString("ownerUsername");
				Date creationDate = resultSet.getDate("creationDate");
				boolean locked = resultSet.getBoolean("locked");
				String type = resultSet.getString("type");
				int parentForumId = 0;
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Forum forum= new Forum(id, name, description, ownerUsername, 
						creationDate, locked, type, parentForumId, deleted);
				
				forums.add(forum);
				
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0 AND type = 'Public' AND (name like ? or ownerUsername like ?)";
				

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
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = 0;
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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
				
				String sql = "SELECT * FROM forums WHERE deleted = 0  AND (type = 'Public' OR type = 'Open') and (name like ? or ownerUsername like ?)";
				

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
					String ownerUsername = resultSet.getString("ownerUsername");
					Date creationDate = resultSet.getDate("creationDate");
					boolean locked = resultSet.getBoolean("locked");
					String type = resultSet.getString("type");
					int parentForumId = 0;
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Forum forum= new Forum(id, name, description, ownerUsername, 
							creationDate, locked, type, parentForumId, deleted);
					
					forums.add(forum);
					
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

		
		return forums;
		
	}
	
	
}
