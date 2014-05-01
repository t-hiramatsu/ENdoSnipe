             ■■■ ENdoSnipe ビルド手順 ■■■

１．ツールの準備
  (1) JDK
    Java5、Java6 がインストールされていることが前提です。
    
  (2) Ant 用ライブラリ
  　 ENdoSnipe をビルドする Ant スクリプトでは、一部 Groovy 
  のスクリプトを使用しています。(log4j.properties の内部を自
  動チェックするため)

  　 Ant によるビルドを行うには、以下の Jar ファイルを 
  (ANT_HOME)/lib 配下にコピーしてください。
　　(jarファイルは、GitHubのendosnipe/ENdoSnipe/ant_libにあります)

    antlr-2.7.7.jar
    asm-2.2.3.jar
    asm-util-2.2.3.jar
    bsf-2.4.0.jar
    commons-logging-1.1.jar
    groovy-1.6.0.jar

   　Ant のバージョンは 1.7.0 以降が必要です。


２．ビルド
  (1)GitHubから、以下のプロジェクトをcloneしてください。
　　(すでにclone済みの場合は不要です。)
  
     endosnipe/ENdoSnipe
  
  (2)ENdoSnipe/build.bat を開き、以下の環境変数を設定してからコミット、
　　syncしてください。
     ・VER   →バージョン
     ・BUILD →ビルド番号
     ・JAVA_HOME_50 →Java5のJDKのパス
     ・JAVA_HOME_60 →Java6のJDKのパス

  (3)build.ps1 を開き、以下の環境変数を設定してからコミット、
　　syncしてください。
     ・$tags   →タグ名称
     
  (4)Git ShellのPowerShellを開き、build.ps1のあるディレクトリに
　　　移動してください。
  
  (5)以下のコマンドラインを実行します。
    > build.ps1
  
  (6)「すべてのビルドプロセスが完了しました。」と表示されたら完了です。
  　　build\ENdoSnipe\releaseディレクトリにビルド結果のファイルが
　　　出力されています。

　(7)リポジトリのdownloadsディレクトリ内に、
　　　タグ名称のディレクトリを作成してください。

　(8)(6)で出力されたファイルを、(5)で作成したディレクトリに配置し、
　　　コミット、syncしてください。

３．ローカルビルド(開発時)
  (1)ENdoSnipe/build.bat を開き、以下の環境変数を設定してください。
     ・JAVA_HOME_50 →Java5のJDKのパス
     ・JAVA_HOME_60 →Java6のJDKのパス

  (2)以下のコマンドラインを実行します。
    > build.bat
  
  (3)「すべてのビルドプロセスが完了しました。」と表示されたら完了です。
  　　ENdoSnipe\releaseディレクトリにビルド結果のファイルが
　　　出力されています。

以上


