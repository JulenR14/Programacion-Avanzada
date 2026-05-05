package es.uji.al435152_al449836.vista;

import es.uji.al435152_al449836.controlador.MainController;
import es.uji.al435152_al449836.modelo.recomendaciones.ModelListener;
import es.uji.al435152_al449836.modelo.recomendaciones.RecommendationModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainView extends Application implements ModelListener {
    private VBox root;

    private Label titleLabel;

    private Label recommendationTypeLabel;
    private RadioButton knnRadioButton;
    private RadioButton kMeansRadioButton;
    private ToggleGroup recommendationTypeGroup;

    private Label distanceTypeLabel;
    private RadioButton euclideanRadioButton;
    private RadioButton manhattanRadioButton;
    private ToggleGroup distanceTypeGroup;

    private Label songsLabel;
    private ListView<String> songsListView;

    private Label numberOfRecommendationsLabel;
    private Spinner<Integer> numberOfRecommendationsSpinner;

    private Button recommendButton;

    private RecommendationModel model;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        titleLabel = new Label("Music Recommender");

        recommendationTypeLabel = new Label("Recommendation Type");
        knnRadioButton = new RadioButton("Recommendation based on song features (KNN)");
        kMeansRadioButton = new RadioButton("Recommendation based on guessed genre (KMeans)");
        recommendationTypeGroup = new ToggleGroup();
        knnRadioButton.setToggleGroup(recommendationTypeGroup);
        kMeansRadioButton.setToggleGroup(recommendationTypeGroup);
        knnRadioButton.setSelected(true);

        distanceTypeLabel = new Label("Distance Type");
        euclideanRadioButton = new RadioButton("Euclidean");
        manhattanRadioButton = new RadioButton("Manhattan");
        distanceTypeGroup = new ToggleGroup();
        euclideanRadioButton.setToggleGroup(distanceTypeGroup);
        manhattanRadioButton.setToggleGroup(distanceTypeGroup);
        euclideanRadioButton.setSelected(true);

        songsLabel = new Label("Song Titles");
        songsListView = new ListView<>();

        numberOfRecommendationsLabel = new Label("Number of recommendations");
        numberOfRecommendationsSpinner = new Spinner<>(1, 20, 5);
        numberOfRecommendationsSpinner.setEditable(true);

        recommendButton = new Button("Recommend");
        recommendButton.setDisable(true);

        root.getChildren().addAll(
                titleLabel,
                recommendationTypeLabel,
                knnRadioButton,
                kMeansRadioButton,
                distanceTypeLabel,
                euclideanRadioButton,
                manhattanRadioButton,
                songsLabel,
                songsListView,
                numberOfRecommendationsLabel,
                numberOfRecommendationsSpinner,
                recommendButton
        );

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Music Recommender");
        primaryStage.show();

        try {
            model = new RecommendationModel();
            model.addListener(this);
            new MainController(this, model);
            setSongs(model.getSongTitles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modelUpdated() {
        setSongs(model.getSongTitles());
    }

    public Parent getRoot() {
        return root;
    }

    public void setSongs(List<String> songs) {
        songsListView.setItems(FXCollections.observableArrayList(songs));
    }

    public String getSelectedSong() {
        return songsListView.getSelectionModel().getSelectedItem();
    }

    public int getNumberOfRecommendations() {
        return numberOfRecommendationsSpinner.getValue();
    }

    public String getSelectedRecommendationType() {
        return knnRadioButton.isSelected() ? "knn" : "kmeans";
    }

    public String getSelectedDistanceType() {
        return euclideanRadioButton.isSelected() ? "euclidean" : "manhattan";
    }

    public Button getRecommendButton() {
        return recommendButton;
    }

    public ListView<String> getSongsListView() {
        return songsListView;
    }
}
