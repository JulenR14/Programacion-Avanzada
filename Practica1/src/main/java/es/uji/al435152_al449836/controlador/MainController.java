package es.uji.al435152_al449836.controlador;


import es.uji.al435152_al449836.vista.MainView;
import es.uji.al435152_al449836.vista.RecommendsView;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private final MainView mainView;
    private RecommendsView recommendsView;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        this.recommendsView = null;

        initialize();
    }

    private void initialize() {
        loadSongs();
        registerEvents();
        updateRecommendButtonState();
    }

    private void loadSongs() {
        // Aqui luego cargarias las canciones reales desde el modelo
        List<String> songs = new ArrayList<>();
        songs.add("Song 1");
        songs.add("Song 2");
        songs.add("Song 3");
        songs.add("Song 4");

        mainView.setSongs(songs);
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
        String recommendationType = mainView.getSelectedRecommendationType();
        String distanceType = mainView.getSelectedDistanceType();
        int numberOfRecommendations = mainView.getNumberOfRecommendations();

        // Aqui luego llamarias a RecSys usando:
        // - recommendationType
        // - distanceType
        // - selectedSong
        // - numberOfRecommendations
        List<String> recommendations = getRecommendations(
                selectedSong,
                recommendationType,
                distanceType,
                numberOfRecommendations
        );

        showRecommendations(selectedSong, recommendations);
    }

    private List<String> getRecommendations(String selectedSong,
                                            String recommendationType,
                                            String distanceType,
                                            int numberOfRecommendations) {
        // Metodo provisional hasta conectar el modelo real
        List<String> recommendations = new ArrayList<>();

        for (int i = 1; i <= numberOfRecommendations; i++) {
            recommendations.add("Recommended song " + i);
        }

        return recommendations;
    }

    private void showRecommendations(String selectedSong, List<String> recommendations) {
        recommendsView = new RecommendsView(selectedSong, recommendations);
        recommendsView.show();
    }
}
