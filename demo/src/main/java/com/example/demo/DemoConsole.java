package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.SubscriptionTypeRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoConsole implements CommandLineRunner {

    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final SubscriptionTypeRepository subTypeRepo;

    public DemoConsole(
        MovieRepository movieRepo,
        UserRepository userRepo,
        SubscriptionTypeRepository subTypeRepo
    ) {
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.subTypeRepo = subTypeRepo;
    }

    @Override
    public void run(String... args) {
        List<User> users = userRepo.getAllUsers();
        users.forEach(System.out::println);
        System.out.println("====");
        System.out.println(userRepo.getUserById(1));
        System.out.println("====");
        System.out.println(userRepo.getUsersByName("harl"));
        System.out.println("====");
        System.out.println(
            userRepo.addUser(
                "Knut",
                "Lute",
                "knute.lute@mail.com",
                "brapassord",
                "Free"
            )
        );
        System.out.println("====");
        System.out.println(
            userRepo.updateUserFirstNameLastName(
                "knute.lute@mail.com",
                "K-nut",
                "Lute"
            )
        );
        System.out.println("====");
        System.out.println(subTypeRepo.getNumberOfUsersInSubscriptionType());
        System.out.println("====");
        System.out.println(movieRepo.getMostWatchedMovies());
        System.out.println("====");
        System.out.println(movieRepo.getMostWatchedGenreByUser(3));
    }
}
