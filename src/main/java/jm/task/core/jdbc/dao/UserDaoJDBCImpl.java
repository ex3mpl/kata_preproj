package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() throws SQLException {
        connection.createStatement().execute(
                """
                        CREATE TABLE IF NOT EXISTS `userdao`.`users` (
                                 `id` INT NOT NULL AUTO_INCREMENT,
                                 `name` VARCHAR(45) NOT NULL,
                                 `lastname` VARCHAR(45) NOT NULL,
                                 `age` INT(3) NULL,
                        PRIMARY KEY (`id`));
                        """);
    }

    public void dropUsersTable() throws SQLException {
        connection.createStatement().execute("DROP TABLE IF EXISTS `userdao`.`users`");
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `userdao`.`users` (name, lastname, age) " +
                        "VALUES (?, ?, ?)"
        );
        statement.setString(1, name);
        statement.setString(2, lastName);
        statement.setByte(3, age);
        statement.executeUpdate();
    }

    public void removeUserById(long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM `userdao`.`users` WHERE `id` = ?;"
        );
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `userdao`.`users`")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        connection.createStatement().executeUpdate("TRUNCATE TABLE `userdao`.`users`");
    }
}
