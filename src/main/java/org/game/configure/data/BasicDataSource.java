package org.game.configure.data;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;

/**
 * 데이터 커넥션을 관리하는 클래스
 * @author jskpubller86
 */
public class BasicDataSource {

    /**
     * 커넥셕은 최대 2개 보유
     * 내부 접근만 허용
     */
    public BasicConnection[] conns = new BasicConnection[2];
    public int cnt=0;

    /**
     * 커넥션을 반환
     * @return BasicConnection
     * @throws SQLException
     * @author jskpubller86
     */
    public BasicConnection getConnection() throws SQLException {
        /**
         * 1. 사용할 수 있는 커넥션이 있는지 찾는다.
         * 2. 없다면 커넥션이 2개 생성되었는지 확인한다.
         * 3. 2개가 생서되지 않았다면 새로 생성하고 아니라면 반환할 커넥션이 없다고 예외를 던진다.
         */

        BasicConnection conn = null;
        for (int i = 0; i < conns.length; i++) {
            if(conns[i] != null){
                conn = conns[i];
                conns[i] = null;
                break;
            }
        }

        if(conn == null){
            if(cnt == 2){
                throw new SQLException();
            } else {
                DriverManager.registerDriver(new OracleDriver());
                Connection rConn =  DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe"
                        , "game"
                        , "1234"
                );
                cnt++;
                return new BasicConnection(rConn, this);
            }
        } else {
            return conn;
        }
    }

    /**
     * 커넥션 반납
     * @param conn 전달받은 커넥션
     * @author jskpubller86
     */
    public void returnConnection(BasicConnection conn) throws SQLException{
        int cnt=0;
        for (int i = 0; i < conns.length; i++) {
            if(conns[i] == null){
                conns[i] = conn;
                break;
            } else {
                cnt++;
            }
        }

        if(cnt==2){
            throw new SQLException("커넥션 보관소에 공간이 없습니다.");
        }
    }
}
