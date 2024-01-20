package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;

public class Exit implements Command{
    @Override
    public void execute(BasicConnection conn) {
        System.out.println("명령을 종료합니다.");
    }
}
