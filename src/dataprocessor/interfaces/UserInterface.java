package dataprocessor.interfaces;

import dataprocessor.OutputFilePaths;
import dataprocessor.SortType;
import java.util.List;

/**
 * Інтерфейс для взаємодії з користувачем.
 * Дозволяє абстрагуватися від конкретної реалізації UI (консоль, Swing тощо).
 */
public interface UserInterface {
    /**
     * Запускає цикл взаємодії з користувачем.
     */
    void start();

    /**
     * Запитує у користувача та повертає вибраний тип сортування.
     * @return Вибраний SortType.
     */
    SortType selectSortType();

    /**
     * Запитує у користувача або повертає шляхи до вихідних файлів.
     * @return Об'єкт OutputFilePaths з шляхами.
     */
    OutputFilePaths getOutputFilenames();

    /**
     * Запитує у користувача або повертає список шляхів до вхідних файлів.
     * @return Список рядків з шляхами.
     */
    List<String> getInputFilenames();
    
    /**
     * Відображає інформаційне повідомлення користувачеві.
     * @param message Повідомлення для відображення.
     */
    void displayMessage(String message);

    /**
     * Відображає повідомлення про помилку користувачеві.
     * @param errorMessage Текст помилки.
     */
    void displayError(String errorMessage);
}
