package org.game.lotto;

import org.game.configure.data.BasicConnection;
import org.game.configure.exceptions.InValidException;
import org.game.configure.interfaces.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

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
    public int[] createNumbers(){
        /**
         * 1. 사용자로부터 첨가할 값을 받는다.
         * 2. 숫자를 생성한다.
         * 3. 기존에 만들어진 숫자가 있는지 찾아 본다.
         * 4. 없다면 배열에 추가한다.
         * 5. 있다면 다시 숫자를 생성한다.
         * 6. 숫자 6개가 완성되면 오름차순으로 정렬한다.
         */

        System.out.println("첨가할 값을 입력해주세요.");
        Scanner in = new Scanner(System.in);
        long salt = in.nextLong();

        int[] numbers = new int[]{0, 0, 0, 0, 0, 0};

        loop: for (int i = 0; i < numbers.length; ) {
            Random random = new Random(salt + System.currentTimeMillis());
            int n = random.nextInt(44)+1;

            for(int j = 0; j < numbers.length; j++){
                if(numbers[j] == n){
                    continue loop;
                }
            }

            numbers[i] = n;
            i++;
        }

        Arrays.sort(numbers);
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

        int pos = 1;
        for (int i = 0; i < numbers.length; i++) {
            pstmt.setInt(pos++, numbers[i]);
            pstmt.setInt(pos++, numbers[i]);
        }
        ResultSet rs = pstmt.executeQuery();

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
