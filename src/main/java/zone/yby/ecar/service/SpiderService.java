package zone.yby.ecar.service;

import zone.yby.ecar.entity.*;

import java.util.List;

/**
 * 易车网爬虫接口
 *
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public interface SpiderService {
    /**
     * 获取品牌列表
     *
     * @return 品牌列表
     */
    List<Brand> getBrands();

    /**
     * 获取车型列表
     *
     * @param mid  品牌id
     * @param page 页码
     * @return 车型列表
     */
    List<Model> getModels(Integer mid, Integer page);

    /**
     * 获取排行榜 1:销量榜 2:热度榜 3:降价榜
     *
     * @param type 排行榜类型 - salesrank:销量榜 - hotrank:热度榜 - jiangjiarank:降价榜
     * @param page 页码
     * @return 排行榜列表
     */
    List<Rank> getRanks(String type, Integer page);

    /**
     * 获取新闻列表
     *
     * @param page 页码
     */
    List<News> getNews(Integer page);

    /**
     * 获取车型详情
     *
     * @param name 车型名称
     */
    Appraisal getAppraisal(String name);
}