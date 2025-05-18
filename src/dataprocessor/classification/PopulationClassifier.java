package dataprocessor.classification;

import dataprocessor.Country;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класифікує країни за категоріями населення.
 */
public class PopulationClassifier {
    // Ключі для категорій населення
    public static final String LOW_POPULATION_KEY = "low";
    public static final String MID_POPULATION_KEY = "mid";
    public static final String HIGH_POPULATION_KEY = "high";

    private final double lowPopulationMax;
    private final double midPopulationMax;

    /**
     * Конструктор для PopulationClassifier.
     * @param lowPopulationMax Максимальне значення для категорії "мало населення".
     * @param midPopulationMax Максимальне значення для категорії "середнє населення".
     */
    public PopulationClassifier(double lowPopulationMax, double midPopulationMax) {
        if (lowPopulationMax >= midPopulationMax) {
            throw new IllegalArgumentException("lowPopulationMax повинен бути меншим за midPopulationMax");
        }
        this.lowPopulationMax = lowPopulationMax;
        this.midPopulationMax = midPopulationMax;
    }

    /**
     * Класифікує список країн за населенням.
     * @param countries Список країн для класифікації.
     * @return Мапа, де ключ - це категорія населення (LOW_POPULATION_KEY, MID_POPULATION_KEY, HIGH_POPULATION_KEY),
     * а значення - список країн, що належать до цієї категорії.
     */
    public Map<String, List<Country>> classifyByPopulation(List<Country> countries) {
        Map<String, List<Country>> classifiedCountries = new HashMap<>();
        classifiedCountries.put(LOW_POPULATION_KEY, new ArrayList<>());
        classifiedCountries.put(MID_POPULATION_KEY, new ArrayList<>());
        classifiedCountries.put(HIGH_POPULATION_KEY, new ArrayList<>());

        for (Country country : countries) {
            if (country.getPopulation() <= lowPopulationMax) {
                classifiedCountries.get(LOW_POPULATION_KEY).add(country);
            } else if (country.getPopulation() <= midPopulationMax) { // country.getPopulation() > lowPopulationMax вже мається на увазі
                classifiedCountries.get(MID_POPULATION_KEY).add(country);
            } else {
                classifiedCountries.get(HIGH_POPULATION_KEY).add(country);
            }
        }
        return classifiedCountries;
    }
}
