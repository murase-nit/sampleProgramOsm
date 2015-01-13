
# はじめに
新B4向け

* OpenStreetMapを使って研究をできるようにするためのテンプレートプログラムを作成する
* 古いバージョンからデータを取得し，どのような動作をしているか確認するとよいです

# バージョン履歴
## 1.0.0
* 地図の表示
* ダブルクリックで移動のみ

## 1.1.0
* 道路データボタンで道路データの表示(下記記述のデータベースを使用している)
* その他のインターフェースは外側だけで動かない

## 1.2.0
* 緯度経度or地名orランドマーク指定で移動([YOLP](http://developer.yahoo.co.jp/webapi/map/)の[Yahoo!ジオコーダAPI](http://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/geocoder.html)を使用している)
* スケールの指定
* 施設データの表示([YOLP](http://developer.yahoo.co.jp/webapi/map/)の[Yahoo!ローカルサーチAPI](http://developer.yahoo.co.jp/webapi/map/openlocalplatform/v1/localsearch.html)を使用している)
* クリックで名前表示



# 使い方
1. srcフォルダをダウンロード
1. Eclipseで新規Javaプロジェクト作成する(sampleMapProgramOSMなど好きな名前を付ける)
1. 文字コードをUTF-8にする
	1. プロジェクトを右クリックし，プロパティーをクリック
	1. 左のメニューのリソースをクリックし，”テキスト・ファイルのエンコード”をその他UTF-8にする
1. ダウンロードしたsrcフォルダを上記で作成したプロジェクトのフォルダに移動
1. PostgreSQL，PostGISのドライバのビルドパスを通す
	1. プロジェクトを右クリックし，ビルドパス->ビルドパスの構成をクリック
	1. 外部jarの追加をクリックして，"C:\Program Files (x86)\PostgreSQL\pgJDBC"にpostgis-jdbc-2.0.0.jarやpostgresql-9.3-1100.jdbc41.jarのようなファイルがればそれを追加する
1. 実行して地図が表示されれば完了

# 使用しているデータベース

* データベース名
	* osm_road_db
* テーブル名
	* osm_japan_car_2po_4pgr

主なデータ

|カラム名|データ型|説明|
|:----|:------|:-----|
|id|int|リンクのID|
|clazz|int|道路クラス|
|source|int|端点のノードID|
|target|int|もう一方のノードID|
|km|double|長さ(km)|
|x1,y1,x2,y2|double|source,targetのそれぞれの緯度経度|
|geom|linestring|リンクの形状を表すジオメトリ(arcと呼ぶことがある)|


# その他
YOLPのAPIキーは自分で取得すること
