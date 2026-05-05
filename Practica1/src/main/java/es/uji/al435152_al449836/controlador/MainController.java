package es.uji.al435152_al449836.controlador;

import es.uji.al435152_al449836.modelo.recomendaciones.RecommendationModel;
import es.uji.al435152_al449836.vista.MainView;
import es.uji.al435152_al449836.vista.RecommendsView;
import javafx.scene.control.Alert;

public class MainController {
    private final MainView mainView;
    private final RecommendationModel model;
    private RecommendsView recommendsView;

    public MainController(MainView mainView, RecommendationModel model) {
        this.mainView = mainView;
        this.model = model;
        this.recommendsView = null;

        initialize();
    }

    private void initialize() {
        registerEvents();
        updateRecommendButtonState();
    }

    private void registerEvents() {
        mainView.getSongsListView()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateRecommendButtonState());

        mainView.getRecommendButton().setOnAction(event -> handleRecommendAction());
    }

    private void updateRecommendButtonState() {
        boolean songSelected = mainView.getSelectedSong() != null;
        mainView.getRecommendButton().setDisable(!songSelected);
    }

    private void handleRecommendAction() {
        String selectedSong = mainView.getSelectedSong();

        if (selectedSong == null) {
            return;
        }

        ensureRecommendationsView();

        try {
            model.updateRecommendations(
                    mainView.getSelectedRecommendationType(),
                    mainView.getSelectedDistanceType(),
                    selectedSong,
                    mainView.getNumberOfRecommendations()
            );

            recommendsView.show();
        } catch (Exception e) {
            showError("Error generating recommendations", e.getMessage());
        }
    }

    private void ensureRecommendationsView() {
        if (recommendsView == null) {
            recommendsView = new RecommendsView(model);
            model.addListener(recommendsView);

            recommendsView.getNumberOfRecommendationsSpinner()
                    .valueProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        if (newValue == null || model.getSelectedSong() == null) {
                            return;
                        }

                        try {
                            model.updateRecommendations(
                                    model.getRecommendationType(),
                                    model.getDistanceType(),
                                    model.getSelectedSong(),
                                    newValue
                            );
                        } catch (Exception e) {
                            showError("Error updating recommendations", e.getMessage());
                        }
                    });
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
