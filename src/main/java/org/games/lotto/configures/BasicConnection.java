package org.games.lotto.configures;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 데이터 커텍션을 종료하지 않고 데이터 소스에 반환하도록 하기 위해서 생성한 클래스
 */
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

    /**
     * 데이터와 관련된 객체들을 종료한다.
     * @param rs
     * @param pstmt
     * @param conn
     */
    public void close(ResultSet rs, PreparedStatement pstmt) {
        if(rs != null){
            try{rs.close();} catch (Exception e2){}
        }
        if(pstmt != null){
            try{pstmt.close();} catch (Exception e2){}
        }
        if(this != null){
            try{this.close();} catch (Exception e2){}
        }
    }
}
