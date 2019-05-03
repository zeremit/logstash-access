package com.kharevich.server.log;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FinagleAccessLogger extends AccessContext implements AccessLogger, AppenderAttachable<IAccessEvent>,
    FilterAttachable<IAccessEvent> {

    final private static String CONFIG_FILE_PROPERTY = "logback.accessConfigurationFile";

    private final static String DEFAULT_CONFIG_FILE = "logback-access.xml";

    private boolean started;

    private URL getConfigurationFileURL() {
        String logbackConfigFile = OptionHelper.getSystemProperty(CONFIG_FILE_PROPERTY);
        if (logbackConfigFile != null) {
            URL result = null;
            try {
                result = new URL(logbackConfigFile);
                return result;
            } catch (MalformedURLException e) {
                // so, resource is not a URL:
                // attempt to get the resource from the class path
                result = Loader.getResource(logbackConfigFile, getClass().getClassLoader());
                if (result != null) {
                    return result;
                }
                File f = new File(logbackConfigFile);
                if (f.exists() && f.isFile()) {
                    try {
                        result = f.toURI().toURL();
                        return result;
                    } catch (MalformedURLException e1) {
                    }
                }
            }
        }
        return Loader.getResource(DEFAULT_CONFIG_FILE, getClass().getClassLoader());
    }

    protected void configure() {
        URL configURL = getConfigurationFileURL();
        if (configURL != null) {
            runJoranOnFile(configURL);
        } else {
            addError("Could not find configuration file for logback-access");
        }
    }

    private void addError(String msg) {
        getStatusManager().add(new ErrorStatus(msg, this));
    }

    private void runJoranOnFile(URL configURL) {
        try {
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(this);
            jc.doConfigure(configURL);
            if (getName() == null) {
                setName("LogbackRequestLog");
            }
        } catch (JoranException e) {
            // errors have been registered as status messages
        }
    }

    @Override
    public void start() {
        configure();
        started = true;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void log(Request request, Response response, Duration duration) {
        this.callAppenders(new FinagleAccessEvent(request, response, duration));
    }

    @Override
    public void log(Request request, Throwable throwable, Duration duration) {
        this.callAppenders(new FinagleThrowableAccessEvent(request, throwable, duration));
    }
}
