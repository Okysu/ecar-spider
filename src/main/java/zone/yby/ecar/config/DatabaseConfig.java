package zone.yby.ecar.config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库Mysql配置类
 * 用于配置数据库连接信息
 *
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public class DatabaseConfig {
    private Connection conn = null; // 数据库连接对象

    private String url = "jdbc:mysql://localhost:3306/train"; // 数据库连接地址

    private String username = "root"; // 数据库用户名
    private String password = "2003"; // 数据库密码

    public DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public DatabaseConfig() {
    }

    /**
     * 连接数据库
     */
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = java.sql.DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     *
     * @return 数据库连接对象
     */
    public Connection getConnection() {
        // 判断数据库连接对象是否为空、已关闭或者已断开
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(3)) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
