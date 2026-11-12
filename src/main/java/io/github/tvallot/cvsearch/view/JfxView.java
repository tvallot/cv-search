package io.github.tvallot.cvsearch.view;

import java.util.ArrayList;
import java.util.List;

import io.github.tvallot.cvsearch.controller.ApplicantController;
import io.github.tvallot.cvsearch.model.StrategyDescriptor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import io.github.tvallot.cvsearch.utils.LanguageManager;
import java.util.Locale;

/**
 * Main view of the application, implemented using JavaFX.
 */
public class JfxView {
    private final HBox searchSkillsBox = new HBox();
    private final VBox resultBox = new VBox();
    private int selectedStrategyIndex = 1;
    private final ApplicantController appController;

    private Label labelSkill;
    private Label labelStrategy;
    private Label labelLanguage;

    private Button submitButton;
    private Button clearButton;
    private Button search;

    private ComboBox<StrategyDescriptor> comboBox;
    private Stage stage;
    /**
     * Constructor initializing the JavaFX view.
     * @param stage The primary stage for the application.
     * @param width The width of the application window.
     * @param height The height of the application window.
     */
    public JfxView(final Stage stage, final int width, final int height,
                   final ApplicantController appController) {
        this.appController = appController;
        this.stage = stage;
        stage.setTitle(LanguageManager.getInstance().get("title"));
        VBox root = new VBox(
                createNewSkillWidget(),
                createLanguageSelector(),
                searchSkillsBox,
                createStrategyWidget(),
                createSearchWidget(),
                resultBox
        );
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create a widget for adding new skills.
     * @return A Node representing the new skill widget.
     */
    private Node createNewSkillWidget() {
        HBox newSkillBox = new HBox(10);
        labelSkill = new Label(LanguageManager.getInstance().get("skill"));
        TextField textField = new TextField();
        submitButton = new Button(LanguageManager.getInstance().get("add_skill"));
        clearButton = new Button(LanguageManager.getInstance().get("clear_skills"));

        submitButton.setOnAction(e -> addSkill(textField));
        textField.setOnAction(e -> addSkill(textField));
        clearButton.setOnAction(e -> searchSkillsBox.getChildren().clear());

        newSkillBox.getChildren().addAll(labelSkill, textField, submitButton, clearButton);
        return newSkillBox;
    }

    /**
     * Add a skill button to the search skills box.
     * @param textField The text field containing the skill to add.
     */
    private void addSkill(final TextField textField) {
        String text = textField.getText().strip();
        if (text.isEmpty()) {
            return;
        }
        HBox box = createSkillHBox(text);
        searchSkillsBox.getChildren().add(box);
        textField.clear();
        textField.requestFocus();
    }

    /**
     * Create an HBox for a skill with a label and a remove button.
     * @param skill The skill to represent.
     * @return An HBox containing the skill label and remove button.
     */
    private HBox createSkillHBox(final String skill) {
        final HBox box = new HBox();
        final Label labelContact = new Label(skill + " ");
        final Button b = new Button("x");
        b.setOnMouseClicked(event -> searchSkillsBox.getChildren().remove(box));
        box.setStyle("-fx-padding: 2;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.getChildren().addAll(labelContact, b);
        return box;
    }

    /**
     * Extracts the list of skills from the searchSkillsBox.
     * @return List of skill names.
     */
    private List<String> getSkillsFromBox() {
        List<String> skills = new ArrayList<>();
        for (Node node : searchSkillsBox.getChildren()) {
            if (node instanceof HBox box) {
                Label label = (Label) box.getChildren().get(0);
                skills.add(label.getText().strip());
            }
        }
        return skills;
    }


    /**
     * Create the search button widget.
     * @return A Node representing the search button widget.
     */
    private Node createSearchWidget() {
        search = new Button(LanguageManager.getInstance().get("search"));
        search.setOnAction(e -> {
            resultBox.getChildren().clear();
            List<String> skills = getSkillsFromBox();
            List<String> results = appController.applyStrategy(selectedStrategyIndex, skills);
            for (String result : results) {
                resultBox.getChildren().add(new Label(result));
            }
        });
        return search;
    }

    /**
     * Create the strategy selection widget.
     * @return A Node representing the strategy selection widget.
     */
    private Node createStrategyWidget() {
        HBox newStratBox = new HBox();
        labelStrategy = new Label(LanguageManager.getInstance().get("strategy"));
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll(appController.getStrategies());
        comboBox.getSelectionModel().selectFirst();

        selectedStrategyIndex = comboBox.getSelectionModel().getSelectedIndex();

        comboBox.setOnAction(e -> {
            selectedStrategyIndex = comboBox.getSelectionModel().getSelectedIndex();
        });

        newStratBox.getChildren().addAll(labelStrategy, comboBox);
        return newStratBox;
    }

    /**
     * Crée un sélecteur de langue avec une ComboBox permettant
     * de changer dynamiquement la langue de l'application.
     *
     * @return Un Node JavaFX contenant le sélecteur de langue.
     */
    private Node createLanguageSelector() {
        HBox languageBox = new HBox(10);
        Label labelLanguage = new Label("Language:");

        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("fr", "en");
        languageComboBox.setValue("fr"); // language by default

        languageComboBox.setOnAction(e -> {
            String selectedLang = languageComboBox.getValue();
            LanguageManager.getInstance().setLocale(new Locale(selectedLang));
            updateTexts();
        });

        languageBox.getChildren().addAll(labelLanguage, languageComboBox);
        return languageBox;
    }

    /**
     * Met à jour dynamiquement tous les textes de l'interface
     * en fonction de la langue sélectionnée (labels, boutons, titre, etc.).
     */
    public void updateTexts() {
        LanguageManager lang = LanguageManager.getInstance();

        // update labels
        labelSkill.setText(lang.get("skill"));
        labelStrategy.setText(lang.get("strategy"));

        // update buttons
        submitButton.setText(lang.get("add_skill"));
        clearButton.setText(lang.get("clear_skills"));
        search.setText(lang.get("search"));

        // update title
        stage.setTitle(lang.get("title"));

        // update strategy ComboBox
        comboBox.getItems().clear();
        comboBox.getItems().addAll(appController.getStrategies());

        comboBox.getSelectionModel().selectFirst();
        selectedStrategyIndex = comboBox.getSelectionModel().getSelectedIndex();
    }
}
