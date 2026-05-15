package es.uji.al435152_al449836.vista;

import es.uji.al435152_al449836.controlador.MainController;
import es.uji.al435152_al449836.modelo.recomendaciones.ModelListener;
import es.uji.al435152_al449836.modelo.recomendaciones.RecommendationModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Ventana principal de la aplicacion JavaFX.
 *
 * <p>Esta vista concentra los controles que configuran una recomendacion:
 * algoritmo, tipo de distancia, cancion seleccionada y numero de resultados.
 * La logica de negocio no vive aqui: la vista solo construye widgets, expone
 * su estado y se deja coordinar por el controlador.
 */
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

    /**
     * Punto de entrada estandar de JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Construye la interfaz principal y la conecta con el modelo.
     *
     * <p>El orden de arranque es importante:
     * - primero se crean y colocan los controles;
     * - despues se muestra la ventana;
     * - por ultimo se instancia el modelo y el controlador.
     *
     * <p>Ese reparto deja claro que la vista solo prepara el escenario, mientras
     * que el controlador se encarga despues de enlazar comportamiento.
     */
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

            // El controlador es quien conecta los eventos de la interfaz con el modelo.
            new MainController(this, model);
            setSongs(model.getSongTitles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Responde a cambios del modelo refrescando la lista visible de canciones.
     *
     * <p>En esta practica la lista de canciones es estable, pero la vista cumple
     * igualmente el contrato de listener para mantener una arquitectura uniforme.
     */
    @Override
    public void modelUpdated() {
        setSongs(model.getSongTitles());
    }

    /**
     * Devuelve el nodo raiz de la escena.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Carga en pantalla la lista de canciones disponibles.
     */
    public void setSongs(List<String> songs) {
        songsListView.setItems(FXCollections.observableArrayList(songs));
    }

    /**
     * Devuelve la cancion que el usuario ha seleccionado como referencia.
     */
    public String getSelectedSong() {
        return songsListView.getSelectionModel().getSelectedItem();
    }

    /**
     * Devuelve cuantas recomendaciones se han solicitado en el spinner.
     */
    public int getNumberOfRecommendations() {
        return numberOfRecommendationsSpinner.getValue();
    }

    /**
     * Traduce la seleccion de radio buttons al identificador que espera el modelo.
     */
    public String getSelectedRecommendationType() {
        return knnRadioButton.isSelected() ? "knn" : "kmeans";
    }

    /**
     * Traduce la distancia seleccionada al identificador que usa el modelo.
     */
    public String getSelectedDistanceType() {
        return euclideanRadioButton.isSelected() ? "euclidean" : "manhattan";
    }

    /**
     * Expone el boton para que el controlador pueda asociarle acciones.
     */
    public Button getRecommendButton() {
        return recommendButton;
    }

    /**
     * Expone la lista de canciones para registrar listeners de seleccion.
     */
    public ListView<String> getSongsListView() {
        return songsListView;
    }
}
