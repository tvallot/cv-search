package io.github.tvallot.cvsearch;

import java.io.File;
import java.util.List;

import io.github.tvallot.cvsearch.controller.ApplicantController;
import io.github.tvallot.cvsearch.model.ApplicantListBuilder;
import io.github.tvallot.cvsearch.view.JfxView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point. Loads the applicants and opens the first window.
 *
 * <p>The applicant directory can be given as first argument; it defaults to
 * {@code data/applicants}.
 */
public class App extends Application {

    private static final String DEFAULT_DATA_DIR = "data/applicants";

    @Override
    public void start(final Stage stage) {
        List<String> args = getParameters().getUnnamed();
        File dataDir = new File(args.isEmpty() ? DEFAULT_DATA_DIR : args.get(0));
        ApplicantController controller =
                new ApplicantController(new ApplicantListBuilder(dataDir).build());
        new JfxView(stage, controller);
    }

    /**
     * Launches the JavaFX application.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
