package one.valuelogic.vertx.pico;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static one.valuelogic.vertx.pico.PicoVerticleFactory.verticleName;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(VertxUnitRunner.class)
public class PicoVerticleFactoryTest {

    private Vertx vertx;
    private MutablePicoContainer picoContainer;


    public static class ExampleVerticle extends AbstractVerticle {
        private final AtomicInteger count;

        public ExampleVerticle(AtomicInteger count) {
            this.count = count;
        }

        @Override
        public void start() throws Exception {
            count.incrementAndGet();
        }
    }

    @Before
    public void before(TestContext context) {
        this.vertx = Vertx.vertx();
        this.picoContainer = new PicoBuilder().withLifecycle().build();
        vertx.exceptionHandler(context.exceptionHandler());
    }

    @After
    public void after(TestContext context) {
        this.picoContainer.stop();
        this.vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void shouldDeployVerticleUsingPicoFactory(TestContext context) {

        final AtomicInteger count = new AtomicInteger(0);
        picoContainer.addComponent(count);
        picoContainer.addComponent(ExampleVerticle.class);
        picoContainer.start();

        vertx.registerVerticleFactory(new PicoVerticleFactory(picoContainer));
        Async async = context.async();

        vertx.deployVerticle(
                verticleName(ExampleVerticle.class),
                new DeploymentOptions().setInstances(5),
                ar -> {
                    if (ar.succeeded()) {
                        assertThat(count.get()).isEqualTo(5);
                        async.complete();
                    }
                    else {
                        context.fail(ar.cause());
                    }
                });
    }

}
