使用说明：
Quick start
	<dependency>
		<groupId>com.luban</groupId>
		<artifactId>mysql-orm</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>
	
1.功能：根据实体类创建表；
 a:支持的数据库：Mysql
 b:需要在classpath目录下创建文件名为jdbc.properties的文件
 c.文件内容如下：
 #mysql
 jdbc.className=com.mysql.jdbc.Driver
 jdbc.url=jdbc:mysql://127.0.0.1:3306/luban
 jdbc.userName=root
 jdbc.passWord=root
 其中:key必须是以上指定的key;
 
 