
windows 环境设置
1.sapjco3.dll 需要与 sapjco3.jar 在同一目录
2.设置系统环境变量,将sapjco3所在目录加入系统环境变量
3.根据自己的操作系统版本选择对应的sapjco3包

32位系统
  例如:
  	新建环境变量
  		变量名: JAVA_SAPJCO 
  		变量值: E:\sapjco3\sapjco3-win32
  	将新建的 JAVA_SAPJCO 环境变量加入 系统环境变量 Path变量集合中.
  		%JAVA_SAPJCO%\sapjco3.jar

3.项目部署运行
	32位系统 
	 将 sapjco3.dll 加入到c:/windows/system32/目录 或者 将 sapjco3.dll 加入到 JDK/bin 目录下
	64位系统
	将 sapjco3.dll 加入到c:/windows/SysWOW64/目录 或者 将 sapjco3.dll 加入到 JDK/bin 目录下

部署异常问题

1.问题
	异常信息 Can't load IA 64-bit .dll on a AMD 64-bit platform
项目编译及运行,根据自己的操作系统版本选择对应的sapjco3包

2.问题
	报错 java.lang.UnsatisfiedLinkError: no sapjco3 in java.library.path   ，
是因为没有找到  sapjco3.dll这个库的路径，安装了JDK的环境中，这个库默认的位置不是在system32下，而是在 JDK/JRE/BIN下面。

sapjco3 开发环境设置
1.开发中需要将sapjco3.jar加入到项目的build path中
2.或者将其加入 本地 maven 库
mvn install:install-file -DgroupId=org.hibersap -DartifactId=sapjco3 -Dversion=3.0 -Dpackaging=jar -Dfile=E:/sapjco3/sapjco3-win32/sapjco3.jar
用以替换 org.hibersap 加载项下载的文件
<dependency>
    <groupId>org.hibersap</groupId>
    <artifactId>sapjco3</artifactId>
    <version>3.0</version>
</dependency>

/******************************************************************************
*
******************************************************************************/

Linux java 环境设置

1.创建目录
mkdir /usr/java
2.把下载的rpm文件copy过去
cp jdk-8u161-linux-x64.rpm /usr/java/
3.进入目录
mkdir cd /usr/java
4.添加可执行权限
chmod +x jdk-8u161-linux-x64.rpm
5.执行rpm命令安装
rpm -ivh jdk-8u161-linux-x64.rpm
6.查看是否安装成功
java -version

Linux sapjco3 环境设置

1.解压 sapjco3-linux64 或 sapjco3-linuxintel-3.0.5 
  	当前生产环境 centos_X64_32 系统使用的是 sapjco3-linux64 
2.将sapjco3.jar 文件复制至  $JAVA_HOME/lib/sapjco3.jar 
3.将 libsapjco3.so 文件复制至 $JAVA_HOME/jre/lib/amd64/server/libsapjco3.so
4.设置环境变量
	vim /etc/profile 修改文件
	JAVA_HOME=/usr/local/java
	PATH=$PATH:$JAVA_HOME/bin
	CLASSPATH=$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/sapjco3.jar
	JRE_HOME=$JAVA_HOME/jre
	LD_LIBRARY_PATH=dir:$LD_LIBRARY_PATH:$JAVA_HOME/jre/lib/amd64/server
	export JAVA_HOME LD_LIBRARY_PATH PATH
5.刷新配置
	source /etc/profile
	
6.配置本地 hosts 将主机名字映射到IP地址
	1.控制台执行  hostname 命令查看计算机名 
	2.控制台执行 hostname -i 查看本机IP 
	3.编辑hosts文件  vi /etc/hosts
	4.在 /etc/hosts中 加入
	  192.168.1.10(本机IP) localhost hostname(计算机名)


建立  Jco Serever 监听服务时相关设置  
	windows jco 监听设置
	进入 %SystemRoot%\System32\drivers\etc
	1.修改 services文件，在services文件尾部 将  jco.server.gwserv:sapgw00 属性值 sapgw00 加入 SAP 端口映射
	  sapdp00  3200/tcp #SAP Server
	  sapgw00  3300/tcp #SAP Gateway
	2.修改 hosts文件，在 hosts中 将 jco.server.gwhost:gmdev01  属性值  gmdev01 加入 SAP服务器IP 地址映射
	  10.86.95.121       gmdev01
	3.具体示例
	       参考项目目录内的 services/hosts 文件 
	.
	linux jco 监听设置
	 1.执行  vi /etc/hosts
	          修改 hosts文件，在 hosts中 将 jco.server.gwhost:gmdev01  属性值  gmdev01 加入 SAP服务器IP 地址映射
	   10.86.95.121       gmdev01
	 2.执行 vi /etc/services
	          修改 services文件，在services文件尾部 将  jco.server.gwserv:sapgw00 属性值 sapgw00 加入 SAP 端口映射
	   sapdp00  3200/tcp #SAP Server
	   sapgw00  3300/tcp #SAP Gateway

linux 下运行
运行
nohup java -jar tms-synchro-sap.jar >iwm-interface-dma-receive.txt &
nohup java -XX:-UseGCOverheadLimit -jar iwm-interface-dma-receive.jar >SYNC_$(date +%Y%m%d%H%M%S).txt &
查找进程
ps -aux|grep iwm-interface-dma-receive
结束进程
kill -s 9 "pid"


