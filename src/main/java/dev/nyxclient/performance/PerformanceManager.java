package dev.nyxclient.performance;

public final class PerformanceManager {
    private long tick;
    private long lastNanos = System.nanoTime();
    private double tickRate = 20.0;

    public void tick() {
        tick++;
        long now = System.nanoTime();
        long delta = now - lastNanos;
        if (delta > 0) {
            double instant = 1_000_000_000.0 / delta;
            tickRate = tickRate * 0.9 + instant * 0.1;
        }
        lastNanos = now;
    }

    public long tickCount() { return tick; }
    public double estimatedTickRate() { return tickRate; }
    public boolean lowFpsMode(int fps) { return fps > 0 && fps < 45; }
}
