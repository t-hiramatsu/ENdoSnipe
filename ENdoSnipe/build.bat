@echo off

rem ---------------------------------------------------------------------------
rem ���̊��ϐ��̓r���h�X�N���v�g�ɂ���Ď����I�� build.properties ��
rem �e�v���W�F�N�g�� MANIFEST.INF �֔��f����܂�
rem ---------------------------------------------------------------------------
set VER=5.0.3
set BUILD=023
rem ---------------------------------------------------------------------------

if exist "C:\Program Files (x86)" goto 64BIT

:32BIT
echo 32 bit environment
set JAVA_HOME_50=C:\Program Files\Java\jdk1.5.0_22
set JAVA_HOME_60=C:\Program Files\Java\jdk1.6.0_38
goto SETTING

:64BIT
echo 64 bit environment
set JAVA_HOME_50=C:\Program Files (x86)\Java\jdk1.5.0_22
set JAVA_HOME_60=C:\Program Files (x86)\Java\jdk1.6.0_38

:SETTING


set JAVA_HOME=%JAVA_HOME_50%
set WORK_DIR=%~dp0deploy
set TAGS=Version_%VER%-%BUILD%
set PATH=%JAVA_HOME%\bin;%PATH%

if exist "%WORK_DIR%" rmdir "%WORK_DIR%" /S /Q


echo �r���h���J�n���܂��B
echo ===============================
echo ��JAVA�o�[�W����(5.0)
"%JAVA_HOME_50%\bin\java" -version
echo ��JAVA�o�[�W����(6.0)
"%JAVA_HOME_60%\bin\java" -version
echo ���^�O
echo %TAGS%
echo ===============================

pause

echo Ant�����s���܂��B

call build_java.bat

set JAVA_HOME=%JAVA_HOME_60%
set PATH=%JAVA_HOME%\bin;%PATH%

pushd ..\Dashboard

call ant dist

copy target\Dashboard.war ..\ENdoSnipe\release

popd

echo �r���h���������܂����B

pause
