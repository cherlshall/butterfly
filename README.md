# butterfly

## 所需环境

1. 前端
   - Node.js 10.15.0
   - npm 6.4.1
2. 后端
   - Java 8
   - Maven 3.6.0
   - IDEA（安装lombok插件）
3. 部署
   - Tomcat 9.0.16

## 本地运行

- 前端（根目录：butterfy/frontend）
  1. 根目录执行cmd命令**npm start**
  2. 浏览器观察**8000端口**
- 后端（根目录：buttery/backend）
  1. 修改**application.yml.template**为application.yml，修改其中部分数据库配置
  2. 使用**IDEA**打开后端工程
  3. 使用**Maven**导入相关依赖
  4. 运行**Application.java**中的main方法

## 打包部署

- 前端
  1. 根目录执行cmd命令**npm run build**
  2. 将根目录下生成的**dict**文件夹上传到tomcat的webapp下，并更名为**ROOT**
- 后端
  1. 执行maven命令**mvn package**
  2. 将根目录/**target**下生成的**war包**上传到tomcat的webapp下，并更名为**server.war**

