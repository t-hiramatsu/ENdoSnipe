#ifndef __ENS_COMMUTATOR_H__
#define __ENS_COMMUTATOR_H__

#include <signal.h>

#define BUFSIZE  1024	// データ読込1行バッファサイズ
#define PROP_BUFSIZE 128	// 設定ファイル項目バッファサイズ
#define NORMAL_RTN 0	// リターンコード正常
#define ERROR_RTN -1	// リターンコード異常
#define TOKEN_SEP ","	// 読込データの区切り

// 電文ヘッダ部データ
#define TELEGRAM_ID_LEN 8	// 電文ID長
#define TELEGRAM_ID 0LL		// 電文ID(0固定)
#define TELEGRAM_LASTTELEGRAM_LEN 1	// last telegram
#define TELEGRAM_LASTTELEGRAM_VALUE 1	// last telegramの値
#define TELEGRAM_TYPE_LEN 1	// 電文種別長
#define TELEGRAM_TYPE_START 14	// 電文種別(開始)
#define TELEGRAM_TYPE_NORM 3	// 電文種別(通常)
#define TELEGRAM_REQUEST_TYPE_LEN 1	// リクエスト種別長
#define TELEGRAM_REQUEST_NOTE 0	// リクエスト種別(通知)
#define TELEGRAM_REQUEST_REQ 1	// リクエスト種別(要求)
#define TELEGRAM_REQUEST_RES 2	// リクエスト種別(応答)
#define TELEGRAM_UNKNOWN2_LEN 3 // 未知の項目2

// 電文データ
#define ITEM_TYPE_LONG 3	// long項目型
#define REPEATE_COUNT 1		// 繰り返し回数
#define VALUE_LEN 8		// 値の長さ
#define LONG_LEN 4		// long項目の長さ
#define BYTE_LEN 1		// byte項目の長さ

#define CRRTIME_OBJ_NAME "resource-time" // 現在時刻オブジェクト名 */
#define CRRTIME_ITEM_NAME "/common/fundamental/time/current"
                                         // 現在時刻項目名
#define OBJ_NAME "resources" // オブジェクト名 */

// 電文ヘッダ部データオフセット
#define OF_TELEGRAM_LEN          0
#define OF_TELEGRAM_ID           (OF_TELEGRAM_LEN + LONG_LEN)
#define OF_TELEGRAM_LASTTELEGRAM (OF_TELEGRAM_ID + TELEGRAM_ID_LEN)
#define OF_TELEGRAM_TYPE         (OF_TELEGRAM_LASTTELEGRAM + TELEGRAM_LASTTELEGRAM_LEN)
#define OF_TELEGRAM_REQUEST_TYPE (OF_TELEGRAM_TYPE + TELEGRAM_TYPE_LEN)
#define OF_TELEGRAM_UNKNOWN2     (OF_TELEGRAM_REQUEST_TYPE + TELEGRAM_REQUEST_TYPE_LEN)

// ヘッダ長
#define HEADER_LEN  OF_TELEGRAM_UNKNOWN2 + TELEGRAM_UNKNOWN2_LEN

// 電文Body部1単位長(オブジェクト名長+項目名長+表示名長+項目種別+繰返回数+値)
// 不定サイズのオブジェクト名と項目名は除く
#define OF_OBJ_NAME_LEN  0
#define OF_ITEM_NAME_LEN (OF_OBJ_NAME_LEN + LONG_LEN)
#define OF_DSP_NAME_LEN  (OF_ITEM_NAME_LEN + LONG_LEN)
#define OF_ITEM_TYPE     (OF_DSP_NAME_LEN + LONG_LEN)
#define OF_REPEAT_COUNT  (OF_ITEM_TYPE + BYTE_LEN)
#define OF_VALUE         (OF_REPEAT_COUNT + LONG_LEN)
#define OF_BODY_LEN      (OF_VALUE + VALUE_LEN)

// 通知電文データ
#define NOTICE_OBJ_NAME_LEN 18L
#define NOTICE_OBJ_NAME "connectInformation"
#define NOTICE_ITEM_NAME1_LEN 4L
#define NOTICE_ITEM_NAME1 "kind"
#define NOTICE_DISP_NAME_LEN 0L // 通知電文表示名長
#define NOTICE_ITEM_NAME2_LEN 6L
#define NOTICE_ITEM_NAME2 "dbName"
#define NOTICE_ITEM_NAME3_LEN 7L
#define NOTICE_ITEM_NAME3 "purpose"
#define ITEM_TYPE_TEXT 2	// 項目型(文字列)
#define ITEM_TYPE_PROPERTY 6	// 項目型(プロパティ・仮)
#define NOTICE_UNKNOWN1 0L
#define NOTICE_UNKNOWN2 0
#define NOTICE_UNKNOWN3 2L
#define NOTICE_UNKNOWN4 1L
#define NOTICE_UNKNOWN5 0L

