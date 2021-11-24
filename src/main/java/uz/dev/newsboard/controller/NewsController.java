package uz.dev.newsboard.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.payload.NewsDto;
import uz.dev.newsboard.repository.NewsRepository;
import uz.dev.newsboard.repository.UserRepository;
import uz.dev.newsboard.service.NewsService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NewsRepository newsRepository;

    /**
     * get user"s all news
     * @param userId
     * @param model
     * @return homePage
     * @throws Exception
     */
    @GetMapping(value = "/myNews/{userId}")
    public String getMyNews(@PathVariable Long userId, Model model) throws Exception {
        List<News> newsList = newsService.findAllByNewsOwnerId(userId , 1);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new Exception("User not found");
        }
        model.addAttribute("user",optionalUser.get());
        model.addAttribute("newsList",newsList);
        long count = newsService.countUserNews(userId);
        boolean hasNext = (15L) < count;
        model.addAttribute("hasPrev", false);
        model.addAttribute("prev", 0);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", 2);
        model.addAttribute("myNews", true);
        return "homePage";
    }


    /**
     * get third page
     * @param userId
     * @param model
     * @return thirdPage
     * @throws Exception
     */
    @GetMapping(value = "/addNews/{userId}")
    public String addNews(@PathVariable Long userId, NewsDto newsDto,  Model model) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new Exception("User not found");
        }
        model.addAttribute("user",optionalUser.get());
        model.addAttribute("add", true);
        model.addAttribute(newsDto);
        return "thirdPage";
    }


    /**
     * get third page
     * @param newsId
     * @param model
     * @return thirdPage
     */
    @GetMapping(value = "/edit/{newsId}")
    public String editNews(@PathVariable Long newsId,
                           Model model) {
        News news = newsRepository.getById(newsId);
        NewsDto newsDto=new NewsDto(news.getTitle(), news.getText());
        model.addAttribute("add", false);
        model.addAttribute("user", news.getNewsOwner());
        model.addAttribute("news", news);
        model.addAttribute("newsDto", newsDto);
        return "thirdPage";

    }

    /**
     * edit news
     * @param newsId
     * @param newsDto
     * @param model
     * @return redirect:/myNews/{userId}
     * @throws NotFoundException
     */
    @PostMapping(value = "/edit/{newsId}")
    public String editNewsPost(@PathVariable Long newsId,
                               @Valid NewsDto newsDto,
                               BindingResult result, Model model) throws NotFoundException {
        if (result.hasErrors()) {
            News news = newsRepository.getById(newsId);
            model.addAttribute("add", false);
            model.addAttribute("user", news.getNewsOwner());
            model.addAttribute("news", news);
            return "thirdPage";
        }
        News news = newsService.editNewsPost(newsDto.getTitle(), newsDto.getText(), newsId);
        User newsOwner = news.getNewsOwner();
        List<News> newsList = newsRepository.findAllByApprove(true);
        model.addAttribute("user", newsOwner);
        model.addAttribute("newsList", newsList);
        return "redirect:/myNews/"+newsOwner.getId();

    }


    /**
     * add news post
     * @param userId
     * @param newsDto
     * @return redirect:/myNews/{userId}
     */
    @PostMapping(value = "/addNews/{userId}")
    public String addNewsPost(@PathVariable Long userId, @Valid NewsDto newsDto, BindingResult result, Model model) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("user",optionalUser.get());
            return "thirdPage";
        }
        newsService.addNewsPost(newsDto.getTitle(), newsDto.getText(), userId);
        return "redirect:/myNews/"+userId;
    }


    /**
     * get unconfirmed news
     * @param userId
     * @param model
     * @return homePage
     */
    @GetMapping(value = "/unconfirmed/{userId}")
    public String unconfirmedNews(@PathVariable Long userId,
                           Model model) {
        List<News> allByApproveAndReject = newsRepository.findAllByApproveAndReject(false, false);
        model.addAttribute("newsList", allByApproveAndReject);
        User user = userRepository.getById(userId);
        model.addAttribute("user",user);
        model.addAttribute("approved",true);
        return "homePage";

    }

    /**
     * approve news
     * @param newsId
     * @param userId
     * @return redirect:/unconfirmed/{userId}
     */
    @GetMapping(value = "/approve/{newsId}/{userId}")
    public String approveNews(@PathVariable(value = "newsId") Long newsId, @PathVariable(value = "userId") Long userId) {
        newsService.approveNews(newsId);
        return "redirect:/unconfirmed/"+userId;
    }


    /**
     * reject news
     * @param newsId
     * @param userId
     * @return redirect:/unconfirmed/{userId}
     */
    @GetMapping(value = "/reject/{newsId}/{userId}")
    public String rejectNews(@PathVariable(value = "newsId") Long newsId, @PathVariable(value = "userId") Long userId) {
        newsService.rejectNews(newsId);
        return "redirect:/unconfirmed/"+userId;
    }


    /**
     * delete news
     * @param newsId
     * @param userId
     * @param model
     * @return redirect:/myNews/{userId}
     */
    @GetMapping("/delete/{newsId}/{userId}")
    public String deleteNews(@PathVariable(value = "newsId") Long newsId,
                             @PathVariable(value = "userId") Long userId,
                             Model model){
        newsRepository.deleteById(newsId);
        return "redirect:/myNews/"+userId;

    }
}
