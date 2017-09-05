package one.valuelogic.vertx.pico;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.picocontainer.PicoContainer;

public class PicoVerticleFactory implements VerticleFactory {

    public static final String PREFIX = "pico";

    public static String verticleName(Class<? extends Verticle> clazz) {
        return PREFIX + ":" + clazz.getName();
    }

    private final PicoContainer picoContainer;

    public PicoVerticleFactory(PicoContainer picoContainer) {
        this.picoContainer = picoContainer;
    }

    @Override
    public String prefix() {
        return PREFIX;
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        Class<Verticle> clazz = (Class<Verticle>) Class.forName(VerticleFactory.removePrefix(verticleName));
        return picoContainer.getComponent(clazz);
    }
}
