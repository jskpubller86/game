package org.games.lotto;

import org.games.lotto.command.*;
import org.games.lotto.command.constant.CommandConst;

import java.util.*;

public class Main {
    static Map<String, Command> commands;
    public static void main(String[] args) {
        CommandManager manager = new CommandManager();

        while (true){
            // 실행한 명령어가 종료이면 프로그램을 종료한다.
            if(manager.excute() == CommandConst.EXIT){
                break;
            }
        }
    }
}


