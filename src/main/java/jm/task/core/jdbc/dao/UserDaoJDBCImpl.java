package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    // logger init
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);

    public UserDaoJDBCImpl() {
        // empty for correct work entity
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
        try (Connection connection = getConnection();
             Statement statement = connection.prepareStatement(command)) {

            connection.setAutoCommit(false);
            try {
                statement.executeUpdate(command);
                connection.commit();
                logger.info("Таблица users успешно создана или уже существовала.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Ошибка при создании таблицы users, транзакция возвращена");
                throw e;
            }
        }
    }

    public void dropUsersTable() throws SQLException {
        String command = "DROP TABLE IF EXISTS `userdao`.`users`";
        try (Connection connection = getConnection();
             Statement statement = connection.prepareStatement(command)) {

            connection.setAutoCommit(false);
            try {
                statement.executeUpdate(command);
                connection.commit();
                logger.info("Таблица users успешно удалена.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Ошибка при удалении таблицы users, транзакция возвращена");
                throw e;
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String command = "INSERT INTO `userdao`.`users` (name, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(command)) {

            connection.setAutoCommit(false); // disable auto-commit for enable transactions
            try {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.executeUpdate();
                connection.commit(); // commit transaction if all good
                logger.info("Пользователь {} {} (возраст {}) успешно сохранен.",
                        name, lastName, age);
            } catch (SQLException e) {
                connection.rollback(); // rollback transaction in case of error
                logger.error("Ошибка при сохранении пользователя {} {} (возраст {}), транзакция возвращена.",
                        name, lastName, age);
                throw e; // РЕАЛИЗУЙ ЛОГИРОВАНИЕ
            }
        }
    }

    public void removeUserById(long id) throws SQLException {
        String command = "DELETE FROM `userdao`.`users` WHERE `id` =?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(command)) {

            connection.setAutoCommit(false);
            try {
                statement.setLong(1, id);
                statement.executeUpdate();
                connection.commit();
                logger.info("Пользователь с ID {} успешно удален", id);
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Ошибка при удалении пользователя с ID {}, транзакция возвращена", id);
                throw e;
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM `userdao`.`users`")) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            logger.info("Получены все пользователи, их кол-во - {}", users.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении всех пользователей");
            throw e;
        }
        return users;
    }

    public void cleanUsersTable() throws SQLException {
        String command = "TRUNCATE TABLE `userdao`.`users`";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);
            try {
                statement.executeUpdate(command);
                connection.commit();
                logger.info("Таблица users успешно очищена.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Ошибка при очистке таблицы users, транзакция возвращена");
                throw e;
            }
        }
    }
}
