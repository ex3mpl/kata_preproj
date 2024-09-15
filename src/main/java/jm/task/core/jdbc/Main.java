package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserServiceImpl userSi = new UserServiceImpl();
        userSi.createUsersTable();

        /*userSi.saveUser("Name1", "LastName1", (byte) 20);
        userSi.saveUser("Name2", "LastName2", (byte) 25);
        userSi.saveUser("Name3", "LastName3", (byte) 31);
        userSi.saveUser("Name4", "LastName4", (byte) 38);

        userSi.removeUserById(1);
        userSi.getAllUsers();
        userSi.cleanUsersTable();
        userSi.dropUsersTable();*/
    }
}
