/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.common;

/**
 * ENdoSnipeCommon のための定数インターフェースです。<br/>
 * 
 * @author y-komori
 */
public interface Constants
{
    // プリファレンスキー
    /** ログレベルのためのプリファレンスキー */
    String PREF_LOG_LEVEL = "pref.endosnipecommon.loglevel";

    // ////////////////
    // 制御用項目名 //
    // ////////////////

    /** 項目名（初期化処理） */
    String ITEMNAME_INITIALIZE = "/initialize";

    /** 項目名(ヒープダンプ取得) */
    String ITEMNAME_HEAPDUMP = "/heapDump";

    /** 項目名(スレッドダンプ取得) */
    String ITEMNAME_THREADDUMP = "/threadDump";

    // ////////////////
    // 取得用項目名 //
    // ////////////////
    // 項目名(DBから取得する系列を指定するキー文字列)

    // -----------------------------------------------------
    // 基礎項目

    /** 項目名（時刻） */
    // String ITEMNAME_TIME = "/time";
    String ITEMNAME_TIME = "/common/fundamental/time/current";

    /** 項目名（リソース値での取得時刻） */
    // String ITEMNAME_ACQUIREDTIME = "/acquiredTime";
    String ITEMNAME_ACQUIREDTIME = "/common/fundamental/time/acquired";

    /** 項目名(呼び出し回数) */
    // String ITEMNAME_CALL_COUNT = "/callCount";
    String ITEMNAME_CALL_COUNT = "/common/fundamental/callCount";

    /** 項目名(今回の処理時間) */
    // String ITEMNAME_CURRENT_INTERVAL = "/currentInterval";
    String ITEMNAME_CURRENT_INTERVAL = "/common/fundamental/interval/current";

    /** 項目名(合計処理時間) */
    // String ITEMNAME_TOTAL_INTERVAL = "/totalInterval";
    String ITEMNAME_TOTAL_INTERVAL = "/common/fundamental/interval/total";

    /** 項目名(最大処理時間) */
    // String ITEMNAME_MAXIMUM_INTERVAL = "/maximumInterval";
    String ITEMNAME_MAXIMUM_INTERVAL = "/common/fundamental/interval/max";

    /** 項目名(最小処理時間) */
    // String ITEMNAME_MINIMUM_INTERVAL = "/minimumInterval";
    String ITEMNAME_MINIMUM_INTERVAL = "/common/fundamental/interval/min";

    /** 項目名(合計CPU時間) */
    // String ITEMNAME_TOTAL_CPU_INTERVAL = "/totalCpuInterval";
    String ITEMNAME_TOTAL_CPU_INTERVAL = "/common/fundamental/cpuInterval/total";

    /** 項目名(最大CPU時間) */
    // String ITEMNAME_MAXIMUM_CPU_INTERVAL = "/maximumCpuInterval";
    String ITEMNAME_MAXIMUM_CPU_INTERVAL = "/common/fundamental/cpuInterval/max";

    /** 項目名(最小CPU時間) */
    // String ITEMNAME_MINIMUM_CPU_INTERVAL = "/minimumCpuInterval";
    String ITEMNAME_MINIMUM_CPU_INTERVAL = "/common/fundamental/cpuInterval/min";

    /** 項目名(合計ユーザ時間) */
    // String ITEMNAME_TOTAL_USER_INTERVAL = "/totalUserInterval";
    String ITEMNAME_TOTAL_USER_INTERVAL = "/common/fundamental/userInterval/total";

    /** 項目名(最大ユーザ時間) */
    // String ITEMNAME_MAXIMUM_USER_INTERVAL = "/maximumUserInterval";
    String ITEMNAME_MAXIMUM_USER_INTERVAL = "/common/fundamental/userInterval/max";

    /** 項目名(最小ユーザ時間) */
    // String ITEMNAME_MINIMUM_USER_INTERVAL = "/minimumUserInterval";
    String ITEMNAME_MINIMUM_USER_INTERVAL = "/common/fundamental/userInterval/min";

