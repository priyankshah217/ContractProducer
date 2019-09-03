package com.example.producer.repository;

import com.example.producer.entity.Tweet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TweetRepositoryTest {
  @Autowired
  TweetRepository tweetRepository;

  @Autowired
  TestEntityManager testEntityManager;

  @Test
  void postTweet() {
    Tweet expectedTweet = new Tweet();
    expectedTweet.setUserID(null);
    expectedTweet.setUserName("FirstUser");
    expectedTweet.setTweetDescription("My first tweet");
    tweetRepository.save(expectedTweet);
    final Tweet savedTweet = testEntityManager.persistFlushFind(expectedTweet);
    assertThat(savedTweet).isEqualTo(expectedTweet);
  }

  @Test
  void getAllTweets() {
    tweetRepository.deleteAll();
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName("FirstUser");
    expectedTweet1.setTweetDescription("My first tweet");
    testEntityManager.persistAndFlush(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("My second tweet");
    testEntityManager.persistAndFlush(expectedTweet2);

    final List<Tweet> actualTweetsList = StreamSupport
        .stream(tweetRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
    assertThat(actualTweetsList.size()).isEqualTo(2);
  }

  @Test
  void getTweetsById() {
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName("FirstUser");
    expectedTweet1.setTweetDescription("My first tweet");
    testEntityManager.persistAndFlush(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("My second tweet");
    testEntityManager.persistAndFlush(expectedTweet2);
    final String id = (String) testEntityManager.getId(expectedTweet1);

    final Optional<Tweet> actualTweet = tweetRepository.findById(id);
    assertThat(actualTweet.get()).isEqualTo(expectedTweet1);
  }

  @Test
  void deleteTweetById() {
    tweetRepository.deleteAll();
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName("FirstUser");
    expectedTweet1.setTweetDescription("My first tweet");
    testEntityManager.persist(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("My second tweet");
    testEntityManager.persist(expectedTweet2);

    tweetRepository.delete(expectedTweet1);

    final List<Tweet> actualTweetsList = StreamSupport
        .stream(tweetRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());
    assertThat(actualTweetsList.contains(expectedTweet1)).isFalse();
  }

}