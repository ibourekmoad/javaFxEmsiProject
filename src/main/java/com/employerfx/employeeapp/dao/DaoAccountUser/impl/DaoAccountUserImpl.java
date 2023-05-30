package com.employerfx.employeeapp.dao.DaoAccountUser.impl;

import com.employerfx.employeeapp.dao.DaoAccountUser.DaoAccountUser;
import com.employerfx.employeeapp.entities.AccountUser;

import java.sql.*;

public class DaoAccountUserImpl implements DaoAccountUser {

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/javaAV";
    private static final String USERNAME = "ubuntu";
    private static final String PASSWORD = "password";

    @Override
    public AccountUser getAccountUser(String name, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM USERACCOUNT WHERE UserName=? AND Password=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("IdUserAccount");
                String username = resultSet.getString("UserName");
                String dbPassword = resultSet.getString("Password");

                AccountUser user = new AccountUser(id,username,dbPassword);
//                user.setId(id);
//                user.setUserName(username);
//                user.setPassword(dbPassword);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
