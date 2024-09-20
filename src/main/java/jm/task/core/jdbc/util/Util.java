package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Util {
    public static Connection getConnection() {
        // подключение с использованием database.properties
        ResourceBundle bundle = ResourceBundle.getBundle("database"); // database.properties

        // переменные хранения данных от значений обращенных ссылок
        String url = bundle.getString("mysql.url");
        String username = bundle.getString("mysql.username");
        String password = bundle.getString("mysql.password");

        // реализация подключения
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
