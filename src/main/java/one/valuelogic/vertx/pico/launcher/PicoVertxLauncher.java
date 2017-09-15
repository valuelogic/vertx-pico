package one.valuelogic.vertx.pico.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import one.valuelogic.vertx.pico.PicoVerticleFactory;

public class PicoVertxLauncher extends Launcher {
    private PicoAppBootstrap bootstrap;

    public PicoVertxLauncher(PicoAppBootstrap bootstrap) {
        if (bootstrap == null ) {
            throw new IllegalArgumentException("PicoAppBootstrap cannot be null");
        }
        this.bootstrap = bootstrap;
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        super.beforeStartingVertx(options);
        options.getMetricsOptions().setEnabled(bootstrap.getConfig().getMetrics().isEnabled());
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        super.afterStartingVertx(vertx);
        vertx.registerVerticleFactory(new PicoVerticleFactory(this.bootstrap.getPicoContainer()));
    }

    @Override
    public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
        super.beforeDeployingVerticle(deploymentOptions);
        deploymentOptions.setInstances(this.bootstrap.getConfig().getMainVerticleInstances());
    }

    @Override
    public void afterStoppingVertx() {
        super.afterStoppingVertx();
        this.bootstrap.close();
    }
}
