#include "jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter.h"
#include <conio.h>
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <pdh.h>	// Pdh.Lib
#include <tchar.h>	// tchar.h
#include <assert.h>	// assert()
#include <pdhmsg.h>	// PDH_MORE_DATA

#define	TactfulMalloc(a)	( (a) ? malloc(a) : 0 )
#define	TactfulFree(a)	if( a ){ free(a); a = NULL; }

/** �J�E���^�l�擾�Ɏ��s�����ꍇ�̖߂�l */
#define	DOUBLE_ERROR_VALUE	(0.0)

/** �J�E���^�̃C���X�^���X���̍ő�T�C�Y */
#define	INSTANCE_NAME_SIZE	(1024)

/** �J�E���^�p�X���̍ő�T�C�Y */
#define	COUNTER_PATH_SIZE	(1024)

/** GetIDProcess() ��ID Process���擾�ł��Ȃ������ꍇ�̖߂�l */
#define	INVALID_ID_PROCESS	(-1)

#define INITIALPATHSIZE 1000

PDH_HQUERY   hQuery;
PDH_HCOUNTER hCounterSysCPUSys;
PDH_HCOUNTER hCounterSysCPUUser;
PDH_HCOUNTER hCounterSysPageUsage;
PDH_HCOUNTER hCounterSysPageBytes;
PDH_HCOUNTER hCounterSysVirtualBytes;
PDH_HCOUNTER hCounterSysPageIn;
PDH_HCOUNTER hCounterSysPageOut;
PDH_HCOUNTER hCounterSysProcUser;
PDH_HCOUNTER hCounterSysProcSys;
PDH_HCOUNTER hCounterSysMajFlt;
PDH_HCOUNTER hCounterSysVSize;
PDH_HCOUNTER hCounterSysRSS;
PDH_HCOUNTER hCounterSysNumThreads;
PDH_HCOUNTER hCounterSysNumFDs;
PDH_HCOUNTER hCounterProcNumFDs;
TCHAR szCurrentInstanceName[INSTANCE_NAME_SIZE];




/****************************************************************************/
/*                    C++ ��������̂݌Ă΂��֐��̒�`                    */
/****************************************************************************/


/**
 * �w�肳�ꂽ�J�E���^�̒l�� double �l�Ŏ擾���܂��B
 *
 * @param hCounter �J�E���^�n���h��
 * @return ���������ꍇ�̓J�E���^�̒l�A���s�����ꍇ�� DOUBLE_ERROR_VALUE (-1)
 */
static double GetFormattedCounterValueByDouble(PDH_HCOUNTER hCounter)
{
	PDH_FMT_COUNTERVALUE fntValue;
	PDH_STATUS status = PdhGetFormattedCounterValue(
			hCounter, PDH_FMT_DOUBLE, NULL, &fntValue);
	if (status != ERROR_SUCCESS) 
	{
		fntValue.doubleValue = DOUBLE_ERROR_VALUE;
	}
	return fntValue.doubleValue;
}


/**
 * �w�肳�ꂽ�J�E���^�̒l�� double �l�Ŏ擾���܂��B
 * ����l�� 100 �Ő������܂���B
 *
 * @param hCounter �J�E���^�n���h��
 * @return ���������ꍇ�̓J�E���^�̒l�A���s�����ꍇ�� DOUBLE_ERROR_VALUE (-1)
 */
static double GetFormattedCounterValueByDoubleNoCap100(PDH_HCOUNTER hCounter)
{
	PDH_FMT_COUNTERVALUE fntValue;
	PDH_STATUS status = PdhGetFormattedCounterValue(
			hCounter, PDH_FMT_DOUBLE | PDH_FMT_NOCAP100, NULL, &fntValue);
	if (status != ERROR_SUCCESS) 
	{
		fntValue.doubleValue = DOUBLE_ERROR_VALUE;
	}
	return fntValue.doubleValue;
}


/**
 * �J�E���^�[�p�X���쐬����B
 *
 * @param pszInstanceName �C���X�^���X��
 * @param pszCounterName �J�E���^�[��
 * @param pszCounterPath �J�E���^�[�p�X���i�[��
 * @param dwCounterPathSize �J�E���^�[�p�X���i�[�\�T�C�Y
 * @return �p�X�̍쐬�ɐ��������ꍇ�� 1 �A���s�����ꍇ�� -1
 */
