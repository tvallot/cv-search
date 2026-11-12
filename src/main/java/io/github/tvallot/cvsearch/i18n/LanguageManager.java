package io.github.tvallot.cvsearch.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Holds the current interface language and gives access to translated labels.
 *
 * <p>There is a single instance for the whole application, so that every window shows
 * the same language. Windows register a listener to refresh their texts when the
 * language changes.
 */
public final class LanguageManager {

    /** Language used when the application starts. */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /** Languages the interface is translated into. */
    public static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, Locale.FRENCH);

    private static final String BUNDLE_NAME = "labels";

    // Without this, asking for English on a French system silently falls back to French.
    private static final ResourceBundle.Control NO_FALLBACK =
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    private static final LanguageManager INSTANCE = new LanguageManager();

    private final List<Runnable> listeners = new ArrayList<>();
    private Locale locale;
    private ResourceBundle bundle;

    private LanguageManager() {
        load(DEFAULT_LOCALE);
    }

    /** Returns the single instance. */
    public static LanguageManager getInstance() {
        return INSTANCE;
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * Changes the interface language and notifies the listeners.
     * Does nothing if the language is already the current one.
     */
    public void setLocale(final Locale newLocale) {
        if (newLocale.equals(locale)) {
            return;
        }
        load(newLocale);
        List.copyOf(listeners).forEach(Runnable::run);
    }

    /** Registers code to run every time the language changes. */
    public void addListener(final Runnable listener) {
        listeners.add(listener);
    }

    /** Removes a listener registered with {@link #addListener(Runnable)}. */
    public void removeListener(final Runnable listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the translation of a label in the current language.
     *
     * @throws java.util.MissingResourceException if the key does not exist
     */
    public String get(final String key) {
        return bundle.getString(key);
    }

    private void load(final Locale newLocale) {
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, newLocale, NO_FALLBACK);
        this.locale = newLocale;
    }
}
