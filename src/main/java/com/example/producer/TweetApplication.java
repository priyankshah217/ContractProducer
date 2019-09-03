package com.example.producer;

import com.example.producer.entity.Tweet;
import com.example.producer.repository.TweetRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TweetApplication implements ApplicationRunner {

  @Autowired
  private TweetRepository tweetRepository;

  public static void main(String[] args) {
    SpringApplication.run(TweetApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    Tweet tweet;
    Faker faker = new Faker();
    for (int index = 0; index < 2; index++) {
      tweet = new Tweet();
      tweet.setUserName("@" + faker.name().firstName()
          + faker.number().randomNumber(3, true));
      final String tweetDescription = generateRandomQuote(faker);
      final String trimmedTweet = tweetDescription.length() > 255 ?
          tweetDescription.substring(0, 255) : tweetDescription;
      tweet.setTweetDescription(trimmedTweet);
      tweetRepository.save(tweet);
    }
  }

  private String generateRandomQuote(Faker faker) {
    final Integer randomInt = faker.random().nextInt(10, 1000);
    if (randomInt % 3 == 0) {
      return "#" + faker.gameOfThrones().getClass().getSimpleName() + ":" +
          faker.gameOfThrones().quote();
    } else if (randomInt % 5 == 0) {
      return "#" + faker.backToTheFuture().getClass().getSimpleName() + ":" +
          faker.backToTheFuture().quote();
    } else if (randomInt % 7 == 0) {
      return "#" + faker.yoda().getClass().getSimpleName() + ":" +
          faker.yoda().quote();
    } else if (randomInt % 11 == 0) {
      return "#" + faker.howIMetYourMother().getClass().getSimpleName() + ":" +
          faker.howIMetYourMother().quote();
    } else if (randomInt % 13 == 0) {
      return "#" + faker.chuckNorris().getClass().getSimpleName() + ":" +
          faker.chuckNorris().fact();
    } else if (randomInt % 17 == 0) {
      return "#" + faker.hitchhikersGuideToTheGalaxy().getClass().getSimpleName() + ":" +
          faker.hitchhikersGuideToTheGalaxy().quote();
    } else {
      return "#" + faker.friends().getClass().getSimpleName() + ":" +
          faker.friends().quote();
    }
  }
}
