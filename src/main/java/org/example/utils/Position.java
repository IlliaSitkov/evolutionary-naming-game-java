package org.example.utils;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {
    private static final long serialVersionUID = 1L;
}