static
bool MakeCounterPath(LPTSTR pszInstanceName, LPTSTR pszCounterName, LPTSTR pszCounterPath, DWORD dwCounterPathSize)
{
	PDH_COUNTER_PATH_ELEMENTS cpe;
	ZeroMemory( &cpe, sizeof(PDH_COUNTER_PATH_ELEMENTS) );

	cpe.szMachineName		= NULL; 
	cpe.szObjectName		= _T("Process");
	cpe.szInstanceName		= pszInstanceName;
	cpe.szParentInstance	= NULL;
	cpe.dwInstanceIndex		= -1;
	cpe.szCounterName		= pszCounterName;

	// �J�E���^�[�p�X�̍쐬
	if( ERROR_SUCCESS != PdhMakeCounterPath( &cpe, pszCounterPath, &dwCounterPathSize, 0 ) )
	{
		return false;
	}

	return true;
}


/**
 * PDH�J�E���^�[�l���擾���܂��B
 *
 * @param pszInstanceName �C���X�^���X��
 * @param pszCounterName �p�t�H�[�}���X�J�E���^��
 * @param dwFormat �擾����l�̃t�H�[�}�b�g�iPDH_FMT_*�j
 * @param pValue �l�i�[��
 * @param dwValueSize �l�̊i�[�\�T�C�Y
 * @param dwSleepMilliSecond 0 ���傫�Ȓl���w�肵���ꍇ�A�l���Q��擾���A�Q��ڂ̒l��Ԃ�
 * @return �l���擾�ł����ꍇ�� true �A�擾�ł��Ȃ������ꍇ�� false
 */
static
bool GetPdhCounterValue( LPTSTR pszInstanceName, LPTSTR pszCounterName, DWORD dwFormat, void* pValue, DWORD dwValueSize, DWORD dwSleepMilliSecond = 0 )
{
	// ���̓`�F�b�N
	if( NULL == pszInstanceName
	 || NULL == pszCounterName
	 || NULL == pValue
	 || 0 == dwValueSize )
	{
		assert( !"���̓G���[" );
		return false;
	}

	bool		bResult = false;		// ����

	HQUERY		hQuery = NULL;		// �v���n���h��
	HCOUNTER	hCounter = NULL;	// �J�E���^�[�n���h��
	

  // �J�E���^�[�p�X�̍쐬
	TCHAR szCounterPath[COUNTER_PATH_SIZE];
	if( !MakeCounterPath( pszInstanceName, pszCounterName, szCounterPath, COUNTER_PATH_SIZE ) )
	{
		goto LABEL_END;
	}

	// �v���n���h���̍쐬
	if( ERROR_SUCCESS != PdhOpenQuery( NULL, 0, &hQuery ) )
	{
		goto LABEL_END;
	}

	// �쐬�����J�E���^�[�p�X��v���n���h���ɓo�^�B�J�E���^�[�n���h���𓾂Ă����B
	if( ERROR_SUCCESS != PdhAddCounter( hQuery, szCounterPath, 0, &hCounter ) )
	{
		goto LABEL_END;
	}

	// �v���f�[�^�̎擾
	DWORD dwErrorCode = PdhCollectQueryData( hQuery );
	if( ERROR_SUCCESS !=  dwErrorCode)
	{
		goto LABEL_END;
	}
	if( 0 < dwSleepMilliSecond )
	{
		Sleep( dwSleepMilliSecond );
		if( ERROR_SUCCESS != PdhCollectQueryData( hQuery ) )
		{
			goto LABEL_END;
		}
	}

	PDH_FMT_COUNTERVALUE	fmtValue;
	PdhGetFormattedCounterValue( hCounter, dwFormat, NULL, &fmtValue );

	bResult = true;

LABEL_END:
	PdhRemoveCounter( hCounter );
	PdhCloseQuery( hQuery );
	if( false == bResult )
	{	// ���s
		return false;
	}

	// ����
	switch( dwFormat )
	{
		case PDH_FMT_LONG:
			if( dwValueSize != sizeof(LONG) )
			{
				assert( !"�󂯎��l�̌^�̎�ނɑ΂��Ēl�󂯎��o�b�t�@�[�̃T�C�Y���s��" );
				return false;
			}
			else
			{
				// pValue�������A�h���X��LONG�l���i�[����
				LONG* plValue = (LONG*)pValue;
				*plValue = fmtValue.longValue;
			}
			break;
		case PDH_FMT_DOUBLE:
			if( dwValueSize != sizeof(double) )
			{
				assert( !"�󂯎��l�̌^�̎�ނɑ΂��Ēl�󂯎��o�b�t�@�[�̃T�C�Y���s��" );
				return false;
			}
			else
			{
				// pValue�������A�h���X��double�l���i�[����
				double* pdValue = (double*)pValue;
				*pdValue = fmtValue.doubleValue;
			}
			break;
		case PDH_FMT_ANSI:
		case PDH_FMT_UNICODE:
		case PDH_FMT_LARGE:
		default:
			assert( !"���Ή�" );
			return false;
	}
	return true;
}


