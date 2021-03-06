package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application implements CommandLineRunner{
    
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private MessageRepository repositoryM;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
    public void run(String... args) throws Exception {
        repository.deleteAll();
        repository.save(new User("Jean", "1"));
        repository.save(new User("Bob", "2"));
        repositoryM.deleteAll();
    }
    
}