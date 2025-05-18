package ui;

import dataprocessor.OutputFilePaths;
import dataprocessor.SortType;
import dataprocessor.interfaces.UserInterface;
import dataprocessor.exceptions.CountryDataException;
import dataprocessor.services.CountryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector; // Краще використовувати ArrayList для моделі JComboBox, якщо можливо

/**
 * Реалізація UserInterface з використанням Swing для графічного інтерфейсу.
 * Надано базовий скелет.
 */
public class SwingUI implements UserInterface {

    private JFrame frame;
    private JComboBox<SortType> sortTypeComboBox;
    private DefaultListModel<String> inputFilesListModel;
    private JList<String> inputFilesList;
    private JTextField outputLowPopField, outputMidPopField, outputHighPopField;
    private JTextArea logArea;
    private JButton processButton;
    private JButton addInputFileButton, removeInputFileButton;
    private JButton selectLowPopButton, selectMidPopButton, selectHighPopButton;

    private final CountryService countryService;

    // Шляхи за замовчуванням
    private final String defaultEuropeFile = "input/europe_сountries.txt";
    private final String defaultAmericaFile = "input/america_countries.txt";
    private final String defaultAsiaFile = "input/asia_countries.txt";
    private final String defaultOutputFileLowPop = "output/countries_low_population_gui.txt";
    private final String defaultOutputFileMidPop = "output/countries_mid_population_gui.txt";
    private final String defaultOutputFileHighPop = "output/countries_high_population_gui.txt";


    public SwingUI(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void start() {
        // Запуск Swing GUI в потоці обробки подій (Event Dispatch Thread)
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Обробка даних про країни (Swing UI)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700, 600)); // Мінімальний розмір вікна
        frame.setLayout(new BorderLayout(10, 10)); // Відступи між компонентами

        // Панель налаштувань (верхня частина)
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Параметри обробки"),
            new EmptyBorder(10, 10, 10, 10) // Внутрішні відступи
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Відступи між елементами сітки
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Вхідні файли ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        settingsPanel.add(new JLabel("Вхідні файли:"), gbc);
        
        inputFilesListModel = new DefaultListModel<>();
        // Додаємо файли за замовчуванням
        inputFilesListModel.addElement(new File(defaultEuropeFile).getAbsolutePath());
        inputFilesListModel.addElement(new File(defaultAmericaFile).getAbsolutePath());
        inputFilesListModel.addElement(new File(defaultAsiaFile).getAbsolutePath());

        inputFilesList = new JList<>(inputFilesListModel);
        JScrollPane inputListScrollPane = new JScrollPane(inputFilesList);
        inputListScrollPane.setPreferredSize(new Dimension(400, 80)); // Розмір списку
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        settingsPanel.add(inputListScrollPane, gbc);

        addInputFileButton = new JButton("Додати...");
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5;
        settingsPanel.add(addInputFileButton, gbc);

        removeInputFileButton = new JButton("Видалити");
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.5;
        settingsPanel.add(removeInputFileButton, gbc);

        // --- Тип сортування ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        settingsPanel.add(new JLabel("Тип сортування:"), gbc);
        // Використовуємо Vector для JComboBox, оскільки він очікує такий тип за замовчуванням
        sortTypeComboBox = new JComboBox<>(new Vector<>(Arrays.asList(SortType.values())));
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1;
        settingsPanel.add(sortTypeComboBox, gbc);

