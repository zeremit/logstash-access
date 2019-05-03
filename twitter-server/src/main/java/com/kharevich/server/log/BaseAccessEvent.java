package com.kharevich.server.log;

import ch.qos.logback.access.pattern.AccessConverter;
import ch.qos.logback.access.spi.IAccessEvent;
import com.twitter.finagle.http.Request;
import com.twitter.util.Duration;
import com.twitter.util.Time;
import scala.collection.JavaConverters;

import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseAccessEvent implements IAccessEvent {

    private Duration duration;

    private Request request;

    private String requestURL;

    private String threadName;

    private Map<String, String> requestHeader;

    private Map<String, String[]> requestParameter;

    public BaseAccessEvent(Request request, Duration duration) {
        this.duration = duration;
        this.request = request;
        this.threadName = Thread.currentThread().getName();
    }

    @Override
    public HttpServletRequest getRequest() {
        return null;
    }

    @Override
    public HttpServletResponse getResponse() {
        return null;
    }

    @Override
    public long getTimeStamp() {
        return Time.now().inMillis();
    }

    @Override
    public long getElapsedTime() {
        return duration.inMillis();
    }

    @Override
    public long getElapsedSeconds() {
        return duration.inSeconds();
    }

    @Override
    public String getRequestURI() {
        return request.uri();
    }

    @Override
    public String getRequestURL() {
        if (requestURL == null) {
            if (request != null) {
                StringBuilder buf = new StringBuilder();
                buf.append(request.method().name());
                buf.append(AccessConverter.SPACE_CHAR);
                buf.append(request.uri());
                buf.append(AccessConverter.SPACE_CHAR);
                buf.append(request.version().versionString());
                requestURL = buf.toString();
            } else {
                requestURL = NA;
            }
        }
        return requestURL;
    }

    @Override
    public String getRemoteHost() {
        return request.remoteHost();
    }

    @Override
    public String getRemoteUser() {
        //TODO
        return null;
    }

    @Override
    public String getProtocol() {
        return request.version().versionString();
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public String getSessionID() {
        //TODO
        return null;
    }

    @Override
    public String getAttribute(String key) {
        //TODO
        return null;
    }

    @Override
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String getThreadName() {
        return threadName;
    }

    @Override
    public String getQueryString() {
        //TODO
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return request.remoteAddress().getHostName();
    }

    @Override
    public String getRequestHeader(String s) {
        return getRequestHeaderMap().get(s);
    }

    @Override
    public Enumeration getRequestHeaderNames() {
        return new Vector<>(getRequestHeaderMap().keySet()).elements();
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        requestHeader = Optional.ofNullable(requestHeader)
            .orElse(JavaConverters.mapAsJavaMapConverter(request.headerMap()).asJava());
        return requestHeader;
    }

    @Override
    public Map<String, String[]> getRequestParameterMap() {
        requestParameter = Optional.ofNullable(requestParameter)
            .orElse(JavaConverters.mapAsJavaMapConverter(request.params()).asJava().
                entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new String[]{e.getValue()})));
        return requestParameter;
    }

    @Override
    public String getRequestContent() {
        return request.contentString();
    }

    @Override
    public String getCookie(String s) {
        //TODO
        return null;
    }

    @Override
    public int getLocalPort() {
        //TODO
        return SENTINEL;
    }

}
