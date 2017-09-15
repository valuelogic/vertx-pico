package one.valuelogic.vertx.pico.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PicoVertxLauncherTest {

    private Vertx vertx = Vertx.vertx();
    private TestAppConfig testAppConfig = new TestAppConfig();
    private PicoAppBootstrap bootstrap = mock(PicoAppBootstrap.class);
    private PicoVertxLauncher picoVertxLauncher = new PicoVertxLauncher(bootstrap);

    @Test
    public void should_configure_vertx_options_before_starting_vertx() throws Exception {
        when(bootstrap.getConfig()).thenReturn(testAppConfig);
        VertxOptions options = new VertxOptions();

        picoVertxLauncher.beforeStartingVertx(options);

        assertThat(options.getMetricsOptions().isEnabled()).isEqualTo(testAppConfig.getMetrics().isEnabled());
    }

    @Test
    public void should_configure_deployment_options_before_deployment() throws Exception {
        TestAppConfig testAppConfig = new TestAppConfig();
        DeploymentOptions options = new DeploymentOptions();
        when(bootstrap.getConfig()).thenReturn(testAppConfig);

        picoVertxLauncher.beforeDeployingVerticle(options);

        assertThat(options.getInstances()).isEqualTo(testAppConfig.getMainVerticleInstances());
    }

    @Test
    public void should_add_pico_factory_after_starting_vertx() {
        int factories = vertx.verticleFactories().size();

        picoVertxLauncher.afterStartingVertx(vertx);

        assertThat(vertx.verticleFactories().size()).isEqualTo(factories + 1);
    }

    @Test
    public void should_close_bootstrap_on_vertx_stop() {
        picoVertxLauncher.afterStoppingVertx();

        verify(bootstrap).close();
    }

    private static class TestAppConfig implements BaseAppConfig {
        @Override
        public int getMainVerticleInstances() {
            return 10;
        }

        @Override
        public BaseMetricsConfig getMetrics() {
            return () -> true;
        }
    }
}