/**
 * �w�肳�ꂽ�C���X�^���X�� ID Process �p�t�H�[�}���X�J�E���^�l���擾���܂��B
 *
 * @param �C���X�^���X��
 * @return �J�E���^�̒l�̎擾�ɐ��������ꍇ�̓p�t�H�[�}���X�J�E���^�l�A���s�����ꍇ�� -1
 */
static
LONG GetIDProcess( LPTSTR pszTargetInstanceName )
{
	LONG value;
	if( !GetPdhCounterValue( pszTargetInstanceName, _T("ID Process"), PDH_FMT_LONG, &value, sizeof(value) ) )
	{
		return INVALID_ID_PROCESS;
	}
	
	return value;
}


/**
 * �J�����g�C���X�^���X�����擾���܂��B
 *
 * @param pszCurrentInstanceNameDest �C���X�^���X���i�[��
 * @param dwCurrentInstanceNameSize �C���X�^���X���̍ő咷
 * @return �C���X�^���X���̎擾�ɐ��������ꍇ�� true �A���s�����ꍇ�� false
 */
static
bool GetCurrentInstanceName( LPTSTR pszCurrentInstanceNameDest, DWORD dwCurrentInstanceNameSize )
{
	if( NULL == pszCurrentInstanceNameDest || 0 == dwCurrentInstanceNameSize )
	{
		return false;
	}

	bool bResult = false;

	// �C���X�^���X���X�g�p�̗̈�Ƃ��ĕK�v�ȑ傫�������߂�
	DWORD	dwCounterListSize = 0;
	LPTSTR	pmszInstanceList = (LPTSTR)TactfulMalloc( INITIALPATHSIZE * sizeof(TCHAR) );
	DWORD	dwInstanceListSize = 0;
	PDH_STATUS  pdhStatus;
	
	pdhStatus = PdhExpandCounterPath(_T("\\Process(*)\\ID Process"), pmszInstanceList, &dwInstanceListSize);
	if( PDH_MORE_DATA == pdhStatus)
	{
		TactfulFree(pmszInstanceList);
		
		// �C���X�^���X���X�g�p�̗̈�̊m��
		pmszInstanceList = (LPTSTR)TactfulMalloc( (dwInstanceListSize + INITIALPATHSIZE) * sizeof(TCHAR) );
		if (NULL == pmszInstanceList )
		{
			assert( !"���s" );
			goto LABEL_END;
		}
		
		pdhStatus = PdhExpandCounterPath(_T("\\Process(*)\\ID Process"), pmszInstanceList, &dwInstanceListSize);

	}

	if( PDH_CSTATUS_VALID_DATA != pdhStatus)
	{
		assert( !"���s" );
		goto LABEL_END;
	}
	
	// �v���Z�XID���L�[�Ƃ��āA�J�����g�C���X�^���X����T��
	LONG lCurrentProcessId = GetCurrentProcessId();
  
	// �C���X�^���X���X�g������o���C���X�^���X���p�̗̈�̊m��
	LPTSTR pszBuffer = (LPTSTR)TactfulMalloc( dwCurrentInstanceNameSize * sizeof(TCHAR) );
	LPTSTR pszInstanceName = pmszInstanceList;
	fflush(stdout);
	while( *pszInstanceName )
	{
		LPTSTR pszInstanceNameStart = strstr(pszInstanceName, _T("\\Process(")) + 9 * sizeof(TCHAR);
		LPTSTR pszInstanceNameEnd = strstr(pszInstanceNameStart, _T(")"));
		_tcsncpy_s( pszBuffer, dwCurrentInstanceNameSize, pszInstanceNameStart, pszInstanceNameEnd - pszInstanceNameStart);

		fflush(stdout);
		if( lCurrentProcessId == GetIDProcess( pszBuffer ) )
		{
			_tcsncpy_s( pszCurrentInstanceNameDest, dwCurrentInstanceNameSize, pszBuffer, dwCurrentInstanceNameSize );

			bResult = true;
			goto LABEL_END;
		}

		pszInstanceName += _tcslen(pszInstanceName) + 1;
	}
LABEL_END:
	// �������̌㏈��
	TactfulFree( pszBuffer );
	TactfulFree( pmszInstanceList );

	return bResult;
}


/**
 * �J�E���^�n���h���쐬���A�o�^���܂��B
 *
 * @param pszInstanceName �C���X�^���X��
 * @param pszCounterName �J�E���^��
 * @param hCounter �J�E���^�n���h���i�[��
 * @return �J�E���^�n���h���̓o�^�ɐ��������ꍇ�� true �A���s�����ꍇ�� false
 */
