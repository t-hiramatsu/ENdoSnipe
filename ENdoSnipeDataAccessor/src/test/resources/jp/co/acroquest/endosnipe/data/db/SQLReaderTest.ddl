/*****************************************************************************/
/* SQLReader �e�X�g�p�� DDL ��                                               */
/*****************************************************************************/

/**********************************/
/* ���ʂŎg�p����V�[�P���X       */
/**********************************/
CREATE SEQUENCE SEQ_LOG_ID;
CREATE SEQUENCE SEQ_SESSION_ID;
CREATE SEQUENCE SEQ_VALUE_ID;

/**********************************/
/* �e�[�u����: �v���Ώۃz�X�g��� */
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
