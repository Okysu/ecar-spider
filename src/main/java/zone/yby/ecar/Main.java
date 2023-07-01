package zone.yby.ecar;

import zone.yby.ecar.dao.PersistenceService;
import zone.yby.ecar.dao.impl.PersistenceServiceImpl;
import zone.yby.ecar.entity.Brand;
import zone.yby.ecar.entity.Model;
import zone.yby.ecar.service.SpiderService;
import zone.yby.ecar.service.impl.SpiderServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 易车网爬虫
 *
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public class Main {
    public static void main(String[] args) {
        // 初始化爬虫服务
        final SpiderService spiderService = new SpiderServiceImpl();
        // 初始化数据库连接
        final PersistenceService persistenceService = new PersistenceServiceImpl();
        // 获取品牌列表
        List<Brand> brands = spiderService.getBrands();
        // 保存品牌列表
        persistenceService.empty("brand");
        persistenceService.save(brands);
        // 获取车型列表
        persistenceService.empty("model");
        // 初始化线程池
        ExecutorService executor = Executors.newFixedThreadPool(8);
        // 遍历品牌列表
        for (Brand brand : brands) {
            // 获取车型列表
            executor.execute(() -> {
                List<Model> models = spiderService.getModels(brand.getMid());
                // 保存车型列表
                persistenceService.save(models);
            });
            // 设置随机延迟 1~3 秒，防止被封 IP
            try {
                Thread.sleep((long) (Math.random() * 2000 + 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 关闭线程池
        executor.shutdown();
        // 从数据库取出品牌信息，并保存到文件
        List<Brand> dataBrands = new ArrayList<>();
        persistenceService.get(Brand.class, dataBrands);
        // 保存到文件
        persistenceService.save(dataBrands, "brand.text");
        // 从数据库取出车型信息，并保存到文件
        List<Model> dataModels = new ArrayList<>();
        persistenceService.get(Model.class, dataModels);
        // 保存到文件
        persistenceService.save(dataModels, "model.text");
    }
}