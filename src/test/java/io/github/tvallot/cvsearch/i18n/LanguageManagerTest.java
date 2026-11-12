package io.github.tvallot.cvsearch.i18n;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LanguageManager}.
 */
class LanguageManagerTest {
    private static final List<String> KEYS = List.of(
            "title", "language", "skill", "add_skill", "clear_skills", "strategy", "search",
            "new_window", "average", "score", "no_results",
            "strategy_all", "strategy_avg", "strategy_any", "strategy_exp");

    private final LanguageManager lang = LanguageManager.getInstance();
    private Locale systemLocale;

    @BeforeEach
    void setUp() {
        systemLocale = Locale.getDefault();
        lang.setLocale(LanguageManager.DEFAULT_LOCALE);
    }

    @AfterEach
    void tearDown() {
        Locale.setDefault(systemLocale);
        lang.setLocale(LanguageManager.DEFAULT_LOCALE);
    }

    @Test
    void defaultLanguageIsEnglish() {
        assertThat(LanguageManager.DEFAULT_LOCALE, is(Locale.ENGLISH));
        assertThat(lang.get("title"), is("CV Search"));
    }

    @Test
    void everyLanguageHasEveryKey() {
        for (Locale locale : LanguageManager.SUPPORTED_LOCALES) {
            lang.setLocale(locale);
            for (String key : KEYS) {
                assertFalse(lang.get(key).isBlank(), "Empty value for " + key + " in " + locale);
            }
        }
    }

    @Test
    void switchingLanguageChangesTexts() {
        String english = lang.get("search");
        lang.setLocale(Locale.FRENCH);

        assertThat(lang.get("search"), is(not(english)));
        assertThat(lang.get("skill"), is("Compétence"));
    }

    @Test
    void englishIsUsedEvenOnFrenchSystem() {
        Locale.setDefault(Locale.FRENCH);
        lang.setLocale(Locale.FRENCH);
        lang.setLocale(Locale.ENGLISH);

        assertThat(lang.get("title"), is("CV Search"));
    }

    @Test
    void listenersAreNotifiedOnlyWhenLanguageChanges() {
        AtomicInteger calls = new AtomicInteger();
        Runnable listener = calls::incrementAndGet;
        lang.addListener(listener);

        lang.setLocale(Locale.FRENCH);
        lang.setLocale(Locale.FRENCH);
        lang.removeListener(listener);
        lang.setLocale(Locale.ENGLISH);

        assertThat(calls.get(), is(1));
    }

    @Test
    void missingKeyThrows() {
        assertThrows(MissingResourceException.class, () -> lang.get("not_existing_key"));
    }

    @Test
    void isASingleton() {
        assertThat(LanguageManager.getInstance(), is(sameInstance(lang)));
    }
}
