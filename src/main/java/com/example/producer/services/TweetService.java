package com.example.producer.services;


import com.example.producer.entity.Tweet;
import com.example.producer.exceptions.UserNotFoundException;
import com.example.producer.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TweetService {

  private TweetRepository tweetRepository;

  @Autowired
  public TweetService(TweetRepository tweetRepository) {
    this.tweetRepository = tweetRepository;
  }

  public Tweet postTweet(Tweet tweet) {
    return tweetRepository.save(tweet);
  }

  public Tweet getTweetByID(String id) {
    return tweetRepository.findById(id).orElseThrow(() ->
        new UserNotFoundException("User does not exists"));
  }

  public List<Tweet> getAllTweets() {
    return StreamSupport.stream(tweetRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  public void deleteTweet(String id) {
    Tweet tweet = getTweetByID(id);
    tweetRepository.delete(tweet);
  }

  public Tweet updateTweet(String id, Tweet tweet) {
    Tweet existingTweet = getTweetByID(id);
    existingTweet.setUserID(tweet.getUserID());
    existingTweet.setUserName(tweet.getUserName());
    existingTweet.setTweetDescription(tweet.getTweetDescription());
    return tweetRepository.save(existingTweet);
  }
}
