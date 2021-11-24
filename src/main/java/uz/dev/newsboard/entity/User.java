package uz.dev.newsboard.entity;

import lombok.*;
import org.hibernate.Hibernate;
import uz.dev.newsboard.entity.enums.RoleName;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public User(String login, String password, Integer age, String email, RoleName roleName) {
        this.login = login;
        this.password = password;
        this.age = age;
        this.email = email;
        this.roleName = roleName;
    }


}
