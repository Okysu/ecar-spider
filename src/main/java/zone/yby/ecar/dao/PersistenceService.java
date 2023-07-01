package zone.yby.ecar.dao;

import java.util.List;
import java.util.Objects;

/**
 * 持久化服务接口
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public interface PersistenceService<T> {
    /**
     * 保存对象到数据库
     * @param objects 对象列表
     */
    void save(List<T> objects);

    /**
     * 清空表
     * @param tableName 表名
     */
    void empty(String tableName);

    /**
     * 从数据库取出对象列表
     * @param clazz 对象类
     * @param Objects 对象列表
     */
    void get(Class<T> clazz, List<T> Objects);

    /**
     * 保存对象列表到文件
     * @param objects 对象列表
     * @param path 文件路径
     */
    void save(List<T> objects, String path);
}