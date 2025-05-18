package dataprocessor;

/**
 * Простий клас для зберігання шляхів до вихідних файлів.
 * Використовується для передачі інформації від UI до CountryService.
 */
public class OutputFilePaths {
    private final String lowPopFile;
    private final String midPopFile;
    private final String highPopFile;

    public OutputFilePaths(String lowPopFile, String midPopFile, String highPopFile) {
        if (lowPopFile == null || lowPopFile.trim().isEmpty() ||
            midPopFile == null || midPopFile.trim().isEmpty() ||
            highPopFile == null || highPopFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Шляхи до вихідних файлів не можуть бути порожніми.");
        }
        this.lowPopFile = lowPopFile;
        this.midPopFile = midPopFile;
        this.highPopFile = highPopFile;
    }

    public String getLowPopFile() {
        return lowPopFile;
    }

    public String getMidPopFile() {
        return midPopFile;
    }

    public String getHighPopFile() {
        return highPopFile;
    }
}
