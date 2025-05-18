package dataprocessor;

import dataprocessor.interfaces.ICountryWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Реалізація ICountryWriter для запису даних про країни у текстовий файл.
 */
public class FileCountryWriter implements ICountryWriter {
    @Override
    public void writeCountries(List<Country> countries, String filename) throws IOException {
        // Використання try-with-resources для автоматичного закриття потоку
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            
            // Записуємо заголовок
            writer.println("N State Population Capital"); 

            int currentLineNumber = 1; // Нумерація записів у вихідному файлі
            for (Country country : countries) {
                // Використовуємо Locale.US для форматування чисел з крапкою як роздільником
                writer.printf(Locale.US, "%d %s %.1f %s%n",
                        currentLineNumber++, // Використовуємо поточний номер рядка
                        country.getState(),
                        country.getPopulation(),
                        country.getCapital());
            }
        }
        // IOException буде прокинуто, якщо виникне під час запису
    }
}
