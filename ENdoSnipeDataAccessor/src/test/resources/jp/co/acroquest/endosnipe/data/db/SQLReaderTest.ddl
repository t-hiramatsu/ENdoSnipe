/*****************************************************************************/
/* SQLReader テスト用の DDL 文                                               */
/*****************************************************************************/

/**********************************/
/* 共通で使用するシーケンス       */
/**********************************/
CREATE SEQUENCE SEQ_LOG_ID;
CREATE SEQUENCE SEQ_SESSION_ID;
CREATE SEQUENCE SEQ_VALUE_ID;

/**********************************/
/* テーブル名: 計測対象ホスト情報 */
/**********************************/
CREATE SEQUENCE SEQ_HOST_ID;

CREATE TABLE HOST_INFO(
  HOST_ID INT DEFAULT NEXTVAL('SEQ_HOST_ID') NOT NULL,
  HOST_NAME VARCHAR NOT NULL,
  IP_ADDRESS VARCHAR NOT NULL,
  PORT INT NOT NULL,
  DESCRIPTION VARCHAR
);

ALTER TABLE HOST_INFO ADD PRIMARY KEY (HOST_ID);
