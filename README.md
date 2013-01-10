情報をかき集めてメモるWebアプリ
Jetty開発環境のRI（Reference Implementation：参照実装）も兼ねます。

# このプロジェクトの説明
Jetty+Wicket+JPAでWebアプリを作りHeroku上で動作させるためのテンプレートプロジェクトです。
もちろん、Heroku上でなくても動作します。

# このプロジェクトのコンセプト
このプロジェクトは

__導入に手間がかからない__

ことを第一に考えて構成しています。

# プロジェクトの使い方
## 事前にインストールしておくもの
### Maven
Javaで依存ライブラリをダウンロードしてくれるツール。
http://maven.apache.org

最新版をダウンロードして解凍したら、binフォルダにPATHを通せばインストールは完成。

### PostgreSQL
http://www.postgresql.jp/download

### eclipse
必須ではないが、あった方がいいでしょう。
http://www.eclipse.org/downloads/

## 動作のさせ方
### eclipseプロジェクトZIPの入手
このプロジェクトの次のURLからダウンロード出来ます。
https://github.com/jabaraster/heroku-web-template/archive/master.zip

### PostgreSQLの実行
このプロジェクトはpostgresというDBに接続します。
通常このDBはPostgreSQLをインストールしたときにデフォルトでインストールされます。
ですので、PostgreSQLを起動しさえすれば準備完了です。

### Mavenの実行
#### eclipseに変数追加
eclipseからMavenのリポジトリを参照可能にするために、次のコマンドを実行します。

```
mvn -Declipse.workspace=<eclipseの.metadataのあるフォルダへのパス> eclipse:add-maven-repo
```

#### 依存JARのダウンロード
eclipseプロジェクトが依存するJARをダウンロードするために、次のコマンドを実行します。

```
mvn eclipse:eclipse
```

初めて実行するときはたくさんのJARがダウンロードされるので、かなり時間がかかります。

依存JARの情報は、Mavenの設定ファイルであるpom.xmlに書かれています。
また、実はこのコマンドは依存JARをダウンロードするためのコマンドではなく、pom.xmlの設定に従ってeclipseプロジェクトを構成するためのコマンドです。
pom.xmlファイルを編集した場合、再度このコマンドを実行してeclipseプロジェクトに反映する必要があります。

### eclipseプロジェクトのインポート
先にダウンロードしたeclipseプロジェクトZIPを解凍し、eclipseにインポートします。

これでWebアプリを実行する準備が整いました。

#### SampleWebStarterクラスの実行
このクラスを実行すると、次のURLでWebアプリにアクセス出来ます。
http://localhost:8081/ui/

# このプロジェクトが提供している機能

## 依存ライブラリ管理
Mavenの設定ファイルであるpom.xmlに、最低限の設定を記述済みです。

## Servlet&JSPによるWebアプリケーションが動作する環境

## Wicketが動作する環境

## JPAが動作する環境
デフォルトでは、JPAが起動するときに既存テーブルをDROPしてから再作成するようになっています。
この挙動は、環境変数```HIBERNATE_HBM2DDL_AUTO```を設定することで上書きできます。

Linux系であれば

```
export HIBERNATE_HBM2DDL_AUTO=none
```

Winows系であれば

```
set HIBERNATE_HBM2DDL_AUTO=nonw
```

と設定します。


## Google GuiceによるDI
```java
@com.google.inject.Inject
```

を付けることにより、オブジェクトが自動的に注入されます。


## Google Guiceによる自動トランザクション制御

次のようなメソッドには自動でトランザクション制御が仕込まれます。

* メソッドを持っているクラスが、```jabara.jpa_guice.DaoBase```クラスを継承している
* ```public```メソッド

このようなメソッドはトランザクション制御対象メソッドです。
トランザクション制御対象メソッドに対しては、メソッド実行直前にトランザクションが開始され、メソッドから例外がスローされた場合は自動でロールバックされます。
例外が発生せず、普通にメソッドから復帰した場合は自動でコミットされます。

トランザクション制御対象メソッドの中から別のトランザクション制御対象メソッドを呼び出した場合、トランザクションが入れ子になることはなく、同じトランザクションの中で実行されます。

## JAX-RSによるRESTアプリケーションが動作する環境

## MemcachedによるHttpSession実装
Memcachedを使うとWebアプリケーションのプロセスが複数ある場合にセッションを共有することが可能になります。

## Heroku上での動作に必要なファイル
### Procfile
このファイルはWebアプリケーションをHeroku上で動作させるための起動コマンドが書かれています。

```
web:    java -cp src/main/webapp/WEB-INF/classes:target/dependency/* -XX:+UseCompressedOops -Dweb.port=${PORT} -Ddatabase.url=${DATABASE_URL} -Dwicket.configuration=deployment -Dmemcached.servers=${MEMCACHIER_SERVERS} -Dmemcached.username=${MEMCACHIER_USERNAME} -Dmemcached.password=${MEMCACHIER_PASSWORD} -server SampleWebStarter 
```

