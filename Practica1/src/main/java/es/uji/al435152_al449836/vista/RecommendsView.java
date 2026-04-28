package es.uji.al435152_al449836.vista;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecommendsView extends Application {
    private VBox layaut;
    private HBox layautCantRecomendaciones;
    private Label labelRecomends;
    private Label labelMSG;
    private Spinner<Integer> numberOfRecommendationsSpinner;
    private ListView<String> listaRecomendados;
    private Button btnClose;

    @Override
    public void start(Stage stage) throws Exception {
        layaut = new VBox();
        labelRecomends = new Label("Número de recomendaciones");

        numberOfRecommendationsSpinner = new Spinner<>(1, 20, 5);
        numberOfRecommendationsSpinner.setEditable(true);

        layautCantRecomendaciones = new HBox();
        layautCantRecomendaciones.getChildren().addAll(labelRecomends,numberOfRecommendationsSpinner);
        labelMSG = new Label("Si te ha gustado xxx, Te gustará: xxx");

        listaRecomendados = new ListView<>();

        btnClose = new Button("Close");
        btnClose.setDisable(true);

        layaut = new VBox();
        layaut.getChildren().addAll(layautCantRecomendaciones,labelMSG,listaRecomendados,btnClose);
    }

}
