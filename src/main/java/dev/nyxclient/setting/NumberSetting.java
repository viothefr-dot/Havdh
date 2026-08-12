package dev.nyxclient.setting;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double min() { return min; }
    public double max() { return max; }
    public double step() { return step; }

    @Override
    public void set(Double value) {
        super.set(Math.max(min, Math.min(max, value)));
    }
}
