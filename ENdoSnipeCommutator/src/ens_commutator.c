/**
 * ENdoSnipe Commutator
 * 
 * Copyright AcroQuest Technology Co,Ltd
 * Create Date : 2013/08/31
 */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <signal.h>
#include <errno.h>
#include <arpa/inet.h>
#include <linux/byteorder/big_endian.h>

#include "ens_commutator.h"

// シグナル ハンドラ内で解放するため、以下はグローバル変数とする。
int Sock;		// 通信用ソケットディスクリプタ
char *Telegram_buf;	// 電文格納バッファ
int Telegram_buf_size;	// 電文格納バッファサイズ
struct st_node *Node;	// 受信データ格納リスト構造体

int main(int argc, char *argv[])
{
    struct prop_st properties;

    if(2 != argc){
        printf("usage : sudo stap <path of javelin_XXX.stp> | ./ens_commutator <path of commutator.properties>\n");
        return(ERROR_RTN);
    }

    if(ERROR_RTN == get_properties(argv[1], &properties)){ // 設定ファイル読み込み
        return(ERROR_RTN);
    }

    if(ERROR_RTN == connect_to_collector(&properties)){ // データコレクタに接続
        return(ERROR_RTN);
    }

    if(ERROR_RTN == send_notice_data(&properties)){ // 通知電文送出
        return(ERROR_RTN);
    }

    // 終了用のSIGNALを設定
    if(SIG_ERR == signal(SIGINT, sigcatch)) {
        close(Sock);
        fprintf(stderr, "faild to set signal hander\n");
        return(ERROR_RTN);
    }


    if(ERROR_RTN == read_stap_data()){ // SystemTapデータ読み込み&電文送出
        return(ERROR_RTN);
    }

    return(NORMAL_RTN); // SIGNALで処理終了するので実行されることはない
}

/**
 * プロパティ取得
 * 設定ファイルを読み込む
 * @param char argv[1] : 設定ファイルパス
 * @param struct prop_st properties : 設定格納構造体
 * @return 0:正常 -1:異常
 */
int get_properties(char *prop_path, struct prop_st *properties)
{
    FILE *fp;
    char read_buf[BUFSIZE];
    int prop_cnt = 0;
    int loopcnt;

    if(NULL == (fp = fopen(prop_path, "r"))){
        fprintf(stderr, "propertifile %s open error.\n", prop_path);
        return(ERROR_RTN);
    }

    memset(properties, '\0', sizeof(struct prop_st));

    while(NULL != fgets(read_buf, BUFSIZE, fp))
    {
        // 改行文字を除く
        if(ERROR_RTN == remove_lf(read_buf)){
            return(ERROR_RTN);
        }

        if(0 == strncmp(read_buf, JAVELIN_HOST, JAVELIN_HOST_LEN)){
            strncpy(properties->connect_host, &read_buf[JAVELIN_HOST_LEN+1], PROP_BUFSIZE);
            ++prop_cnt;
        } else if(0 == strncmp(read_buf, JAVELIN_PORT, JAVELIN_PORT_LEN)) {
            strncpy(properties->connect_port, &read_buf[JAVELIN_PORT_LEN+1], PROP_BUFSIZE);
            ++prop_cnt;
        } else if(0 == strncmp(read_buf, JAVELIN_CLUSTER, JAVELIN_CLUSTER_LEN)) {
            strncpy(properties->cluster_name, &read_buf[JAVELIN_CLUSTER_LEN+1], PROP_BUFSIZE);
            ++prop_cnt;
        } else if(0 == strncmp(read_buf, JAVELIN_AGENT, JAVELIN_AGENT_LEN)) {
            strncpy(properties->agent_name, &read_buf[JAVELIN_AGENT_LEN+1], PROP_BUFSIZE);
            ++prop_cnt;
        }
        memset(read_buf, '\0', BUFSIZE);
    }

    fclose(fp);

    if(PROP_NUM != prop_cnt){
        fprintf(stderr, "number of properties is not %d\n", PROP_NUM);
        return(ERROR_RTN);
    }

    return(NORMAL_RTN);
}

