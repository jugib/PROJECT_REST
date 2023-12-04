
package huy.java.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	private String jdbcURL = "jdbc:mariadb://mariadb.vamk.fi/e2001349_project";
	private String jdbcUserName = "e2001349";
	private String jdbcPassword = "ZAYsTnNcew8";

	// Constructors
	public UserDAO(String url, String userName, String password) {
		this.jdbcURL = url;
		this.jdbcUserName = userName;
		this.jdbcPassword = password;
	}

	public UserDAO() {
	}

	private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM students";
	private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE studentID=?";
	private static final String INSERT_USER_QUERY = "INSERT INTO users (id, firstname, lastname) VALUES (?, ?, ?)";
	private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";

	protected Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void insertUser(User user) {
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT_USER_QUERY);) {
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getUserID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS_QUERY);) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String id = rs.getString(3);
                String email = rs.getString(4);

                users.add(new User(firstName, lastName, id, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User selectUserByID(String id) {
        User user = null;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_ID);) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");

                user = new User(firstName, lastName, id, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean deleteUser(String id) {
        boolean rowDeleted = false;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(DELETE_USER_QUERY);) {
            ps.setString(1, id);
            rowDeleted = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }
}


