package com.kharevich.server;

import com.twitter.finatra.http.AbstractController;
import com.twitter.util.Future;

public class GreetingController extends AbstractController {
  @Override public void configureRoutes() {
    get("/hello", req -> response().ok().plain("Greetings from Twitter Server!!!"));
  }
}
