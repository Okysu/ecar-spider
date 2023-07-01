package zone.yby.ecar.service;

import zone.yby.ecar.entity.Brand;
import zone.yby.ecar.entity.Model;

import java.util.List;

/**
 * 易车网爬虫接口
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public interface SpiderService {
    List<Brand> getBrands();

    List<Model> getModels(Integer mid);
}