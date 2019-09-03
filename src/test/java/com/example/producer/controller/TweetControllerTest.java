package com.example.producer.controller;

import com.example.producer.entity.Tweet;
import com.example.producer.exceptions.UserNotFoundException;
import com.example.producer.services.TweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
class TweetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TweetService tweetService;

  private ObjectMapper mapper;

  @Test
  void postTweet() throws Exception {
    Tweet tweet1 = new Tweet();
    tweet1.setUserID("1");
    tweet1.setTweetDescription("This is my first tweet");
    mapper = new ObjectMapper();
    final String requestBody = mapper.writeValueAsString(tweet1);
    when(tweetService.postTweet(any())).thenReturn(tweet1);
    mockMvc.perform(post("/tweet/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.userID", new Is<>(equalTo(tweet1.getUserID()))))
        .andExpect(jsonPath("$.tweetDescription",
            new Is<>(equalTo(tweet1.getTweetDescription()))));
  }

  @Test
  void deleteTweet() throws Exception {
    mockMvc.perform(delete("/tweet/1"))
        .andExpect(status().is2xxSuccessful());

    doThrow(UserNotFoundException.class).when(tweetService).deleteTweet("1");
    mockMvc.perform(delete("/tweet/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateTweet() throws Exception {
    Tweet tweet1 = new Tweet();
    tweet1.setUserID("1");
    tweet1.setTweetDescription("This is my first tweet");
    mapper = new ObjectMapper();
    final String requestBody = mapper.writeValueAsString(tweet1);
    when(tweetService.updateTweet(any(), any())).thenReturn(tweet1);

    mockMvc.perform(put("/tweet/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().is2xxSuccessful());

    doThrow(UserNotFoundException.class).when(tweetService).updateTweet(any(), any());
    mockMvc.perform(put("/tweet/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTweetById() throws Exception {
    Tweet tweet1 = new Tweet();
    tweet1.setUserID("1");
    tweet1.setTweetDescription("This is my first tweet");

    when(tweetService.getTweetByID(any())).thenReturn(tweet1);
    mockMvc.perform(get("/tweet/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.userID", new Is<>(equalTo(tweet1.getUserID()))))
        .andExpect(jsonPath("$.tweetDescription",
            new Is<>(equalTo(tweet1.getTweetDescription()))));
  }

  @Test
  void getAllTweets() throws Exception {
    Tweet tweet1 = new Tweet();
    tweet1.setUserID("1");
    tweet1.setTweetDescription("This is my first tweet");
    Tweet tweet2 = new Tweet();
    tweet2.setUserID("2");
    tweet2.setTweetDescription("This is my second tweet");
    List<Tweet> tweetList = Arrays.asList(tweet1, tweet2);

    when(tweetService.getAllTweets()).thenReturn(tweetList);
    mockMvc.perform(get("/tweet/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$[0].userID", new Is<>(equalTo(tweet1.getUserID()))))
        .andExpect(jsonPath("$[0].tweetDescription",
            new Is<>(equalTo(tweet1.getTweetDescription()))))
        .andExpect(jsonPath("$[1].userID", new Is<>(equalTo(tweet2.getUserID()))))
        .andExpect(jsonPath("$[1].tweetDescription",
            new Is<>(equalTo(tweet2.getTweetDescription()))));
  }
}