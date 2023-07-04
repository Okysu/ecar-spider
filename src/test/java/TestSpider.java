import zone.yby.ecar.entity.News;
import zone.yby.ecar.service.SpiderService;
import zone.yby.ecar.service.impl.SpiderServiceImpl;

import java.util.List;

public class TestSpider {
    public static void main(String[] args) {
        // 初始化爬虫服务
        final SpiderService spiderService = new SpiderServiceImpl();
        // 获取新闻信息
        List<News> news = spiderService.getNews(1);
        // 打印新闻信息
        for (News n : news) {
            System.out.println(n);
        }
        // 获取车型详情
        System.out.println(spiderService.getAppraisal("xinaodia6l"));
    }
}
