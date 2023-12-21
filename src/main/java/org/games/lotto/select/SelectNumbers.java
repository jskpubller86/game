package org.games.lotto.select;

import org.games.lotto.configures.BasicConnection;
import org.games.lotto.configures.Context;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Random;

public class SelectNumbers extends Context {
    /**
     * 로또 번호를 생성한다.
     * @return 로또 번호
     */
    public int[] selectNumbers() throws Exception{
        PreparedStatement pstmt = null;
        BasicConnection conn = null;
        ResultSet rs = null;
        int[] numbers = null;

        while (true){
            boolean isChecked = false;
            numbers = createNumbers();
            // 번호 검증
            try {
                if(conn == null){
                    conn = ds.getConnection();
                }

                // 번호를 오름차순으로 정렬
                Arrays.sort(numbers);

                // 검증1: 숫자의 합이 250 이하인가?
                int temp = 0;
                for (int num :  numbers){
                    temp+=num;
                }
                if(60 < temp && temp < 210){
                    isChecked = true;
                }

                // 검증2 : 기존의 1, 2등 당첨 번호인지 확인
                if(isChecked){

                    String sql = "select count(*) from game.lotto where (number_1=? or bonus=?) and (number_2=? or bonus=?) and (number_3=? or bonus=?) and (number_4=? or bonus=?) and (number_5=? or bonus=?) and (number_6=? or bonus=?)";
                    pstmt = conn.prepareStatement(sql);

                    int pos = 1;
                    for (int i = 0; i < numbers.length; i++) {
                        pstmt.setInt(pos++, numbers[i]);
                        pstmt.setInt(pos++, numbers[i]);
                    }

                    System.out.println("생성된 쿼리문:");
                    System.out.println(pstmt);
                    rs = pstmt.executeQuery();

                    // 이전에 있던 번호라면 다시 번호를 생성하고 아니라면 종료한다
                    rs.next();
                    if(rs.getInt(1) > 0){
                        isChecked = false;
                    }
                    close(rs, pstmt, null);
                }

                // 검증3 : 중복확률 제거 쿼리
                if(isChecked){
                    String sql = "select count(*) from game.lotto where"
                            + " (number_1="+numbers[0]+" and number_2="+numbers[1]+" and number_3="+numbers[2]+")"
                            + " or"
                            + " (number_1="+numbers[0]+" and number_2="+numbers[1]+" and number_3="+numbers[2]+" and  (number_4="+numbers[3]+" or (number_4="+numbers[3]+" and  number_5="+numbers[4]+") or (number_4="+numbers[3]+" and  number_5="+numbers[4]+" and number_6="+numbers[5]+")))"
                            + " or"
                            + " (number_1="+numbers[0]+" and number_3="+numbers[2]+" and number_4="+numbers[3]+" and (number_5="+numbers[4]+" or (number_5="+numbers[4]+" and number_6="+numbers[5]+")))"
                            + " or"
                            + " (number_1="+numbers[0]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")"
                            + " or"
                            + " (number_2="+numbers[1]+" and number_3="+numbers[2]+" and number_4="+numbers[3]+" and (number_5="+numbers[4]+" or (number_5="+numbers[4]+" and number_6="+numbers[5]+")))"
                            + " or"
                            + " (number_3="+numbers[2]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")"
                            + " or"
                            + " (number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")";

                    System.out.println("생성된 쿼리문:");
                    System.out.println(sql);

                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();

                    // 적은 확률의 번호라면 다시 번호 생성
                    rs.next();
                    if(rs.getInt(1) > 0){
                        isChecked = false;
                        close(rs, pstmt, null);
                    } else {
                        break;
                    }
                }
            } catch (Exception e){
                throw e;
            } finally {
                close(rs, pstmt, conn);
            }
        }

        // 최종 번호
        return numbers;
    }

    /**
     * 데이터와 관련된 객체들을 종료한다.
     * @param rs
     * @param pstmt
     * @param conn
     */
    private void close(ResultSet rs, PreparedStatement pstmt, BasicConnection conn) {
        if(rs != null){
            try{rs.close();} catch (Exception e2){}
        }
        if(pstmt != null){
            try{pstmt.close();} catch (Exception e2){}
        }
        if(conn != null){
            try{conn.close();} catch (Exception e2){}
        }
    }

    /**
     * 숫자를 생성한다.
     * @return
     */
    private int[] createNumbers(){
        int[] numbers = new int[]{50, 50, 50, 50, 50, 50};
        // 번호 생성
        for (int i = 0; i < numbers.length; ) {
            // 숫자 생성
            Random random = new Random();
            int number = random.nextInt(45);
            System.out.printf("generated number: %d %n", number);

            // 이전에 있었던 숫자인지 확인 후 없다면 추가
            int matchNumber = Arrays.binarySearch(numbers, number);
            if (matchNumber < 0) {
                numbers[i] = number;
                Arrays.sort(numbers);
                i++;
            }
        }
        return numbers;
    }

}
