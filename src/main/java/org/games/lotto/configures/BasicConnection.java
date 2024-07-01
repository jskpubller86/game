package org.games.lotto.configures;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 데이터 커텍션을 종료하지 않고 데이터 소스에 반환하도록 하기 위해서 생성한 클래스
 * @author jskpubller86
 */
public class BasicConnection {
    private Connection conn;
    private BasicDataSource dataSource;

    /**
     * 생성자
     * @param conn 커넥션
     * @param dataSource 데이터소스
     * @author jskpubller86
     */
    public BasicConnection(Connection conn, BasicDataSource dataSource){
        this.conn = conn;
        this.dataSource = dataSource;
    }

    /**
     * preparedStatement 객체를 반환한다.
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
    public void close(){
        dataSource.returnConnection(this);
    }
}