static
bool AddCounter( LPTSTR pszInstanceName, LPTSTR pszCounterName, PDH_HCOUNTER &hCounter )
{
	// ���̓`�F�b�N
	if( NULL == pszInstanceName || NULL == pszCounterName )
	{
		assert( !"���̓G���[" );
		return false;
	}

	// �J�E���^�[�p�X�̍쐬
	TCHAR szCounterPath[COUNTER_PATH_SIZE];
	if( !MakeCounterPath( pszInstanceName, pszCounterName, szCounterPath, COUNTER_PATH_SIZE ) )
	{
		return false;
	}

	// �쐬�����J�E���^�[�p�X��v���n���h���ɓo�^�B�J�E���^�[�n���h���𓾂Ă����B
	if( ERROR_SUCCESS != PdhAddCounter( hQuery, szCounterPath, 0, &hCounter ) )
	{
		return false;
	}

	return true;
}

/**
 * �v���Z�X�̒l���擾����J�E���^��o�^���܂��B
 *
 * @param lpszCurrentInstanceName ���̃v���Z�X�̃C���X�^���X��
 */
static void AddCounterForProcess( LPCTSTR lpszCurrentInstanceName )
{
	AddCounter( szCurrentInstanceName, _T("% User Time"), hCounterSysProcUser );
	AddCounter( szCurrentInstanceName, _T("% Privileged Time"), hCounterSysProcSys );
	AddCounter( szCurrentInstanceName, _T("Page Faults/sec"), hCounterSysMajFlt );
	AddCounter( szCurrentInstanceName, _T("Virtual Bytes"), hCounterSysVSize );
	AddCounter( szCurrentInstanceName, _T("Working Set"), hCounterSysRSS );
	AddCounter( szCurrentInstanceName, _T("Thread Count"), hCounterSysNumThreads );
	AddCounter( szCurrentInstanceName, _T("Handle Count"), hCounterProcNumFDs );
}

/**
 * �o�^�����J�E���^�n���h�����폜���܂��B
 */
static void RemoveCounterForProcess()
{
	PdhRemoveCounter(hCounterSysProcUser);
	PdhRemoveCounter(hCounterSysProcSys);
	PdhRemoveCounter(hCounterSysMajFlt);
	PdhRemoveCounter(hCounterSysVSize);
	PdhRemoveCounter(hCounterSysRSS);
	PdhRemoveCounter(hCounterSysNumThreads);
	PdhRemoveCounter(hCounterProcNumFDs);
}


/****************************************************************************/
/*                        Java����Ă΂��֐��̒�`                        */
/****************************************************************************/


JNIEXPORT jboolean JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_openQuery
  (JNIEnv *env, jobject obj) {
	if ( PdhOpenQuery(NULL, 0, &hQuery) == ERROR_SUCCESS )
  	{
		return true;
	}
	return false;
}

/**
 * �o�^����Ă���J�E���^�̒l���v�����܂��B
 *
 * @return ���������ꍇ�� true �A���s�����ꍇ�� false
 */
JNIEXPORT jboolean JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_collectQueryData
  (JNIEnv *env, jobject obj){
	if ( PdhCollectQueryData( hQuery ) == ERROR_SUCCESS )
	{
		return true;
	}
  	return false;
}

