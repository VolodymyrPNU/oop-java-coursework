package ui;

import dataprocessor.OutputFilePaths;
import dataprocessor.SortType;
import dataprocessor.interfaces.UserInterface;
import dataprocessor.exceptions.CountryDataException;
import dataprocessor.services.CountryService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Реалізація UserInterface для взаємодії з користувачем через консоль.
 */
public class ConsoleUI implements UserInterface {
    private final Scanner scanner;
    private final CountryService countryService;

    // Шляхи до файлів за замовчуванням. Можна зробити їх конфігурованими.
    private final List<String> defaultInputFiles = Arrays.asList("input/europe_сountries.txt", "input/america_countries.txt", "input/asia_countries.txt");
    private final String defaultOutputFileLowPop = "output/countries_low_population.txt";
    private final String defaultOutputFileMidPop = "output/countries_mid_population.txt";
    private final String defaultOutputFileHighPop = "output/countries_high_population.txt";

    public ConsoleUI(CountryService countryService) {
        this.scanner = new Scanner(System.in);
        this.countryService = countryService;
    }

    @Override
    public void start() {
        displayMessage("Ласкаво просимо до програми обробки даних про країни!");
        displayMessage("=====================================================");

        try {
            List<String> inputFiles = getInputFilenames(); 
            OutputFilePaths outputPaths = getOutputFilenames(); 
            SortType sortType = selectSortType();

            displayMessage("\nОбраний тип сортування: " + sortType.getDescription());
            displayMessage("Обробка файлів...");

            countryService.processData(inputFiles, outputPaths, sortType);
            
            displayMessage("\nОбробку успішно завершено.");
            displayMessage("Результати збережено у файлах:");
            displayMessage("- " + outputPaths.getLowPopFile());
            displayMessage("- " + outputPaths.getMidPopFile());
            displayMessage("- " + outputPaths.getHighPopFile());

        } catch (IOException e) {
            displayError("Сталася помилка вводу/виводу: " + e.getMessage());
            // e.printStackTrace(); // Для налагодження
        } catch (CountryDataException e) {
            displayError("Сталася помилка формату даних: " + e.getMessage());
            // e.printStackTrace(); // Для налагодження
        } catch (IllegalArgumentException e) {
            displayError("Невірні параметри: " + e.getMessage());
        } catch (Exception e) {
            displayError("Сталася непередбачена помилка: " + e.getMessage());
            // e.printStackTrace(); // Для налагодження
        } finally {
            // scanner.close(); // Закриття Scanner тут може призвести до проблем, якщо System.in ще потрібен
        }
    }

    @Override
    public SortType selectSortType() {
        displayMessage("\nВиберіть тип сортування:");
        SortType[] options = SortType.values();
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i].getDescription());
        }

        int choice = -1;
        while (true) {
            System.out.print("Введіть номер опції (1-" + options.length + "): ");
            try {
                String line = scanner.nextLine();
                choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= options.length) {
                    break; 
                } else {
                    displayError("Неправильний вибір. Будь ласка, введіть число від 1 до " + options.length + ".");
                }
            } catch (NumberFormatException e) {
                displayError("Неправильний ввід. Будь ласка, введіть число.");
            }
        }
        return options[choice - 1];
    }

    @Override
    public OutputFilePaths getOutputFilenames() {
        // Для простоти, поки що використовуємо шляхи за замовчуванням.
        // Можна розширити для запиту шляхів у користувача.
        displayMessage("\nВикористовуються стандартні імена вихідних файлів:");
        displayMessage("- Шлях для країн з малим населенням: " + defaultOutputFileLowPop);
        displayMessage("- Шлях для країн з середнім населенням: " + defaultOutputFileMidPop);
        displayMessage("- Шлях для країн з великим населенням: " + defaultOutputFileHighPop);
        // Тут можна додати запит користувача, чи хоче він змінити ці шляхи
        return new OutputFilePaths(defaultOutputFileLowPop, defaultOutputFileMidPop, defaultOutputFileHighPop);
    }

    @Override
    public List<String> getInputFilenames() {
        // Для простоти, поки що використовуємо шляхи за замовчуванням.
        // Можна розширити для запиту шляхів у користувача.
        displayMessage("\nВикористовуються стандартні вхідні файли:");
        defaultInputFiles.forEach(file -> displayMessage("- " + file));
        // Тут можна додати запит користувача, чи хоче він змінити ці шляхи або додати інші
        return new ArrayList<>(defaultInputFiles); // Повертаємо копію
    }


    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String errorMessage) {
        System.err.println("ПОМИЛКА: " + errorMessage);
    }
}
