package uz.dev.newsboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dev.newsboard.entity.User;
import uz.dev.newsboard.entity.enums.RoleName;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByLoginAndEmail(String login, String email);
    Optional<User> findByRoleName(RoleName roleName);
    Boolean existsByLogin(String login);
}
