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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Ventana secundaria que muestra las recomendaciones generadas.
 *
 * <p>Su papel no se limita a listar canciones: tambien acompana cada resultado
 * con dos visores web que buscan la cancion activa en YouTube y Wikipedia.
 */
public class RecommendsView implements ModelListener {
    private final RecommendationModel model;
    private final Stage stage;
    private final Label labelMessage;
    private final Spinner<Integer> numberOfRecommendationsSpinner;
    private final ListView<String> recommendationsList;
    private final Button closeButton;
    private final WebView youtubeView;
    private final WebView wikiview;
    private String lastLoadedSong;

    /**
     * Construye la ventana de resultados y prepara sus listeners internos.
     */
    public RecommendsView(RecommendationModel model) {
        this.model = model;

        stage = new Stage();
        stage.setMaximized(true);

        Label labelRecommendations = new Label("Nr of recommendations:");
        labelMessage = new Label();

        numberOfRecommendationsSpinner = new Spinner<>(1, 20, 5);
        numberOfRecommendationsSpinner.setEditable(true);

        recommendationsList = new ListView<>();

        closeButton = new Button("Close");
        closeButton.setOnAction(event -> stage.close());

        youtubeView = new WebView();
        wikiview = new WebView();

        youtubeView.setPrefHeight(300);
        wikiview.setPrefHeight(300);

        Label youtubeLabel = new Label("YouTube");
        Label wikilabel = new Label("Wikipedia");

        VBox mediaPanel = new VBox(
                10,
                youtubeLabel,
                youtubeView,
                wikilabel,
                wikiview
        );

        VBox.setVgrow(youtubeView, Priority.ALWAYS);
        VBox.setVgrow(wikiview, Priority.ALWAYS);

        HBox recommendationsBox = new HBox(10, labelRecommendations, numberOfRecommendationsSpinner);
        VBox root = new VBox(10, labelMessage, recommendationsBox, recommendationsList, closeButton);
        HBox webViews = new HBox(10, root, mediaPanel);

        root.setPadding(new Insets(15));

        // La parte izquierda contiene control y resultados; la derecha, contexto web.
        HBox.setHgrow(root, Priority.SOMETIMES);
        HBox.setHgrow(mediaPanel, Priority.ALWAYS);
        VBox.setVgrow(recommendationsList, Priority.ALWAYS);

        stage.setTitle("Recommendations");
        stage.setScene(new Scene(webViews, 450, 400));

        youtubeView.getEngine().loadContent(
                "<html><body><h3>Selecciona una cancion para buscarla en YouTube.</h3></body></html>"
        );

        wikiview.getEngine().loadContent(
                "<html><body><h3>Selecciona una cancion para buscarla en Wikipedia.</h3></body></html>"
        );

        // Este listener ilustra muy bien el flujo de la ventana:
        // al cambiar la recomendacion seleccionada, se actualizan ambos paneles web.
        recommendationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSong, newSong) -> {
            if (newSong == null) {
                return;
            }

            if (newSong.equals(lastLoadedSong)) {
                return;
            }

            youtubeView.getEngine().load(buildYoutubeSearchUrl(newSong));
            wikiview.getEngine().load(buildWikiSearchUrl(newSong));
            lastLoadedSong = newSong;
        });
    }

    /**
     * Refresca la lista de recomendaciones cuando el modelo cambia.
     *
     * <p>Primero intenta conservar la seleccion previa para no desorientar al
     * usuario. Si esa cancion ya no esta en la lista, selecciona la primera.
     * Esa nueva seleccion activa de forma natural la carga de YouTube y Wikipedia.
     * Tambien es mas eficiente, pues si el usuario se dedica a ir reduciendo el
     * numero de canciones, si se elimina la cancion seleccionada y se selecciona
     * la anteriror a ella, en la siguiente reduccion del numero de canciones se
     * eliminara tambien, entrando en un proceso mas costoso que seleccionar
     * directamente la primera cancion
     */
    @Override
    public void modelUpdated() {
        String previousSelection = recommendationsList.getSelectionModel().getSelectedItem();

        recommendationsList.setItems(FXCollections.observableArrayList(model.getRecommendations()));

        if (previousSelection != null && recommendationsList.getItems().contains(previousSelection)) {
            recommendationsList.getSelectionModel().select(previousSelection);
        } else if (!recommendationsList.getItems().isEmpty()) {
            recommendationsList.getSelectionModel().selectFirst();
        }
    }

    /**
     * Muestra la ventana y la trae al frente.
     */
    public void show() {
        stage.show();
        stage.toFront();
    }

    /**
     * Expone el spinner para que el controlador pueda escuchar sus cambios.
     */
    public Spinner<Integer> getNumberOfRecommendationsSpinner() {
        return numberOfRecommendationsSpinner;
    }

    /**
     * Sincroniza el spinner de esta ventana con la cantidad elegida en la vista principal.
     */
    public void setNumberOfRecommendations(int value) {
        numberOfRecommendationsSpinner.getValueFactory().setValue(value);
    }

    /**
     * Construye la URL de busqueda de YouTube para la cancion dada.
     */
    private String buildYoutubeSearchUrl(String songTitle) {
        String query = URLEncoder.encode(songTitle, StandardCharsets.UTF_8);
        return "https://www.youtube.com/results?search_query=" + query;
    }

    /**
     * Construye la URL de busqueda de Wikipedia a partir de un titulo limpiado.
     *
     * <p>Esta limpieza no afecta al algoritmo. Solo mejora el flujo visual de la
     * demo para que las busquedas externas encuentren la cancion con mas facilidad.
     */
    private String buildWikiSearchUrl(String songTitle) {
        String cleanTitle = normalizeSongTitleForWikipedia(songTitle);

        if (cleanTitle.isBlank()) {
            cleanTitle = songTitle;
        }

        String query = URLEncoder.encode(cleanTitle, StandardCharsets.UTF_8);
        return "https://en.wikipedia.org/w/index.php?search=" + query;
    }

    /**
     * Reduce ruido habitual del dataset antes de consultar Wikipedia.
     *
     * <p>Intenta eliminar comillas, parentesis, sufijos tipo remix y separadores
     * decorativos para quedarse con una forma mas limpia del titulo.
     */
    private String normalizeSongTitleForWikipedia(String rawTitle) {
        if (rawTitle == null) {
            return "";
        }

        String title = rawTitle.trim();
        title = title.replace("\"", "");
        title = title.replaceAll("\\(.*?\\)", "").trim();

        if (title.contains(" - ")) {
            title = title.split(" - ")[0].trim();
        }

        title = title.replace("|", " ");
        title = title.replace("/", " ");
        title = title.replaceAll("\\s+", " ").trim();

        return title;
    }
}
