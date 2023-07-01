package zone.yby.ecar.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zone.yby.ecar.entity.Brand;
import zone.yby.ecar.entity.Model;
import zone.yby.ecar.service.SpiderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 易车网爬虫
 *
 * @author 于柏杨(160521118)
 * @since 2023-06-30 12:30:00
 */
public class SpiderServiceImpl implements SpiderService {
    @Override
    public List<Brand> getBrands() {
        // 获取网页内容
        Document document;
        try {
            document = Jsoup.connect("https://car.yiche.com/").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取所有品牌
        List<Brand> brands = new ArrayList<>();
        Elements elements = document.getElementsByClass("item-brand");
        for (Element element : elements) {
            Integer id = Integer.valueOf(element.attr("data-id"));
            String name = element.attr("data-name");
            brands.add(new Brand(id, name));
        }
        // 返回品牌列表
        return brands;
    }

    void getModel(Integer mid, Document document, List<Model> models) {
        // 获取所有车型
        Elements elements = document.getElementsByClass("search-result-list-item");
        for (Element element : elements) {
            Integer id = Integer.valueOf(element.attr("data-id"));
            String name = element.getElementsByClass("cx-name").text();
            String price = element.getElementsByClass("cx-price").text();
            models.add(new Model(mid, name, price, id));
        }
    }

    @Override
    public List<Model> getModels(Integer mid) {
        // 获取网页内容
        Document document;
        try {
            document = Jsoup.connect("https://car.yiche.com/xuanchegongju/?mid=" + mid).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取分页信息
        String pageText = document.getElementsByClass("pagenation-box").attr("data-pages");
        List<Model> models = new ArrayList<>();
        getModel(mid, document, models);
        if (pageText.equals("")) {
            return models;
        }
        int page = Integer.parseInt(pageText);
        for (int i = 2; i <= page; i++) {
            try {
                document = Jsoup.connect("https://car.yiche.com/xuanchegongju/?mid=" + mid + "&page=" + i).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            getModel(mid, document, models);
        }
        return models;
    }
}
