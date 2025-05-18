package main; // Рекомендовано використовувати нижній регістр для назв пакетів

import dataprocessor.classification.PopulationClassifier;
import dataprocessor.FileCountryReader;
import dataprocessor.FileCountryWriter;
import dataprocessor.interfaces.ICountryReader;
import dataprocessor.interfaces.ICountryWriter;
import dataprocessor.services.CountryService;
import dataprocessor.sorting.CountrySorter;
import ui.ConsoleUI;
import ui.SwingUI;
import dataprocessor.interfaces.UserInterface;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        // Створення залежностей для CountryService
        ICountryReader reader = new FileCountryReader();
        ICountryWriter writer = new FileCountryWriter();
        
        // Пороги для класифікатора населення (можна винести в конфігурацію)
        double lowPopMax = 10.0;
        double midPopMax = 50.0;
        PopulationClassifier classifier = new PopulationClassifier(lowPopMax, midPopMax);
        
        CountrySorter sorter = new CountrySorter();

        // Створення екземпляра CountryService
        CountryService countryService = new CountryService(reader, writer, classifier, sorter);

        // Вибір типу інтерфейсу (можна зробити через аргумент командного рядка, наприклад)
        UserInterface ui;
        if (args.length > 0 && args[0].equalsIgnoreCase("swing")) {
            try {
                // Спробуємо встановити системний LookAndFeel для кращого вигляду
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Не вдалося встановити системний LookAndFeel: " + e.getMessage());
            }
            ui = new SwingUI(countryService);
        } else {
            ui = new ConsoleUI(countryService);
        }

        // Запуск вибраного інтерфейсу
        ui.start();
    }
}
