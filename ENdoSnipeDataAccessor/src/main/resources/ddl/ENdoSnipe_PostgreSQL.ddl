/**********************************/
/* テーブル名: Javelinログ */
/**********************************/
CREATE TABLE JAVELIN_LOG(
  LOG_ID BIGINT DEFAULT NEXTVAL('SEQ_LOG_ID') NOT NULL,
  SESSION_ID BIGINT DEFAULT NEXTVAL('SEQ_SESSION_ID') NOT NULL,
  SEQUENCE_ID INT NOT NULL,
  JAVELIN_LOG BYTEA NOT NULL,
  LOG_FILE_NAME VARCHAR,
  START_TIME TIMESTAMP,
  END_TIME TIMESTAMP,
  SESSION_DESC VARCHAR,
  LOG_TYPE INT,
  CALLEE_NAME VARCHAR,
  CALLEE_SIGNATURE VARCHAR,
  CALLEE_CLASS VARCHAR,
  CALLEE_FIELD_TYPE VARCHAR,
  CALLEE_OBJECTID INT,
  CALLER_NAME VARCHAR,
  CALLER_SIGNATURE VARCHAR,
  CALLER_CLASS VARCHAR,
  CALLER_OBJECTID INT,
  EVENT_LEVEL SMALLINT,
  ELAPSED_TIME BIGINT,
  MODIFIER VARCHAR,
  THREAD_NAME VARCHAR,
  THREAD_CLASS VARCHAR,
  THREAD_OBJECTID INT,
  MEASUREMENT_ITEM_NAME VARCHAR,
  DIAGNOSED BOOLEAN DEFAULT FALSE
);

/**********************************/
/* テーブル名: Javelin計測項目 */
/**********************************/
CREATE TABLE JAVELIN_MEASUREMENT_ITEM(
  MEASUREMENT_ITEM_ID INT DEFAULT NEXTVAL('SEQ_MEASUREMENT_ITEM_ID') NOT NULL,
  MEASUREMENT_ITEM_NAME VARCHAR NOT NULL,
  LAST_INSERTED TIMESTAMP NOT NULL
);

/**********************************/
/* テーブル名: Javelin 計測値 */
/**********************************/
CREATE TABLE MEASUREMENT_VALUE(
  MEASUREMENT_TIME TIMESTAMP NOT NULL,
  MEASUREMENT_ITEM_ID INT NOT NULL,
  MEASUREMENT_VALUE VARCHAR NOT NULL
);

/**********************************/
/* テーブル名: 計測対象ホスト情報 */
/**********************************/
CREATE TABLE HOST_INFO(
  HOST_ID INT DEFAULT NEXTVAL('SEQ_HOST_ID') NOT NULL,
  HOST_NAME VARCHAR,
  IP_ADDRESS VARCHAR NOT NULL,
  PORT INT NOT NULL,
  DESCRIPTION VARCHAR
);

/**********************************/
/* テーブル名: シグナル定義テーブル */
/**********************************/
CREATE TABLE SIGNAL_DEFINITION(
  SIGNAL_ID serial NOT NULL,
  SIGNAL_NAME VARCHAR NOT NULL UNIQUE,
  MATCHING_PATTERN VARCHAR NOT NULL,
  LEVEL INT NOT NULL,
  PATTERN_VALUE VARCHAR NOT NULL,
  ESCALATION_PERIOD DOUBLE PRECISION
);

/**********************************/
/* テーブル名: マップテーブル */
/**********************************/
CREATE TABLE MAP_INFO(
 MAP_ID SERIAL NOT NULL,
 NAME VARCHAR(64),
 DATA text,
 LAST_UPDATE TIMESTAMP NOT NULL
);

/**********************************/
/* テーブル名: パフォーマンスドクター診断結果蓄積テーブル */
/**********************************/
CREATE TABLE PERFDOCTOR_RESULT(
  LOG_ID BIGINT DEFAULT NEXTVAL('SEQ_LOG_ID') NOT NULL,
  OCCURRENCE_TIME TIMESTAMP,
  DESCRIPTION VARCHAR,
  LEVEL VARCHAR,
  CLASS_NAME VARCHAR,
  METHOD_NAME VARCHAR,
  JAVELIN_LOG_NAME VARCHAR,
  MEASUREMENT_ITEM_NAME VARCHAR
);

/**********************************/
/* テーブル名: レポート出力結果テーブル */
/**********************************/
CREATE TABLE REPORT_EXPORT_RESULT(
  REPORT_ID SERIAL NOT NULL,
  REPORT_NAME VARCHAR NOT NULL,
  TARGET_MEASUREMENT_NAME VARCHAR NOT NULL,
  FM_TIME VARCHAR NOT NULL,
  TO_TIME VARCHAR NOT NULL,
  STATUS VARCHAR NOT NULL

);

/**********************************/
/* テーブル名: スケジュールレポート定義テーブル */
/**********************************/
CREATE TABLE SCHEDULING_REPORT_DEFINITION (
  REPORT_ID SERIAL PRIMARY KEY,
  REPORT_NAME  VARCHAR  ,
  TARGET_MEASUREMENT_NAME VARCHAR  ,
  SCHEDULE_TERM VARCHAR ,
  SCHEDULE_TIME VARCHAR  ,
  SCHEDULE_DAY VARCHAR  ,
  SCHEDULE_DATE VARCHAR	,
  LAST_EXPORT_REPORT_TIME TIMESTAMP	NOT NULL
);

