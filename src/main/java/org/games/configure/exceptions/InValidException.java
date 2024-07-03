package org.games.configure.exceptions;

public class InValidException extends Exception {
    public InValidException(){
        super("유효성 검사 실패");
    }
}
