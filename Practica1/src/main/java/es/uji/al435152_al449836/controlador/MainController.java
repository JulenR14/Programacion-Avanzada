package es.uji.al435152_al449836.controlador;

import es.uji.al435152_al449836.modelo.recomendaciones.RecommendationModel;
import es.uji.al435152_al449836.vista.MainView;
import es.uji.al435152_al449836.vista.RecommendsView;
import javafx.scene.control.Alert;

/**
 * Controlador principal de la aplicacion.
 *
 * <p>Su responsabilidad es enlazar la ventana inicial con el modelo de
 * recomendaciones. No implementa algoritmos ni almacena resultados: escucha
 * eventos de la interfaz, recoge las opciones elegidas por el usuario y delega
 * el calculo al modelo.
 *
 * <p>El flujo principal es:
 * 1. El usuario selecciona una cancion en la vista principal.
 * 2. El controlador habilita el boton de recomendacion.
 * 3. Al pulsar el boton, se leen algoritmo, distancia y numero de resultados.
 * 4. El modelo recalcula las recomendaciones necesarias.
 * 5. La vista secundaria se abre o se refresca con los nuevos datos.
 */
public class MainController {
    private final MainView mainView;
    private final RecommendationModel model;
    private RecommendsView recommendsView;

    /**
     * Construye el controlador y deja registrados los eventos de la interfaz.
     */
    public MainController(MainView mainView, RecommendationModel model) {
        this.mainView = mainView;
        this.model = model;
        this.recommendsView = null;

        initialize();
    }

    /**
     * Centraliza la inicializacion del controlador.
     */
    private void initialize() {
        registerEvents();
        updateRecommendButtonState();
    }

    /**
     * Registra los eventos clave de la ventana principal.
     *
     * <p>El primer listener reacciona a cambios de seleccion en la lista de
     * canciones. El segundo captura el click sobre el boton de recomendar.
     */
    private void registerEvents() {
        mainView.getSongsListView()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateRecommendButtonState());

        mainView.getRecommendButton().setOnAction(event -> handleRecommendAction());
    }

    /**
     * Activa o desactiva el boton en funcion de si hay una cancion elegida.
     */
    private void updateRecommendButtonState() {
        boolean songSelected = mainView.getSelectedSong() != null;
        mainView.getRecommendButton().setDisable(!songSelected);
    }

    /**
     * Gestiona el click sobre el boton principal.
     *
     * <p>Aqui se cierra el ciclo entre interfaz y modelo: se recopilan los
     * parametros visibles en pantalla, se solicitan las recomendaciones y, si
     * todo va bien, se muestra la ventana con los resultados.
     */
    private void handleRecommendAction() {
        String selectedSong = mainView.getSelectedSong();
        int numberOfRecommendations = mainView.getNumberOfRecommendations();

        if (selectedSong == null) {
            return;
        }

        ensureRecommendationsView();

        try {
            model.updateRecommendations(
                    mainView.getSelectedRecommendationType(),
                    mainView.getSelectedDistanceType(),
                    selectedSong,
                    numberOfRecommendations
            );

            // Sincronizamos el spinner de la segunda ventana con lo elegido en la primera
            // para que ambos controles reflejen el mismo estado visible.
            recommendsView.setNumberOfRecommendations(numberOfRecommendations);
            recommendsView.show();
        } catch (Exception e) {
            showError("Error generating recommendations", e.getMessage());
        }
    }

    /**
     * Crea la ventana de resultados una sola vez y la registra como listener.
     *
     * <p>La vista secundaria se conserva en memoria para no reconstruir toda su
     * interfaz cada vez. Ademas escucha el spinner interno para recalcular la
     * cantidad de recomendaciones manteniendo el contexto actual.
     */
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

                        // Si el valor ya coincide con el estado del modelo, no hace falta
                        // recalcular nada. Esto evita una actualizacion duplicada cuando el
                        // controlador sincroniza programaticamente el spinner.
                        if (newValue.equals(model.getNumberOfRecommendations())) {
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

    /**
     * Muestra un cuadro de error modal cuando algo falla en el flujo.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