        // --- Вихідні файли ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        settingsPanel.add(new JLabel("Файл (мало населення):"), gbc);
        outputLowPopField = new JTextField(new File(defaultOutputFileLowPop).getAbsolutePath(), 25);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        settingsPanel.add(outputLowPopField, gbc);
        selectLowPopButton = new JButton("Обрати...");
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        settingsPanel.add(selectLowPopButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        settingsPanel.add(new JLabel("Файл (середнє населення):"), gbc);
        outputMidPopField = new JTextField(new File(defaultOutputFileMidPop).getAbsolutePath(), 25);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1;
        settingsPanel.add(outputMidPopField, gbc);
        selectMidPopButton = new JButton("Обрати...");
        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        settingsPanel.add(selectMidPopButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        settingsPanel.add(new JLabel("Файл (багато населення):"), gbc);
        outputHighPopField = new JTextField(new File(defaultOutputFileHighPop).getAbsolutePath(), 25);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1;
        settingsPanel.add(outputHighPopField, gbc);
        selectHighPopButton = new JButton("Обрати...");
        gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0;
        settingsPanel.add(selectHighPopButton, gbc);

        frame.add(settingsPanel, BorderLayout.NORTH);

        // Панель логів (центральна частина)
        logArea = new JTextArea(15, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Лог обробки"),
            new EmptyBorder(5,5,5,5)
        ));
        frame.add(logScrollPane, BorderLayout.CENTER);

        // Кнопка запуску (нижня частина)
        processButton = new JButton("Почати обробку");
        processButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(new EmptyBorder(5,0,10,0));
        bottomPanel.add(processButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Додавання обробників подій
        addInputFileButton.addActionListener(this::addInputFileAction);
        removeInputFileButton.addActionListener(this::removeInputFileAction);
        
        selectLowPopButton.addActionListener(e -> selectOutputFileAction(outputLowPopField, "Виберіть файл для країн з малим населенням"));
        selectMidPopButton.addActionListener(e -> selectOutputFileAction(outputMidPopField, "Виберіть файл для країн з середнім населенням"));
        selectHighPopButton.addActionListener(e -> selectOutputFileAction(outputHighPopField, "Виберіть файл для країн з великим населенням"));

        processButton.addActionListener(this::processAction);

        frame.pack(); // Підганяє розмір вікна під вміст
        frame.setLocationRelativeTo(null); // Центрувати вікно на екрані
        frame.setVisible(true);
        displayMessage("Графічний інтерфейс запущено.\nНалаштуйте параметри та натисніть 'Почати обробку'.\n");
    }

    private void addInputFileAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Виберіть вхідні файли");
        fileChooser.setMultiSelectionEnabled(true);
        // Встановлюємо поточну директорію, щоб було зручніше шукати файли input
        File inputDir = new File("input");
        if (inputDir.exists() && inputDir.isDirectory()) {
            fileChooser.setCurrentDirectory(inputDir);
        } else {
            fileChooser.setCurrentDirectory(new File(".")); // Поточна директорія проекту
        }

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                inputFilesListModel.addElement(file.getAbsolutePath());
            }
            logArea.append("Додано вхідні файли до списку.\n");
        }
    }

    private void removeInputFileAction(ActionEvent e) {
        int[] selectedIndices = inputFilesList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            // Видаляємо елементи з кінця, щоб уникнути проблем з індексами
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                inputFilesListModel.removeElementAt(selectedIndices[i]);
            }
            logArea.append("Видалено вибрані файли зі списку.\n");
        } else {
            displayError("Будь ласка, виберіть файли для видалення зі списку.");
        }
    }
    
    private void selectOutputFileAction(JTextField targetField, String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        // Встановлюємо поточну директорію для вихідних файлів
        File outputDir = new File("output");
         if (outputDir.exists() && outputDir.isDirectory()) {
            fileChooser.setCurrentDirectory(outputDir);
        } else {
            fileChooser.setCurrentDirectory(new File("."));
        }
        // Встановлюємо ім'я файлу за замовчуванням з текстового поля
        File currentFile = new File(targetField.getText());
        fileChooser.setSelectedFile(currentFile);

        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            targetField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            logArea.append("Встановлено шлях для вихідного файлу: " + targetField.getText() + "\n");
        }
    }

    private void processAction(ActionEvent e) {
        SortType selectedSortType = selectSortType(); // Отримуємо з ComboBox
        OutputFilePaths outputPaths = getOutputFilenames(); // Отримуємо з текстових полів
        List<String> inputPaths = getInputFilenames(); // Отримуємо зі списку

        if (inputPaths.isEmpty()) {
            displayError("Не вибрано жодного вхідного файлу!");
            return;
        }
        // Перевірка, чи шляхи не порожні (OutputFilePaths конструктор вже це робить)
        try {
            // Для наочності, можна перевірити ще раз тут
            if (outputPaths.getLowPopFile().trim().isEmpty() || 
                outputPaths.getMidPopFile().trim().isEmpty() || 
                outputPaths.getHighPopFile().trim().isEmpty()) {
                displayError("Будь ласка, вкажіть усі шляхи до вихідних файлів.");
                return;
            }
        } catch (IllegalArgumentException iae) {
             displayError(iae.getMessage());
             return;
        }


        logArea.append("\nРозпочато обробку...\n");
        logArea.append("Тип сортування: " + selectedSortType.getDescription() + "\n");
        logArea.append("Вхідні файли:\n");
        inputPaths.forEach(path -> logArea.append("  - " + path + "\n"));
        logArea.append("Вихідні файли:\n");
        logArea.append("  - Мало населення: " + outputPaths.getLowPopFile() + "\n");
        logArea.append("  - Середнє населення: " + outputPaths.getMidPopFile() + "\n");
        logArea.append("  - Багато населення: " + outputPaths.getHighPopFile() + "\n");
        
        processButton.setEnabled(false); // Блокуємо кнопку на час обробки

        // Виконуємо обробку в окремому потоці (SwingWorker), щоб не блокувати GUI
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Тут відбувається основна робота
                countryService.processData(inputPaths, outputPaths, selectedSortType);
                return null; // Повертаємо null, оскільки результат не потрібен напряму
            }

            @Override
            protected void process(List<String> chunks) {
                // Цей метод викликається в EDT для оновлення GUI проміжними результатами (якщо потрібно)
                // Наразі не використовується, але можна було б передавати сюди повідомлення для логу
                for (String message : chunks) {
                    logArea.append(message + "\n");
                }
            }

            @Override
            protected void done() {
                // Цей метод викликається в EDT після завершення doInBackground
                try {
                    get(); // Перевіряємо, чи не було винятків під час doInBackground
                    publish("\nОбробку успішно завершено.\n"); // Використовуємо publish для логування
                } catch (InterruptedException ex) {
                    displayError("Обробку було перервано: " + ex.getMessage());
                    Thread.currentThread().interrupt(); // Відновлюємо статус переривання
                } catch (Exception ex) {
                    // Отримуємо оригінальний виняток, якщо він був обгорнутий ExecutionException
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    if (cause instanceof IOException) {
                        displayError("Сталася помилка вводу/виводу: " + cause.getMessage());
                    } else if (cause instanceof CountryDataException) {
                        displayError("Сталася помилка формату даних: " + cause.getMessage());
                    } else if (cause instanceof IllegalArgumentException) {
                        displayError("Невірні параметри: " + cause.getMessage());
                    }
                    else {
                        displayError("Сталася непередбачена помилка під час обробки: " + cause.getMessage());
                    }
                    // cause.printStackTrace(); // Для детального логу помилки в консоль розробника
                } finally {
                    processButton.setEnabled(true); // Розблоковуємо кнопку
                }
            }
             // Допоміжний метод для SwingWorker для публікації повідомлень в лог з будь-якого потоку
            protected void publishMessage(String message) {
                publish(message);
            }

        }.execute(); // Запускаємо SwingWorker
    }

    // Реалізація методів інтерфейсу UserInterface
    @Override
    public SortType selectSortType() {
        // У SwingUI тип сортування отримується з JComboBox в момент обробки
        return (SortType) sortTypeComboBox.getSelectedItem();
    }

    @Override
    public OutputFilePaths getOutputFilenames() {
        // Отримуємо шляхи з текстових полів
        return new OutputFilePaths(
                outputLowPopField.getText(),
                outputMidPopField.getText(),
                outputHighPopField.getText()
        );
    }

    @Override
    public List<String> getInputFilenames() {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < inputFilesListModel.size(); i++) {
            paths.add(inputFilesListModel.getElementAt(i));
        }
        return paths;
    }

    @Override
    public void displayMessage(String message) {
        // Переконуємося, що оновлення GUI відбувається в потоці обробки подій
        if (SwingUtilities.isEventDispatchThread()) {
            logArea.append(message); // Додаємо без \n, якщо повідомлення вже його містить
        } else {
            SwingUtilities.invokeLater(() -> logArea.append(message));
        }
    }

    @Override
    public void displayError(String errorMessage) {
        if (SwingUtilities.isEventDispatchThread()) {
            logArea.append("ПОМИЛКА: " + errorMessage + "\n");
            JOptionPane.showMessageDialog(frame, errorMessage, "Помилка", JOptionPane.ERROR_MESSAGE);
        } else {
            SwingUtilities.invokeLater(() -> {
                logArea.append("ПОМИЛКА: " + errorMessage + "\n");
                JOptionPane.showMessageDialog(frame, errorMessage, "Помилка", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}
