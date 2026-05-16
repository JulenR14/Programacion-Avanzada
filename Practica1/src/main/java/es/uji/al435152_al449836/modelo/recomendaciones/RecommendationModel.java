package es.uji.al435152_al449836.modelo.recomendaciones;

import es.uji.al435152_al449836.modelo.algoritmos.Algorithm;
import es.uji.al435152_al449836.modelo.algoritmos.KMeans;
import es.uji.al435152_al449836.modelo.algoritmos.KNN;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.ManhattanDistance;
import es.uji.al435152_al449836.modelo.datos.Table;
import es.uji.al435152_al449836.modelo.lecturas.CSVLabeledFileReader;
import es.uji.al435152_al449836.modelo.lecturas.CSVUnlabeledFileReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Modelo principal de la aplicacion de recomendaciones.
 *
 * Centraliza el estado compartido entre las vistas y oculta los detalles de
 * carga de datos, construccion de algoritmos y calculo de resultados. Desde la
 * perspectiva de la interfaz, esta clase es la fuente de verdad del sistema.
 *
 * Tambien implementa un mecanismo simple de observacion: cuando cambian las
 * recomendaciones, notifica a todas las vistas registradas para que se refresquen.
 */
public class RecommendationModel {
    private static final String BASE_PATH = "recommender";
    private static final int DEFAULT_KMEANS_CLUSTERS = 15;
    private static final int DEFAULT_KMEANS_ITERATIONS = 200;
    private static final long DEFAULT_KMEANS_SEED = 4321L;

    private final List<ModelListener> listeners;
    private final List<String> songTitles;

    private String selectedSong;
    private String recommendationType;
    private String distanceType;
    private int numberOfRecommendations;
    private List<String> recommendations;
    private RecSys recSys;

    /**
     * Carga los nombres de canciones y fija el estado inicial del modelo.
     */
    public RecommendationModel() throws IOException, URISyntaxException {
        listeners = new ArrayList<>();
        songTitles = readNames(BASE_PATH + "/songs_test_names.csv");
        recommendations = new ArrayList<>();

        selectedSong = null;
        recommendationType = "knn";
        distanceType = "euclidean";
        numberOfRecommendations = 5;
        recSys = null;
    }

    /**
     * Registra un nuevo listener.
     */
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Elimina un listener registrado previamente.
     */
    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Informa a todas las vistas de que el modelo ha cambiado.
     */
    private void notifyListeners() {
        for (ModelListener listener : listeners) {
            listener.modelUpdated();
        }
    }

    /**
     * Metodo central del flujo de la interfaz.
     *
     * Se invoca cuando el usuario pulsa el boton de recomendar o cambia el
     * numero de resultados en la ventana secundaria. El proceso es:
     * - actualizar el estado publico del modelo;
     * - comprobar si hay que reconstruir el recomendador;
     * - si cambia algoritmo o distancia, recargar tablas y reentrenar;
     * - pedir la nueva lista de canciones;
     * - notificar a las vistas para que se actualicen.
     */
    public void updateRecommendations(String recommendationType,
                                      String distanceType,
                                      String selectedSong,
                                      int numberOfRecommendations) throws Exception {
        boolean needsReload = recSys == null
                || !recommendationType.equalsIgnoreCase(this.recommendationType)
                || !distanceType.equalsIgnoreCase(this.distanceType);

        this.recommendationType = recommendationType;
        this.distanceType = distanceType;
        this.selectedSong = selectedSong;
        this.numberOfRecommendations = numberOfRecommendations;

        if (needsReload) {
            Distance distance = createDistance(distanceType);
            Algorithm algorithm = createAlgorithm(recommendationType, distance);

            // Solo se recargan tablas y se reentrena cuando cambia algo que
            // afecta a la forma de estimar clases o clusters.
            Table trainTable = loadTrainTable(recommendationType);
            Table testTable = loadTestTable(recommendationType);

            recSys = new RecSys(algorithm);
            recSys.train(trainTable);
            recSys.initialise(testTable, songTitles);
        }

        // Si solo cambia la cancion elegida o la cantidad pedida, se reutiliza
        // el recomendador ya preparado y solo se consulta el mapa de resultados.
        recommendations = recSys.recommend(selectedSong, numberOfRecommendations);
        notifyListeners();
    }

    /**
     * Fabrica la estrategia de distancia solicitada por la vista.
     */
    private Distance createDistance(String distanceType) {
        if ("manhattan".equalsIgnoreCase(distanceType)) {
            return new ManhattanDistance();
        }
        return new EuclideanDistance();
    }

    /**
     * Fabrica el algoritmo correspondiente a la opcion elegida por el usuario.
     */
    private Algorithm createAlgorithm(String recommendationType, Distance distance) {
        if ("kmeans".equalsIgnoreCase(recommendationType)) {
            return new KMeans(
                    DEFAULT_KMEANS_CLUSTERS,
                    DEFAULT_KMEANS_ITERATIONS,
                    DEFAULT_KMEANS_SEED,
                    distance
            );
        }
        return new KNN(distance);
    }

    /**
     * Carga el conjunto de entrenamiento apropiado para el algoritmo activo.
     */
    private Table loadTrainTable(String recommendationType) throws IOException {
        if ("kmeans".equalsIgnoreCase(recommendationType)) {
            return new CSVUnlabeledFileReader(BASE_PATH + "/songs_train_withoutnames.csv")
                    .readTableFromSource();
        }
        return new CSVLabeledFileReader(BASE_PATH + "/songs_train.csv")
                .readTableFromSource();
    }

    /**
     * Carga el conjunto de test apropiado para el algoritmo activo.
     */
    private Table loadTestTable(String recommendationType) throws IOException {
        if ("kmeans".equalsIgnoreCase(recommendationType)) {
            return new CSVUnlabeledFileReader(BASE_PATH + "/songs_test_withoutnames.csv")
                    .readTableFromSource();
        }
        return new CSVLabeledFileReader(BASE_PATH + "/songs_test.csv")
                .readTableFromSource();
    }

    /**
     * Lee la lista de nombres de canciones mostrada en la interfaz.
     */
    private List<String> readNames(String fileOfItemNames) throws IOException, URISyntaxException {
        String path = getClass().getClassLoader().getResource(fileOfItemNames).toURI().getPath();

        List<String> names = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));

        while (scanner.hasNextLine()) {
            names.add(scanner.nextLine());
        }

        scanner.close();
        return names;
    }

    /**
     * Devuelve una copia defensiva de los titulos disponibles.
     */
    public List<String> getSongTitles() {
        return new ArrayList<>(songTitles);
    }

    /**
     * Devuelve una copia defensiva de las recomendaciones actuales.
     */
    public List<String> getRecommendations() {
        return new ArrayList<>(recommendations);
    }

    /**
     * Devuelve la cancion seleccionada como referencia.
     */
    public String getSelectedSong() {
        return selectedSong;
    }

    /**
     * Devuelve el algoritmo activo en el modelo.
     */
    public String getRecommendationType() {
        return recommendationType;
    }

    /**
     * Devuelve la distancia activa en el modelo.
     */
    public String getDistanceType() {
        return distanceType;
    }

    /**
     * Devuelve la cantidad de recomendaciones solicitadas.
     */
    public int getNumberOfRecommendations() {
        return numberOfRecommendations;
    }
}
