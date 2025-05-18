package dataprocessor.interfaces;

import dataprocessor.Country;
import dataprocessor.exceptions.CountryDataException;
import java.io.IOException;
import java.util.List;

/**
 * Інтерфейс для читання даних про країни з будь-якого джерела.
 */
public interface ICountryReader {
    /**
     * Читає дані про країни з вказаного шляху.
     * @param filePath Шлях до джерела даних (наприклад, ім'я файлу).
     * @return Список об'єктів Country.
     * @throws IOException Якщо виникає помилка вводу/виводу.
     * @throws CountryDataException Якщо дані мають невірний формат.
     */
    List<Country> readCountries(String filePath) throws IOException, CountryDataException;
}
