package uz.dev.newsboard.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.dev.newsboard.entity.News;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.entity.enums.RoleName;
import uz.dev.newsboard.payload.UserDto;
import uz.dev.newsboard.repository.UserRepository;
import uz.dev.newsboard.service.NewsService;
import uz.dev.newsboard.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    UserRepository userRepository;


    /**
     * get sign in page
     *
     * @return firstPage
     */
    @GetMapping(value = "/")
    public String getSignIn() {
        return "firstPage";
    }


    /**
     * sign in system
     *
     * @param login
     * @param password
     * @param model
     * @return home page
     */
    @PostMapping("/signIn")
    public String postSignIn(@RequestParam(name = "login") String login,
                             @RequestParam(name = "password") String password,
                             Model model) {
        List<News> newsList = newsService.findAllNewsByApprove(1);
        model.addAttribute("newsList", newsList);
        long count = newsService.count();
        boolean hasNext = (15L) < count;
        model.addAttribute("hasPrev", false);
        model.addAttribute("prev", 0);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", 2);
        User user = null;
        try {
            user = userService.findByLoginAndPassword(login, password);
        } catch (NotFoundException e) {
            model.addAttribute("error", true);
            return "firstPage";
        }
        model.addAttribute("user", user);
        return "homePage";
    }

    /**
     * get next or prev news
     * @param page
     * @param userId
     * @param myNews
     * @param model
     * @return homePage
     * @throws Exception
     */
    @GetMapping("/news/{page}/{userId}/{myNews}")
    public String getNews(@PathVariable(value = "page") Integer page,
                          @PathVariable(value = "userId") Long userId,
                          @PathVariable(value = "myNews") boolean myNews,
                          Model model) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        model.addAttribute("user", optionalUser.get());
        List<News> newsList;
        long count;
        if (myNews) {
            newsList = newsService.findAllByNewsOwnerId(userId, page);
            count = newsService.countUserNews(userId);
            model.addAttribute("myNews", true);
        } else {
            newsList = newsService.findAllNewsByApprove(page);
            count = newsService.count();
        }
        boolean hasPrev = page > 1;
        boolean hasNext = (page * 15L) < count;
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", page - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", page + 1);
        model.addAttribute("newsList", newsList);
        model.addAttribute("approve", false);
        return "homePage";
    }


    /**
     * get sign up page
     *
     * @return signup
     */
    @GetMapping(value = "/signUp")
    public String getSignUp(UserDto userDto, Model model) {
        model.addAttribute(userDto);
        return "secondPage";
    }


    /**
     * get all news
     * @param userId
     * @param model
     * @return homePage
     * @throws Exception
     */
    @GetMapping("/getNews/{userId}")
    public String getNews(@PathVariable(value = "userId") Long userId, Model model) throws Exception {
        List<News> newsList = newsService.findAllNewsByApprove(1);
        Optional<User> optionalUser = userRepository.findById(userId);
        model.addAttribute("newsList", newsList);
        long count = newsService.count();
        boolean hasNext = (15L) < count;
        model.addAttribute("hasPrev", false);
        model.addAttribute("prev", 0);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", 2);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found ");
        }
        User user = optionalUser.get();
        model.addAttribute("user", user);
        return "homePage";
    }


    /**
     * sign up system
     * @param userDto
     * @param bindingResult
     * @param model
     * @return result
     */
    @PostMapping("signUp")
    public String saveUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "secondPage";
        }
        try {
            User user = userService.saveUser(userDto);
            return "redirect:/getNews/" + user.getId();
        } catch (Exception exception) {
            model.addAttribute("error", true);
            return "secondPage";
        }
    }


}