/**
 * 接続確立
 * @param struct prop_st *properties : 設定格納構造体のポインタ
 * @return 0:正常 -1:異常
 */
int connect_to_collector(struct prop_st *properties)
{
    struct sockaddr_in server;

    Sock = socket(AF_INET, SOCK_STREAM, 0);
    server.sin_family = AF_INET;
    server.sin_port = htons(atoi(properties->connect_port));
    server.sin_addr.s_addr = inet_addr(properties->connect_host);

//    printf("ipaddr: %s\n", properties->connect_host);
//    printf("portno: %d\n", server.sin_port);

    if(-1 == connect(Sock, (struct sockaddr *)&server, sizeof(server))){
        fprintf(stderr, "connect error : errno %d\n",errno);
        return(ERROR_RTN);
    }

    return(NORMAL_RTN);
}

/**
 * 通知電文送信
 * 最初に送る通知電文を編集して送信する
 * @param struct prop_st *properties : 設定格納構造体のポインタ
 * @return 0:正常 -1:異常
 */
int send_notice_data(struct prop_st *properties)
{
    char host_name[BUFSIZE];
    char prop_buf[BUFSIZE];
    int prop_len;
    long long telegram_id = 1L;

    if(-1 == gethostname(host_name, BUFSIZE)){
        close(Sock);
        fprintf(stderr, "host name get errro.\n");
        return(ERROR_RTN);
    }
    
    if(strlen(properties->agent_name) == 0){
        //agent名が空の場合はID文字列の２番目をホスト名とする
        sprintf(prop_buf, "/%s/%s/", properties->cluster_name, host_name);
    }else{
        //agent名が設定されている場合はID文字列の２番目をagent名とする
        sprintf(prop_buf, "/%s/%s/", properties->cluster_name, properties->agent_name);
    }
    prop_len = strlen(prop_buf);
    
    Telegram_buf_size = NOTICE_BUF_SIZE + prop_len;
    if(NULL == (Telegram_buf = malloc(Telegram_buf_size))){
        close(Sock);
        fprintf(stderr, "memory alloc error.\n");
        return(ERROR_RTN);
    }
    
    memset(Telegram_buf, '\0', Telegram_buf_size);

    // ヘッダー部編集 */
    *((long*)(&Telegram_buf[OF_TELEGRAM_LEN])) = htonl((long)Telegram_buf_size);
    *((long long*)(&Telegram_buf[OF_TELEGRAM_ID])) = trans_endian(0LL);
    Telegram_buf[OF_TELEGRAM_LASTTELEGRAM] = TELEGRAM_LASTTELEGRAM_VALUE;
    Telegram_buf[OF_TELEGRAM_TYPE] = TELEGRAM_TYPE_START;
    Telegram_buf[OF_TELEGRAM_REQUEST_TYPE] = TELEGRAM_REQUEST_NOTE;

    // ボディ部編集
    *((long*)(&Telegram_buf[OF_NOTICE_OBJ_NAME1_LEN])) = htonl(NOTICE_OBJ_NAME_LEN);
    memcpy(Telegram_buf + OF_NOTICE_OBJ_NAME1, NOTICE_OBJ_NAME, NOTICE_OBJ_NAME_LEN);
    *((long*)(&Telegram_buf[OF_NOTICE_ITEM_NAME1_LEN])) = htonl(NOTICE_ITEM_NAME1_LEN);
    memcpy(Telegram_buf + OF_NOTICE_ITEM_NAME1, NOTICE_ITEM_NAME1, NOTICE_ITEM_NAME1_LEN);
    *((long*)(&Telegram_buf[OF_NOTICE_DSP_NAME1_LEN])) = NOTICE_DISP_NAME_LEN;
    Telegram_buf[OF_NOTICE_ITEM_TYPE1] = ITEM_TYPE_TEXT;
    *((long*)(&Telegram_buf[OF_NOTICE_REPEAT_COUNT1])) = htonl(REPEATE_COUNT);
    *((long long*)(&Telegram_buf[OF_NOTICE_UNKNOWN1])) = htonl(NOTICE_UNKNOWN1);
    *((long*)(&Telegram_buf[OF_NOTICE_OBJ_NAME2_LEN])) = htonl(NOTICE_OBJ_NAME_LEN);
    memcpy(Telegram_buf + OF_NOTICE_OBJ_NAME2, NOTICE_OBJ_NAME, NOTICE_OBJ_NAME_LEN);
    *((long*)(&Telegram_buf[OF_NOTICE_ITEM_NAME2_LEN])) = htonl(NOTICE_ITEM_NAME2_LEN);
    memcpy(Telegram_buf + OF_NOTICE_ITEM_NAME2, NOTICE_ITEM_NAME2, NOTICE_ITEM_NAME2_LEN);
    *((long*)(&Telegram_buf[OF_NOTICE_DSP_NAME2_LEN])) = htonl(NOTICE_DISP_NAME_LEN);
    Telegram_buf[OF_NOTICE_ITEM_TYPE2] = ITEM_TYPE_PROPERTY;
    *((long*)(&Telegram_buf[OF_NOTICE_REPEAT_COUNT2])) = htonl(REPEATE_COUNT);
    *((long*)(&Telegram_buf[OF_NOTICE_PROPERTY_LEN])) = htonl((long)prop_len);
    memcpy(Telegram_buf + OF_NOTICE_PROPERTY, prop_buf, prop_len);
    *((long*)(&Telegram_buf[OF_NOTICE_OBJ_NAME3_LEN + prop_len])) = htonl(NOTICE_OBJ_NAME_LEN);
    memcpy(Telegram_buf + OF_NOTICE_OBJ_NAME3 + prop_len, NOTICE_OBJ_NAME, NOTICE_OBJ_NAME_LEN);
    *((long*)(&Telegram_buf[OF_NOTICE_ITEM_NAME3_LEN + prop_len])) = htonl(NOTICE_ITEM_NAME3_LEN);
    memcpy(Telegram_buf + OF_NOTICE_ITEM_NAME3 + prop_len, NOTICE_ITEM_NAME3, NOTICE_ITEM_NAME3_LEN);
    Telegram_buf[OF_NOTICE_UNKNOWN2 + prop_len] = NOTICE_UNKNOWN2;
    *((long*)(&Telegram_buf[OF_NOTICE_UNKNOWN3 + prop_len])) = htonl(NOTICE_UNKNOWN3);
    *((long*)(&Telegram_buf[OF_NOTICE_UNKNOWN4 + prop_len])) = htonl(NOTICE_UNKNOWN4);
    *((long*)(&Telegram_buf[OF_NOTICE_UNKNOWN5 + prop_len])) = htonl(NOTICE_UNKNOWN5);
    if(ERROR_RTN == send_telegram()){
        return(ERROR_RTN);
    }

    free(Telegram_buf);
    Telegram_buf = NULL;
    return(NORMAL_RTN);
}


