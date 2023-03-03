##环境变量
在调试环境中 直接在右边profiles  里面选择，更新,运行即是对应的环境变量
</br></br>##打包的一些问题
以下用来生成完整的jar包，其下面的插件需要注释掉
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <skipTests>true</skipTests>
    </configuration>
</plugin>
```
**生成分离jar**时，需要将application-prod.yml 拷贝到config/下面,并以application.yml命名，是因为打包时没有将yml文件打入 ，但是已经将
config加入了classpath。同时还需要将lib/放在运行目录下面，而这个也是在打包的时候指定了的。</br>
```
java -jar ./spring-boot.jar
```

接口文档路径
http://127.0.0.1:8080/swagger-ui.html#/

服务器日志路径
http://www.ylc5858.com:9681/