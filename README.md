# Image Processing Apps
 
任意の画像に対して画像処理を行った結果画像を表示するAndroidアプリ。
 
# デモ
 
端末内に保存している画像もしくはカメラで撮影した写真を選択後、画像処理方法を3種類の中から選ぶ。

<div align="center">
    <img src="https://user-images.githubusercontent.com/86580511/145665963-5b630bc1-a46c-4020-b0bc-890ff41e3a68.gif" alt="属性">
</div>

# 特徴
 
Docker+flaskを用いてAPIサーバを構築した。  
アプリ側からhttp通信で画像を送信し、API側でリクエストに応じた画像処理を施して返す、という仕組みになっている。
 
# 動作環境

### サーバ側
* Ubuntu 20.04.3 LTS
* docker 20.10.10
* python 3.8.6
* flask 2.0.2

### アプリ側
* windows10
* Android studio
* Kotlin 1.4.0
 
# 使用方法
 
1. ローカルサーバを起動

```bash
sudo service docker start
docker-compose build
docker-compose up
```

2. Android Studio上で、
File > Open > android_camera
の順に選択しプロジェクトを開く。

3. Run 'app'により実行

4. 終了する場合は立てたコンテナを削除する  
`docker-compose down`
 
# 注意
 
動作環境によって画像処理の際にエラーが発生する場合がある。
主にWindowsではエラーが発生しやすく、Macでは発生しづらい。  

これはエミュレータの問題であり実機では発生しない可能性が高い。
