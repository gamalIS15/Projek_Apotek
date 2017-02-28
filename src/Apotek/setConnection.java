package Apotek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class setConnection {
    Connection connection;
    
    public setConnection() throws SQLException {
            connection = DriverManager.getConnection("jdbc:"
                    + "mysql://sql6.freemysqlhosting.net:3306/sql6152717","sql6152717","S5ISjzaFKC");
            System.out.println("Berhasil Konek");
    }
}
