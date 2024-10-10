package org.game;

import org.game.annuity.Annuity;
import org.game.configure.data.BasicConnection;
import org.game.configure.data.BasicDataSource;
import org.game.lotto.Lotto;

import java.util.Scanner;

public class Main {
//    public static void main(String[] args) {
//        BasicDataSource ds = new BasicDataSource();
//        BasicConnection conn = null;
//        try {
//            conn = ds.getConnection();
//            boolean status = true;
//
//            while (status){
//                System.out.println("게임을 선택해 주세요. 1: 연금, 2: 로또, 3: 종료");
//                Scanner in = new Scanner(System.in);
//                String key = in.next();
//
//                if(key == null) {throw new NullPointerException();}
//
//                switch (key){
//                    case "1" : new Annuity().setConnection(conn).execute();
//                        break;
//                    case "2" : new Lotto().setConnection(conn).execute();
//                        break;
//                    case "3" : status = false;
//                        break;
//                }
//            }
//        } catch (Exception e){
//            try{if(conn != null) conn.close();} catch (Exception e2){}
//        }
//    }
}
