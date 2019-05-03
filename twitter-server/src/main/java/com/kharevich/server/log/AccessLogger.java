package com.kharevich.server.log;

import ch.qos.logback.core.spi.LifeCycle;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Duration;

public interface AccessLogger extends LifeCycle {

    void log(Request request, Response response, Duration duration);

    void log(Request request, Throwable throwable, Duration duration);
}
