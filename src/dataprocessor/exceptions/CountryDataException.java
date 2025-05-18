package dataprocessor.exceptions;

/**
 * Спеціалізований виняток для помилок, пов'язаних з обробкою даних країн
 * (наприклад, помилки парсингу файлу).
 */
public class CountryDataException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5165835938880409767L;

	public CountryDataException(String message) {
        super(message);
    }

    public CountryDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
