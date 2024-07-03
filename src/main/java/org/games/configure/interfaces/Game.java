package org.games.configure.interfaces;

import org.games.configure.data.BasicConnection;

/**
 * 게임 인터페이스
 */
public interface Game {
    /**
     * 게임을 실행하는 함수
     * @throws Exception
     */
    void execute() throws Exception;

    /**
     * DB 커넥션을 전달하는 함수
     * @param conn
     * @return Game 인터페이스
     */
    Game setConnection(BasicConnection conn);
}
