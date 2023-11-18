package org.games.lotto.configures;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasicConnection {
    private Connection conn;
    private BasicDataSource dataSource;

    public BasicConnection(Connection conn, BasicDataSource dataSource){
        this.conn = conn;
        this.dataSource = dataSource;
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public void close(){
        dataSource.returnConnection(this);
    }
}
