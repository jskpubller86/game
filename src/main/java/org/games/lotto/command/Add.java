package org.games.lotto.command;


import org.games.lotto.configures.BasicConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 당첨 번호 추가 명령 클래스
 * @author jskpubller86
 */
public class Add implements Command {
    private BasicConnection conn = null;
    @Override
    public void setConnection(BasicConnection conn){
        this.conn = conn;
    }

    @Override
    public void execute() throws Exception {
        int seq = 0; // 입력 오류가 발생한 곳을 표시하기 위해서 사용
        int round = 0;
        String date = null;
        int[] winNumbers = new int[7];

        // 스캐너를 통해 입력 정보를 받고 저장
        Scanner in = new Scanner(System.in);

        // 당첨번호에 대한 정보를 입력받고 저장한다.
        while(true){
            // 회차
            if(seq == 0){
                System.out.println("회차를 입력해주세요.");
                round = in.nextInt();
                seq++;
            }
            else if (seq == 1){
                // 일자
                System.out.println("일자를 입력해주세요.");
                date = in.next("\\d{8}");
                seq++;
            } else {
                // seq 변수가 2와 같거나 큰 수로 들어 옴
                for (int i = seq-2; i < winNumbers.length; i++, seq++) {
                    if(i == winNumbers.length-1){
                        System.out.println("보너스 번호를 입력해주세요.");
                    } else {
                        System.out.println(i + 1 + " 번째 번호를 입력해주세요.");
                    }

                    winNumbers[i] = in.nextInt();
                }

                // 쿼리를 생성하고 실행한다.
                String sql = "insert into game.lotto values (?,to_date(?, 'yyyymmdd'),?,?,?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // 쿼리 매개 변수에 값을 대입한다.
                pstmt.setInt(1, round);
                pstmt.setString(2, date);
                for (int i = 0, j=3; i < winNumbers.length; i++, j++) {
                    pstmt.setInt(j, winNumbers[i]);
                }
                // 쿼리 실행
                pstmt.executeUpdate();
                System.out.println("데이터를 저장했습니다.");

                // 쿼리 연결점 종료 및 커넥션 반환
                try{pstmt.close();}catch (SQLException ex){}
                break;
            }
        }
    }
}