/** 
 * SystemTapデータ読み込み
 * SystemTapが標準出力に送ったデータをpipeを介して読み込み、電文を編集する。
 * @return 0:正常 -1:異常
 */
int read_stap_data()
{
    char buffer[BUFSIZE + 1];	// SystemTapデータ読み込み用バッファ
    char *item_name;	// 項目名格納ポインタ
    char *str_value;	// 項目値格納ポインタ
    long long value;	// 項目値

    // 初期状態設定(NULLなら領域未確保)
    Telegram_buf = NULL;
    Node = NULL;

    memset(buffer, '\0', BUFSIZE+1);

    while(fgets(buffer, BUFSIZE, stdin) != NULL)
    {
	// データ終端チェック
	if(0 == strncmp(buffer, DATA_END, BUFSIZE)){
            if(ERROR_RTN == create_telegram()){	// 電文作成
                return(ERROR_RTN);
            }
            if(ERROR_RTN == send_telegram()){	// 電文送信
                return(ERROR_RTN);
            }
            continue;
        }

        // データ切り出し
        item_name = strtok(buffer,TOKEN_SEP);
        if(NULL == item_name){
            release_work_area();
            fprintf(stderr,"Not found item name.\n");
            return(ERROR_RTN);
        }
        str_value = strtok(NULL,TOKEN_SEP);
        if(NULL == str_value){
            release_work_area();
            fprintf(stderr,"Not found item value.\n");
            return(ERROR_RTN);
        }
        value = atoll(str_value);

        // データ格納
        if(ERROR_RTN == add_node(item_name, value)){
            return(ERROR_RTN);
        }

        memset(buffer, '\0', BUFSIZE+1);
    }

    return(NORMAL_RTN);
}
/**
 * データ格納
 * 構造体のリストを検索し、既存のデータがあれば値を更新、
 * なければリストに追加する。
 * @param char *item_name : 項目名
 * @param long long value : 項目値
 * return : 0 正常,-1 異常
 */
