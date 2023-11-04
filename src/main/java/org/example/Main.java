package org.example;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class Main {
    static Connection conn = null;
    static PreparedStatement pstmt = null;
    static ResultSet rs = null;
    public static void main(String[] args) {
        int[] numbers = null;
        lotto: while (true){
            // 번호 생성
            numbers = new int[6];
            for (int i = 0; i < numbers.length; ) {
                // 숫자 생성
                Random random = new Random();
                int number = random.nextInt(45);
                System.out.printf("generated number: %d %n", number);

                // 이전에 있었던 숫자인지 확인 후 없다면 추가
                int matchNumber = Arrays.binarySearch(numbers, number);
                if(matchNumber < 0){
                    numbers[i] = number;
                    i++;
                }
            }

            // 번호 검증
            try {
                // 번호를 오름차순으로 정렬
                Arrays.sort(numbers);
                String sql = null;

                if(conn == null){
                    DriverManager.registerDriver(new OracleDriver());
                    conn = DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:xe"
                            , "system"
                            , "oracle"
                    );
                }

                sql = "select count(*) from game.lotto where (number_1=? or bonus=?) and (number_2=? or bonus=?) and (number_3=? or bonus=?) and (number_4=? or bonus=?) and (number_5=? or bonus=?) and (number_6=? or bonus=?)";
                pstmt = conn.prepareStatement(sql);

                int pos = 1;
                for (int s = 0; s < numbers.length; s++) {
                    pstmt.setInt(pos++, numbers[s]);
                    pstmt.setInt(pos++, numbers[s]);
                }

                rs = pstmt.executeQuery();
                // 이전에 있던 번호라면 다시 번호를 생성하고 아니라면 종료한다
                if(rs.getInt(1) > 0){
                    try{pstmt.close();} catch (Exception e){}
                    try{rs.close();} catch (Exception e){}
                    continue lotto;
                } else {
                    try{pstmt.close();} catch (Exception e){}
                    try{rs.close();} catch (Exception e){}
                }

                // 중복확률 제거 쿼리
                sql = "select count(*) from  LOTTO where"
                    + " (number_1=? and number_2=? and number_3=? and  (number_4=? or (number_4=? and  number_5=?) or (number_4=? and  number_5=? and number_6=?)))"
                    + " or"
                    + " (number_1=? and number_3=? and number_4=? and (number_5=? or (number_5=? and number_6=?)))"
                    + " or"
                    + " (number_1=? and number_4=? and number_5=? and number_6=?)"
                    + " or"
                    + " (number_2=? and number_3=? and number_4=? and (number_5=? or (number_5=? and number_6=?)))"
                    + " or"
                    + " (number_3=? and number_4=? and number_5=? and number_6)";

                pstmt = conn.prepareStatement(sql);
                //1번
                pstmt.setInt(1, numbers[0]);
                pstmt.setInt(10, numbers[0]);
                pstmt.setInt(16, numbers[0]);
                //2번
                pstmt.setInt(2, numbers[1]);
                pstmt.setInt(19, numbers[1]);
                //3번
                pstmt.setInt(3, numbers[2]);
                pstmt.setInt(11, numbers[2]);
                pstmt.setInt(20, numbers[2]);
                pstmt.setInt(26, numbers[2]);
                //4번
                pstmt.setInt(4, numbers[3]);
                pstmt.setInt(5, numbers[3]);
                pstmt.setInt(7, numbers[3]);
                pstmt.setInt(12, numbers[3]);
                pstmt.setInt(17, numbers[3]);
                pstmt.setInt(22, numbers[3]);
                pstmt.setInt(27, numbers[3]);
                //5번
                pstmt.setInt(6, numbers[4]);
                pstmt.setInt(8, numbers[4]);
                pstmt.setInt(13, numbers[4]);
                pstmt.setInt(14, numbers[4]);
                pstmt.setInt(18, numbers[4]);
                pstmt.setInt(23, numbers[4]);
                pstmt.setInt(24, numbers[4]);
                pstmt.setInt(28, numbers[4]);
                //6번
                pstmt.setInt(9, numbers[5]);
                pstmt.setInt(15, numbers[5]);
                pstmt.setInt(19, numbers[5]);
                pstmt.setInt(26, numbers[5]);
                pstmt.setInt(30, numbers[5]);

                rs = pstmt.executeQuery();

                if(rs.getInt(1) > 0){
                    try{pstmt.close();} catch (Exception e){}
                    try{rs.close();} catch (Exception e){}
                    continue lotto;
                } else {
                    closeConnection();
                    break;
                }

            } catch (Exception e){
                closeConnection();
                break;
            }
        }

        // 최종 번호
        System.out.println(Arrays.toString(numbers));
    }

    private static void closeConnection() {
        try{rs.close();} catch (Exception e2){}
        try{pstmt.close();} catch (Exception e2){}
        try{conn.close();} catch (Exception e2){}
    }
}