    // -----------------------------------------------------
    // 累積値

    /** 項目名(合計処理時間) */
    // String ITEMNAME_ACCUMULATED_TOTAL_INTERVAL = "totalAccumulatedInterval";
    String ITEMNAME_ACCUMULATED_TOTAL_INTERVAL = "/common/accumulated/interval/total";

    /** 項目名(最大処理時間) */
    // String ITEMNAME_ACCUMULATED_MAXIMUM_INTERVAL =
    // "maximumAccumulatedInterval";
    String ITEMNAME_ACCUMULATED_MAXIMUM_INTERVAL = "/common/accumulated/interval/max";

    /** 項目名(最小処理時間) */
    // String ITEMNAME_ACCUMULATED_MINIMUM_INTERVAL =
    // "minimumAccumulatedInterval";
    String ITEMNAME_ACCUMULATED_MINIMUM_INTERVAL = "/common/accumulated/interval/min";

    /** 項目名(合計CPU時間) */
    // String ITEMNAME_ACCUMULATED_TOTAL_CPU_INTERVAL =
    // "totalAccumulatedCpuInterval";
    String ITEMNAME_ACCUMULATED_TOTAL_CPU_INTERVAL = "/common/accumulated/cpuInterval/total";

    /** 項目名(最大CPU時間) */
    // String ITEMNAME_ACCUMULATED_MAXIMUM_CPU_INTERVAL =
    // "maximumAccumulatedCpuInterval";
    String ITEMNAME_ACCUMULATED_MAXIMUM_CPU_INTERVAL = "/common/accumulated/cpuInterval/max";

    /** 項目名(最小CPU時間) */
    // String ITEMNAME_ACCUMULATED_MINIMUM_CPU_INTERVAL =
    // "minimumAccumulatedCpuInterval";
    String ITEMNAME_ACCUMULATED_MINIMUM_CPU_INTERVAL = "/common/accumulated/cpuInterval/min";

    /** 項目名(合計CPU時間) */
    // String ITEMNAME_ACCUMULATED_TOTAL_USER_INTERVAL =
    // "totalAccumulatedUserInterval";
    String ITEMNAME_ACCUMULATED_TOTAL_USER_INTERVAL = "/common/accumulated/userInterval/total";

    /** 項目名(最大CPU時間) */
    // String ITEMNAME_ACCUMULATED_MAXIMUM_USER_INTERVAL =
    // "maximumAccumulatedUserInterval";
    String ITEMNAME_ACCUMULATED_MAXIMUM_USER_INTERVAL = "/common/accumulated/userInterval/max";

    /** 項目名(最小CPU時間) */
    // String ITEMNAME_ACCUMULATED_MINIMUM_USER_INTERVAL =
    // "minimumAccumulatedUserInterval";
    String ITEMNAME_ACCUMULATED_MINIMUM_USER_INTERVAL = "/common/accumulated/userInterval/min";

    // -----------------------------------------------------
    // システム項目: 基礎値

    /** 項目名（リソース値でのプロセッサ数） */
    // String ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT =
    // "/system.cpu.processor.count";
    String ITEMNAME_SYSTEM_CPU_PROCESSOR_COUNT = "/system/cpu/processor/number";

    /** 項目名（ユーザモードでのCPU使用量） */
    // String ITEMNAME_SYSTEM_CPU_USERMODE_TIME = "/system.cpu.usermode.time";
    String ITEMNAME_SYSTEM_CPU_USERMODE_TIME = "/system/cpu/time/user(d)";

    /** 項目名（システムモードでのCPU使用量） */
    // String ITEMNAME_SYSTEM_CPU_SYSTEM_TIME = "/system.cpu.system.time";
    String ITEMNAME_SYSTEM_CPU_SYSTEM_TIME = "/system/cpu/time/system(d)";

