package dataprocessor.services;

import dataprocessor.Country;
import dataprocessor.SortType;
import dataprocessor.classification.PopulationClassifier;
import dataprocessor.exceptions.CountryDataException;
import dataprocessor.interfaces.ICountryReader;
import dataprocessor.interfaces.ICountryWriter;
import dataprocessor.sorting.CountrySorter;
import dataprocessor.OutputFilePaths; // Припускаємо, що OutputFilePaths знаходиться в пакеті ui

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервісний клас, що координує процес обробки даних про країни.
 * Він використовує ICountryReader, ICountryWriter, PopulationClassifier та CountrySorter.
 */
public class CountryService {
    private final ICountryReader countryReader;
    private final ICountryWriter countryWriter;
    private final PopulationClassifier populationClassifier;
    private final CountrySorter countrySorter;

    // Пороги для класифікації населення. Можна винести в конфігураційний файл або передавати.
    // Ці значення взяті з вашого оригінального CountryProcessor.
    private static final double DEFAULT_POPULATION_LOW_MAX = 10.0;
    private static final double DEFAULT_POPULATION_MID_MAX = 50.0;

    /**
     * Конструктор для CountryService.
     * @param reader Реалізація ICountryReader.
     * @param writer Реалізація ICountryWriter.
     * @param classifier Реалізація PopulationClassifier.
     * @param sorter Реалізація CountrySorter.
     */
    public CountryService(ICountryReader reader, ICountryWriter writer, PopulationClassifier classifier, CountrySorter sorter) {
        this.countryReader = reader;
        this.countryWriter = writer;
        this.populationClassifier = classifier; // Використовуємо переданий класифікатор
        this.countrySorter = sorter;
    }
    
    /**
     * Альтернативний конструктор, який використовує класифікатор з порогами за замовчуванням.
     */
    public CountryService(ICountryReader reader, ICountryWriter writer, CountrySorter sorter) {
        this.countryReader = reader;
        this.countryWriter = writer;
        this.populationClassifier = new PopulationClassifier(DEFAULT_POPULATION_LOW_MAX, DEFAULT_POPULATION_MID_MAX);
        this.countrySorter = sorter;
    }


    /**
     * Обробляє дані про країни: читає з вхідних файлів, класифікує за населенням,
     * сортує кожну категорію та записує у відповідні вихідні файли.
     * @param inputFilenames Список імен вхідних файлів.
     * @param outputFiles Об'єкт з іменами вихідних файлів.
     * @param sortType Тип сортування.
     * @throws IOException Якщо виникає помилка вводу/виводу під час читання або запису файлів.
     * @throws CountryDataException Якщо дані у вхідних файлах мають невірний формат.
     */
    public void processData(List<String> inputFilenames, OutputFilePaths outputFiles, SortType sortType)
            throws IOException, CountryDataException {
        
        if (inputFilenames == null || inputFilenames.isEmpty()) {
            throw new IllegalArgumentException("Список вхідних файлів не може бути порожнім.");
        }
        if (outputFiles == null) {
            throw new IllegalArgumentException("Об'єкт OutputFilePaths не може бути null.");
        }
        if (sortType == null) {
            throw new IllegalArgumentException("Тип сортування не може бути null.");
        }

        List<Country> allCountries = new ArrayList<>();
        for (String filename : inputFilenames) {
            // Обробка помилок читання для кожного файлу окремо.
            // Якщо один файл не вдалося прочитати, сервіс може продовжити з іншими
            // або перервати операцію, прокинувши виняток.
            // Поточна реалізація прокидає виняток, зупиняючи обробку.
            allCountries.addAll(countryReader.readCountries(filename));
        }

        if (allCountries.isEmpty()) {
            // Якщо жодної країни не було прочитано (наприклад, всі файли порожні або містять лише заголовки)
            // Можна вивести повідомлення або нічого не робити.
            // Для узгодженості, якщо файли були, але даних немає, то створяться порожні вихідні файли.
            System.out.println("Попередження: Не знайдено даних про країни у вхідних файлах.");
        }

        // Класифікація країн за населенням
        Map<String, List<Country>> classifiedCountries = populationClassifier.classifyByPopulation(allCountries);

        // Отримання списків країн для кожної категорії
        List<Country> lowPopulationCountries = classifiedCountries.get(PopulationClassifier.LOW_POPULATION_KEY);
        List<Country> midPopulationCountries = classifiedCountries.get(PopulationClassifier.MID_POPULATION_KEY);
        List<Country> highPopulationCountries = classifiedCountries.get(PopulationClassifier.HIGH_POPULATION_KEY);

        // Сортування кожної категорії
        countrySorter.sort(lowPopulationCountries, sortType);
        countrySorter.sort(midPopulationCountries, sortType);
        countrySorter.sort(highPopulationCountries, sortType);

        // Запис відсортованих списків у відповідні файли
        // Помилки запису також прокидаються далі
        countryWriter.writeCountries(lowPopulationCountries, outputFiles.getLowPopFile());
        countryWriter.writeCountries(midPopulationCountries, outputFiles.getMidPopFile());
        countryWriter.writeCountries(highPopulationCountries, outputFiles.getHighPopFile());
    }
}
