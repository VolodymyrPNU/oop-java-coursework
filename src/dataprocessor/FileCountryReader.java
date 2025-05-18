package dataprocessor;

import dataprocessor.interfaces.ICountryReader;
import dataprocessor.exceptions.CountryDataException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Реалізація ICountryReader для читання даних про країни з текстового файлу.
 */
public class FileCountryReader implements ICountryReader {

    @Override
    public List<Country> readCountries(String filename) throws IOException, CountryDataException {
        List<Country> countries = new ArrayList<>();
        // Використання try-with-resources для автоматичного закриття потоку
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filename), StandardCharsets.UTF_8))) {
            
            String line;
            // Читаємо перший рядок - очікуємо, що це заголовок, і пропускаємо його
            String headerLineOriginal = reader.readLine(); 
            
            if (headerLineOriginal == null) {
                throw new CountryDataException("Файл порожній або не вдалося прочитати заголовок: " + filename);
            }
            // Можна додати логування пропущеного заголовка, якщо потрібно
            // System.out.println("Пропущено заголовок: " + headerLineOriginal);

            int currentLineNumber = 1; // Для більш точних повідомлень про помилки
            while ((line = reader.readLine()) != null) {
                currentLineNumber++;
                line = line.trim();

                // Пропускаємо порожні рядки, розділювачі та можливі дублікати заголовків
                if (line.isEmpty() || line.startsWith("---") || line.equalsIgnoreCase("N State Population Capital")) {
                    continue;
                }
                
                // Парсимо рядок і додаємо країну до списку
                countries.add(parseLine(line, currentLineNumber, filename));
            }
        } catch (FileNotFoundException e) {
            // Обгортаємо FileNotFoundException в IOException для узгодженості інтерфейсу
            throw new IOException("Файл не знайдено: " + filename, e);
        }
        // Інші IOException (наприклад, проблеми читання) будуть прокинуті як є
        return countries;
    }

    /**
     * Парсить один рядок з файлу та створює об'єкт Country.
     * @param line Рядок для парсингу.
     * @param lineNumber Номер рядка у файлі (для повідомлень про помилки).
     * @param filename Ім'я файлу (для повідомлень про помилки).
     * @return Об'єкт Country.
     * @throws CountryDataException Якщо рядок має невірний формат.
     */
    private Country parseLine(String line, int lineNumber, String filename) throws CountryDataException {
        // Розділяємо рядок за одним або більше пробілами
        String[] parts = line.split("\\s+");

        // Перевірка на мінімальну кількість частин (Номер, НазваДержави, Населення, [Столиця])
        // Якщо столиця може бути відсутня, логіка має це враховувати.
        // Поточна логіка очікує щонайменше 3 частини (N, State, Population), столиця може бути далі.
        // Якщо формат "N State Population Capital" є строгим, то parts.length < 4 буде помилкою.
        // Ваша оригінальна логіка була більш гнучкою, спробуємо її відтворити.
        if (parts.length < 3) { // Номер, НазваДержави(мін 1 слово), Населення
            throw new CountryDataException(String.format("Рядок %d у файлі '%s': Замало частин (очікується щонайменше 3). Рядок: '%s'", lineNumber, filename, line));
        }

        int number;
        try {
            number = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new CountryDataException(String.format("Рядок %d у файлі '%s': Помилка парсингу номера '%s'. Рядок: '%s'", lineNumber, filename, parts[0], line), e);
        }

        double population = -1;
        int populationIndex = -1;

        // Шукаємо населення (число з плаваючою комою) справа наліво, але не перше слово (номер)
        // і не останнє (якщо це столиця і вона є)
        // Покращений пошук населення:
        // Населення - це останнє числове значення перед потенційною столицею (якщо столиця є)
        // або просто останнє числове значення, якщо столиці немає або вона не розпізнається як текст.
        for (int i = parts.length - 1; i >= 1; i--) { // Починаємо з кінця, але не з 0-го елемента (номер)
            try {
                population = Double.parseDouble(parts[i].replace(',', '.')); // Заміна коми на крапку для чисел
                populationIndex = i;
                break; 
            } catch (NumberFormatException e) {
                // Це не число, продовжуємо пошук
            }
        }

        if (populationIndex == -1) {
            throw new CountryDataException(String.format("Рядок %d у файлі '%s': Не вдалося знайти значення населення. Рядок: '%s'", lineNumber, filename, line));
        }

        // Визначаємо назву держави: частини від parts[1] до parts[populationIndex - 1]
        if (populationIndex < 2) { // Якщо populationIndex = 1, то parts[1] - це населення, отже, немає місця для назви держави.
            throw new CountryDataException(String.format("Рядок %d у файлі '%s': Неможливо визначити назву держави (недостатньо частин перед населенням). Рядок: '%s'", lineNumber, filename, line));
        }
        StringBuilder stateBuilder = new StringBuilder();
        for (int i = 1; i < populationIndex; i++) {
            stateBuilder.append(parts[i]);
            if (i < populationIndex - 1) {
                stateBuilder.append(" ");
            }
        }
        String state = stateBuilder.toString();
        if (state.isEmpty()) {
             throw new CountryDataException(String.format("Рядок %d у файлі '%s': Назва держави порожня. Рядок: '%s'", lineNumber, filename, line));
        }


        // Визначаємо столицю: частини від parts[populationIndex + 1] до кінця
        // Столиця є, якщо після населення є ще слова
        String capital = ""; // За замовчуванням порожня, якщо столиці немає
        if (populationIndex < parts.length - 1) { // Є слова після населення
            StringBuilder capitalBuilder = new StringBuilder();
            for (int i = populationIndex + 1; i < parts.length; i++) {
                capitalBuilder.append(parts[i]);
                if (i < parts.length - 1) {
                    capitalBuilder.append(" ");
                }
            }
            capital = capitalBuilder.toString();
        } else {
            // Якщо столиці немає, це може бути прийнятно або помилка залежно від вимог.
            // Для даної задачі, якщо столиця обов'язкова, тут треба кидати виняток.
            // Судячи з оригінального коду, столиця очікується.
             throw new CountryDataException(String.format("Рядок %d у файлі '%s': Відсутня столиця після населення. Рядок: '%s'", lineNumber, filename, line));
        }
        if (capital.isEmpty()){ // Додаткова перевірка, якщо столиця обов'язкова
            throw new CountryDataException(String.format("Рядок %d у файлі '%s': Назва столиці порожня. Рядок: '%s'", lineNumber, filename, line));
        }


        return new Country(number, state, population, capital);
    }
}