    /** 項目名（iowaitでのCPU使用量） */
    // String ITEMNAME_SYSTEM_CPU_IOWAIT_TIME = "/system.cpu.iowait.time";
    String ITEMNAME_SYSTEM_CPU_IOWAIT_TIME = "/system/cpu/time/iowait(d)";

    // -----------------------------------------------------
    // システム項目: CPU(算出値)

    /** 項目名（CPU使用率（システム）の合計） */
    // String ITEMNAME_SYSTEM_CPU_TOTAL_USAGE = "/system.cpu.total.usage";
    String ITEMNAME_SYSTEM_CPU_TOTAL_USAGE = "/system/cpu/usage/total:%";

    /** 項目名（CPU使用率（システム）のうちのシステムの使用率） */
    // String ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE = "/system.cpu.system.usage";
    String ITEMNAME_SYSTEM_CPU_SYSTEM_USAGE = "/system/cpu/usage/system:%";

    /** 項目名（CPU使用率（システム）のうちのI/O Waitの使用率） */
    // String ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE = "/system.cpu.iowait.usage";
    String ITEMNAME_SYSTEM_CPU_IOWAIT_USAGE = "/system/cpu/usage/iowait:%";

    /** 項目名（CPU使用率（システム）のうちのユーザーの使用率） */
    String ITEMNAME_SYSTEM_CPU_USER_USAGE = "/system/cpu/usage/user:%";

    /** 項目名（CPUごとの負荷：後ろにCPUコア番号(1～N)を付ける） */
    // String ITEMNAME_CPU_ARRAY = "/sys_cputime_1";
    String ITEMNAME_CPU_ARRAY = "/system/cpu/load(d)";

    // -----------------------------------------------------
    // システム項目: メモリ(算出値)

    /** 項目名（システム全体のメモリ最大値） */
    // String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX =
    // "/system.memory.physical.max";
    String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_MAX = "/system/memory/physical/max:bytes";

    /** 項目名（システム全体の空きメモリ） */
    // String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE =
    // "/system.memory.physical.free";
    String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_FREE = "/system/memory/physical/free:bytes";

    /** 項目名（システム全体のメモリ使用量） */
    // String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_USED =
    // "/system.memory.physical.used";
    String ITEMNAME_SYSTEM_MEMORY_PHYSICAL_USED = "/system/memory/physical/used:bytes";

    /** 項目名（リソース値での仮想メモリ容量） */
    // String ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED =
    // "/system.memory.virtual.used";
    String ITEMNAME_SYSTEM_MEMORY_VIRTUAL_USED = "/system/memory/virtual/used:bytes";

    /** 項目名（リソース値でのスワップ領域容量） */
    // String ITEMNAME_SYSTEM_MEMORY_SWAP_MAX = "/system.memory.swap.max";
    String ITEMNAME_SYSTEM_MEMORY_SWAP_MAX = "/system/memory/swap/max:bytes";

    /** 項目名（リソース値でのスワップ領域空き容量） */
    // String ITEMNAME_SYSTEM_MEMORY_SWAP_FREE = "/system.memory.swap.free";
    String ITEMNAME_SYSTEM_MEMORY_SWAP_FREE = "/system/memory/swap/free:bytes";

    /** 項目名（page in） */
    // String ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT =
    // "/system.memory.pagein.count";
    String ITEMNAME_SYSTEM_MEMORY_PAGEIN_COUNT = "/system/memory/pagein(d)";

    /** 項目名（page out） */
    // String ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT =
    // "/system.memory.pageout.count";
    String ITEMNAME_SYSTEM_MEMORY_PAGEOUT_COUNT = "/system/memory/pageout(d)";

    // -----------------------------------------------------
    // システム項目: ファイル

    /** 項目名（リソース値での、ファイル入力量） */
    // String ITEMNAME_FILEINPUTSIZEOFSYSTEM = "/system.file.input";
    String ITEMNAME_FILEINPUTSIZEOFSYSTEM = "/system/file/read:bytes(d)";

