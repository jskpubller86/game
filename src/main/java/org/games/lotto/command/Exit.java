package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;

public class Exit implements Command{
    @Override
    public void setConnection(BasicConnection conn){}
    @Override
    public void execute() {
        System.out.println("명령을 종료합니다.");
    }
}
