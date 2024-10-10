package org.game;

import org.game.configure.data.BasicConnection;
import org.game.configure.data.BasicDataSource;

import java.util.Arrays;

public class TestMain {
    public static void main(String[] args) {
        /**
         * 1. 커넥션을 두 개 생성한다.
         * 2. 커넥션 2개는 사용 중이므로 커넥션 보관소에 커넥션이 없는 지 확인한다.
         */
        try {
            BasicDataSource ds = new BasicDataSource();
            BasicConnection conn = ds.getConnection();
            BasicConnection conn1 = ds.getConnection();
            System.out.println(ds.cnt);
            conn.close();
            conn1.close();
            conn = null;
            conn = ds.getConnection();
            System.out.println(conn);
//            System.out.println("커넥션이 2개 생성됨");

            System.out.println(Arrays.toString(ds.conns));
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}