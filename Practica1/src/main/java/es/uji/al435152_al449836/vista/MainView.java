package es.uji.al435152_al449836.vista;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainView extends Application {
    private  VBox root;

    private  Label titleLabel;

    private  Label recommendationTypeLabel;
    private  RadioButton knnRadioButton;
    private  RadioButton kMeansRadioButton;
    private  ToggleGroup recommendationTypeGroup;

    private  Label distanceTypeLabel;
    private  RadioButton euclideanRadioButton;
    private  RadioButton manhattanRadioButton;
    private  ToggleGroup distanceTypeGroup;

    private  Label songsLabel;
    private  ListView<String> songsListView;

    private  Label numberOfRecommendationsLabel;
    private  Spinner<Integer> numberOfRecommendationsSpinner;

    private  Button recommendButton;

    public static void main(String[] args){
        launch(args);

    }
    @Override
    public void start(Stage primaryStage) {
        root = new VBox(10);
        root.setPadding(new     Insets(15));

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
        );                        // Muestra la ventana
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Opciones");
        primaryStage.show();
    }

    public MainView() {

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
        RadioButton selected = (RadioButton) recommendationTypeGroup.getSelectedToggle();
        return selected.getText();
    }

    public String getSelectedDistanceType() {
        RadioButton selected = (RadioButton) distanceTypeGroup.getSelectedToggle();
        return selected.getText();
    }

    public Button getRecommendButton() {
        return recommendButton;
    }

    public ListView<String> getSongsListView() {
        return songsListView;
    }





}
