package one.valuelogic.vertx.pico.launcher;

import org.picocontainer.PicoContainer;

public interface PicoAppBootstrap {
    PicoContainer getPicoContainer();
    BaseAppConfig getConfig();
    void close();
}