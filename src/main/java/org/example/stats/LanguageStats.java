package org.example.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.entities.Agent;
import org.example.entities.World;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LanguageStats {

    private final double languageThreshold;

    @Getter
    List<UniformLanguage> languages = new ArrayList<>();

    @Getter
    private List<UniformLanguage> languagesSnapshot;

    UniformLanguage currentUniformLanguage = null;

    public void trackIteration(int iteration, World world) {
        if (iteration == 0) {
            languagesSnapshot = takeLanguagesSnapshot(world);
        }

        UniformLanguage language = getUniformLanguage(languageThreshold, world);

        if (language == null || currentUniformLanguage != null && currentUniformLanguage.getLanguage() == language.getLanguage()) {
            return;
        }

        if (currentUniformLanguage == null) {
            currentUniformLanguage = language;
            language.setDisplacedTime(iteration);
            language.setIteration(iteration);
            languages.add(currentUniformLanguage);
        } else {
            language.setDisplacedTime(iteration - currentUniformLanguage.getIteration());
            language.setIteration(iteration);
            languages.add(language);
            currentUniformLanguage = language;
        }
    }

    public double getAvgDisplacedTime() {
        double totalTime = 0;
        int count = 0;

        for (UniformLanguage uniformLanguage: languages) {
            if (uniformLanguage.getDisplacedTime() == null) {
                continue;
            }
            totalTime += uniformLanguage.getDisplacedTime();
            count++;
        }

        return totalTime / count;
    }

    private UniformLanguage getUniformLanguage(double languageThreshold, World world) {
        Map<String, Integer> languageCounts = countLanguages(world);
        int totalAgents = world.getAgents().size();

        for (Map.Entry<String, Integer> entry : languageCounts.entrySet()) {
            double percentage = (double) entry.getValue() / totalAgents;
            if (percentage >= languageThreshold) {
                return new UniformLanguage(percentage, entry.getKey());
            }
        }

        return null;
    }

    private Map<String, Integer> countLanguages(World world) {
        Map<String, Integer> languageCounts = new HashMap<>();
        List<Agent> agents = world.getAgents();

        for (Agent agent : agents) {
            if (agent.getLexicon().isEmpty()) {
                continue;
            }
            String language = agent.getLexicon().getTopWord();
            languageCounts.put(language, languageCounts.getOrDefault(language, 0) + 1);
        }

        return languageCounts;
    }

    private List<UniformLanguage> takeLanguagesSnapshot(World world) {
        Map<String, Integer> languageCounts = countLanguages(world);
        int totalAgents = world.getAgents().size();
        List<UniformLanguage> languagesSnapshot = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : languageCounts.entrySet()) {
            double percentage = (double) entry.getValue() / totalAgents;
            languagesSnapshot.add(new UniformLanguage(percentage, entry.getKey()));
        }

        return languagesSnapshot;
    }

}
