package org.games.lotto;

import oracle.jdbc.driver.OracleDriver;
import org.games.lotto.select.SelectNumbers;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Main {
//    static Connection conn = null;
//    static PreparedStatement pstmt = null;
//    static ResultSet rs = null;

    public static void main(String[] args) {
        try{
            int[] numbers = new SelectNumbers().selectNumbers();
            System.out.println(Arrays.toString(numbers));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