int add_node(char *item_name, long long value)
{
    struct st_node *ptr;

    // 先頭ノードがなければ作成しデータをセット
    if(NULL == Node){
        if(NULL == (Node = malloc(sizeof(struct st_node)))){
            close(Sock);
            fprintf(stderr, "Memory alloc error.\n");
            return(ERROR_RTN);
        }
        strncpy(Node->item_name, item_name, BUFSIZE);
        Node->item_name_len = strlen(Node->item_name);
        Node->value = value;
        Node->next = NULL;
        return(NORMAL_RTN);
    }

    // 既存ノードがあれば順次たどって処理する。
    ptr = Node;
    while(1)
    {
        // 既存ノードで項目名が一致したら値をセット
        if(0 == strncmp(ptr->item_name, item_name, BUFSIZE)){
            ptr->value = value;
            return(NORMAL_RTN);
        }

        // 一致しなくて次のノードが無ければ追加しデータをセット。
        if(NULL == ptr->next){
            if(NULL == (ptr->next = malloc(sizeof(struct st_node)))){
                release_work_area();
                fprintf(stderr, "Memory alloc error.\n");
                return(ERROR_RTN);
            }
            strncpy(ptr->next->item_name, item_name, BUFSIZE);
            ptr->next->item_name_len = strlen(ptr->next->item_name);
            ptr->next->value = value;
            ptr->next->next = NULL;
            return(NORMAL_RTN);
        }

        // 現在のノードで項目名が一致せず、次のノードがあればそちらへ移動。
        ptr = ptr->next;
    }
}

/**
 * ワークエリア解放
 * データ保持用、および電文編集用に確保したメモリを解放する。
 * ソケットもクローズする。
 */
void release_work_area(void)
{
    struct st_node *ptr;
    struct st_node *savptr;

    // 電文編集エリアを解放
    if(NULL != Telegram_buf){
        free(Telegram_buf);
    }

    // データ保持用リストを解放
    ptr = Node;
    while(1)
    {
        if(NULL == ptr){
             break;
        } 
        savptr = ptr->next;
        free(ptr);
        ptr = savptr;
       
    }

    // ソケットをクローズ
    close(Sock);

    return;
}

/**
 * リストサイズ取得
 * 電文編集エリアのサイズを計算するため、データリスト中の項目名の合計長と
 * データの件数を求める。
 * @param リストに格納したデータの合計サイズを受け取る変数のポインタ。
 * @return データの件数
 */
int get_list_size(int *datasize)
{
    struct st_node *ptr;
    int length = 0;
    int count = 0;

    ptr = Node;
    while(1)
    {
        if(NULL == ptr){
            *datasize = length;
            return(count);
        }

        length += ptr->item_name_len;
        ++count;
        ptr = ptr->next;
    }
}

/**
 * ノード取得
 * リストの各ノードに格納されたデータを戻す。
 * @param int order : データを戻すノードの順の指定(0番より開始)
 * @param char *buffer : 項目名を戻すバッファのポインタ
 * @param long long *value : 値を戻す変数のポインタ
 * @param int *item_name_len : 項目名長を戻す変数のポインタ
 * return : 0 データあり,-1 データなし
 */
