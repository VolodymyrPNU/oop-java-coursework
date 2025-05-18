package dataprocessor;

/**
 * Перелік можливих типів сортування країн.
 * Цей enum залишається без змін.
 */
public enum SortType {
    BY_STATE("По назві держави"),
    BY_POPULATION_STATE("По населенню (потім по назві)"),
    BY_CAPITAL_STATE("По столиці (потім по назві)");

    private final String description;

    SortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description; // Для зручного відображення в UI
    }
}
