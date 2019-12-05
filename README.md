# butterfly

## 所需环境

1. 前端
   - Node.js 10.15.0
   - npm 6.4.1
2. 后端
   - Java 8
   - Maven 3.6.0
   - IDEA（安装lombok插件）
   - MySQL 8.0.13
   - Redis 5.0.5

## 本地运行

1. 前端（根目录：butterfy/frontend）
   - 根目录执行cmd命令**npm install**下载依赖
   - 根目录执行cmd命令**npm start**启动
   - 浏览器观察**8000端口**

2. 后端（根目录：buttery/backend）
   - 使用**IDEA**打开后端工程
   - 使用**Maven**导入相关依赖
   - 修改个模块resource下application.yml中的数据库配置
   - 启动各模块，需要先启动eureka模块

## 打包部署

1. 前端（根目录：butterfy/frontend）
   - 根目录执行cmd命令**npm install**下载依赖
   - 根目录执行cmd命令**npm run build**
   - 将根目录下生成的**dict**文件夹下的所有文件，复制到ui模块的**resource/static**目录下

2. 后端（根目录：buttery/backend）
   - 根目录执行maven命令**mvn install**下载依赖
   - 根目录执行maven命令**mvn package**打包
   - 启动各模块**target**下生成的**jar包**，需要等待**eureka**模块启动成功后，再启动其他模块

