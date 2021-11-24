package uz.dev.newsboard.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.entity.enums.RoleName;
import uz.dev.newsboard.payload.UserDto;
import uz.dev.newsboard.repository.NewsRepository;
import uz.dev.newsboard.repository.UserRepository;


import java.util.Optional;

@Service
public class UserService {

    private final int PAGE_COUNT = 15;


    @Autowired
    UserRepository userRepository;
    @Autowired
    NewsRepository newsRepository;


    public User saveUser(UserDto userDto) throws Exception {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            throw new Exception("This login already exist");
        }
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setRoleName(RoleName.USER);
        return userRepository.save(user);
    }


    public User findByLoginAndPassword(String login, String password) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(login, password);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }
}
