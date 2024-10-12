
create table game.LOTTO
(
    id       NUMBER(5) not null,
    cre_DATE DATE  not null,
    no1    NUMBER(2) not null,
    no2    NUMBER(2) not null,
    no3    NUMBER(2) not null,
    no4    NUMBER(2) not null,
    no5    NUMBER(2) not null,
    no6    NUMBER(2) not null,
    BONUS       NUMBER(2) not null
    , constraint pk_lotto_id primary key(id)
    , constraint uk_lotto_win_no unique(no1, no2, no3, no4, no5, no6)
);

COMMENT ON COLUMN game.lotto.id IS '식별자';
COMMENT ON COLUMN game.lotto.cre_DATE IS '당첨 일자';
COMMENT ON COLUMN game.lotto.no1 IS '번호 1';
COMMENT ON COLUMN game.lotto.no2 IS '번호 2';
COMMENT ON COLUMN game.lotto.no3 IS '번호 3';
COMMENT ON COLUMN game.lotto.no4 IS '번호 4';
COMMENT ON COLUMN game.lotto.no5 IS '번호 5';
COMMENT ON COLUMN game.lotto.no6 IS '번호 6';
COMMENT ON COLUMN game.lotto.bonus IS '보너스';


-- 유저 생성문

CREATE USER game IDENTIFIED BY 1234;

SELECT * FROM all_users where username='GAME';

alter USER game IDENTIFIED BY 1234;

-- 권한 주기

GRANT CREATE SESSION, Create any table TO game;


ALTER USER game DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP;

ALTER USER game QUOTA unlimited ON USERS;

SELECT * FROM DBA_TS_QUOTAS WHERE TABLESPACE_NAME = 'USERS' AND USERNAME = 'GAME';


