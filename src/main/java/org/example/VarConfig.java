package org.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VarConfig implements Serializable {
    
    private final Map<String, Number> config = new HashMap<>(Map.of(
        "N", 1,
        "L", 60,
        "WORD_LENGTH", 4,
        "A", 0.05,
        "B", 5.0,
        "C", 0.005,
        "T", 10000,
        "P_MUT", 0.001,
        "L_ROWS", 0,
        "L_COLS", 0
    ));

    public VarConfig(Map<String, Number> config) {
        this.config.putAll(config);
    }

    public int N() {
        return config.get("N").intValue();
    }

    public int L() {
        return config.get("L").intValue();
    }

    public int WORD_LENGTH() {
        return config.get("WORD_LENGTH").intValue();
    }

    public double A() {
        return config.get("A").doubleValue();
    }

    public double B() {
        return config.get("B").doubleValue();
    }

    public double C() {
        return config.get("C").doubleValue();
    }

    public int T() {
        return config.get("T").intValue();
    }

    public double P_MUT() {
        return config.get("P_MUT").doubleValue();
    }

    public int L_ROWS() {
        return config.get("L_ROWS").intValue();
    }

    public int L_COLS() {
        return config.get("L_COLS").intValue();
    }

    @Override
    public String toString() {
        return config.toString();
    }
}