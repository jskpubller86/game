package org.games.lotto.command;


import org.games.lotto.configures.BasicConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Add implements Command {

    public void execute(BasicConnection conn) throws SQLException {
        Scanner in = new Scanner(System.in);
        int seq = 0;
        Integer round = null;
        String date = null;
        Integer[] winNumbers = new Integer[7];

        // 당첨번호 정보를 받고 저장한다.
        while(true){
            try{
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
                    for (int i = seq-3; i < winNumbers.length; i++, seq++) {
                        if(i == winNumbers.length-1){
                            System.out.println("보너스 번호를 입력해주세요.");
                        } else {
                            System.out.println(i + 1 + " 번째 번호를 입력해주세요.");
                        }

                        winNumbers[i] = in.nextInt();
                    }
                }

                // 쿼리를 생성하고 실행한다.
                String sql = "insert into game.lotto values (?,to_date(?, 'yyyymmdd'),?,?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, round);
                pstmt.setString(2, date);

                for (int i = 0, j=3; i < winNumbers.length; i++, j++) {
                    pstmt.setInt(j, winNumbers[i]);
                }
                pstmt.executeUpdate();
                break;
            } catch (Exception ex){}
        }

    }
}
