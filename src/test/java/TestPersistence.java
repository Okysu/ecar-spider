import zone.yby.ecar.dao.PersistenceService;
import zone.yby.ecar.dao.impl.PersistenceServiceImpl;
import zone.yby.ecar.entity.Brand;

import java.util.ArrayList;
import java.util.List;

public class TestPersistence {
    public static void main(String[] args) {
        // 初始化数据库连接
        final PersistenceService<Brand> persistenceService = new PersistenceServiceImpl<>();
        // 自定义品牌列表
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(1, "奥迪"));
        brands.add(new Brand(2, "宝马"));
        brands.add(new Brand(3, "奔驰"));
        brands.add(new Brand(4, "本田"));
        brands.add(new Brand(5, "别克"));
        // 清空数据库
        persistenceService.empty("brand");
        // 保存品牌列表
        persistenceService.save(brands);
        // 读取品牌列表
        List<Brand> brandsSaved = new ArrayList<>();
        persistenceService.get(Brand.class, brandsSaved);
        // 打印品牌列表
        for (Brand brand : brandsSaved) {
            System.out.println(brand);
        }
    }
}
