package io.github.tvallot.cvsearch.view;

import java.util.List;
import java.util.Locale;

import io.github.tvallot.cvsearch.controller.SearchController;
import io.github.tvallot.cvsearch.i18n.LanguageManager;
import io.github.tvallot.cvsearch.model.SearchResult;
import io.github.tvallot.cvsearch.model.strategy.StrategyOption;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * A search window, written with JavaFX.
 *
 * <p>The view holds no business logic: it collects the searched skills and the chosen
 * strategy, asks the controller for the results and displays them. Several windows can
 * be open at the same time on the same controller, and they all follow the current
 * language.
 */
public class JfxView {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final double SPACING = 10;
    private static final String CHIP_STYLE = "-fx-padding: 2 2 2 8;"
            + "-fx-border-color: #888; -fx-border-radius: 12;"
            + "-fx-background-color: #eef; -fx-background-radius: 12;";

    private final Stage stage;
    private final SearchController controller;
    private final LanguageManager lang = LanguageManager.getInstance();
    private final Runnable languageListener = this::refreshTexts;

    private final ObservableList<String> skills = FXCollections.observableArrayList();
    private final FlowPane skillsPane = new FlowPane(6, 6);
    private final TextField skillField = new TextField();
    private final ComboBox<Locale> languageBox = new ComboBox<>();
    private final ComboBox<StrategyOption> strategyBox = new ComboBox<>();
    private final ListView<SearchResult> resultList = new ListView<>();
    private boolean searchDone = false;

    private final Label languageLabel = new Label();
    private final Label skillLabel = new Label();
    private final Label strategyLabel = new Label();
    private final Label placeholder = new Label();
    private final Button addButton = new Button();
    private final Button clearButton = new Button();
    private final Button searchButton = new Button();
    private final Button newWindowButton = new Button();

    /**
     * Builds the window and shows it.
     *
     * @param stage      the JavaFX window to fill
     * @param controller the controller running the searches
     */
    public JfxView(final Stage stage, final SearchController controller) {
        this.stage = stage;
        this.controller = controller;

        VBox root = new VBox(SPACING,
                createLanguageRow(),
                createSkillRow(),
                skillsPane,
                createStrategyRow(),
                createResultList());
        root.setPadding(new Insets(12));
        VBox.setVgrow(resultList, Priority.ALWAYS);

        refreshTexts();
        lang.addListener(languageListener);
        stage.setOnHidden(e -> lang.removeListener(languageListener));

        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.show();
    }

    private Node createLanguageRow() {
        languageBox.getItems().setAll(LanguageManager.SUPPORTED_LOCALES);
        languageBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(final Locale locale) {
                if (locale == null) {
                    return "";
                }
                String name = locale.getDisplayLanguage(locale);
                return name.substring(0, 1).toUpperCase(locale) + name.substring(1);
            }

            @Override
            public Locale fromString(final String text) {
                return null;
            }
        });
        languageBox.setOnAction(e -> {
            if (languageBox.getValue() != null) {
                lang.setLocale(languageBox.getValue());
            }
        });

        newWindowButton.setOnAction(e -> new JfxView(new Stage(), controller));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return row(languageLabel, languageBox, spacer, newWindowButton);
    }

    private Node createSkillRow() {
        skillField.setOnAction(e -> addSkill());
        addButton.setOnAction(e -> addSkill());
        clearButton.setOnAction(e -> skills.clear());
        skills.addListener((ListChangeListener<String>) change -> redrawSkills());

        HBox.setHgrow(skillField, Priority.ALWAYS);
        return row(skillLabel, skillField, addButton, clearButton);
    }

    private Node createStrategyRow() {
        strategyBox.getItems().setAll(controller.getStrategies());
        strategyBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(final StrategyOption option) {
                return option == null ? "" : lang.get(option.labelKey());
            }

            @Override
            public StrategyOption fromString(final String text) {
                return null;
            }
        });
        strategyBox.getSelectionModel().selectFirst();
        searchButton.setOnAction(e -> runSearch());
        return row(strategyLabel, strategyBox, searchButton);
    }

    private Node createResultList() {
        resultList.setPlaceholder(placeholder);
        resultList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(final SearchResult item, final boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format(item));
            }
        });
        return resultList;
    }

    private static HBox row(final Node... children) {
        HBox box = new HBox(SPACING, children);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void addSkill() {
        String skill = skillField.getText().strip().toLowerCase(Locale.ROOT);
        if (!skill.isEmpty() && !skills.contains(skill)) {
            skills.add(skill);
        }
        skillField.clear();
        skillField.requestFocus();
    }

    private void redrawSkills() {
        skillsPane.getChildren().setAll(skills.stream().map(this::createSkillChip).toList());
    }

    private Node createSkillChip(final String skill) {
        Button remove = new Button("×");
        remove.setStyle("-fx-background-color: transparent; -fx-padding: 0 6 0 6;");
        remove.setOnAction(e -> skills.remove(skill));
        HBox chip = new HBox(2, new Label(skill), remove);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setStyle(CHIP_STYLE);
        return chip;
    }

    private void runSearch() {
        List<SearchResult> results = controller.search(strategyBox.getValue(), List.copyOf(skills));
        resultList.getItems().setAll(results);
        searchDone = true;
        refreshPlaceholder();
    }

    private String format(final SearchResult result) {
        return String.format(lang.getLocale(), "%s   —   %s %.1f   ·   %s %.1f",
                result.applicantName(),
                lang.get("average"), result.average(),
                lang.get("score"), result.score());
    }

    private void refreshPlaceholder() {
        placeholder.setText(searchDone ? lang.get("no_results") : "");
    }

    /** Updates every text of the window to the current language. */
    private void refreshTexts() {
        stage.setTitle(lang.get("title"));
        languageLabel.setText(lang.get("language"));
        skillLabel.setText(lang.get("skill"));
        strategyLabel.setText(lang.get("strategy"));
        addButton.setText(lang.get("add_skill"));
        clearButton.setText(lang.get("clear_skills"));
        searchButton.setText(lang.get("search"));
        newWindowButton.setText(lang.get("new_window"));
        languageBox.setValue(lang.getLocale());

        // Re-create the strategy cells so that their labels are translated again.
        StrategyOption selected = strategyBox.getValue();
        strategyBox.getItems().setAll(controller.getStrategies());
        strategyBox.setValue(null);
        strategyBox.setValue(selected);

        resultList.refresh();
        refreshPlaceholder();
    }
}
