package es.uji.al435152_al449836.controlador;


import es.uji.al435152_al449836.vista.MainView;
import es.uji.al435152_al449836.vista.RecommendsView;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    // Vista principal de la aplicacion.
    // El controlador la usa para leer lo que el usuario elige
    // y para actualizar elementos de la interfaz cuando haga falta.
    private final MainView mainView;

    // Ventana donde se mostraran las recomendaciones.
    // Al arrancar todavia no existe, por eso se inicializa mas adelante.
    private RecommendsView recommendsView;

    public MainController(MainView mainView) {
        // Recibimos la vista desde fuera para que el controlador
        // trabaje con ella sin tener que crearla aqui dentro.
        this.mainView = mainView;

        // Al principio no hay ninguna ventana de resultados abierta.
        this.recommendsView = null;

        // Nada mas crear el controlador, dejamos preparada la interfaz.
        initialize();
    }

    private void initialize() {
        // Cargamos las canciones iniciales en la lista.
        loadSongs();

        // Registramos los eventos de la interfaz:
        // seleccion de canciones, clic en botones, etc.
        registerEvents();

        // Revisamos si el boton Recommend debe estar activo o no.
        updateRecommendButtonState();
    }

    private void loadSongs() {
        // De momento usamos canciones de prueba para tener la vista funcionando.
        // Mas adelante estas canciones se cargaran desde el modelo real.
        List<String> songs = new ArrayList<>();

        // Añadimos algunas canciones de ejemplo.
        songs.add("Song 1");
        songs.add("Song 2");
        songs.add("Song 3");
        songs.add("Song 4");

        // Enviamos la lista a la vista para que la muestre en pantalla.
        mainView.setSongs(songs);
    }

    private void registerEvents() {
        // Escuchamos cuando cambia la cancion seleccionada.
        // Cada vez que el usuario seleccione algo distinto,
        // volvemos a comprobar si el boton Recommend debe activarse.
        mainView.getSongsListView()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateRecommendButtonState());

        // Indicamos que hacer cuando el usuario pulse el boton Recommend.
        mainView.getRecommendButton().setOnAction(event -> handleRecommendAction());
    }

    private void updateRecommendButtonState() {
        // Comprobamos si ahora mismo hay una cancion seleccionada.
        boolean songSelected = mainView.getSelectedSong() != null;

        // Si no hay cancion seleccionada, desactivamos el boton
        // para evitar que el usuario provoque un error.
        mainView.getRecommendButton().setDisable(!songSelected);
    }

    private void handleRecommendAction() {
        // Pedimos a la vista la cancion elegida por el usuario.
        String selectedSong = mainView.getSelectedSong();

        // Leemos el tipo de recomendacion seleccionado: KNN o KMeans.
        String recommendationType = mainView.getSelectedRecommendationType();

        // Leemos la distancia que quiere usar el usuario.
        String distanceType = mainView.getSelectedDistanceType();

        // Leemos cuantas recomendaciones quiere recibir.
        int numberOfRecommendations = mainView.getNumberOfRecommendations();

        // Con esos datos, pedimos las recomendaciones.
        // Ahora mismo este metodo devuelve datos de prueba,
        // pero mas adelante llamara al modelo real.
        List<String> recommendations = getRecommendations(
                selectedSong,
                recommendationType,
                distanceType,
                numberOfRecommendations
        );

        // Mostramos las recomendaciones en la ventana de resultados.
        showRecommendations(selectedSong, recommendations);
    }

    private List<String> getRecommendations(String selectedSong,
                                            String recommendationType,
                                            String distanceType,
                                            int numberOfRecommendations) {
        // Metodo provisional hasta conectar el modelo real.
        // Aqui ira mas adelante la llamada a RecSys.
        List<String> recommendations = new ArrayList<>();

        // Generamos tantas recomendaciones falsas como haya pedido el usuario.
        // Esto sirve para comprobar que vista y controlador se comunican bien.
        for (int i = 1; i <= numberOfRecommendations; i++) {
            recommendations.add("Recommended song " + i);
        }

        // Devolvemos la lista para que luego se muestre en pantalla.
        return recommendations;
    }

    private void showRecommendations(String selectedSong, List<String> recommendations) {
        // Creamos la ventana de resultados y le pasamos la informacion
        // que necesita para enseñarla al usuario.
        recommendsView = new RecommendsView(selectedSong, recommendations);

        // Hacemos visible esa ventana.
        recommendsView.show();
    }
}
