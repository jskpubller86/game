package org.games.lotto.command;

import org.games.lotto.configures.BasicDataSource;
import org.games.lotto.command.constant.CommandConst;

import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    private Map<String, Command> commands;

    public CommandManager setCommands(Map<String, Command> commands) {
        this.commands = commands;
        return this;
    }

    public boolean excute(){
        Scanner in = new Scanner(System.in);
        System.out.printf("실행할 명령어를 입력해 주세요. %s, %s 또는 %s %n",CommandConst.SELECT, CommandConst.INSERT, CommandConst.EXIT);
        String command = in.next();
        try{
            excute(command);
        } catch (Exception ex){
            System.out.println("명령어가 올바르지 않습니다.");
        } finally {
            if(CommandConst.EXIT.equalsIgnoreCase(command) ) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void excute(String command) throws Exception {
        Command obj = commands.get(command);

        if(obj == null) {throw new Exception();}
        else {obj.execute(new BasicDataSource().getConnection());}
    }
}
