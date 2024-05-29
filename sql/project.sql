USE prj2;

# board 테이블 생성
CREATE TABLE board
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(100)  NOT NULL,
    content  VARCHAR(1000) NOT NULL,
    writer   VARCHAR(100)  NOT NULL,
    inserted DATETIME      NOT NULL DEFAULT NOW()
);

SELECT *
FROM board;
# board 테이블 수정
# writer column 지우기
ALTER TABLE board
    DROP COLUMN writer;
ALTER TABLE board
    ADD COLUMN member_id INT REFERENCES member (id) AFTER content;
UPDATE board
SET member_id = (SELECT id FROM member ORDER BY id DESC LIMIT 1)
WHERE id > 0;
ALTER TABLE board
    MODIFY COLUMN member_id INT NOT NULL;
desc board;
# 회원 테이블 생성
CREATE TABLE member
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    nick_name VARCHAR(100) NOT NULL UNIQUE,
    inserted  DATETIME     NOT NULL DEFAULT NOW()
);

DESC member;
SELECT *
FROM member;

# 권한 테이블
CREATE TABLE authority
(
    member_id INT         NOT NULL REFERENCES member (id),
    name      VARCHAR(20) NOT NULL,
    PRIMARY KEY (member_id, name)
);
SELECT *
FROM authority;

INSERT INTO authority (member_id, name)
VALUES (30, 'admin');

# 게시물 여러개 입력
INSERT INTO board
    (title, content, member_id)
SELECT title, content, member_id
FROM board;

desc board;

SELECT *
FROM member;

UPDATE member
SET nick_name = 'abcd'
WHERE id = 31;
UPDATE member
SET nick_name = 'efgh'
WHERE id = 30;

UPDATE board
SET member_id = 30
WHERE id % 2 = 0;
UPDATE board
SET member_id = 31
WHERE id % 2 = 1;

USE prj2;
DESC board;

CREATE TABLE board_file
(
    board_id INT          NOT NULL REFERENCES board (id),
    name     VARCHAR(500) NOT NULL,
    PRIMARY KEY (board_id, name)
);
SELECT *
FROM board_file;

# board_like 만들기
CREATE TABLE board_like
(
    board_id  INT NOT NULL REFERENCES board (id),
    member_id INT NOT NULL REFERENCES member (id),
    PRIMARY KEY (board_id, member_id)
);
SELECT *
FROM board_like;
DELETE
FROM board_like
WHERE member_id = 1;

