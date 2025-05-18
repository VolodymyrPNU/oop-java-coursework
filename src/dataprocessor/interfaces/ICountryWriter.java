package dataprocessor.interfaces;

import dataprocessor.Country;
import java.io.IOException;
import java.util.List;

/**
 * Інтерфейс для запису даних про країни в будь-яке сховище.
 */
public interface ICountryWriter {
    /**
     * Записує список країн у вказаний шлях.
     * @param countries Список країн для запису.
     * @param filePath Шлях до сховища (наприклад, ім'я файлу).
     * @throws IOException Якщо виникає помилка вводу/виводу.
     */
    void writeCountries(List<Country> countries, String filePath) throws IOException;
}
