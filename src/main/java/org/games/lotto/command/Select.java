package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;
import org.games.lotto.configures.BasicDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

/**
 * 로또 번호 선택 명령 클래스
 * @author jskpubller86
 */
public class Select implements Command {
    private BasicConnection conn = null;
    @Override
    public void setConnection(BasicConnection conn){
        this.conn = conn;
    }


    @Override
    public void execute() throws Exception{
        // 로또 번호 생성
        int[] numbers = null;

        // 번호 6개가 모두 뽑힐 때까지 반복한다.
        while (numbers == null){
            boolean isValid = false;
            numbers = createNumbers();
            // 번호 검증
            try {
                // 번호를 오름차순으로 정렬
                Arrays.sort(numbers);

                // 검증1: 숫자의 합이 250 이하인가?
                if(validateMinMax(numbers)){
                    isValid = true;
                }

                // 검증2 : 기존의 1, 2등 당첨 번호인지 확인
                if(isValid && !validateWinNumbers(numbers)){
                    isValid = false;
                }

                if(!isValid){
                    numbers = null;
                }
            } catch (Exception e){
                throw e;
            }
        }

        // 최종 번호 출력
        System.out.printf("생성된 로또번호 : %s %n", Arrays.toString(numbers));
    }

    /**
     * 숫자를 생성하는 함수
     * @return 6개의 숫자 배열
     * @author jskpubller86
     */
    private int[] createNumbers(){
        int[] numbers = new int[]{50, 50, 50, 50, 50, 50};
        // 번호 생성
        for (int i = 0; i < numbers.length; ) {
            // 숫자 생성
            Random random = new Random();
            int number = random.nextInt(45)+1;

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
     * 번호 6개의 합을 최댓값과 최솟값으로 비교
     * @param numbers 생성한 번호 6개
     * @return 검토 결과
     */
    private boolean validateMinMax(int[] numbers){
        int temp = 0;
        for (int num :  numbers){
            temp += num;
        }

        if(48 < temp && temp < 238){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 1등 또는 2등 당첨 번호인지 검증하는 함수
     * @param numbers 생성된 번호
     * @return 검증 결과
     * @throws SQLException
     * @author jskpubller86
     */
    private boolean validateWinNumbers(int[] numbers) throws SQLException {
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
        boolean isValid = false;
        rs.next();
        if(rs.getInt(1) > 0){
            System.out.println("중복된 번호 : " + Arrays.toString(numbers));
            isValid = false;
        } else {
            System.out.println("유효한 번호 : " + Arrays.toString(numbers));
            isValid = true;
        }
        try{rs.close();} catch (SQLException ex){}
        try{pstmt.close();} catch (SQLException ex){}

        return isValid;
    }
}
