package org.game;

import org.game.configure.data.BasicConnection;
import org.game.configure.data.BasicDataSource;
import org.game.lotto.Lotto;

import java.util.Arrays;

public class TestMain {
    public static void main(String[] args) {
        testRandomNumbers();
    }

    /**
     * 랜덤 숫자 생성 검사
     * @author jskpubller86
     */
    private static void testRandomNumbers(){
        /**
         * 1. 랜덤한 숫자가 중복없이 6개가 만들어지는지 확인한다.
         */
        Lotto lotto = new Lotto();
        System.out.println(Arrays.toString(lotto.createNumbers()));
    }

    /**
     * 커넥션 연결 검사
     * @author jskpubller86
     */
    private static void testConnection(){
        /**
         * 1. 커넥션을 생성한다.
         * 2. 커넥션 보관소에 반환한다.
         * 3. 보관소를 확인한다.
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