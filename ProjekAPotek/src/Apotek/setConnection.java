package Apotek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class setConnection {
    Connection connection;
    
    public setConnection() throws SQLException {
            connection = DriverManager.getConnection("jdbc:"
                    + "mysql://31.220.110.131/u573612604_008","u573612604_008","timagent008");
            System.out.println("Berhasil Konek");
    }
}
