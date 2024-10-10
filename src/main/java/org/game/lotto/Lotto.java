package org.game.lotto;

import org.game.configure.data.BasicConnection;
import org.game.configure.exceptions.InValidException;
import org.game.configure.interfaces.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

/**
 * 로또 번호 선택 명령 클래스
 * @author jskpubller86
 */
public class Lotto implements Game {
    private BasicConnection conn = null;
    @Override
    public Game setConnection(BasicConnection conn){
        this.conn = conn;
        return this;
    }

    @Override
    public void execute() throws Exception{
        /**
         * 1. 번호를 생성한다.
         * 2. 1등 또는 2등 당첨번호인지 검증한다.
         * 3. 검증을 통과하면 당첨번호를 확정한다.
         */
        int[] numbers = null;

        while (numbers == null){
            numbers = createNumbers();
            try {
                Arrays.sort(numbers);
                validateWinNumbers(numbers);
            } catch (InValidException e){
                numbers = null;
            } catch (Exception e){
                throw e;
            }
        }

        // 최종 번호 출력
        System.out.printf("생성된 로또번호 : %s %n", Arrays.toString(numbers));
    }

    /**
     * 당첨번호를 생성하는 함수
     * @return 6개의 숫자 배열
     * @author jskpubller86
     */
    private int[] createNumbers(){
        /**
         * 1. 랜덤 숫자를 생성
         */
        int[] numbers = new int[]{0, 0, 0, 0, 0, 0};

        for (int i = 0; i < numbers.length; ) {
            Random random = new Random();
            int number = random.nextInt(44)+1;

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
     * 1등 또는 2등 당첨 번호인지 검증하는 함수
     * @param numbers 생성된 번호
     * @return 검증 결과
     * @throws SQLException
     * @author jskpubller86
     */
    private void validateWinNumbers(int[] numbers) throws SQLException, InValidException {
        String sql = "select count(*) from game.lotto where (no1=? or bonus=?) and (no2=? or bonus=?) and (no3=? or bonus=?) and (no4=? or bonus=?) and (no5=? or bonus=?) and (no6=? or bonus=?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // 쿼리 조건에 생성된 번호를 대입
        int pos = 1;
        for (int i = 0; i < numbers.length; i++) {
            pstmt.setInt(pos++, numbers[i]);
            pstmt.setInt(pos++, numbers[i]);
        }
        ResultSet rs = pstmt.executeQuery();

        // 이전에 있던 번호이면 true, 없는 번호이면 false를 반환한다.
        rs.next();

        try{
            if(rs.getInt(1) > 0){
                System.out.println("중복된 번호 : " + Arrays.toString(numbers));
                throw new InValidException();
            } else {
                System.out.println("유효한 번호 : " + Arrays.toString(numbers));
            }
        } finally {
            // 데이터 관련된 객체들을 종료
            try{rs.close();} catch (SQLException ex){}
            try{pstmt.close();} catch (SQLException ex){}
        }
    }
}
