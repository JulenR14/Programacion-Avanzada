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

        // Creamos las dos vistas web.
        youtubeView = new WebView();
        wikiview = new WebView();

        // Les damos una altura razonable para que no queden demasiado pequeñas.
        youtubeView.setPrefHeight(300);
        wikiview.setPrefHeight(300);

        // Títulos visuales para que el usuario entienda qué está viendo.
        Label youtubeLabel = new Label("YouTube");
        Label wikilabel = new Label("Wikipedia");

        // Metemos cada visor debajo de su título.
        // Así la parte derecha de la ventana queda más ordenada y clara.
        VBox mediaPanel = new VBox(
                10,
                youtubeLabel,
                youtubeView,
                wikilabel,
                wikiview
        );

        // Esto ayuda a que los WebView crezcan cuando la ventana se hace grande.
        // Si no lo ponéis, a veces el reparto visual queda un poco pobre.
        VBox.setVgrow(youtubeView, Priority.ALWAYS);
        VBox.setVgrow(wikiview, Priority.ALWAYS);



        HBox recommendationsBox = new HBox(10, labelRecommendations, numberOfRecommendationsSpinner);
        VBox root = new VBox(10, labelMessage, recommendationsBox, recommendationsList, closeButton);
        HBox webViews = new HBox(10,root,mediaPanel);

        root.setPadding(new Insets(15));

        // Dejamos que la lista y el panel web respiren bien cuando se agranda la ventana.
        HBox.setHgrow(root, Priority.SOMETIMES);
        HBox.setHgrow(mediaPanel, Priority.ALWAYS);
        VBox.setVgrow(recommendationsList, Priority.ALWAYS);

        stage.setTitle("Recommendations");
        stage.setScene(new Scene(webViews, 450, 400));

        // Cargamos un mensaje inicial sencillo para que las ventanas
        // no aparezcan completamente vacías la primera vez.
        youtubeView.getEngine().loadContent(
                "<html><body><h3>Selecciona una canción para buscarla en YouTube.</h3></body></html>"
        );

        wikiview.getEngine().loadContent(
                "<html><body><h3>Selecciona una canción para buscarla en Wikipedia.</h3></body></html>"
        );

        // Este listener está bien en el constructor, porque la lista es la misma
        // durante toda la vida de la ventana. Lo importante es no recargar si la
    // canción realmente no ha cambiado para el usuario.
        recommendationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSong, newSong) -> {
            if (newSong == null) {
                return;
            }

            // Si ya tenemos cargada exactamente esta canción,
            // no hacemos nada. Así evitamos refrescos innecesarios.
            if (newSong.equals(lastLoadedSong)) {
                return;
            }

            youtubeView.getEngine().load(buildYoutubeSearchUrl(newSong));
            wikiview.getEngine().load(buildWikiSearchUrl(newSong));

            // Actualizamos el recuerdo de la última canción realmente mostrada.
            lastLoadedSong = newSong;
        });

    }

    @Override
    public void modelUpdated() {
        // Antes de tocar la lista, recordamos qué canción estaba seleccionada.
// Esto es importante porque al refrescar las recomendaciones,
// la ListView puede perder temporalmente la selección.
        String previousSelection = recommendationsList.getSelectionModel().getSelectedItem();

        recommendationsList.setItems(FXCollections.observableArrayList(model.getRecommendations()));

// Si la canción que estaba seleccionada sigue estando en la nueva lista,
// la restauramos. Así el usuario mantiene contexto.
        if (previousSelection != null && recommendationsList.getItems().contains(previousSelection)) {
            recommendationsList.getSelectionModel().select(previousSelection);
        }
// Solo seleccionamos la primera si la canción anterior ya no está.
// Esto evita saltos raros y recargas que no aportan nada.
        else if (!recommendationsList.getItems().isEmpty()) {
            recommendationsList.getSelectionModel().selectFirst();
        }

    }

    public void show() {
        stage.show();
        stage.toFront();
    }

    public Spinner<Integer> getNumberOfRecommendationsSpinner() {
        return numberOfRecommendationsSpinner;
    }
    private String buildYoutubeSearchUrl(String songTitle) {
        String query = URLEncoder.encode(songTitle, StandardCharsets.UTF_8);
        return "https://www.youtube.com/results?search_query=" + query;
    }

    // Aquí construimos la búsqueda final para Wikipedia.
// En vez de usar el título bruto, usamos una versión limpiada.
// Añadimos "song" porque Wikipedia suele encontrar mejor canciones así.
    private String buildWikiSearchUrl(String songTitle) {
        String cleanTitle = normalizeSongTitleForWikipedia(songTitle);

        // Si el texto se queda vacío por una limpieza agresiva,
        // usamos el original como plan B para no romper la búsqueda.
        if (cleanTitle.isBlank()) {
            cleanTitle = songTitle;
        }

        // Añadimos "song" como pista para que Wikipedia busque la canción
        // y no otro concepto con el mismo nombre.
        String query = URLEncoder.encode(cleanTitle, StandardCharsets.UTF_8);

        return "https://en.wikipedia.org/w/index.php?search=" + query;
    }

    // Este método intenta convertir el texto del dataset en algo más razonable
// para buscar en Wikipedia. No hace magia, pero sí quita bastante ruido.
    private String normalizeSongTitleForWikipedia(String rawTitle) {
        if (rawTitle == null) {
            return "";
        }

        String title = rawTitle.trim();

        // Quitamos comillas dobles al principio y final.
        title = title.replace("\"", "");

        // Quitamos el contenido entre paréntesis, que muchas veces es:
        // feat., remix, version, etc., y suele estorbar más que ayudar.
        title = title.replaceAll("\\(.*?\\)", "").trim();

        // Si hay algo tipo "Canción - Artista" o "Tema - Remix",
        // nos quedamos con la parte de la izquierda, que suele ser el título.
        if (title.contains(" - ")) {
            title = title.split(" - ")[0].trim();
        }

        // Si aparecen barras verticales decorativas, las convertimos en espacios.
        title = title.replace("|", " ");

        // Si hay muchas barras normales, también estorban bastante para Wikipedia.
        title = title.replace("/", " ");

        // Si hay dobles espacios después de limpiar, los compactamos.
        title = title.replaceAll("\\s+", " ").trim();

        return title;
    }


}