    /** 項目名（リソース値での、ファイル出力量） */
    // String ITEMNAME_FILEOUTPUTSIZEOFSYSTEM = "/system.file.output";
    String ITEMNAME_FILEOUTPUTSIZEOFSYSTEM = "/system/file/write:bytes(d)";

    /** 項目名（FD/ハンドル数） */
    // String ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER =
    // "/system.handle.total.number";
    String ITEMNAME_SYSTEM_HANDLE_TOTAL_NUMBER = "/system/file/handle/number";

    // -----------------------------------------------------
    // プロセス項目: CPU(基礎値)

    /** 項目名（CPU使用時間:total） */
    // String ITEMNAME_PROCESS_CPU_TOTAL_TIME = "/process.cpu.total.time";
    String ITEMNAME_PROCESS_CPU_TOTAL_TIME = "/process/cpu/time/total(d)";

    /** 項目名（CPU使用時間:system） */
    // String ITEMNAME_PROC_CPUTIME_SYS = "/process.cpu.system.time";
    String ITEMNAME_PROCESS_CPU_SYSTEM_TIME = "/process/cpu/time/system(d)";

    /** 項目名（CPU使用時間:iowait） */
    // String ITEMNAME_PROCESS_CPU_IOWAIT_TIME = "/process.cpu.iowait.time";
    String ITEMNAME_PROCESS_CPU_IOWAIT_TIME = "/process/cpu/time/iowait(d)";

    /** 項目名（リソース値でのJava稼働時間） */
    // String ITEMNAME_JAVAUPTIME = "/javaUpTime";
    String ITEMNAME_JAVAUPTIME = "/process/fundamental/uptime";

    // -----------------------------------------------------
    // プロセス項目: CPU(算出値)

    /** 項目名（CPU使用率（プロセス）の合計） */
    // String ITEMNAME_PROCESS_CPU_TOTAL_USAGE = "/process.cpu.total.usage";
    String ITEMNAME_PROCESS_CPU_TOTAL_USAGE = "/process/cpu/usage/total:%";

    /** 項目名（CPU使用率（プロセス）のうちのシステムの使用率） */
    // String ITEMNAME_PROCESS_CPU_SYSTEM_USAGE = "/process.cpu.system.usage";
    String ITEMNAME_PROCESS_CPU_SYSTEM_USAGE = "/process/cpu/usage/system:%";

    /** 項目名（CPU使用率（プロセス）のうちのIOWAITの使用率） */
    // String ITEMNAME_PROCESS_CPU_IOWAIT_USAGE = "/process.cpu.iowait.usage";
    String ITEMNAME_PROCESS_CPU_IOWAIT_USAGE = "/process/cpu/usage/iowait:%";

    /** 項目名（CPU使用率（プロセス）のうちのUSERの使用率） */
    String ITEMNAME_PROCESS_CPU_USER_USAGE = "/process/cpu/usage/user:%";

    // -----------------------------------------------------
    // プロセス項目: メモリ(算出値)

    /** 項目名（リソース値での物理メモリ容量） */
    // String ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX =
    // "/process.memory.physical.max";
    String ITEMNAME_PROCESS_MEMORY_PHYSICAL_MAX = "/process/memory/physical/max:bytes";

    /** 項目名（物理メモリ使用量（プロセス）） */
    // String ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED =
    // "/process.memory.physical.used";
    String ITEMNAME_PROCESS_MEMORY_PHYSICAL_USED = "/process/memory/physical/used:bytes";

    /** 項目名（リソース値での物理メモリ空き容量） */
    // String ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE =
    // "/process.memory.physical.free";
    String ITEMNAME_PROCESS_MEMORY_PHYSICAL_FREE = "/process/memory/physical/free:bytes";

    /** 項目名（リソース値での仮想マシンメモリ容量） */
    // String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX =
    // "/process.memory.virtualmachine.max";
    String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_MAX = "/process/memory/virtual/max:bytes";

