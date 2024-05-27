

# 让您的校园生活多姿多彩！！



## 一、前端部署

1. 方式一 - 使用源码

  1）获取源码：解压代码压缩包.

  2）安装依赖: 在项目目录下运行命令，安装项目所需的依赖包。通常使用npm（Node.js）或者yarn进行依赖安装.

```plain
npm install  或者 yarn install
```
​	3）获取服务器 ip：在后端服务器部署之后，获取后端服务器ip.
​	4）修改服务器 ip：在前端 src/util/request.js 文件中修改 BaseURL，将 ip 修改为后端服务器 ip（比如localhost）.

​	5）运行前端应用：保存修改后，输入命令在根目录运行.

```plain
npm run serve
```
​	6)访问前端应用：点击网址即可运行前端项目.

2. 方式二 - 使用 dist 文件夹
1）服务器环境: 首先，您需要一个服务器环境来托管您的前端代码。您可以选择使用云托管服务或者自己的服务器.

​	2）Web 服务器: 您需要一个 Web 服务器来提供前端文件。常见的选择包括 Nginx 和Apache.

​	3）上传 dist 文件: 将构建好的dist文件上传到您的服务器.

​	4）配置 Web 服务器: 配置您的 Web 服务器以提供前端文件。以下是一些常见的配置示例：

Nginx 配置示例：

```plain
server {     
  listen 80;    
  server_name your_domain.com;      
  location / {         
    root /path/to/your/dist/folder;         
    index index.html;    
} } 
```
>请确保将 your_domain.com 替换为您的域名或服务器IP地址，将 /path/to/your/dist/folder 替换为dist文件的实际路径.
5）启动Web服务器: 启动您的Web服务器，使其开始提供前端文件.

6）域名配置: 如果使用域名访问，请确保您的域名指向了服务器的IP地址。这通常需要在域名注册商的控制面板中进行配置.

7）访问前端应用: 一旦配置完成，您可以通过浏览器访问您的域名或服务器IP地址来查看部署的前端应用.

## 二、后端部署

方式：

1）获取项目 jar 包.

2）打开命令行窗口.

    1. Windows环境：桌面鼠标右键，点击"在终端中打开"；或者按住win键+R键，输入"cmd".
    2. linux环境：桌面鼠标右键，点击"在终端中打开".
3）获取服务器 ip：

    3. Windows环境：在命令行输入命令"ipconfig"，获取后端服务器ip.
    4. linux环境：在命令行输入命令"ifconfig"或者"ip addr"，获取后端服务器ip.
4）运行后端项目：在命令行输入命令"java -jar"（注意命令中的空格），然后把项目 jar 包拖进命令行中.

