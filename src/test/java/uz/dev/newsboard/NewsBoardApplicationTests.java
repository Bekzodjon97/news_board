package uz.dev.newsboard;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.repository.NewsRepository;
import uz.dev.newsboard.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewsBoardApplicationTests {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @Order(1)
    public void testCreateNews(){
        News news=new News();
        news.setTitle("new");
        news.setText("news");
        news.setNewsOwner(userRepository.getById(1L));
        newsRepository.save(news);
        assertNotNull(newsRepository.findById(1L).get());
    }

    @Test
    @Order(2)
    public void testReadAllNews(){
        List<News> newsList = newsRepository.findAll();
        assertNotNull(newsList);
    }

    @Test
    @Order(3)
    public void testSingleNews(){
        News news = newsRepository.findById(1L).get();
        assertEquals("new", news.getTitle());
    }

    @Test
    @Order(4)
    public void testUpdateNews(){
        News news = newsRepository.findById(1L).get();
        news.setTitle("nnn");
        News save = newsRepository.save(news);
        assertNotEquals("new", save.getTitle());
    }

    @Test
    @Order(5)
    public void testDelete(){
        newsRepository.deleteById(1L);
        assertThat(newsRepository.existsById(1L)).isFalse();
    }
}
