package org.game.annuity;

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
public class Annuity implements Game {
    private BasicConnection conn = null;
    @Override
    public Game setConnection(BasicConnection conn){
        this.conn = conn;
        return this;
    }


    @Override
    public void execute() throws Exception{
        // 로또 번호 생성
        String numbers = null;

        // 번호 6개가 모두 뽑힐 때까지 반복한다.
        while (numbers == null){
            numbers = createNumbers();
            // 번호 검증
            try {
                // 검증1
                validateWinNumbers(numbers);
            } catch (InValidException e){
                numbers = null;
            } catch (Exception e){
                throw e;
            }
        }

        // 최종 번호 출력
        System.out.printf("생성된 연금복권 번호 : %s조 %s %n", createGroup(), numbers);
    }

    /**
     * 숫자를 생성하는 함수
     * @return 숫자 6자리
     * @author jskpubller86
     */
    private String createNumbers(){
        int[] numbers = new int[]{0, 0, 0, 0, 0, 0};
        // 번호 생성
        for (int i = 0; i < numbers.length; i++) {
            // 숫자 생성
            Random random = new Random();
            int number = random.nextInt(9);

            // 이전에 있었던 숫자인지 확인 후 없다면 추가
            numbers[i] = number;
        }
        return Arrays.toString(numbers);
    }

    /**
     * 그룹 번호 생성
     * @return 그룹 번호
     * @author jskpubller86
     */
    private int createGroup(){
        // 번호 생성
        return new Random().nextInt(4)+1;
    }

    /**
     * 1등 또는 보너스 당첨 번호인지 검증하는 함수
     * @param numbers 생성된 번호
     * @throws SQLException, InValidException
     * @author jskpubller86
     */
    private void validateWinNumbers(String numbers) throws SQLException, InValidException {
        String sql = "select count(*) from game.annuity where numbers=? or bonus=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // 쿼리 조건에 생성된 번호를 대입
        pstmt.setString(1, numbers);
        pstmt.setString(2, numbers);

        ResultSet rs = pstmt.executeQuery();

        // 이전에 있던 번호이면 true, 없는 번호이면 false를 반환한다.
        rs.next();
        try{
            if(rs.getInt(1) > 0){
                System.out.println("중복된 번호 : " + numbers);
                throw new InValidException();
            } else {
                System.out.println("유효한 번호 : " + numbers);
            }
        } finally {
            // 데이터 관련된 객체들을 종료
            try{rs.close();} catch (SQLException ex){}
            try{pstmt.close();} catch (SQLException ex){}
        }
    }
}
