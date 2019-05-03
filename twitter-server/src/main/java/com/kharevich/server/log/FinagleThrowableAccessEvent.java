package com.kharevich.server.log;


import ch.qos.logback.access.spi.ServerAdapter;
import com.twitter.finagle.http.Request;
import com.twitter.util.Duration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FinagleThrowableAccessEvent extends BaseAccessEvent{

    private Throwable throwable;

    public FinagleThrowableAccessEvent(Request request, Throwable throwable, Duration duration) {
        super(request, duration);
        this.throwable = throwable;
    }
    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public String[] getRequestParameter(String key) {
        return new String[0];
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

    @Override
    public String getResponseContent() {
        return throwable.getMessage();
    }

    @Override
    public ServerAdapter getServerAdapter() {
        return null;
    }

    @Override
    public String getResponseHeader(String key) {
        return NA;
    }

    @Override
    public Map<String, String> getResponseHeaderMap() {
        return Collections.emptyMap();
    }

    @Override
    public List<String> getResponseHeaderNameList() {
        return Collections.emptyList();
    }

    @Override
    public void prepareForDeferredProcessing() {

    }
}
