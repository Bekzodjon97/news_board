package uz.dev.newsboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByNewsOwnerId(Long newsOwner_id);
    List<News> findAllByApprove(boolean approve);
    List<News> findAllByApproveAndReject(boolean approve, boolean reject);

    @Query(value = "select * from news where approve=true order by approve_date desc limit :limit offset :offset", nativeQuery = true)
    List<News> getAllByPage(Integer limit, Integer offset);

    @Query(value = "select * from news where news_owner_id=:ownerId order by approve_date desc limit :limit offset :offset", nativeQuery = true)
    List<News> getAllByPageByUserId(Long ownerId, Integer limit, Integer offset);


    Long countNewsByApprove(boolean approve);
    Long countNewsByNewsOwnerId(Long newsOwner_id);
}