    /** 項目名（仮想メモリ使用量（プロセス）） */
    // String ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED =
    // "/process.memory.virtual.used";
    String ITEMNAME_PROCESS_MEMORY_VIRTUAL_USED = "/process/memory/virtual/used:bytes";

    /** 項目名（リソース値での仮想マシンメモリ空き容量） */
    // String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE =
    // "/process.memory.virtualmachine.free";
    String ITEMNAME_PROCESS_MEMORY_VIRTUALMACHINE_FREE = "/process/memory/virtual/free:bytes";

    /** 項目名（リソース値でのヒープメモリコミット容量） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT =
    // "/javaprocess.memory.heap.commit";
    String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_COMMIT = "/process/heap/commit:bytes";

    /** 項目名（リソース値でのヒープメモリ最大） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX =
    // "/javaprocess.memory.heap.max";
    String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_MAX = "/process/heap/max:bytes";

    /** 項目名（リソース値でのヒープメモリ使用量） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED =
    // "/javaprocess.memory.heap.used";
    String ITEMNAME_JAVAPROCESS_MEMORY_HEAP_USED = "/process/heap/used:bytes";

    /** 項目名（リソース値でのヒープ以外のメモリコミット容量） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT =
    // "/javaprocess.memory.nonheap.commit";
    String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_COMMIT = "/process/nonheap/commit:bytes";

    /** 項目名（リソース値でのヒープ以外のメモリ最大） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX =
    // "/javaprocess.memory.nonheap.max";
    String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_MAX = "/process/nonheap/max:bytes";

    /** 項目名（リソース値でのヒープ以外のメモリ使用量） */
    // String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED =
    // "/javaprocess.memory.nonheap.used";
    String ITEMNAME_JAVAPROCESS_MEMORY_NONHEAP_USED = "/process/nonheap/used:bytes";

    /** 項目名（メジャーフォールト数） */
    // String ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT =
    // "/process.memory.majorfault.count";
    String ITEMNAME_PROCESS_MEMORY_MAJORFAULT_COUNT = "/process/memory/majorfault/count(d)";

    // -----------------------------------------------------
    // プロセス項目: ネットワーク

    /** 項目名（リソース値での、プロセス全体のネットワークデータ受信量） */
    // String ITEMNAME_NETWORKINPUTSIZEOFPROCESS = "/proc.network.input";
    String ITEMNAME_NETWORKINPUTSIZEOFPROCESS = "/process/network/read:bytes(d)";

    /** 項目名（リソース値での、プロセス全体のネットワークデータ送信量） */
    // String ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS = "/proc.network.output";
    String ITEMNAME_NETWORKOUTPUTSIZEOFPROCESS = "/process/network/write:bytes(d)";

    // -----------------------------------------------------
    // プロセス項目: ファイル

    /** 項目名（リソース値での、ファイル入力量） */
    // String ITEMNAME_FILEINPUTSIZEOFPROCESS = "/proc.file.input";
    String ITEMNAME_FILEINPUTSIZEOFPROCESS = "/process/file/read:bytes(d)";

    /** 項目名（リソース値での、ファイル出力量） */
    // String ITEMNAME_FILEOUTPUTSIZEOFPROCESS = "/proc.file.output";
    String ITEMNAME_FILEOUTPUTSIZEOFPROCESS = "/process/file/write:bytes(d)";

    // -----------------------------------------------------
    // プロセス項目: スレッド

    /** 項目名（スレッド数） */
    // String ITEMNAME_PROCESS_THREAD_TOTAL_COUNT =
    // "/process.thread.total.count";
    String ITEMNAME_PROCESS_THREAD_TOTAL_COUNT = "/process/thread/native";

    /** 項目名（FD/ハンドル数） */
    // String ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER =
    // "/process.handle.total.number";
    String ITEMNAME_PROCESS_HANDLE_TOTAL_NUMBER = "/process/file/handle/number";

