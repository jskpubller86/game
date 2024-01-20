package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;
import org.games.lotto.configures.BasicDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class Select implements Command {
    /**
     * 로또 번호를 생성한다.
     * @return 로또 번호
     */
    public void execute(BasicConnection conn) throws Exception{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int[] numbers = null;

        while (true){
            boolean isChecked = false;
            numbers = createNumbers();
            // 번호 검증
            try {
                // 번호를 오름차순으로 정렬
                Arrays.sort(numbers);

                // 검증1: 숫자의 합이 250 이하인가?
                isChecked = checkRange(numbers);

                // 검증2 : 기존의 1, 2등 당첨 번호인지 확인
                if(isChecked){
                    isChecked = checkDuplicatedWinNumbers(numbers, conn, pstmt, rs);
                }

                // 검증3 : 중복확률 제거 쿼리
                if(isChecked){
                    if(true == checkPattern(numbers, conn, pstmt, rs)){
                        break;
                    }
                }
            } catch (Exception e){
                throw e;
            } finally {
                BasicDataSource.close(rs, pstmt, conn);
            }
        }

        // 최종 번호 출력
        System.out.printf("생성된 로또번호 : %s %n", Arrays.toString(numbers));
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

    /**
     * 번호의 범위를 검증한다.
     * @param numbers
     * @return
     */
    private boolean checkRange(int[] numbers){
        int temp = 0;
        for (int num :  numbers){
            temp+=num;
        }

        if(60 < temp && temp < 210){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 1등 또는 2등 당첨번호인지 검증한다.
     * @param numbers
     * @param conn
     * @param pstmt
     * @param rs
     * @return
     * @throws SQLException
     */
    private boolean checkDuplicatedWinNumbers(int[] numbers, BasicConnection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        String sql = "select count(*) from game.lotto where (number_1=? or bonus=?) and (number_2=? or bonus=?) and (number_3=? or bonus=?) and (number_4=? or bonus=?) and (number_5=? or bonus=?) and (number_6=? or bonus=?)";
        pstmt = conn.prepareStatement(sql);

        int pos = 1;
        for (int i = 0; i < numbers.length; i++) {
            pstmt.setInt(pos++, numbers[i]);
            pstmt.setInt(pos++, numbers[i]);
        }
        rs = pstmt.executeQuery();

        // 이전에 있던 번호라면 다시 번호를 생성하고 아니라면 종료한다
        rs.next();
        int cnt = rs.getInt(1);
        BasicDataSource.close(rs, pstmt, null);

        if(cnt > 0){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 번호 패턴을 검증한다.
     * @param numbers
     * @param conn
     * @param pstmt
     * @param rs
     * @return
     * @throws SQLException
     */
    private boolean checkPattern(int[] numbers, BasicConnection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        String sql = "select count(*) from game.lotto where"
                + " (number_1="+numbers[0]+" and number_2="+numbers[1]+" and number_3="+numbers[2]+")"
                + " or"
                + " (number_1="+numbers[0]+" and number_3="+numbers[2]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+")"
                + " or"
                + " (number_1="+numbers[0]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")"
                + " or"
                + " (number_2="+numbers[1]+" and number_3="+numbers[2]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+")"
                + " or"
                + " (number_3="+numbers[2]+" and number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")"
                + " or"
                + " (number_4="+numbers[3]+" and number_5="+numbers[4]+" and number_6="+numbers[5]+")";

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        // 적은 확률의 번호라면 다시 번호 생성
        rs.next();
        int cnt = rs.getInt(1);
        BasicDataSource.close(rs, pstmt);

        if(cnt > 0){
            return false;
        } else {
            return true;
        }
    }

}
