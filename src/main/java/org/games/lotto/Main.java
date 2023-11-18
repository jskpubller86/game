package org.games.lotto;

import oracle.jdbc.driver.OracleDriver;
import org.games.lotto.select.SelectNumbers;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class Main {
//    static Connection conn = null;
//    static PreparedStatement pstmt = null;
//    static ResultSet rs = null;

    public static void main(String[] args) {
        int[] numbers = new SelectNumbers().selectNumbers();
        System.out.println(Arrays.toString(numbers));
    }

}


