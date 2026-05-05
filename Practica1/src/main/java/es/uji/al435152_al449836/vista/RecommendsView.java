package es.uji.al435152_al449836.vista;

import es.uji.al435152_al449836.modelo.recomendaciones.ModelListener;
import es.uji.al435152_al449836.modelo.recomendaciones.RecommendationModel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecommendsView implements ModelListener {
    private final RecommendationModel model;
    private final Stage stage;
    private final Label labelMessage;
    private final Spinner<Integer> numberOfRecommendationsSpinner;
    private final ListView<String> recommendationsList;
    private final Button closeButton;

    public RecommendsView(RecommendationModel model) {
        this.model = model;

        stage = new Stage();

        Label labelRecommendations = new Label("Nr of recommendations:");
        labelMessage = new Label();

        numberOfRecommendationsSpinner = new Spinner<>(1, 20, 5);
        numberOfRecommendationsSpinner.setEditable(true);

        recommendationsList = new ListView<>();

        closeButton = new Button("Close");
        closeButton.setOnAction(event -> stage.close());

        HBox recommendationsBox = new HBox(10, labelRecommendations, numberOfRecommendationsSpinner);
        VBox root = new VBox(10, labelMessage, recommendationsBox, recommendationsList, closeButton);
        root.setPadding(new Insets(15));

        stage.setTitle("Recommendations");
        stage.setScene(new Scene(root, 450, 400));
    }

    @Override
    public void modelUpdated() {
        labelMessage.setText("If you liked \"" + model.getSelectedSong() + "\" you might like:");
        Integer currentValue = numberOfRecommendationsSpinner.getValue();

        if (currentValue == null || currentValue.intValue() != model.getNumberOfRecommendations()) {
            numberOfRecommendationsSpinner.getValueFactory().setValue(model.getNumberOfRecommendations());
        }

        recommendationsList.setItems(FXCollections.observableArrayList(model.getRecommendations()));
    }

    public void show() {
        stage.show();
        stage.toFront();
    }

    public Spinner<Integer> getNumberOfRecommendationsSpinner() {
        return numberOfRecommendationsSpinner;
    }

}
