CREATE TABLE MEMBER (
	user_id 		VARCHAR2(10) PRIMARY KEY,
	user_pw 	VARCHAR2(10),
	user_name	VARCHAR2(50),
	user_email	VARCHAR2(50),
	JOIN 	DATE DEFAULT SYSDATE
);

INSERT INTO MEMBER VALUES('JKS2024', 'SPHB8250', '곰돌이사육사', 'jks2024@gmail.com', sysdate);
INSERT INTO MEMBER VALUES('1234567', '1234567', '안유진', 'anyou@gmail.com', sysdate);
INSERT INTO MEMBER VALUES('56785678', '1234567', '이영지', 'lyj12@gmail.com', sysdate);
INSERT INTO MEMBER VALUES('mimi1234', 'mimi1234', '미미', 'mimi@gmail.com', sysdate);


SELECT * FROM member;
SELECT * FROM board;
COMMIT;

-- 게시판
CREATE TABLE board (
	board_idx		number(4) PRIMARY KEY,
	board_name 		varchar2(20),
	board_title 	varchar2(100),
	board_content 	varchar2(300),
	user_id			varchar2(10),
	board_date		DATE DEFAULT sysdate,
	board_hit 		number(4) DEFAULT 0
);

CREATE SEQUENCE board_seq;

INSERT INTO board VALUES(board_seq.nextval, '뉴스', '글 제목1', '글 내용1', 'jks2024', sysdate, 0);
INSERT INTO board VALUES(board_seq.nextval, '공지', '글 제목2', '글 내용2', 'mimi1234', sysdate, 0);

-- 댓글
CREATE TABLE reply (
	reply_idx		NUMBER(4) PRIMARY KEY,
	reply_name 		varchar2(20),
	reply_content	varchar2(30),
	reply_date		DATE DEFAULT sysdate,
	reply_board_idx	number(4)  -- 어느 게시물의 댓글인지 알아야하므로 외래키 하나 있는거
);

SELECT * FROM reply;
CREATE SEQUENCE reply_board_seq;
INSERT INTO reply VALUES(reply_board_seq.nextval, '이영지', '댓글 제목1', sysdate, 1);
INSERT INTO reply VALUES(reply_board_seq.nextval, '이은지', '댓글 제목2', sysdate, 2);

COMMIT;

제발 좀 올라가!!!

