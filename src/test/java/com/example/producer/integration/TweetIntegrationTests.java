package com.example.producer.integration;

import com.example.producer.entity.Tweet;
import com.example.producer.repository.TweetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetIntegrationTests {

  @Autowired
  private TweetRepository tweetRepository;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void getAllTweets() throws IOException {
//  assign
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName("FirstUser");
    expectedTweet1.setTweetDescription("This is my first tweet");
    tweetRepository.save(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("This is my second tweet");
    tweetRepository.save(expectedTweet2);
//    act
    HttpHeaders headers = new HttpHeaders();
    final HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
    final ResponseEntity<String> actualResponseAsString = testRestTemplate.exchange("/tweet/",
        HttpMethod.GET, httpEntity, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    final List<Tweet> actualTweetList = objectMapper.readValue(actualResponseAsString.getBody(),
        new TypeReference<List<Tweet>>() {
        });
//    assert
    assertThat(actualTweetList.contains(expectedTweet1)).isTrue();
    assertThat(actualTweetList.contains(expectedTweet2)).isTrue();
  }

  @Test
  public void getTweetByID() throws IOException {
//    assign
    Tweet expectedTweet1 = new Tweet();
    String userName = "FirstUser";
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName("FirstUser");
    expectedTweet1.setTweetDescription("This is my first tweet");
    tweetRepository.save(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("This is my second tweet");
    tweetRepository.save(expectedTweet2);
    String userId = getIdFromUser(userName);
//    act
    final ResponseEntity<String> tweetResponseEntity = testRestTemplate.getForEntity("/tweet/" + userId,
        String.class);
//    assert
    ObjectMapper objectMapper = new ObjectMapper();
    final Tweet actualTweet = objectMapper.readValue(tweetResponseEntity.getBody(), Tweet.class);
    assertThat(actualTweet.getUserName()).isEqualTo("FirstUser");
    assertThat(actualTweet.getTweetDescription()).isEqualTo("This is my first tweet");
  }

  @Test
  public void updateTweetByID() throws IOException {
//    assign
    Tweet originalTweet = new Tweet();
    String userName = "FirstUser";
    originalTweet.setUserID(null);
    originalTweet.setUserName("FirstUser");
    originalTweet.setTweetDescription("This is my first tweet");
    tweetRepository.save(originalTweet);
    final String userID = getIdFromUser(userName);

    Tweet updatedTweet = new Tweet();
    updatedTweet.setUserID(userID);
    updatedTweet.setUserName("FirstUser -- Modified");
    updatedTweet.setTweetDescription("This is my first tweet -- Modified");
//    act
    testRestTemplate.put("/tweet/" + userID, updatedTweet);
//    assert
    final ResponseEntity<String> tweetResponseEntity = testRestTemplate.getForEntity("/tweet/" + userID,
        String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    final Tweet actualTweet = objectMapper.readValue(tweetResponseEntity.getBody(), Tweet.class);
    assertThat(actualTweet.getUserName()).isEqualTo("FirstUser -- Modified");
    assertThat(actualTweet.getTweetDescription()).isEqualTo("This is my first " +
        "tweet -- Modified");
  }

  @Test
  public void deleteTweetByID() throws IOException {
//    assign
    final String userName = "FirstUser";
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName(userName);
    expectedTweet1.setTweetDescription("This is my first tweet");
    tweetRepository.save(expectedTweet1);

    Tweet expectedTweet2 = new Tweet();
    expectedTweet2.setUserID(null);
    expectedTweet2.setUserName("SecondUser");
    expectedTweet2.setTweetDescription("This is my second tweet");
    tweetRepository.save(expectedTweet2);

    final String userID = getIdFromUser(userName);
//  action
    HttpHeaders headers = new HttpHeaders();
    final HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
    testRestTemplate.delete("/tweet/" + userID);

//    assert
    final ResponseEntity<String> actualResponseAsString = testRestTemplate.exchange("/tweet/",
        HttpMethod.GET, httpEntity, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    final List<Tweet> actualTweetList = objectMapper.readValue(actualResponseAsString.getBody(),
        new TypeReference<List<Tweet>>() {
        });
    assertThat(actualTweetList.contains(expectedTweet1)).isFalse();
  }

  private String getIdFromUser(String userName) {
    final List<Tweet> listOfUsers = StreamSupport.stream(tweetRepository.findAll().spliterator(),
        false).filter(tweet -> tweet.getUserName().equals(userName)).collect(Collectors.toList());
    return listOfUsers.get(0).getUserID();
  }

  @Test
  public void showErrorIfResourceIsNotFound() {
//    assign
    String userName = "FirstUser";
    Tweet expectedTweet1 = new Tweet();
    expectedTweet1.setUserID(null);
    expectedTweet1.setUserName(userName);
    expectedTweet1.setTweetDescription("This is my first tweet");
    tweetRepository.save(expectedTweet1);
    final String userID = getIdFromUser(userName);
//    act
    HttpHeaders headers = new HttpHeaders();
    final HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
    testRestTemplate.delete("/tweet/" + userID);
//  assert
    final ResponseEntity<String> actualException = testRestTemplate.exchange(
        "/tweet/" + userID,
        HttpMethod.GET, httpEntity, String.class);
    assertThat(actualException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void showErrorIfTryToUpdateTweetWhichDoesNotExists() {
//    assign
    Tweet tweet = new Tweet();
    tweet.setUserID(null);
    tweet.setUserName("FirstUser");
    tweet.setTweetDescription("This is my first tweet");
    tweetRepository.save(tweet);
//    act
    HttpHeaders headers = new HttpHeaders();
    final HttpEntity<Tweet> httpEntity = new HttpEntity<>(tweet, headers);
    final ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange("/tweet/9999",
        HttpMethod.PUT, httpEntity, String.class);
//  assert
    assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @AfterEach
  public void tearDown() {
    tweetRepository.deleteAll();
  }
}
