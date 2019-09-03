package com.example.producer.services;

import com.example.producer.entity.Tweet;
import com.example.producer.exceptions.UserNotFoundException;
import com.example.producer.repository.TweetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class TweetServiceTest {
  @Mock
  TweetRepository tweetRepository;

  @InjectMocks
  TweetService tweetService;

  @BeforeEach
  void setUp() {
    initMocks(this);
  }

  @Test
  void postTweet() {
    Tweet tweet = new Tweet();
    tweet.setUserID("1");
    tweet.setTweetDescription("My first tweet");
    when(tweetRepository.save(any())).thenReturn(tweet);
    final Tweet savedTweet = tweetService.postTweet(tweet);
    assertThat(savedTweet).isEqualTo(tweet);
  }

  @Test
  void getTweetByID() {
    Tweet tweet = new Tweet();
    tweet.setUserID("1");
    tweet.setTweetDescription("My first tweet");
    when(tweetRepository.findById(any())).thenReturn(java.util.Optional.of(tweet));
    final Tweet retrievedTweet = tweetService.getTweetByID("1");
    assertThat(retrievedTweet).isEqualTo(tweet);

    doThrow(UserNotFoundException.class).when(tweetRepository).findById("1");
    assertThatThrownBy(() -> tweetService.getTweetByID("1"));
  }

  @Test
  void getAllTweets() {
    Tweet tweet1 = new Tweet();
    tweet1.setUserID("1");
    tweet1.setTweetDescription("My first tweet");
    Tweet tweet2 = new Tweet();
    tweet2.setUserID("1");
    tweet2.setTweetDescription("My second tweet");
    List<Tweet> expectedListOfTweets = Arrays.asList(tweet1, tweet2);
    when(tweetRepository.findAll()).thenReturn(expectedListOfTweets);
    final List<Tweet> actualListOfTweets = tweetService.getAllTweets();
    assertThat(actualListOfTweets).isEqualTo(expectedListOfTweets);
  }

//  @Test
//  void deleteTweet() {
//    Tweet tweet = new Tweet();
//    tweet.setUserID("1");
//    tweet.setTweetDescription("My first tweet");
//    when(tweetRepository.findById("1")).thenReturn(java.util.Optional.of(tweet));
//    doNothing().when(tweetRepository).delete(tweet);
//    tweetService.deleteTweet("1");
//  }
//
//  @Test
//  void updateTweet() {
//
//  }
}