/**
 * CPU�g�p���i�V�X�e���j���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueSysCPUSys
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysCPUSys);
}

/*
 * CPU�g�p���i���[�U�j���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueSysCPUUser
  (JNIEnv *env, jobject obj){
  	return GetFormattedCounterValueByDouble(hCounterSysCPUUser);
}

/*
 * �����������i�ő�j���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getMemoryTotal
  (JNIEnv *env, jobject obj){
	MEMORYSTATUSEX state;
	state.dwLength = sizeof(state);
	GlobalMemoryStatusEx(&state);
	return (jdouble)(state.ullTotalPhys);
}

/*
 * �����������i�󂫁j���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueMemAvailable
  (JNIEnv *env, jobject obj){
	MEMORYSTATUSEX state;
	state.dwLength = sizeof(state);
	GlobalMemoryStatusEx(&state);
	return (jdouble)(state.ullAvailPhys);
}

/*
 * �y�[�W�t�@�C���g�p�����擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValuePageFileUsage
  (JNIEnv *env, jobject obj){
	MEMORYSTATUSEX state;
	state.dwLength = sizeof(state);
	GlobalMemoryStatusEx(&state);
	if (state.ullTotalPageFile == 0)
	{
		return (jdouble)0.0;
	}
	return (jdouble)(state.ullAvailPageFile * 100.0 / state.ullTotalPageFile);
}

/*
 * �y�[�W�t�@�C���g�p�ʂ��擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValuePageFileBytes
  (JNIEnv *env, jobject obj){
	MEMORYSTATUSEX state;
	state.dwLength = sizeof(state);
	GlobalMemoryStatusEx(&state);
	return (jdouble)(state.ullAvailPageFile);
}

/*
 * �V�X�e���S�̂̉��z�������g�p�ʂ��擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueVirtualBytes
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysVirtualBytes);
}

/*
 * �V�X�e���S�̂̎g�pFD�ʂ��擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueSystemFDs
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysNumFDs);
}

/*
 * �y�[�W�C�����擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValuePageIn
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysPageIn);
}

/*
 * �y�[�W�A�E�g���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValuePageOut
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysPageOut);
}

/*
 * �v���Z�X��User Time���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueProcessUserTime
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDoubleNoCap100(hCounterSysProcUser);
}

/*
 * �v���Z�X��Privileged Time���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueProcessPrivilegedTime
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDoubleNoCap100(hCounterSysProcSys);
}

/*
 * ���W���[�t�H�[���g���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueMajFlt
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysMajFlt);
}

/*
 * VSize���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueVSize
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysVSize);
}

/*
 * RSS���擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueRSS
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysRSS);
}

/*
 * �v���Z�X�̃X���b�h�����擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueNumThreads
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterSysNumThreads);
}

/*
 * �v���Z�X�̎g�pFD�����擾
 */
JNIEXPORT jdouble JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_getFormattedCounterValueProcFDs
  (JNIEnv *env, jobject obj){
	return GetFormattedCounterValueByDouble(hCounterProcNumFDs);
}

/*
 * �N�G���[�̎g�p���I��
 */
JNIEXPORT jboolean JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_closeQuery
  (JNIEnv *env, jobject obj){
	PdhCloseQuery( hQuery );
	return true;
}

/**
 * �N�G���[��ǉ�
 *
 * @return ���������ꍇ�� true �A���s�����ꍇ�� false
 */
JNIEXPORT jboolean JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_addCounter
(JNIEnv *env, jobject obj, jstring counterPath){
        // 3�񎎍s����B
	if( !GetCurrentInstanceName( szCurrentInstanceName, INSTANCE_NAME_SIZE ) )
	{
		if( !GetCurrentInstanceName( szCurrentInstanceName, INSTANCE_NAME_SIZE ) )
		{
			if( !GetCurrentInstanceName( szCurrentInstanceName, INSTANCE_NAME_SIZE ) )
			{
			   return false;
			}
		}
	}

	AddCounterForProcess(szCurrentInstanceName);

	PdhAddCounter( hQuery, "\\Processor(_Total)\\% Privileged Time", 0, &hCounterSysCPUSys );
	PdhAddCounter( hQuery, "\\Processor(_Total)\\% User Time", 0, &hCounterSysCPUUser );
	PdhAddCounter( hQuery, "\\Paging File(_Total)\\% Usage", 0, &hCounterSysPageUsage );
	PdhAddCounter( hQuery, "\\Process(_Total)\\Page File Bytes", 0, &hCounterSysPageBytes );
	PdhAddCounter( hQuery, "\\Memory\\Pages Input/sec", 0, &hCounterSysPageIn );
	PdhAddCounter( hQuery, "\\Memory\\Pages Output/sec", 0, &hCounterSysPageOut );
	PdhAddCounter( hQuery, "\\Process(_Total)\\Handle Count", 0, &hCounterSysNumFDs );

  return true;
}

/**
 * �K�v�ł���΁A�n���h�����X�V���܂��B
 *
 * @return �n���h�����X�V�����ꍇ�� true �A�X�V���Ȃ������ꍇ�� false
 */
JNIEXPORT jboolean JNICALL Java_jp_co_acroquest_endosnipe_javelin_resource_proc_PerfCounter_updateHandles
(JNIEnv *env, jobject obj){

	TCHAR szNewInstanceName[INSTANCE_NAME_SIZE];
	if( !GetCurrentInstanceName( szNewInstanceName, INSTANCE_NAME_SIZE ) )
	{
	   return false;
	}

	if ( _tcscmp(szCurrentInstanceName, szNewInstanceName) != 0 )
	{
		_tcscpy(szCurrentInstanceName, szNewInstanceName);
		RemoveCounterForProcess();
		AddCounterForProcess(szCurrentInstanceName);
		return true;
	}

	return false;
}

