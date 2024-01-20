package org.games.lotto;

import org.games.lotto.command.*;
import org.games.lotto.command.constant.CommandConst;

import java.util.*;

public class Main {
    static Map<String, Command> commands;
    public static void main(String[] args) {
        // 명령어 연결 작업
        commands = new HashMap<String, Command>();
        commands.put(CommandConst.INSERT, new Add());
        commands.put(CommandConst.SELECT, new Select());
        commands.put(CommandConst.EXIT, new Exit());

        CommandManager manager = new CommandManager().setCommands(commands);

        while (true){
            if(manager.excute()){
                break;
            }
        }
    }


}