int get_node(int order, char *buffer, long long *value, int *item_name_len)
{
    struct st_node *ptr;
    int count = 0;

    ptr = Node;
    while(1)
    {
        // 該当ノードなし
        if(NULL == ptr){
            release_work_area();
            fprintf(stderr, "Not found node\n");
            release_work_area();
            return(ERROR_RTN);
        }

        if(order == count){
            strncpy(buffer, ptr->item_name, BUFSIZE);
            *value = ptr->value;
            *item_name_len = ptr->item_name_len;
            return(NORMAL_RTN);
        }
        ptr = ptr->next;
        ++count;
    }
}

/**
 * 電文作成
 * DataCollectorに送るための電文を作成する。
 * @retrun : 0 正常,-1 異常
 */
int create_telegram()
{
    static int s_datacnt;
    int datasize;
    char item_name[BUFSIZE];
    long long value;
    int item_name_len;
    int loopcnt;
    int offset;
    int fixdatasize = OF_BODY_LEN;

    // まだバッファが確保されていなければ確保する。
    // 一度データを取得しないと、サイズが決定されないので、事前に確保できない。
    // 毎回、確保して解放するのは効率が悪いので、初回データ取得時に確保する。
    if(NULL == Telegram_buf){
        // データの件数と合計サイズを獲得する。
        s_datacnt = get_list_size(&datasize);

        // 必要なバッファのサイズを算出する
        Telegram_buf_size = HEADER_LEN +
            OF_BODY_LEN * s_datacnt +
            strlen(CRRTIME_OBJ_NAME) +
            strlen(OBJ_NAME) * (s_datacnt - 1) +
            datasize;

        // 電文編集バッファを確保する。
        if(NULL == (Telegram_buf = malloc(Telegram_buf_size))){
            release_work_area();
            fprintf(stderr, "Memory alloc error.\n");
            return(ERROR_RTN);
        }
    }
 
    memset(Telegram_buf, '\0', Telegram_buf_size);

    // ヘッダー部編集
    *((long*)(&Telegram_buf[OF_TELEGRAM_LEN])) = htonl((long)Telegram_buf_size);
    *((long long*)(&Telegram_buf[OF_TELEGRAM_ID])) = trans_endian(TELEGRAM_ID);
    Telegram_buf[OF_TELEGRAM_LASTTELEGRAM] = TELEGRAM_LASTTELEGRAM_VALUE;
    Telegram_buf[OF_TELEGRAM_TYPE] = TELEGRAM_TYPE_NORM;
    Telegram_buf[OF_TELEGRAM_REQUEST_TYPE] = TELEGRAM_REQUEST_RES;

    // ボディ部編集
    offset = HEADER_LEN;
    for(loopcnt = 0; loopcnt < s_datacnt; loopcnt++)
    {
        // 項目名と値を取り出し
        if(ERROR_RTN == get_node(loopcnt, item_name, &value, &item_name_len)){
            return(ERROR_RTN);
        }
        // 電文1項目のデータをセット
        offset = telegram_unit(offset, item_name, value, item_name_len);
    }

    return(NORMAL_RTN);
}

/**
 * 電文項目作成
 * 電文1項目のデータのオフセットを計算してデータをセットする
 * @param int top_offest : 電文の先頭のオフセット(配列の添え字)
 * @param char *item_name : 項目名のポインタ
 * @param long long value : 項目の値
 * @param int item_name_len : 項目名長
 * @return 次のデータの先頭のオフセット
 */
int telegram_unit(int top_offset, char *item_name, long long value, int item_name_len)
{
    int obj_name_len;
    int offset = top_offset;

    // 現在時刻のみオブジェクト名が異なる
    if(0 == strncmp(item_name, CRRTIME_ITEM_NAME, BUFSIZE)){
        // オブジェクト名長
        obj_name_len = strlen(CRRTIME_OBJ_NAME);
        *((long*)(&Telegram_buf[offset])) = htonl((long)obj_name_len);
        // オブジェクト名
        offset += LONG_LEN;
        memcpy(&Telegram_buf[offset], CRRTIME_OBJ_NAME, obj_name_len);
    } else {
        // オブジェクト名長
        obj_name_len = strlen(OBJ_NAME);
        *((long*)(&Telegram_buf[offset])) = htonl((long)obj_name_len);
        // オブジェクト名
        offset += LONG_LEN;
        memcpy(&Telegram_buf[offset], OBJ_NAME, obj_name_len);
    }

    // 項目名長
    offset += obj_name_len;
    *((long*)(&Telegram_buf[offset])) = htonl((long)item_name_len);
    // 項目名
    offset += LONG_LEN;
    memcpy(&Telegram_buf[offset], item_name, item_name_len);
    // 表示名長(常に0)
    offset += item_name_len;
    // 項目型
    offset += LONG_LEN;
    Telegram_buf[offset] = ITEM_TYPE_LONG;
    // 繰返し回数
    offset += BYTE_LEN;
    *((long*)(&Telegram_buf[offset])) = htonl(REPEATE_COUNT);
    // 値
    offset += LONG_LEN;
    *((long long*)(&Telegram_buf[offset])) = trans_endian(value);

    offset += VALUE_LEN;
    return(offset);
}

