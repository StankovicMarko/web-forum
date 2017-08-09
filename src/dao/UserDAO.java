package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Forum;
import model.Reply;
import model.User;


public class UserDAO {

	private UserDAO() { }
		
		
		
		public static boolean insert(User user){
			return insert(user.getUsername(), user.getPassword(), 
					 user.getName(), user.getSurName(), user.getMail(), user.getDateReg()
					, user.getRole(), user.isBanned(), user.getPicture(), user.isDeleted());
		}
		
		
		public static boolean insert(String username, String password, 
				 String name, String surName, String mail, Date date, String role, boolean banned,
				 String picture, boolean deleted){
			
			boolean success = false;
			PreparedStatement statement = null;
			Connection connection = null;
			
			try {
				
				connection = ConnectionManager.getConnection();
				
				String sql = "INSERT INTO users(username, password, name, surName, "
						+ "mail, dateReg, role, banned, picture, deleted) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, username);
				statement.setString(index++, password);
				statement.setString(index++, name);
				statement.setString(index++, surName);
				statement.setString(index++, mail);
				statement.setDate(index++, new java.sql.Date(date.getTime()));
				statement.setString(index++, role);
				statement.setBoolean(index++, banned);
				statement.setString(index++, picture);
				statement.setBoolean(index++, deleted);
				
				
				statement.executeUpdate();
				success=true;
				

			} catch (Exception e) {
				// TODO: handle exception\
				success=false;
//				e.printStackTrace();
			}
			finally {
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}}
			
			
			return success;
		
	}
		
		
		public static User get(String username, String password){
			User user = null;
			
			ResultSet resultSet = null;
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection  = ConnectionManager.getConnection();
				
				
				String sql = "SELECT id, name, surname, mail, dateReg, role, banned, picture, deleted FROM users WHERE username = ? AND password = ?";
				
				statement = connection.prepareStatement(sql);
				int index = 1;
				statement.setString(index++, username);
				statement.setString(index++, password);
				resultSet = statement.executeQuery();
				
				resultSet.next();
				int id =  resultSet.getInt("id");
//				String usn= resultSet.getString("username");
//				String psw = resultSet.getString("password");
				String name = resultSet.getString("name");
				String surName= resultSet.getString("surName");
				String mail= resultSet.getString("mail");
				Date dateReg = resultSet.getDate("dateReg");
				String role= resultSet.getString("role");
				boolean banned = resultSet.getBoolean("banned");
				String picture= resultSet.getString("picture");
				boolean deleted = resultSet.getBoolean("deleted");
				
				user = new User(id, username, password, name, surName, mail, dateReg, role, banned, picture, deleted);
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
			return user;
			
			
		}
		
		public static User get(User user){
			return get(user.getUsername(), user.getPassword());
			
		}
	
		
		public static String getPicture(String username){
			String picture= "";
			ResultSet resultSet = null;
			PreparedStatement statement = null;
			Connection connection = null;
			
			try {
				
				
				connection  = ConnectionManager.getConnection();
				
				String sql = "SELECT picture FROM users WHERE username = ?";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, username);
				
				resultSet = statement.executeQuery();
				
				resultSet.next();
				picture = resultSet.getString("picture");
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
			return picture;
			
		}
		
		
		public static User get(String username){
			User user = null;
			
			ResultSet resultSet = null;
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection  = ConnectionManager.getConnection();
				
				
				String sql = "SELECT * FROM users WHERE username = ? AND deleted = 0";
				
				statement = connection.prepareStatement(sql);
				int index = 1;
				statement.setString(index++, username);
				resultSet = statement.executeQuery();
				
				resultSet.next();
				int id =  resultSet.getInt("id");
				String usn= resultSet.getString("username");
				String psw = resultSet.getString("password");
				String name = resultSet.getString("name");
				String surName= resultSet.getString("surName");
				String mail= resultSet.getString("mail");
				Date dateReg = resultSet.getDate("dateReg");
				String role= resultSet.getString("role");
				boolean banned = resultSet.getBoolean("banned");
				String picture= resultSet.getString("picture");
				boolean deleted = resultSet.getBoolean("deleted");
				
				user = new User(id, usn, psw, name, surName, mail, dateReg, role, banned, picture, deleted);
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
			return user;
			
			
		}
		
		
		public static List<User> get(){
			List<User> users = new ArrayList<User>();
			ResultSet resultSet = null;
			PreparedStatement statement = null;
			Connection connection = null;
			
			try {
				
				
				connection  = ConnectionManager.getConnection();
				
				String sql = "SELECT * from users where deleted = 0";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String username = resultSet.getString("username");
					String password = resultSet.getString("password");
					String name = resultSet.getString("name");
					String surName = resultSet.getString("surName");
					String mail = resultSet.getString("mail");
					Date dateReg = resultSet.getDate("dateReg");
					String role = resultSet.getString("role");
					boolean banned = resultSet.getBoolean("banned");
					String picture = resultSet.getString("picture");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					User user= new User(id, username, password, name,
							surName, mail, dateReg, role, banned, picture, deleted);
					
					users.add(user);
					
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}

			
			return users;
			
		}
		
		
		public static boolean ban(int id){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				
				String sql = "UPDATE users SET banned = 1 WHERE id = ?";
				
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
		
		public static boolean unban(int id){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				
				String sql = "UPDATE users SET banned = 0 WHERE id = ?";
				
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
				
				String sql = "UPDATE users SET deleted = 1 WHERE id = ?";
				
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
		
		
		public static boolean edit(int id, String psw, String mail, String role){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				String sql="";
			
				if(psw.equals("")){
					
					 sql= "UPDATE users SET mail = ?, role = ? WHERE id = ?";
					 statement = connection.prepareStatement(sql);
						int index= 1;
						statement.setString(index++, mail);
						statement.setString(index++, role);
						statement.setInt(index++, id);
						
						
						success= statement.executeUpdate()==1;
				}else{
				
					sql = "UPDATE users SET password = ?, mail = ?, role = ? WHERE id = ?";
					statement = connection.prepareStatement(sql);
					int index= 1;
					statement.setString(index++, psw);
					statement.setString(index++, mail);
					statement.setString(index++, role);
					statement.setInt(index++, id);
					
					
					success= statement.executeUpdate()==1;
				
				}
				
				
				
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
		
		public static boolean updateName(String usn, String name, String surName){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				
					String sql = "UPDATE users SET name = ?, surName = ? WHERE username = ?";
					statement = connection.prepareStatement(sql);
					int index= 1;
					statement.setString(index++, name);
					statement.setString(index++, surName);
					statement.setString(index++, usn);
					
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
		
		public static boolean updatePass(String usn, String pass){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				
					String sql = "UPDATE users SET password = ? WHERE username = ?";
					statement = connection.prepareStatement(sql);
					int index= 1;
					statement.setString(index++, pass);
					statement.setString(index++, usn);
					
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
		
		
		public static boolean changeAvatar(String usn, String fn){
			boolean success = false;
			
			PreparedStatement statement = null;
			Connection connection = null;
			try {
				connection = ConnectionManager.getConnection();
				
					String sql = "UPDATE users SET picture = ? WHERE username = ?";
					statement = connection.prepareStatement(sql);
					int index= 1;
					statement.setString(index++, fn);
					statement.setString(index++, usn);
					
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
		
		
		public static List<User> get(String term, boolean search){
			List<User> users = new ArrayList<User>();
			ResultSet resultSet = null;
			PreparedStatement statement = null;
			Connection connection = null;
			
			try {
				
				
				connection  = ConnectionManager.getConnection();
				
				String sql = "SELECT * FROM users WHERE deleted = 0 AND (username like ? or mail like ? or role like ?)";
				

				//sql injection protection 
				statement = connection.prepareStatement(sql);
				int index= 1;
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				statement.setString(index++, "%"+term+"%");
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					int id  = Integer.parseInt(resultSet.getString("id"));
					String username = resultSet.getString("username");
					String password = resultSet.getString("password");
					String name = resultSet.getString("name");
					String surName = resultSet.getString("surName");
					String mail = resultSet.getString("mail");
					Date dateReg = resultSet.getDate("dateReg");
					String role = resultSet.getString("role");
					boolean banned = resultSet.getBoolean("banned");
					String picture = resultSet.getString("picture");
					boolean deleted = resultSet.getBoolean("deleted");
				
							
					User user= new User(id, username, password, name,
							surName, mail, dateReg, role, banned, picture, deleted);
					
					users.add(user);
					
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally{
				try {statement.close();} catch (Exception e) {e.printStackTrace();}
				try {resultSet.close();} catch (Exception e) {e.printStackTrace();}
				try {connection.close();} catch (Exception e) {e.printStackTrace();}
			}
			
			
		
			
			return users;
			
		}
		
}
