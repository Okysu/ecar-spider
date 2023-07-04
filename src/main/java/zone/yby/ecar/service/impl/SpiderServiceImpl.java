package zone.yby.ecar.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zone.yby.ecar.entity.*;
import zone.yby.ecar.service.SpiderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            String namespace = element.getElementsByTag("a").attr("href").replace("/", "");
            models.add(new Model(mid, name, namespace, id));
        }
    }

    @Override
    public List<Model> getModels(Integer mid, Integer page) {
        if (page == 0) {
            return new ArrayList<>();
        }
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
        int MaxPage = Integer.parseInt(pageText);
        if (page == -1) {
            page = MaxPage;
        } else if (page > MaxPage) {
            page = MaxPage;
        }
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

    void getRank(Document document, List<Rank> ranks, String type) {
        // 获取排名信息
        Elements elements = document.getElementsByClass("rk-item");
        for (Element element : elements) {
            Integer vid = Integer.valueOf(element.attr("point-cid"));
            String name = element.getElementsByClass("rk-car-name").text();
            String num = element.getElementsByClass("rk-car-num").text();
            ranks.add(new Rank(vid, name, num, type));
        }
    }

    @Override
    public List<Rank> getRanks(String type, Integer page) {
        if (page == 0) {
            return new ArrayList<>();
        }
        // 获取网页内容
        Document document;
        try {
            document = Jsoup.connect("https://car.yiche.com/newcar/" + type + "/").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取分页信息，获取倒数第二个元素
        Elements pageElement = document.getElementsByClass("link-btn");
        String pageText = pageElement.get(pageElement.size() - 2).attr("data-current");
        List<Rank> ranks = new ArrayList<>();
        getRank(document, ranks, type);
        if (pageText.equals("")) {
            return ranks;
        }
        int MaxPage = Integer.parseInt(pageText);
        if (page == -1) {
            page = MaxPage;
        } else if (page > MaxPage) {
            page = MaxPage;
        }
        for (int i = 2; i <= page; i++) {
            try {
                document = Jsoup.connect("https://car.yiche.com/newcar/" + type + "/?page=" + i).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            getRank(document, ranks, type);
        }
        return ranks;
    }

    void getNew(Document document, List<News> news) {
        // 获取新闻信息
        // 首先获取FocusNews
        Elements focusWrapper = document.getElementsByClass("focus-map-wrapper");
        // 获取li标签
        Elements focusNews = focusWrapper.get(0).getElementsByTag("li");
        for (Element element : focusNews) {
            String title = element.getElementsByClass("title").text();
            String url = "https://news.yiche.com/" + element.getElementsByTag("a").attr("href");
            news.add(new News(title, url));
        }
        // 获取news-item
        Elements itemNews = document.getElementsByClass("news-item");
        for (Element element : itemNews) {
            String title = element.getElementsByClass("title").text();
            String url = "https://news.yiche.com/" + element.getElementsByTag("a").attr("href");
            news.add(new News(title, url));
        }
    }

    @Override
    public List<News> getNews(Integer page) {
        if (page == 0) {
            return new ArrayList<>();
        }
        // 获取网页内容
        Document document;
        try {
            document = Jsoup.connect("https://news.yiche.com/").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取分页信息
        // 获取分页信息，获取倒数第二个元素
        Elements pageElement = document.getElementsByClass("link-btn");
        String pageText = pageElement.get(pageElement.size() - 2).attr("data-current");
        List<News> news = new ArrayList<>();
        getNew(document, news);
        if (pageText.equals("")) {
            return news;
        }
        int MaxPage = Integer.parseInt(pageText);
        if (page == -1) {
            page = MaxPage;
        } else if (page > MaxPage) {
            page = MaxPage;
        }
        for (int i = 2; i <= page; i++) {
            try {
                document = Jsoup.connect("https://news.yiche.com/?page=" + i).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            getNew(document, news);
        }
        return news;
    }

    @Override
    public Appraisal getAppraisal(String name) {
        // 获取网页内容
        Document document;
        try {
            document = Jsoup.connect("https://car.yiche.com/" + name + "/").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取车型信息
        String localPrice = document.getElementsByClass("ref-price").text();
        String factoryPrice = document.getElementsByClass("guide-price").get(0).text();
        return new Appraisal(localPrice, factoryPrice, name);
    }
}