    /** 項目名（リソース値での、スレッド数） */
    // String ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT =
    // "/javaprocess.thread.total.count";
    String ITEMNAME_JAVAPROCESS_THREAD_TOTAL_COUNT = "/process/thread/java";

    // -----------------------------------------------------
    // プロセス情報: コレクション情報

    /** 項目名（リソース値での、コレクションの数） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT =
    // "/javaprocess.collection.list.count";
    String ITEMNAME_JAVAPROCESS_COLLECTION_LIST_COUNT = "/process/collection/list";

    /** 項目名（リソース値での、コレクションの数） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_QUEUE_COUNT =
    // "/javaprocess.collection.queue.count";
    String ITEMNAME_JAVAPROCESS_COLLECTION_QUEUE_COUNT = "/process/collection/queue";

    /** 項目名（リソース値での、コレクションの数） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_SET_COUNT =
    // "/javaprocess.collection.set.count";
    String ITEMNAME_JAVAPROCESS_COLLECTION_SET_COUNT = "/process/collection/set";

    /** 項目名（リソース値での、コレクションの数） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_MAP_COUNT =
    // "/javaprocess.collection.map.count";
    String ITEMNAME_JAVAPROCESS_COLLECTION_MAP_COUNT = "/process/collection/map";

    /** 項目名（リソース値での、クラスヒストグラムから取得したオブジェクトのサイズ） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_SIZE =
    // "/javaprocess.collection.histogram.size";
    String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_SIZE = "/process/histogram/object/size:bytes";

    /** 項目名（リソース値での、クラスヒストグラムから取得したオブジェクトの数） */
    // String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_COUNT =
    // "/javaprocess.collection.histogram.count";
    String ITEMNAME_JAVAPROCESS_COLLECTION_HISTOGRAM_COUNT = "/process/histogram/object/number";

    // -----------------------------------------------------
    // プロセス情報: レスポンス情報

    /** 項目名（リソース値での、Turn Around Timeの平均値） */
    // String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE =
    // "/process.response.time.average";
    String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE = "/response/total/average";

    /** 項目名（リソース値での、Turn Around Timeの最大値） */
    // String ITEMNAME_PROCESS_RESPONSE_TIME_MAX = "/process.response.time.max";
    String ITEMNAME_PROCESS_RESPONSE_TIME_MAX = "/response/total/max";

    /** 項目名（リソース値での、Turn Around Timeの最小値） */
    // String ITEMNAME_PROCESS_RESPONSE_TIME_MIN = "/process.response.time.min";
    String ITEMNAME_PROCESS_RESPONSE_TIME_MIN = "/response/total/min";

    /** 項目名（リソース値での、Turn Around Timeの呼び出し回数） */
    // String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT =
    // "/process.response.total.count";
    String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT = "/response/total/count";

    /** 項目名（リソース値での、Turn Around Timeの平均値(SQL以外)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE_EXCL_SQL = "/response/nosql/average";

    /** 項目名（リソース値での、Turn Around Timeの最大値(SQL以外)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_MAX_EXCL_SQL = "/response/nosql/max";

    /** 項目名（リソース値での、Turn Around Timeの最小値(SQL以外)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_MIN_EXCL_SQL = "/response/nosql/min";

    /** 項目名（リソース値での、Turn Around Timeの呼び出し回数(SQL以外)） */
    String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT_EXCL_SQL = "/response/nosql/count";

    /** 項目名（リソース値での、Turn Around Timeの平均値(SQLのみ)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_AVERAGE_ONLY_SQL = "/response/sql/average";

    /** 項目名（リソース値での、Turn Around Timeの最大値(SQLのみ)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_MAX_ONLY_SQL = "/response/sql/max";

    /** 項目名（リソース値での、Turn Around Timeの最小値(SQLのみ)） */
    String ITEMNAME_PROCESS_RESPONSE_TIME_MIN_ONLY_SQL = "/response/sql/min";

