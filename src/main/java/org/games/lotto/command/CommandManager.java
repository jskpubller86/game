package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;
import org.games.lotto.configures.BasicDataSource;
import org.games.lotto.command.constant.CommandConst;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 번호 추가, 선택 등의 명령을 관리하는 클래스
 * @author jskpubller86
 */
public class CommandManager {
    private Map<String, Command> commands;
    private BasicDataSource basicDS;

    /**
     * 생성자
     * @author jskpubller86
     */
    public CommandManager() {
        basicDS = new BasicDataSource();
        commands = new HashMap<>();
        commands.put(CommandConst.INSERT,  new Add());
        commands.put(CommandConst.SELECT,  new Select());
        commands.put(CommandConst.EXIT,  new Exit());
    }

    /**
     *  입력받은 명령어를 실행하는 함수
     */
    public String excute(){
        String key = null;
        BasicConnection conn = null;
        try {
            System.out.printf("실행할 명령어를 입력해 주세요. %s, %s 또는 %s %n", CommandConst.SELECT, CommandConst.INSERT, CommandConst.EXIT);

            Scanner in = new Scanner(System.in);
            key = in.next();
            Command cmd = commands.get(key);
            if(cmd == null) {throw new NullPointerException();}
            else if(!CommandConst.EXIT.equals(key)) {
                conn = basicDS.getConnection();
                cmd.setConnection(conn);
                cmd.execute();
            }
        } catch (Exception ex){
            System.out.println(ex.toString());
            conn.close();
        }
        return key;
    }
}