/**********************************/
/* テーブル名: 複数系列グラフ定義テーブル */
/**********************************/
CREATE TABLE MULTIPLE_RESOURCE_GRAPH(
  MULTIPLE_RESOURCE_GRAPH_ID SERIAL NOT NULL,
  MULTIPLE_RESOURCE_GRAPH_NAME VARCHAR NOT NULL,
  MEASUREMENT_ITEM_ID_LIST VARCHAR NOT NULL
);

/**********************************/
/* テーブル名: SQL計画実行定義テーブル */
/**********************************/
CREATE TABLE SQL_PLAN(
  MEASUREMENt_ITEM_NAME VARCHAR NOT NULL,
  SQL_STATEMENT VARCHAR NOT NULL,
  EXECUTION_PLAN VARCHAR NOT NULL,
  GETTING_PLAN_TIME TIMESTAMP NOT NULL,
  STACK_TRACE TEXT NOT NULL,
  UNIQUE(MEASUREMENt_ITEM_NAME, EXECUTION_PLAN),
  UNIQUE(MEASUREMENt_ITEM_NAME, STACK_TRACE)
);

/**********************************/
/* テーブル名: SUMMARY　SIGNAL定義テーブル */
/**********************************/
CREATE TABLE SUMMARY_SIGNAL_DEFINITION(
  SUMMARY_SIGNAL_ID SERIAL NOT NULL,
  SUMMARY_SIGNAL_NAME VARCHAR NOT NULL UNIQUE,
  TARGET_SIGNAL_ID VARCHAR NOT NULL,
  SIGNAL_TYPE INT NOT NULL,
  PRIORITY_NO INT NOT NULL
);

/**********************************/
/* テーブル名: PROPERTY　SETTINGテーブル */
/**********************************/
CREATE TABLE PROPERTY_SETTING_DEFINITION(
  PROPERTY_SETTING_ID SERIAL NOT NULL,
  PROPERTY_SETTING_KEY VARCHAR NOT NULL UNIQUE,
  PROPERTY_SETTING_VALUE VARCHAR NOT NULL
);

ALTER TABLE JAVELIN_LOG ADD PRIMARY KEY (LOG_ID);
CREATE INDEX IDX_JAVELIN_LOG_START_TIME ON JAVELIN_LOG (START_TIME);
CREATE INDEX IDX_JAVELIN_LOG_END_TIME ON JAVELIN_LOG (END_TIME);
CREATE INDEX IDX_JAVELIN_LOG_SESSION_ID ON JAVELIN_LOG (SESSION_ID);
CREATE INDEX IDX_JAVELIN_LOG_LOG_FILE_NAME ON JAVELIN_LOG (LOG_FILE_NAME);

CREATE INDEX IDX_FUNC_JAVELIN_MEASUREMENT_ITEM_NAME
  ON JAVELIN_MEASUREMENT_ITEM
  (REPLACE(REPLACE(REPLACE(MEASUREMENT_ITEM_NAME, CHR(13) || CHR(10), ' '), CHR(13), ' '), CHR(10), ' '));

CREATE INDEX IDX_FUNC_JAVELIN_MEASUREMENT_ITEM_PART2
  ON JAVELIN_MEASUREMENT_ITEM
  (SPLIT_PART(REPLACE(REPLACE(REPLACE(MEASUREMENT_ITEM_NAME, CHR(13) || CHR(10), ' '), CHR(13), ' '), CHR(10), ' '), '/', 2));

CREATE INDEX IDX_FUNC_JAVELIN_MEASUREMENT_ITEM_PART3
  ON JAVELIN_MEASUREMENT_ITEM
  (SPLIT_PART(REPLACE(REPLACE(REPLACE(MEASUREMENT_ITEM_NAME, CHR(13) || CHR(10), ' '), CHR(13), ' '), CHR(10), ' '), '/', 3));

CREATE INDEX IDX_JAVELIN_MEASUREMENT_ITEM_NAME
  ON JAVELIN_MEASUREMENT_ITEM
  (MEASUREMENT_ITEM_NAME);

CREATE INDEX IDX_JAVELIN_MEASUREMENT_ITEM_PART2
  ON JAVELIN_MEASUREMENT_ITEM
  (SPLIT_PART(MEASUREMENT_ITEM_NAME, '/', 2));

CREATE INDEX IDX_JAVELIN_MEASUREMENT_ITEM_PART3
  ON JAVELIN_MEASUREMENT_ITEM
  (SPLIT_PART(MEASUREMENT_ITEM_NAME, '/', 3));

ALTER TABLE JAVELIN_MEASUREMENT_ITEM ADD PRIMARY KEY (MEASUREMENT_ITEM_ID);

ALTER TABLE MEASUREMENT_VALUE ADD PRIMARY KEY (MEASUREMENT_TIME, MEASUREMENT_ITEM_ID);
ALTER TABLE MEASUREMENT_VALUE ADD CONSTRAINT FK_MEASUREMENT_VALUE_0 FOREIGN KEY (MEASUREMENT_ITEM_ID) REFERENCES JAVELIN_MEASUREMENT_ITEM (MEASUREMENT_ITEM_ID);

ALTER TABLE PERFDOCTOR_RESULT ADD PRIMARY KEY (LOG_ID);

CREATE INDEX IDX_SQL_PLAN_SESSION_ID ON SQL_PLAN (MEASUREMENt_ITEM_NAME);
