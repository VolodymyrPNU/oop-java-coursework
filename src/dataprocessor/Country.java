package dataprocessor;

/**
 * Представляє країну з її атрибутами.
 * Цей клас залишається без змін, оскільки він добре виконує свою роль об'єкта даних (DTO).
 */
public class Country {
    private int number; // Порядковий номер (може бути переосмислений при записі)
    private String state; // Назва держави
    private double population; // Населення (в мільйонах)
    private String capital; // Столиця

    public Country(int number, String state, double population, String capital) {
        this.number = number;
        this.state = state;
        this.population = population;
        this.capital = capital;
    }

    // Getters
    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public double getPopulation() {
        return population;
    }

    public String getCapital() {
        return capital;
    }

    @Override
    public String toString() {
        // Цей метод може використовуватися для налагодження або простого виведення.
        // Формат для запису у файл контролюється класом FileCountryWriter.
        return String.format("Номер: %d, Держава: %s, Населення: %.1f млн, Столиця: %s",
                number, state, population, capital);
    }
}
