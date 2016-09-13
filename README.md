### WifiProxy
WiFi环境下的HTTP代理，兼容Android4 - Android5，Android6还未尝试

### 下载地址
<img src="https://github.com/abcmmee/WifiProxy/raw/master/picture/360.png" width="200px" height="200px">
https://yunpan.cn/ckZCvYiicmHAf  访问密码 0e91

### 使用说明
#### 连接
1、连接Wifi<br>
2、打开WifiProxy软件，填写主机名和端口<br>
3、点击“连接”按钮<br>
4、成功后，Wifi会自动重连

#### 不使用的时候
1、在连接Wifi的状态下，打开WifiProxy软件<br>
2、点击重置按钮<br>
3、重置后，Wifi会自动重连，恢复初始状态

### 适用人群
1、经常需要抓包进行调试的开发人员<br>
2、需要经过局域网中的其它主机代理流量的人员

### 开发原因
以前设置HTTP代理时，需要经过以下步奏：<br>
1、打开WiFi设置->2、长按WiFi->3、设置代理<br>
不用的时候又得重复做繁琐的动作，然后我就去网上找HTTP代理软件，发现基本上手机都要ROOT，而且还有一个致命的缺陷，代理后，上HTTPS的网站时，无法识别自己导入的证书，但是从系统设置那里填写的HTTP代理是可以识别的。<br>
然后我就用Google搜索找方法，原理是调用系统私有API。<br>

### 小技巧
当你导入证书后，发现Android会提示你“网络可能会被监听”，这个提示不能去掉。假如你的手机ROOT后，可以把证书导入系统就不会有提示了。<br>
http://wiki.cacert.org/FAQ/ImportRootCert#Android_Phones_.26_Tablets<br>
上面这个链接是教你怎么制作系统证书的，目前我在国内网站上还没有见到过这种导入Android系统证书的文章，有的话，也都不适用了。

### 软件截图
<img src="https://raw.githubusercontent.com/abcmmee/WifiProxy/master/picture/1.png" width="300" height="500">

