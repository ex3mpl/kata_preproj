package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() throws SQLException {
        String command = """
                CREATE TABLE IF NOT EXISTS `userdao`.`users` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(45) NOT NULL,
                         `lastname` VARCHAR(45) NOT NULL,
                         `age` INT(3) NULL,
                PRIMARY KEY (`id`));
                """;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(command);
        }
    }

    public void dropUsersTable() throws SQLException {
        String command = "DROP TABLE IF EXISTS `userdao`.`users`";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(command);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String command = "INSERT INTO `userdao`.`users` (name, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(command)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        }
    }

    public void removeUserById(long id) throws SQLException {
        String command = "DELETE FROM `userdao`.`users` WHERE `id` =?";
        try (PreparedStatement statement = connection.prepareStatement(command)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `userdao`.`users`" )) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id" ));
                user.setName(resultSet.getString("name" ));
                user.setLastName(resultSet.getString("lastname" ));
                user.setAge(resultSet.getByte("age" ));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        String command = "TRUNCATE TABLE `userdao`.`users`";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(command);
        }
    }
}
