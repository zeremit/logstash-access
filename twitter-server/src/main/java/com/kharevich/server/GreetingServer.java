package com.kharevich.server;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.kharevich.LogbackAccessFilter;
import com.kharevich.server.log.LogbackAccessModule;
import com.twitter.finatra.http.AbstractHttpServer;
import com.twitter.finatra.http.filters.CommonFilters;
import com.twitter.finatra.http.routing.HttpRouter;

import java.util.Collection;

public class GreetingServer extends AbstractHttpServer {


  @Override
  public Collection<Module> javaModules() {
    return ImmutableList.<Module>of(
        new LogbackAccessModule());
  }

  @Override public void configureHttp(HttpRouter router) {
    router
        .filter(LogbackAccessFilter.class, true)
        .filter(CommonFilters.class, true)
        .add(GreetingController.class);
  }
}
