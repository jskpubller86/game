package org.games.lotto.configures;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicDataSource {

    // 커넥셕은 최대 6개 보유
    BasicConnection[] connections = new BasicConnection[6];


    public BasicConnection getConnection() throws SQLException {
        if(isConnection()){
            // 첫 번째 커넥션을 주고 배열에서 삭제한다.
            BasicConnection conn = connections[0];
            BasicConnection[] copyConnections = new BasicConnection[6];
            System.arraycopy(connections, 1, copyConnections,0, connections.length-1);
            return conn;
        } else {
            DriverManager.registerDriver(new OracleDriver());
            Connection conn =  DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe"
                    , "system"
                    , "oracle"
            );

            return new BasicConnection(conn, this);
        }
    }

    /**
     * 커넥션 반납
     * @param connection
     */
    public void returnConnection(BasicConnection connection){
        for (int i = 0; i < connections.length; i++) {
            if(connections[i] == null){
                connections[i] = connection;
                break;
            }
        }
    }

    private boolean isConnection(){
        boolean rs = false;
        for (BasicConnection connection : connections){
            if(connection != null){
                rs = true;
            }
        }
        return rs;
    }

    public static void close(ResultSet rs, PreparedStatement pstmt){
        close(rs, pstmt, null);
    }
    public static void close(ResultSet rs, PreparedStatement pstmt, BasicConnection conn){
        try{rs.close();} catch (Exception ex){}
        try{pstmt.close();} catch (Exception ex){}
        try{conn.close();} catch (Exception ex){}
    }
}
