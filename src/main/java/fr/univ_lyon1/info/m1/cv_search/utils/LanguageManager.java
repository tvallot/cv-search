package fr.univ_lyon1.info.m1.cv_search.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton managing application language using ResourceBundle.
 */
public final class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle resourceBundle;

    private LanguageManager() {
        setLocale(Locale.FRENCH); // Valeur par défaut
    }

    /**
     * Get the singleton instance.
     * @return the singleton LanguageManager
     */
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    /**
     * Set the current locale and reload resource bundle.
     * @param locale the new locale
     */
    public void setLocale(final Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("labels", locale);
    }

    /**
     * Get the localized string for a given key.
     * @param key the key to translate
     * @return the localized string
     */
    public String get(final String key) {
        return resourceBundle.getString(key);
    }
}
