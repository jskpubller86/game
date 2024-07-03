package org.games.configure.data;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;

/**
 * 데이터 커넥션을 관리하는 클래스
 * @author jskpubller86
 */
public class BasicDataSource {

    // 커넥셕은 최대 6개 보유
    BasicConnection[] connections = new BasicConnection[6];

    /**
     * 커넥션을 반환
     * @return
     * @throws SQLException
     * @author jskpubller86
     */
    public BasicConnection getConnection() throws SQLException {
        int idx = findConnectionIdx();// 커넥션이 존재하는 배열의 인덱스를 구한다.
        // 커넥션을 발견하면 해당 커넥션을 반환하고 아니라면 새로 커넥션을 생성한다
        if(idx > -1){
            // 첫 번째 커넥션 반화하고 그 커넥션을 삭제한 새로운 배열을 생성
            BasicConnection conn = connections[idx];
            connections[idx] = null;
            return conn;
        } else {
            // 첫번째 커넥션이 없다면 새로 생성해서 반환
            DriverManager.registerDriver(new OracleDriver());
            Connection conn =  DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe"
                    , "game"
                    , "1234"
            );

            return new BasicConnection(conn, this);
        }
    }

    /**
     * 커넥션 반납
     * @param conn 전달받은 커넥션
     * @author jskpubller86
     */
    public void returnConnection(BasicConnection conn){
        // 커넥션 배열에서 가장 먼저 비어 있는 자리를 찾고 전달 받은 커넥션으로 교체한다.
        for (int i = 0; i < connections.length; i++) {
            if(connections[i] == null){
                connections[i] = conn;
                break;
            }
        }
    }

    /**
     * 커넥션을 발견한 자리의 인덱스를 반환하는 함수
     * @return 찾으면 양의 정수를, 못 찾으면 -1을 반환한다.
     * @author jskpubller86
     */
    private int findConnectionIdx(){
        for (int i = 0; i < connections.length; i++) {
            if(connections[i] != null){
               return i;
            }
        }
        return -1;
    }
}