/**
 * データ送信
 * 電文をEndoSnipeのData Collectorに送信し、応答を受け取る。
 * @retrun 0:正常 -1:異常
 */
int send_telegram()
{
/* test用
    int i;
    for(i = 0; i < Telegram_buf_size; i++) 
    {
        printf("%02X ",(unsigned char)Telegram_buf[i]);
    }
    printf("\n");
    fflush(stdout);
    return(NORMAL_RTN);

*/
    char head_buf[HEADER_LEN+1];
    char *body_buf;
    long telegram_size;
    int body_len;

    if(-1 == write(Sock, Telegram_buf, Telegram_buf_size)){	// 送信
        release_work_area();
        fprintf(stderr, "write error : errno %d\n", errno);
        return(ERROR_RTN);
    }

    while(1)
    {

        if(-1 == read(Sock, head_buf, HEADER_LEN)){	// 返信ヘッダ部受信
            release_work_area();
            fprintf(stderr, "read error : errno %d\n", errno);
            return(ERROR_RTN);
        }

        telegram_size = htonl(*(long*)head_buf);	// 残りがあれば受信
        if(telegram_size > (long)HEADER_LEN){
            body_len = telegram_size - HEADER_LEN + 1;
            if(NULL == (body_buf = malloc(body_len))){
                release_work_area();
                fprintf(stderr, "malloc error\n");
                return(ERROR_RTN);
            }
            if(-1 == read(Sock, body_buf, body_len)){
                free(body_buf);
                release_work_area();
                fprintf(stderr, "read error : errno %d\n", errno);
                return(ERROR_RTN);
            }
            free(body_buf);
        }
        if((TELEGRAM_TYPE_NORM == head_buf[OF_TELEGRAM_TYPE]) &&
            (TELEGRAM_REQUEST_REQ == head_buf[OF_TELEGRAM_REQUEST_TYPE])){
            break;
        }
    }

    return(NORMAL_RTN);   
}
/**
 * エンディアン変換 
 * 64bit little endianのデータをbig endiganに変換する。
 * @param long input : 入力値
 * @return long : 出力値
 */
long long trans_endian(long long input)
{
    union {
        long long llong_value;
        char byte_value[8];
    } before, after;
    int loopcnt;

    before.llong_value = input;
    for(loopcnt = 0; loopcnt < 8; loopcnt++)
    {
        after.byte_value[7 - loopcnt] = before.byte_value[loopcnt];
    }
// printf("%lld\n",input);
    return(after.llong_value);
}

/**
 * SIGNAL処理
 * SIGINTを受け取ると、バッファを解放し、ネットワーク接続を切断して終了する。
 */
void sigcatch(int signo)
{
    printf("good bye%d\n",signo);
    release_work_area();
    exit(0);
}

/**
 * 改行削除処理
 * @param char *buffer
 * @retrun 0:正常 -1:異常
 */
int remove_lf(char *buffer)
{
    int loopcnt;

    for(loopcnt=0; loopcnt<BUFSIZE; loopcnt++)
    {
        if('\n' == buffer[loopcnt]){
            buffer[loopcnt] = '\0';
            return(NORMAL_RTN);
        }
    }
    fprintf(stderr, "Max line length is %d\n", BUFSIZE);
    return(ERROR_RTN);
}
