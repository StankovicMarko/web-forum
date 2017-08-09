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
import model.Reply;
import model.Topic;

public class ReplyDAO {

	private ReplyDAO(){}
	
	
	public static boolean insert(Reply reply){
		return insert(reply.getContent(), reply.getOwnerUsername(), reply.getCreationDate()
				, reply.getParentTopicId(), reply.isDeleted());
	}
	
	
	public static boolean insert(String content, 
			 String ownerUsername, Date creationDate, int parentTopicId, boolean deleted){
		
		boolean success = false;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			connection = ConnectionManager.getConnection();
			
			String sql = "INSERT INTO replies(content, ownerUsername, creationDate, "
					+ "parentTopicId, deleted) "
					+ "VALUES(?, ?, ?, ?, ?)";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, content);
			statement.setString(index++, ownerUsername);
			statement.setDate(index++, new java.sql.Date(creationDate.getTime()));
			statement.setInt(index++, parentTopicId);
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
	
//	public static List<Reply> get(String role, int starting_point, int results){
//		List<Reply> replies = new ArrayList<Reply>();
//		ResultSet resultSet = null;
//		PreparedStatement statement = null;
//		Connection connection = null;
//		
//		if(role.equals("admin") || role.equals("moderator") || role.equals("guest") || role.equals("registered")){
//		try {
//			
//			
//			connection  = ConnectionManager.getConnection();
//			
//			String sql = "SELECT * FROM replies WHERE deleted = 0 AND parentForumId is null LIMIT ?, ?";
//			
//
//			//sql injection protection 
//			statement = connection.prepareStatement(sql);
//			int index= 1;
//			statement.setInt(index++, starting_point);
//			statement.setInt(index++, results);
//			
//			resultSet = statement.executeQuery();
//			
//			while(resultSet.next()){
//				int id  = Integer.parseInt(resultSet.getString("id"));
//				String name = resultSet.getString("name");
//				String description = resultSet.getString("description");
//				String ownerUsername = resultSet.getString("ownerUsername");
//				Date creationDate = resultSet.getDate("creationDate");
//				boolean locked = resultSet.getBoolean("locked");
//				String type = resultSet.getString("type");
//				int parentForumId = 0;
//				boolean deleted = resultSet.getBoolean("deleted");
//			
//						
//				Forum forum= new Forum(id, name, description, ownerUsername, 
//						creationDate, locked, type, parentForumId, deleted);
//				
//				forums.add(forum);
//				
//			}
//			
//			
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		finally{
//			try {statement.close();} catch (Exception e) {e.printStackTrace();}
//			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
//			try {connection.close();} catch (Exception e) {e.printStackTrace();}
//		}
//		
//		
//		}
//		return replies;
//		
//	}
	
	
	public static List<Reply> get(int parenTopicId, String role, int starting_point, int results){
		List<Reply> replies = new ArrayList<Reply>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator") || role.equals("guest") || role.equals("registered")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
//			String sql = "SELECT * FROM replies WHERE deleted = 0 AND parentTopicId = ? LIMIT ?, ?";
//			String sql = "SELECT A.id, A.content, A.ownerUsername, A.creationDate, A.parentTopicId, A.deleted, B.picture "
//					+ "FROM replies A, users B "
//					+ "WHERE A.deleted = 0 AND A.parentTopicId = ? and A.ownerUsername = B.username Limit ?, ?;";
			
			String sql = "SELECT A.id, A.content, A.ownerUsername, "
					+ "A.creationDate, A.parentTopicId, A.deleted, B.picture "
					+ "FROM replies A, users B "
					+ "WHERE A.deleted = 0 AND A.parentTopicId = ? and "
					+ "A.ownerUsername = B.username order by id desc limit ?, ?";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, parenTopicId);
			statement.setInt(index++, starting_point);
			statement.setInt(index++, results);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				String ownerPicture = resultSet.getString("picture");
				Date creationDate = resultSet.getDate("creationDate");
				int parentTopicId = resultSet.getInt("parentTopicId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Reply reply= new Reply(id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
				
				replies.add(reply);
				
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
		
		return replies;
		
	}
	
	
	
	public static boolean delete(int id){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
		
			String sql = "UPDATE replies SET deleted = 1 WHERE id = ?";
		
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
	
	
	public static boolean edit(int id, String content){
		boolean success = false;
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "UPDATE replies SET content = ? WHERE id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
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
	
	public static ArrayList<Integer> canEditDelete(String username, int parenTopicId){
		ArrayList<Integer> topicsIds = new ArrayList<Integer>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "select id from replies where ownerUsername in (select username from users where (username=? or role='registered')) and deleted = 0 and parentTopicId = ?";

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, username);
			statement.setInt(index++, parenTopicId);
			
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
	
	public static ArrayList<Integer> canEditDeleteRegi(String username, int parenTopicId){
		ArrayList<Integer> topicsIds = new ArrayList<Integer>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "select id from replies where ownerUsername in (select username from users where username=?) and deleted = 0 and parentTopicId = ?";

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, username);
			statement.setInt(index++, parenTopicId);
			
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
			
			String sql = "select username, role from users where username in (select ownerUsername from replies where id = ?)";

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
	
	public static int getParentId(int id){
		int parentId=0;
		
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionManager.getConnection();
			
			String sql = "select parentTopicId from replies where id = ?";
			
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			
			resultSet = statement.executeQuery();
			
			resultSet.next();
			try {
				parentId = resultSet.getInt("parentTopicId");
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
	
	public static Reply get(int id){
		Reply reply= null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
	
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			
			String sql = "SELECT A.id, A.content, A.ownerUsername, "
					+ "A.creationDate, A.parentTopicId, A.deleted, B.picture "
					+ "FROM replies A, users B "
					+ "WHERE A.deleted = 0 AND A.id = ? and "
					+ "A.ownerUsername = B.username order by id desc";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setInt(index++, id);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int reply_id  = Integer.parseInt(resultSet.getString("id"));
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				String ownerPicture = resultSet.getString("picture");
				Date creationDate = resultSet.getDate("creationDate");
				int parentTopicId = resultSet.getInt("parentTopicId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				 reply= new Reply(reply_id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
				
			
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		
		
		return reply;
		
	}
	
	public static List<Reply> get(String username){
		List<Reply> replies = new ArrayList<Reply>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT A.id, A.content, A.ownerUsername, "
					+ "A.creationDate, A.parentTopicId, A.deleted, B.picture "
					+ "FROM replies A, users B "
					+ "WHERE A.deleted = 0 AND A.ownerUsername = ? and "
					+ "A.ownerUsername = B.username order by id desc";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, username);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				String ownerPicture = resultSet.getString("picture");
				Date creationDate = resultSet.getDate("creationDate");
				int parentTopicId = resultSet.getInt("parentTopicId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Reply reply= new Reply(id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
				
				replies.add(reply);
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {statement.close();} catch (Exception e) {e.printStackTrace();}
			try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
			try {connection.close();} catch (Exception e) {e.printStackTrace();}
		}

		
		return replies;
		
	}
	
	
	public static List<Reply> get(String term, String role){
		List<Reply> replies = new ArrayList<Reply>();
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if(role.equals("admin") || role.equals("moderator")){
		try {
			
			
			connection  = ConnectionManager.getConnection();
			
			String sql = "SELECT A.*, B.picture FROM replies A, users B "
					+ "WHERE A.deleted = 0 AND (A.content like ? or A.ownerUsername like ?) and "
					+ "A.ownerUsername = B.username";
			

			//sql injection protection 
			statement = connection.prepareStatement(sql);
			int index= 1;
			statement.setString(index++, "%"+term+"%");
			statement.setString(index++, "%"+term+"%");
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				int id  = Integer.parseInt(resultSet.getString("id"));
				String content = resultSet.getString("content");
				String ownerUsername = resultSet.getString("ownerUsername");
				String ownerPicture = resultSet.getString("picture");
				Date creationDate = resultSet.getDate("creationDate");
				int parentTopicId = resultSet.getInt("parentTopicId");
				boolean deleted = resultSet.getBoolean("deleted");
			
						
				Reply reply= new Reply(id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
				
				replies.add(reply);
				
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
				
				String sql = "SELECT A.*, B.picture FROM replies A, users B "
						+ "WHERE A.deleted = 0 AND (A.content like ? or A.ownerUsername like ?)"
						+ "and A.ownerUsername = B.username and parentTopicId in "
						+ "(select id from topics where deleted = 0 and parentForumId in "
						+ "(select id from forums where deleted = 0 and type='Public'))";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String content = resultSet.getString("content");
					String ownerUsername = resultSet.getString("ownerUsername");
					String ownerPicture = resultSet.getString("picture");
					Date creationDate = resultSet.getDate("creationDate");
					int parentTopicId = resultSet.getInt("parentTopicId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Reply reply= new Reply(id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
					
					replies.add(reply);
					
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
				
				String sql = "SELECT A.*, B.picture FROM replies A, users B "
						+ "WHERE A.deleted = 0 AND (A.content like ? or A.ownerUsername like ?)"
						+ "and A.ownerUsername = B.username and parentTopicId in "
						+ "(select id from topics where deleted = 0 and parentForumId in "
						+ "(select id from forums where deleted = 0 and (type='Public' or type='Open')))";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String content = resultSet.getString("content");
					String ownerUsername = resultSet.getString("ownerUsername");
					String ownerPicture = resultSet.getString("picture");
					Date creationDate = resultSet.getDate("creationDate");
					int parentTopicId = resultSet.getInt("parentTopicId");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					Reply reply= new Reply(id, content, ownerUsername, ownerPicture ,creationDate, parentTopicId, deleted);
					
					replies.add(reply);
					
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

		
		return replies;
		
	}
	
}

