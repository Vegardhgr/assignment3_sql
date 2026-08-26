package com.example.demo;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.repository.interfaces.UserRepositoryInterface;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner testUserRepository(
        UserRepositoryInterface userRepository
    ) {
        return args -> {
            List<User> users = userRepository.getAllUsers();
            users.forEach(System.out::println);
            System.out.println("====");
            System.out.println(userRepository.getUserById(1));
            System.out.println("====");
            System.out.println(userRepository.getUsersByName("harl"));
            System.out.println("====");
            System.out.println(
                userRepository.addUser(
                    "Knut",
                    "Lute",
                    "knute.lute@mail.com",
                    "brapassord",
                    "Free"
                )
            );
            System.out.println("====");
            System.out.println(
                userRepository.updateUserFirstNameLastName(
                    "knute.lute@mail.com",
                    "K-nut",
                    "Lute"
                )
            );
            System.out.println("====");
            System.out.println(
                userRepository.getNumberOfUsersInSubscriptionType()
            );
            System.out.println("====");
            System.out.println(userRepository.getMostWatchedMovies());
            System.out.println("====");
            System.out.println(userRepository.getMostWatchedGenreByUser(3));
        };
    }
}
