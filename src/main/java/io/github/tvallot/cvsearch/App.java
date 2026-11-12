package io.github.tvallot.cvsearch;

import io.github.tvallot.cvsearch.controller.ApplicantController;
import io.github.tvallot.cvsearch.view.JfxView;
import javafx.application.Application;
import javafx.stage.Stage;

// import controller

/**
 * Main class for the application (structure imposed by JavaFX).
 */
public class App extends Application {

    /**
     * With javafx, start() is called when the application is launched.
     */

    

    @Override
    public void start(final Stage stage) throws Exception {
        ApplicantController appController = new ApplicantController();
        new JfxView(stage, 600, 600, appController);

        new JfxView(new Stage(), 400, 400, appController);
    }


    /**
     * A main method in case the user launches the application using
     * App as the main class.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
