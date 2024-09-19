package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Util {
    public static Connection getConnection() {
        // подключение с использованием hibernate.properties
        ResourceBundle bundle = ResourceBundle.getBundle("hibernate"); // hibernate.properties

        // переменные хранения данных от значений обращенных ссылок
        String url = bundle.getString("hibernate.connection.url");
        String username = bundle.getString("hibernate.connection.username");
        String password = bundle.getString("hibernate.connection.password");

        // реализация подключения
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void HibernateConnection() {
        try (SessionFactory sessionFactory = new Configuration().buildSessionFactory()) {
            sessionFactory.openSession();
        }
    }
}
