package com.kharevich.server.log;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.access.spi.ServerAdapter;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FinagleAccessEvent extends BaseAccessEvent implements IAccessEvent {

    private ServerAdapter serverAdapter;

    private Response response;

    private Map<String, String> responseHeader;

    FinagleAccessEvent(Request request, Response response, Duration duration) {
        super(request, duration);
        this.response = response;
        this.serverAdapter = new FinagleServerAdapter(response);
    }

    @Override
    public String getServerName() {
        return response.server().get();
    }

    @Override
    public String[] getRequestParameter(String s) {
        return getRequestParameterMap().get(s);
    }

    @Override
    public long getContentLength() {
        return response.length();
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getResponseContent() {
        return response.contentString();
    }

    @Override
    public ServerAdapter getServerAdapter() {
        return serverAdapter;
    }

    @Override
    public String getResponseHeader(String s) {
        return getResponseHeaderMap().get(s);
    }

    @Override
    public Map<String, String> getResponseHeaderMap() {
        responseHeader = Optional.ofNullable(responseHeader)
            .orElse(serverAdapter.buildResponseHeaderMap());
        return responseHeader;
    }

    @Override
    public List<String> getResponseHeaderNameList() {
        return new ArrayList<>(getResponseHeaderMap().keySet());
    }

    @Override
    public void prepareForDeferredProcessing() {
        //TODO
    }
}
