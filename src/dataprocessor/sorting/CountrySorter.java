package dataprocessor.sorting;

import dataprocessor.Country;
import dataprocessor.SortType;
import java.util.Comparator;
import java.util.List;

/**
 * Відповідає за сортування списку країн.
 */
public class CountrySorter {

    /**
     * Сортує наданий список країн відповідно до вказаного типу сортування.
     * @param countries Список країн для сортування (модифікується на місці).
     * @param sortType Тип сортування.
     */
    public void sort(List<Country> countries, SortType sortType) {
        if (countries == null || sortType == null) {
            // Або кинути IllegalArgumentException, або просто нічого не робити
            System.err.println("Список країн або тип сортування не може бути null.");
            return;
        }
        countries.sort(getComparator(sortType));
    }

    /**
     * Повертає Comparator для об'єктів Country на основі типу сортування.
     * @param sortType Тип сортування.
     * @return Відповідний Comparator.
     */
    public Comparator<Country> getComparator(SortType sortType) {
        switch (sortType) {
            case BY_STATE:
                return Comparator.comparing(Country::getState, String.CASE_INSENSITIVE_ORDER);
            case BY_POPULATION_STATE:
                return Comparator.comparingDouble(Country::getPopulation)
                                 .thenComparing(Country::getState, String.CASE_INSENSITIVE_ORDER);
            case BY_CAPITAL_STATE:
                return Comparator.comparing(Country::getCapital, String.CASE_INSENSITIVE_ORDER)
                                 .thenComparing(Country::getState, String.CASE_INSENSITIVE_ORDER);
            default:
                // За замовчуванням або якщо тип невідомий (хоча з enum це малоймовірно)
                System.err.println("Невідомий тип сортування: " + sortType + ". Використовується сортування за назвою держави.");
                return Comparator.comparing(Country::getState, String.CASE_INSENSITIVE_ORDER);
        }
    }
}
