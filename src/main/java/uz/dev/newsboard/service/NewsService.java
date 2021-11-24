package uz.dev.newsboard.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.repository.NewsRepository;
import uz.dev.newsboard.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final int PAGE_COUNT = 15;

    @Autowired
    NewsRepository newsRepository;
    @Autowired
    UserRepository userRepository;


    public void addNewsPost(String title, String text, Long userId) {
        User user = userRepository.getById(userId);
        News news = new News();
        news.setText(text);
        news.setTitle(title);
        news.setNewsOwner(user);
        news.setApprove(false);
        news.setReject(false);
        newsRepository.save(news);
    }

    public News editNewsPost(String title, String text, Long newsId) throws NotFoundException {
        Optional<News> optional = newsRepository.findById(newsId);
        if (optional.isPresent()) {
            News news = optional.get();
            news.setText(text);
            news.setTitle(title);
            news.setUpdateAt(new Timestamp(System.currentTimeMillis()));
            news.setApprove(false);
            news.setReject(false);
            return newsRepository.save(news);
        }
        throw new NotFoundException("User not found");
    }

    public void approveNews(Long newsId) {
        News news = newsRepository.getById(newsId);
        news.setApprove(true);
        news.setApproveDate(new Timestamp(System.currentTimeMillis()));
        newsRepository.save(news);
    }

    public void rejectNews(Long newsId) {
        News news = newsRepository.getById(newsId);
        news.setReject(true);
        newsRepository.save(news);
    }

    public List<News> findAllNewsByApprove(Integer page) {
        if (page != 1) {
            page = page - 1;
            page = page * PAGE_COUNT;
            return newsRepository.getAllByPage(PAGE_COUNT, page - 1);
        } else {
            return newsRepository.getAllByPage(PAGE_COUNT, 0);
        }
    }

    public long count() {
        return newsRepository.countNewsByApprove(true);
    }

    public List<News> findAllByNewsOwnerId(Long userId, Integer page) {
        if (page != 1) {
            page = page - 1;
            page = page * PAGE_COUNT;
            return newsRepository.getAllByPageByUserId(userId, PAGE_COUNT, page - 1);
        } else {
            return newsRepository.getAllByPageByUserId(userId, PAGE_COUNT, 0);
        }
    }

    public long countUserNews(Long userId) {
        return newsRepository.countNewsByNewsOwnerId(userId);
    }
}