    /** 項目名（リソース値での、Turn Around Timeの呼び出し回数(SQLのみ)） */
    String ITEMNAME_PROCESS_RESPONSE_TOTAL_COUNT_ONLY_SQL = "/response/sql/count";

    /** 項目名(HTTP例外) */
    // String ITEMNAME_JAVAPROCESS_HTTP_EXCEPTION =
    // "/javaprocess.http.exception.occurence.count";
    String ITEMNAME_JAVAPROCESS_HTTP_EXCEPTION = "/response/http/thrown";

    // -----------------------------------------------------
    // プロセス情報: VM

    /** 項目名(Java 仮想マシンが実行を開始してからロードされたクラスの合計数) */
    // String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL =
    // "/javaprocess.classloader.class.total";
    String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_TOTAL = "/process/classloader/class/total";

    /** 項目名(Java 仮想マシンに現在ロードされているクラスの数) */
    // String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT =
    // "/javaprocess.classloader.class.current";
    String ITEMNAME_JAVAPROCESS_CLASSLOADER_CLASS_CURRENT = "/process/classloader/class/current";

    /** 項目名（リソース値での、トータルのガベージコレクションの時間） */
    // String ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL = "/javaprocess.gc.time.total";
    String ITEMNAME_JAVAPROCESS_GC_TIME_TOTAL = "/process/gc/time/total(d)";

    /** 項目名（リソース値での、ファイナライズ待ちオブジェクト数） */
    // String ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT =
    // "/javaprocess.gc.finalizequeue.count";
    String ITEMNAME_JAVAPROCESS_GC_FINALIZEQUEUE_COUNT = "/process/gc/finalizequeue/number";

    /** 項目名(例外発生回数) */
    // String ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT =
    // "/javaprocess.exception.occurence.count";
    String ITEMNAME_JAVAPROCESS_EXCEPTION_OCCURENCE_COUNT = "/response/java/thrown";

    /** 項目名(ストール発生回数) */
    // String ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT =
    // "/javaprocess.method.stall.count";
    String ITEMNAME_JAVAPROCESS_STALL_OCCURENCE_COUNT = "/response/java/stalled";

    // -----------------------------------------------------
    // プロセス情報: APサーバ情報

    /** 項目名（APサーバのワーカースレッドプール(最大数,稼動数)） */
    // String ITEMNAME_SERVER_POOL = "/serverPool";
    String ITEMNAME_SERVER_POOL = "/process/apserver/worker/number";

    /** 項目名（プール(最大数,稼動数)） */
    // String ITEMNAME_POOL_SIZE = "/poolSize";
    String ITEMNAME_POOL_SIZE = "/process/commons/poolsize";

    /** 項目名（リソース値での、HttpSession数） */
    // String ITEMNAME_HTTPSESSION_NUM = "/httpSessionNumber";
    String ITEMNAME_HTTPSESSION_NUM = "/process/httpsession/instance/number";

    /** 項目名（リソース値での、HttpSession総サイズ） */
    // String ITEMNAME_HTTPSESSION_TOTALSIZE = "/httpSessionTotalSize";
    String ITEMNAME_HTTPSESSION_TOTALSIZE = "/process/httpsession/size/total:bytes";

    // -----------------------------------------------------
    // Agent情報

    /** 項目名（カバレッジ） */
    // String ITEMNAME_COVERAGE = "/coverage";
    String ITEMNAME_COVERAGE = "/javelin/converter/coverage:%";

    /** 項目名（イベント種別毎のイベント発生回数） */
    // String ITEMNAME_EVENT_COUNT = "/eventCount";
    String ITEMNAME_EVENT_COUNT = "/javelin/event/occured/count";

    /** 項目名(CallNodeTree数) */
    // String ITEMNAME_NODECOUNT = "/callTreeNodeCount";
    String ITEMNAME_NODECOUNT = "/javelin/calltreenode/generated/current";

