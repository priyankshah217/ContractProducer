package com.example.producer.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Tweet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String userID;

  @NotBlank
  private String userName;

  @NotBlank
  private String tweetDescription;
}
