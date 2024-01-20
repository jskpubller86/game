package org.games.lotto.command;

import org.games.lotto.configures.BasicConnection;

public interface Command {
    void execute(BasicConnection conn) throws Exception;
}
