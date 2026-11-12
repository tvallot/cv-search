package io.github.tvallot.cvsearch.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for LanguageManager.
 */
public class LanguageManagerTest {
    private LanguageManager lang;

    /** Setup before each test. */
    @BeforeEach
    public void setup() {
        lang = LanguageManager.getInstance();
    }

    /** Test getting French values. */
    @Test
    public void testMainKeysExistInFrench() {
        lang.setLocale(Locale.FRENCH);

        List<String> keys = List.of(
                "title", "search", "add_skill", "clear_skills",
                "skill", "strategy", "average",
                "strategy_all", "strategy_avg", "strategy_any", "strategy_exp"
        );

        for (String key : keys) {
            String value = lang.get(key);
            assertNotNull(value, "Clé manquante : " + key);
            assertFalse(value.isEmpty(), "Valeur vide pour la clé : " + key);
        }
    }

    /** Test getting English values. */
    @Test
    public void testMainKeysExistInEnglish() {
        lang.setLocale(Locale.ENGLISH);

        List<String> keys = List.of(
                "title", "search", "add_skill", "clear_skills",
                "skill", "strategy", "average",
                "strategy_all", "strategy_avg", "strategy_any", "strategy_exp"
        );

        for (String key : keys) {
            String value = lang.get(key);
            assertNotNull(value, "Missing key: " + key);
            assertFalse(value.isEmpty(), "Empty value for key: " + key);
        }
    }

    /** Test that switching languages changes the text. */
    @Test
    void testLanguageSwitchChangesText() {
        LanguageManager lm = LanguageManager.getInstance();

        lm.setLocale(Locale.FRENCH);
        String frenchTitle = lm.get("title");

        lm.setLocale(Locale.ENGLISH);
        String englishTitle = lm.get("title");

        assertFalse(frenchTitle.equals(englishTitle), "Titles should differ between languages");
    }

    /** Test default language is French. */
    @Test
    public void testDefaultLanguageIsFrench() {
        assertThat(lang.get("title"), is("Recherche de CV"));
    }

    /** Test changing language to English. */
    @Test
    public void testChangeLanguageToEnglish() {
        lang.setLocale(Locale.ENGLISH);
        assertThat(lang.get("title"), is("Search for CV"));
    }

    /** Test changing back to French. */
    @Test
    public void testChangeBackToFrench() {
        lang.setLocale(Locale.FRENCH);
        assertThat(lang.get("title"), is("Recherche de CV"));
    }

    /** Test that requesting a missing key throws an exception. */
    @Test
    public void testMissingKeyThrowsException() {
        assertThrows(Exception.class, () -> lang.get("not_existing_key"));
    }

    /**
     * Test that LanguageManager is a singleton.
     */
    @Test
    public void testSingletonIdentity() {
        LanguageManager anotherInstance = LanguageManager.getInstance();
        assertThat(lang == anotherInstance, is(true)); // should be the same instance
    }
}
