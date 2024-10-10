package org.game.configure.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 커넥션이 종료되지 않고 유지될 수 있게 하기 위해서 만든 프록시 커넥션
 * @author jskpubller86
 */
public class BasicConnection {
    private Connection conn;
    private BasicDataSource ds;

    /**
     * 생성자
     * @param conn 커넥션
     * @param ds 데이터소스
     * @author jskpubller86
     */
    public BasicConnection(Connection conn, BasicDataSource ds){
        this.conn = conn;
        this.ds = ds;
    }

    /**
     * 실제 커넥션에서 preparedStatement 객체를 반환한다.
     * @param query 실행할 쿼리문
     * @return preparedStatement
     * @throws SQLException
     * @author jskpubller86
     */
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    /**
     * 커넥션을 데이터소스에 반환한다.
     * @author jskpubller86
     */
    public void close() throws SQLException{
        ds.returnConnection(this);
    }
}
