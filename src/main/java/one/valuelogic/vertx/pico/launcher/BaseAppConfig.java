package one.valuelogic.vertx.pico.launcher;

public interface BaseAppConfig {
    int getMainVerticleInstances();
    BaseMetricsConfig getMetrics();
}