この起動コマンドの意味は次の通りです。

* Webアプリケーションを起動します。
* リッスンポートはHerokuが設定する環境変数```PORT```の値を使います。
* データベースはHerokuがデフォルトで提供するものを使います。つまりHerokuが設定する環境変数```DATABASE_URL```の値を接続先として使います。
* Wicketを```deployment```モードで起動します。
* Memcachedによるセッション管理を行います。Herokuのアドオン```MemCachier```が追加されている前提です。Memcachedに関する設定は、```MemCachier```が設定する各種環境変数の値を使います。
* JVMをserverモードで起動します。

# パッケージ構成
推奨パッケージ構成を説明します。
ルートパッケージを\<root_package>と表記します。

## Web関連クラスのパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>.web|ServletAPIに近いクラス。WebInitializerクラスもここに置く|
|\<root_package>.web.rest|JAX-RSに関するクラス|
|\<root_package>.web.ui|画面を構成するクラス。Wicketの場合は各種Pageクラス。Pageクラスが多い場合はweb.ui.pageパッケージを切って、そこにまとめた方が見通しが良くなります|
|\<root_package>.web.ui.component|画面を構成する部品。Wicketの場合はPanelなどのUI部品|

## ビジネスロジッククラスのパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>.entity|エンティティクラス（データベースモデルをJavaクラスに写したクラス）を置く|
|\<root_package>.model|エンティティクラスよりも高級なデータモデルを置く|
|\<root_package>.service|ビジネスロジックのインターフェイスを置く。業務的な例外クラスもここに置く|
|\<root_package>.service.impl|ビジネスロジックの実装クラスを置く|

## その他のパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>|グローバル設定値を置く。環境変数など。ただし、ここに属するクラス／変数などは最小限になるように注意するべき|
|\<root_package>.util|Serviceにするほどでないロジックを置く。例えば日付の表現形式を統一するためのユーティリティメソッドなど|

# 大事なクラス

## SampleWebInitializerクラス
いくつかある大事なクラスの中でも、このクラスが飛び抜けて大事です。

このクラスは```javax.servlet.ServletContextListener```インターフェイスをimplementsしています。これにより、Webアプリケーション起動時にこのクラスの```contextInitialized```メソッドがただ一度だけ呼び出されるようになります。

また、ServletAPI3.0になってから、ServletContextを通してコードからServletやFilterを登録することが出来るようになりました。

つまり```contextInitialized```メソッドの中でこの機能を利用すれば、web.xmlに頼らずにServletやFilterを登録することが出来るようになるわけです。

### web.xmlに頼らずに済むことの利点
web.xmlに頼らずJavaコードでServletやFilterを登録する利点は、__型安全性__です。web.xmlはクラス名を誤って記述してしまっても、Servletコンテナを起動するまでは気付けません。しかしJavaコードで登録する場合、工夫次第でクラス名を謝るなどということは起こらないようにすることが出来ます。


ただし、場合によってはweb.xmlを編集する必要が生じることに注意が必要です。例えばファイルの拡張子とMIMEタイプのマッピングは、web.xmlでないと登録できません。

## SampleRestApplicationクラス
JAX-RSを使うために必要です。```javax.ws.rs.core.Application```クラスを継承します。

このクラスの主な役割は

1. リソースクラスの登録
1. 各種拡張クラスの登録

です。

なお、```SampleRestApplication```クラスをWebアプリケーションから使えるようにするには、前述の```SampleWebInitializer```クラスの中で```SampleRestApplication```クラスを登録する必要があります。

## SampleWicketAppliationクラス
Wicketを使うためのクラスです。
Wicketの詳しい説明は別項に譲りますが、日本語の情報は多いですし、メーリングリストも充実しているので手詰まりになることはまずありません。

なお```SampleWicketApplication```クラスをWebアプリケーションから使えるようにするには、前述の```SampleWebInitializer```クラスの中で```SampleWicketApplication```クラスを登録する必要があります。


# 各種設定ファイルについて
## pom.xml
このプロジェクトはMaven2というパッケージ管理システムの利用を前提に設定されています。
http://maven.apache.org

Maven2の唯一の設定ファイルが```pom.xml```です。
```pom.xml```にはプロジェクトが依存するライブラリやプロジェクトのディレクトリ構成が書かれています。

## Procfile
このファイルは、このプロジェクトをHerokuにデプロイする際に必要となるファイルです。HerokuというのはPaaS環境です。
http://www.heroku.com

Herokuを使わない場合はこのファイルは無視して下さい。

## .gitignore
ソースをGitで管理する場合に、管理対象外とするファイルのパターンを書くファイルです。
Gitを使わない場合はこのファイルは無視して下さい。

# 残作業
## Glassfishへのデプロイ検証
WARを作成して、それがGlassfish上で動作するかどうかの検証を行う必要があります。

## JSPでELが使えない
これは困る。

## WicketのページにCSSやJSを適用するサンプル

## 複数DB接続先への対応


