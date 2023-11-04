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
        while (true){
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
                // 번호 정렬
                Arrays.sort(numbers);
                // 이전에 당첨된 번호와 같은지 비교
                if(conn == null){
                    DriverManager.registerDriver(new OracleDriver());
                    conn = DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:xe"
                            , "system"
                            , "oracle"
                    );
                }

                if(pstmt == null){
                    String sql = "select count(*) from game.lotto where (number_1=? or bonus=?) and (number_2=? or bonus=?) and (number_3=? or bonus=?) and (number_4=? or bonus=?) and (number_5=? or bonus=?) and (number_6=? or bonus=?)";
                    pstmt = conn.prepareStatement(sql);
                }

                int pos = 1;
                for (int s = 0; s < numbers.length; s++) {
                    pstmt.setInt(pos++, numbers[s]);
                    pstmt.setInt(pos++, numbers[s]);
                }
                rs = pstmt.executeQuery();
                if(rs.getInt(1) == 0){
                    closeConnection();
                    break;
                } else {
                    try{rs.close();} catch (Exception e){}
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


