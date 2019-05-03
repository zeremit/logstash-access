package com.kharevich.server.log;

import com.google.inject.Provides;
import com.twitter.inject.Injector;
import com.twitter.inject.TwitterModule;

import javax.inject.Singleton;

public class LogbackAccessModule extends TwitterModule {
    @Override
    public void singletonStartup(Injector injector) {
        injector.instance(AccessLogger.class).start();
    }

    @Override
    public void singletonShutdown(Injector injector) {
        injector.instance(AccessLogger.class).stop();
    }

    @Singleton
    @Provides
    public AccessLogger logbackAccess() {
        return new FinagleAccessLogger();
    }


}