    /** 項目名(最大CallTreeNode数) */
    // String ITEMNAME_MAX_NODECOUNT = "/maxCallTreeNodeCount";
    String ITEMNAME_MAX_NODECOUNT = "/javelin/calltreenode/generated/max";

    /** 項目名(合計CallTreeNode数) */
    // String ITEMNAME_ALL_NODECOUNT = "/allCallTreeNodeCount";
    String ITEMNAME_ALL_NODECOUNT = "/javelin/calltreenode/generated/all";

    /** 項目名(CallTree数) */
    // String ITEMNAME_CALLTREECOUNT = "/callTreeCount";
    String ITEMNAME_CALLTREECOUNT = "/javelin/calltree/generated/current";

    /** 項目名(JavelinConverterで変換したメソッド数) */
    // String ITEMNAME_CONVERTEDMETHOD = "/convertedMethodCount";
    String ITEMNAME_CONVERTEDMETHOD = "/javelin/converter/methods/converted";

    /** 項目名(JavelinConverterで変換対象から除外したメソッド数) */
    // String ITEMNAME_EXCLUDEDMETHOD = "/excludedMethodCount";
    String ITEMNAME_EXCLUDEDMETHOD = "/javelin/converter/methods/excluded";

    /** 項目名(JavelinConverterで変換を行ったメソッドのうち、呼び出されたメソッド数) */
    // String ITEMNAME_CALLEDMETHODCOUNT = "/calledMethodCount";
    String ITEMNAME_CALLEDMETHODCOUNT = "/javelin/converter/methods/executed";

    /** 項目名（JVNファイル JVNファイル名） */
    // String ITEMNAME_JVN_FILE_NAME = "/jvnFileName";
    String ITEMNAME_JVN_FILE_NAME = "/javelin/jvnfile/name";

    /** 項目名（JVNファイル JVNファイル内容） */
    // String ITEMNAME_JVN_FILE_CONTENT = "/jvnFileContent";
    String ITEMNAME_JVN_FILE_CONTENT = "/javelin/jvnfile/contents";

    // -----------------------------------------------------
    // その他

    /** 項目名（JMX計測値） */
    // String ITEMNAME_JMX = "jmx";
    String ITEMNAME_JMX = "/jmx";

    // ItemType用数字
    /** 不明 */
    int ITEMTYPE_UNKNOWN = -1;

    /** byte型 */
    int ITEMTYPE_BYTE = 0;

    /** short型 */
    int ITEMTYPE_SHORT = 1;

    /** int型 */
    int ITEMTYPE_INT = 2;

    /** long型 */
    int ITEMTYPE_LONG = 3;

    /** float型 */
    int ITEMTYPE_FLOAT = 4;

    /** double型 */
    int ITEMTYPE_DOUBLE = 5;

    /** String型 */
    int ITEMTYPE_STRING = 6;

    /** JSON型 */
    int ITEMTYPE_JMX = 7;

    // -----------------------------------------------------
    // 監視情報

    /** 項目名(全ての呼び出し元の名前) */
    String ITEMNAME_ALL_CALLER_NAMES = "/allCallerNames";

    /** 項目名(ルート) */
    String ITEMNAME_ROOT_NODE = "/rootNode";

    /** 項目名(計測対象) */
    String ITEMNAME_TARGET = "/target";

    /** 項目名(トランザクショングラフ出力対象) */
    String ITEMNAME_TRANSACTION_GRAPH = "/transactionGraph";

    /** 項目名(TATアラーム閾値) */
    String ITEMNAME_ALARM_THRESHOLD = "/alarmThreshold";

    /** 項目名(CPUアラーム閾値) */
    String ITEMNAME_ALARM_CPU_THRESHOLD = "/alarmCpuThreshold";

    /** 項目名(クラス名) */
    String ITEMNAME_CLASSTOREMOVE = "/classToRemove";

}
