package in.goldi.creatorstore;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "in.goldi.creatorstore.entities")
public class CreatorStoreApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach((DotenvEntry entry) ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(CreatorStoreApplication.class, args);
    }
}