// 通知電文Body部データオフセット
#define OF_NOTICE_OBJ_NAME1_LEN  (OF_TELEGRAM_UNKNOWN2 + TELEGRAM_UNKNOWN2_LEN)
#define OF_NOTICE_OBJ_NAME1      (OF_NOTICE_OBJ_NAME1_LEN + LONG_LEN)
#define OF_NOTICE_ITEM_NAME1_LEN (OF_NOTICE_OBJ_NAME1 + NOTICE_OBJ_NAME_LEN)
#define OF_NOTICE_ITEM_NAME1     (OF_NOTICE_ITEM_NAME1_LEN + LONG_LEN)
#define OF_NOTICE_DSP_NAME1_LEN  (OF_NOTICE_ITEM_NAME1 + NOTICE_ITEM_NAME1_LEN)
#define OF_NOTICE_ITEM_TYPE1     (OF_NOTICE_DSP_NAME1_LEN + LONG_LEN)
#define OF_NOTICE_REPEAT_COUNT1  (OF_NOTICE_ITEM_TYPE1 + BYTE_LEN)
#define OF_NOTICE_UNKNOWN1       (OF_NOTICE_REPEAT_COUNT1 + LONG_LEN)
#define OF_NOTICE_OBJ_NAME2_LEN  (OF_NOTICE_UNKNOWN1 + LONG_LEN)
#define OF_NOTICE_OBJ_NAME2      (OF_NOTICE_OBJ_NAME2_LEN + LONG_LEN)
#define OF_NOTICE_ITEM_NAME2_LEN (OF_NOTICE_OBJ_NAME2 + NOTICE_OBJ_NAME_LEN)
#define OF_NOTICE_ITEM_NAME2     (OF_NOTICE_ITEM_NAME2_LEN + LONG_LEN)
#define OF_NOTICE_DSP_NAME2_LEN  (OF_NOTICE_ITEM_NAME2 + NOTICE_ITEM_NAME2_LEN)
#define OF_NOTICE_ITEM_TYPE2     (OF_NOTICE_DSP_NAME2_LEN + LONG_LEN)
#define OF_NOTICE_REPEAT_COUNT2  (OF_NOTICE_ITEM_TYPE2 + BYTE_LEN)
#define OF_NOTICE_PROPERTY_LEN   (OF_NOTICE_REPEAT_COUNT2 + LONG_LEN)
#define OF_NOTICE_PROPERTY       (OF_NOTICE_PROPERTY_LEN + LONG_LEN)
#define OF_NOTICE_OBJ_NAME3_LEN  (OF_NOTICE_PROPERTY)	// PROPERTY長は設定ファイルに依存するので以後の項目に後から足す
#define OF_NOTICE_OBJ_NAME3      (OF_NOTICE_OBJ_NAME3_LEN + LONG_LEN)
#define OF_NOTICE_ITEM_NAME3_LEN (OF_NOTICE_OBJ_NAME3 + NOTICE_OBJ_NAME_LEN)
#define OF_NOTICE_ITEM_NAME3     (OF_NOTICE_ITEM_NAME3_LEN + LONG_LEN)
#define OF_NOTICE_UNKNOWN2       (OF_NOTICE_ITEM_NAME3 + NOTICE_ITEM_NAME3_LEN)
#define OF_NOTICE_UNKNOWN3       (OF_NOTICE_UNKNOWN2 + BYTE_LEN)
#define OF_NOTICE_UNKNOWN4       (OF_NOTICE_UNKNOWN3 + LONG_LEN)
#define OF_NOTICE_UNKNOWN5       (OF_NOTICE_UNKNOWN4 + LONG_LEN)

#define NOTICE_BUF_SIZE          (OF_NOTICE_UNKNOWN5 + LONG_LEN)

// 設定格納構造体
struct prop_st {
    char connect_host[PROP_BUFSIZE];
    char connect_port[PROP_BUFSIZE];
    char cluster_name[PROP_BUFSIZE];
    char agent_name[PROP_BUFSIZE];
};

// SystemTapデータ保持リスト構造体
struct st_node {
    char item_name[BUFSIZE];
    long long value;
    int item_name_len;
    struct st_node *next;
};


// SystemTapより送られる1回分のデータの終わりを示す
#define DATA_END ".\n"

// 設定文字列
#define JAVELIN_HOST "javelin.connectHost"
#define JAVELIN_PORT "javelin.connectPort"
#define JAVELIN_CLUSTER "javelin.clusterName"
#define JAVELIN_AGENT "javelin.agentName"
#define JAVELIN_HOST_LEN 19
#define JAVELIN_PORT_LEN 19
#define JAVELIN_CLUSTER_LEN 19
#define JAVELIN_AGENT_LEN 17
#define PROP_NUM 4

// プロトタイプ宣言
int get_properties(char *prop_path, struct prop_st *properties);
int connect_to_collector(struct prop_st *properties);
int send_notice_data(struct prop_st *properties);
int read_stap_data(void);
int set_item(char array[], int top_offset, char *obj_name, char *item_name, long long value);
long long trans_endian(long long input);
void sigcatch(int signo);
int remove_lf(char *buffer);
int create_telegram(void);
int send_telegram(void);
void release_global_area(void);
int add_node(char *item_name, long long value);
void release_work_area(void);
int get_list_size(int *datasize);
int get_node(int order, char *buffer, long long *value, int *item_name_len);
int create_telegram(void);
int telegram_unit(int top_offset, char *item_name, long long value, int item_name_len);

#endif
