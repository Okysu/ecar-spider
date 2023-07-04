# 易车网Java爬虫

本仓库为常熟理工学院21级软件工程（嵌入式培养）2023年暑期实训项目。

完成时间：2023-7-04

## 项目简介

本项目为易车网Java爬虫，使用Java语言编写，使用Maven进行项目管理，使用Jsoup进行网页解析，使用MySQL进行数据存储。

### 实现功能

- 爬取易车网的新闻、车型、车型详情、品牌、排行榜等信息
- 将爬取的信息存储到MySQL数据库中
- 从MySQL数据库中读取信息
- 从MySQL数据库中读取信息并生成文本文件

### 项目主要依赖

- JDK 1.8
- Maven 3.6.3
- MySQL 8.0.25
- Jsoup 1.16.1
- MySQL Connector/J 8.0.33
- Lombok 1.18.28
- Persistence-api 2.2
- Hibernate-core 6.2.5.Final

### 项目结构

```
│  .gitignore - Git忽略文件
│  pom.xml - Maven项目配置文件
│  README.md - 项目说明文件
│  train.sql - 数据库建表语句
│          
├─src - 源代码
│  ├─main
│  │  ├─java
│  │  │  └─zone
│  │  │      └─yby
│  │  │          └─ecar
│  │  │              │  Main.java - 主函数
│  │  │              │  
│  │  │              ├─config - 配置文件
│  │  │              │      DatabaseConfig.java - 数据库配置
│  │  │              │      
│  │  │              ├─dao - 数据库操作
│  │  │              │  │  PersistenceService.java - 持久化服务接口
│  │  │              │  │  
│  │  │              │  └─impl
│  │  │              │          PersistenceServiceImpl.java - 持久化服务实现
│  │  │              │          
│  │  │              ├─entity - 实体类
│  │  │              │      Appraisal.java - 车型详情实体类
│  │  │              │      Brand.java - 品牌实体类
│  │  │              │      Model.java - 车型实体类
│  │  │              │      News.java - 新闻实体类
│  │  │              │      Rank.java - 排行榜实体类
│  │  │              │      
│  │  │              ├─service - 服务
│  │  │              │  │  SpiderService.java - 爬虫服务接口
│  │  │              │  │  
│  │  │              │  └─impl - 爬虫服务实现
│  │  │              │          SpiderServiceImpl.java
│  │  │              │          
│  │  │              └─util - 工具类
│  │  │                      FileUtil.java - 文件工具类
│  │  │                      
│  │  └─resources - 资源文件
│  │          application.yaml - 应用配置文件
│  │          
│  └─test - 测试代码
│      └─java
│              TestPersistence.java - 持久化服务测试
│              TestSpider.java - 爬虫服务测试
```

### 项目特点

对于数据库的操作，创建了符合JPA的实体类，虽然使用的是JDBC，但是方便爬虫获取数据后新建对象来进行维护，最后再进行持久化。
同时对于存储函数`save`和获取函数`get`，使用了泛型，方便了对于不同实体类的操作。

```java
public interface PersistenceService<T> {
    /**
     * 保存对象到数据库
     * @param objects 对象列表
     */
    void save(List<T> objects);

    /**
     * 从数据库取出对象列表
     * @param clazz 对象类
     * @param Objects 对象列表
     */
    void get(Class<T> clazz, List<T> Objects);
}
```

在代码设计中我们使用了反射，通过反射来获取实体类的属性，方便了对于不同实体类的操作。
避免了重复的代码，不需要对不同的实体类写不同的代码，只需要在调用时传入不同的实体类即可。

```java
public class PersistenceServiceImpl<T> implements PersistenceService<T> {
    // 数据库连接池
    private static final DatabaseConfig DATA_SOURCE = new DatabaseConfig();

    /**
     * 保存对象列表到数据库
     *
     * @param objects 对象列表
     */
    @Override
    public void save(List<T> objects) {
        if (objects == null || objects.size() == 0) {
            return;
        }
        // 利用反射获取对象的类名，作为数据表名，但不要获取父类的类名
        String tableName = objects.get(0).getClass().getSimpleName().toLowerCase();
        // 利用反射获取对象的字段列表
        Field[] fields = objects.get(0).getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        // 打印表名和字段列表
        System.out.println("tableName: " + tableName);
        System.out.println("fields: " + Arrays.toString(fieldNames));
        // 拼接SQL语句
        StringBuilder sql = new StringBuilder("INSERT INTO `" + tableName + "` (");
        for (String field : fieldNames) {
            sql.append(field).append(",");
        }
        sql.deleteCharAt(sql.length() - 1).append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1).append(")");
        // 打印SQL语句
        System.out.println("SQL: " + sql);
        // 初始化PreparedStatement
        try (Connection conn = DATA_SOURCE.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(sql.toString());
                // 遍历对象列表
                for (T object : objects) {
                    // 利用反射获取对象的字段值
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        Object content = fields[i].get(object);
                        pstmt.setObject(i + 1, content);
                    }
                    // 添加到批处理
                    pstmt.addBatch();
                }
                // 执行批处理
                pstmt.executeBatch();
                conn.commit();
            } catch (SQLException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从数据库获取对象列表
     *
     * @param clazz   对象类
     * @param objects 对象列表
     */
    @Override
    public void get(Class<T> clazz, List<T> objects) {
        // 利用反射获取对象的类名，作为数据表名
        String tableName = clazz.getSimpleName().toLowerCase();
        // 利用反射获取对象的字段列表
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        // 打印表名和字段列表
        System.out.println("tableName: " + tableName);
        System.out.println("fields: " + Arrays.toString(fieldNames));
        // 拼接SQL语句
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String field : fieldNames) {
            sql.append(field).append(",");
        }
        sql.deleteCharAt(sql.length() - 1).append(" FROM ").append("`").append(tableName).append("`");
        // 打印SQL语句
        System.out.println("SQL: " + sql);
        // 初始化PreparedStatement
        try (Connection conn = DATA_SOURCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            // 执行查询
            try (ResultSet rs = pstmt.executeQuery()) {
                // 遍历结果集
                while (rs.next()) {
                    // 利用反射创建对象
                    T object = clazz.newInstance();
                    // 利用反射设置对象的字段值
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        fields[i].set(object, rs.getObject(i + 1));
                    }
                    // 添加到对象列表
                    objects.add(object);
                }
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## 项目运行

首先需要在你的数据库中新建一个名为`train`的数据库，然后导入`train.sql`文件。
在`config/DatabaseConfig`中配置数据库连接信息，然后运行`Main`类即可。