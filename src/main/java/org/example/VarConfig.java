package org.example;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VarConfig implements Serializable {

    public enum ConfigKey {
        N, L, WORD_LENGTH, A, B, C, T, P_MUT, L_ROWS, L_COLS, REPR_LIPOWSKA;
    }

    private final Map<ConfigKey, Number> config = new HashMap<>(Map.ofEntries(
        new AbstractMap.SimpleEntry<>(ConfigKey.N, 1),
        new AbstractMap.SimpleEntry<>(ConfigKey.L, 60),
        new AbstractMap.SimpleEntry<>(ConfigKey.WORD_LENGTH, 4),
        new AbstractMap.SimpleEntry<>(ConfigKey.A, 0.05),
        new AbstractMap.SimpleEntry<>(ConfigKey.B, 5.0),
        new AbstractMap.SimpleEntry<>(ConfigKey.C, 0.005),
        new AbstractMap.SimpleEntry<>(ConfigKey.T, 10000),
        new AbstractMap.SimpleEntry<>(ConfigKey.P_MUT, 0.001),
        new AbstractMap.SimpleEntry<>(ConfigKey.L_ROWS, 0),
        new AbstractMap.SimpleEntry<>(ConfigKey.L_COLS, 0),
        new AbstractMap.SimpleEntry<>(ConfigKey.REPR_LIPOWSKA, 1)
    ));

    public VarConfig(Map<ConfigKey, Number> config) {
        this.config.putAll(config);
    }

    public int N() {
        return config.get(ConfigKey.N).intValue();
    }

    public int L() {
        return config.get(ConfigKey.L).intValue();
    }

    public int WORD_LENGTH() {
        return config.get(ConfigKey.WORD_LENGTH).intValue();
    }

    public double A() {
        return config.get(ConfigKey.A).doubleValue();
    }

    public double B() {
        return config.get(ConfigKey.B).doubleValue();
    }

    public double C() {
        return config.get(ConfigKey.C).doubleValue();
    }

    public int T() {
        return config.get(ConfigKey.T).intValue();
    }

    public void setT(int value) {
        config.put(ConfigKey.T, value);
    }

    public double P_MUT() {
        return config.get(ConfigKey.P_MUT).doubleValue();
    }

    public int L_ROWS() {
        return config.get(ConfigKey.L_ROWS).intValue();
    }

    public int L_COLS() {
        return config.get(ConfigKey.L_COLS).intValue();
    }

    public int REPR_LIPOWSKA() {
        return config.get(ConfigKey.REPR_LIPOWSKA).intValue();
    }

    @Override
    public String toString() {
        return config.toString();
    }
}