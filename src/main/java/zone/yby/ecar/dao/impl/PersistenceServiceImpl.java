package zone.yby.ecar.dao.impl;

import zone.yby.ecar.config.DatabaseConfig;
import zone.yby.ecar.dao.PersistenceService;
import zone.yby.ecar.util.FileUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 持久化服务实现类
 *
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
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
        // 利用反射获取对象的类名，作为数据表名
        String className = objects.get(0).getClass().getName();
        String tableName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
        // 利用反射获取对象的字段列表
        Field[] fields = objects.get(0).getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        // 打印表名和字段列表
        System.out.println("tableName: " + tableName);
//        System.out.println("fields: " + Arrays.toString(fieldNames));
//        // 清空数据表内容，防止重复插入
//        try (Connection conn = DATA_SOURCE.getConnection()) {
//            try (PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE " + tableName)) {
//                pstmt.execute();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        // 拼接SQL语句
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
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
        try (Connection conn = DATA_SOURCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            conn.setAutoCommit(false);
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
        }
    }

    /**
     * 清空数据表
     *
     * @param tableName 数据表名
     */
    @Override
    public void empty(String tableName) {
        try (Connection conn = DATA_SOURCE.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE " + tableName)) {
                pstmt.execute();
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
        String className = clazz.getName();
        String tableName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
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
        sql.deleteCharAt(sql.length() - 1).append(" FROM ").append(tableName);
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

    /**
     * 保存到本地文件
     *
     * @param objects 对象列表
     * @param path    文件路径
     */
    @Override
    public void save(List<T> objects, String path) {
        String text = "";
        for (T object : objects) {
            text += object.toString() + "\n";
        }
        try {
            FileUtil.write(path, text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}