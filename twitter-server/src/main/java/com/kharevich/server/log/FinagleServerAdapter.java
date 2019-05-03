package com.kharevich.server.log;

import ch.qos.logback.access.spi.ServerAdapter;
import com.twitter.finagle.http.Response;
import com.twitter.util.Duration;
import com.twitter.util.Time;

import java.util.Map;

public class FinagleServerAdapter implements ServerAdapter {

  private Response response;

  public FinagleServerAdapter(Response response) {
    this.response = response;
  }


  @Override public long getRequestTimestamp() {
    return Time.now().inMillis();
  }

  @Override public long getContentLength() {
    return response.getLength();
  }

  @Override public int getStatusCode() {
    return this.response.getStatusCode();
  }

  @Override public Map<String, String> buildResponseHeaderMap() {
    return null;
  }
}
