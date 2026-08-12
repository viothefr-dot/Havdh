package dev.nyxclient.setting;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Setting<T> {
    private final String name;
    private final T defaultValue;
    private T value;
    private final Supplier<Boolean> visibility;

    protected Setting(String name, T defaultValue) {
        this(name, defaultValue, () -> true);
    }

    protected Setting(String name, T defaultValue, Supplier<Boolean> visibility) {
        this.name = Objects.requireNonNull(name);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.visibility = visibility;
    }

    public String name() { return name; }
    public T get() { return value; }
    public void set(T value) { this.value = Objects.requireNonNull(value); }
    public T defaultValue() { return defaultValue; }
    public boolean visible() { return visibility.get(); }
    public void reset() { value = defaultValue; }
}
