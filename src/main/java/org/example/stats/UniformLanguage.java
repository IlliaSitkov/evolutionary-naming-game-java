package org.example.stats;

import lombok.Getter;
import lombok.Setter;

public class UniformLanguage {
    @Getter
    @Setter
    private int iteration;
    @Setter
    @Getter
    private Integer displacedTime;
    @Getter
    private final double percentage;
    @Getter
    private final String language;

    public UniformLanguage(double percentage, String language) {
        this.percentage = percentage;
        this.language = language;
    }

}
