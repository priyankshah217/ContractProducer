package com.example.producer.controller;

import com.example.producer.entity.Tweet;
import com.example.producer.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/")
public class TweetController {

  private TweetService tweetService;

  @Autowired
  public TweetController(TweetService tweetService) {
    this.tweetService = tweetService;
  }

  @PostMapping("/tweet/")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Tweet> postTweet(@RequestBody Tweet tweet) {
    final Tweet savedTweet = tweetService.postTweet(tweet);
    return ResponseEntity.ok(savedTweet);
  }

  @GetMapping("/tweet/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Tweet> getTweetById(@PathVariable String id) {
    final Tweet tweet = tweetService.getTweetByID(id);
    return ResponseEntity.ok(tweet);
  }

  @GetMapping("/tweet/")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<Tweet>> getAllTweets() {
    final List<Tweet> allTweets = tweetService.getAllTweets();
    return ResponseEntity.ok(allTweets);
  }

  @DeleteMapping("/tweet/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<String> deleteTweet(@PathVariable String id) {
    tweetService.deleteTweet(id);
    return ResponseEntity.ok("Tweet deleted");
  }

  @PutMapping("/tweet/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<String> updateTweet(@PathVariable String id, @RequestBody Tweet tweet) {
    tweetService.updateTweet(id, tweet);
    return ResponseEntity.ok("Tweet updated");
  }
}
