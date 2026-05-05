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

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ModelListener listener : listeners) {
            listener.modelUpdated();
        }
    }

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

            Table trainTable = loadTrainTable(recommendationType);
            Table testTable = loadTestTable(recommendationType);

            recSys = new RecSys(algorithm);
            recSys.train(trainTable);
            recSys.initialise(testTable, songTitles);
        }

        recommendations = recSys.recommend(selectedSong, numberOfRecommendations);

        notifyListeners();
    }


    private Distance createDistance(String distanceType) {
        if ("manhattan".equalsIgnoreCase(distanceType)) {
            return new ManhattanDistance();
        }
        return new EuclideanDistance();
    }

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

    private Table loadTrainTable(String recommendationType) throws IOException {
        if ("kmeans".equalsIgnoreCase(recommendationType)) {
            return new CSVUnlabeledFileReader(BASE_PATH + "/songs_train_withoutnames.csv")
                    .readTableFromSource();
        }
        return new CSVLabeledFileReader(BASE_PATH + "/songs_train.csv")
                .readTableFromSource();
    }

    private Table loadTestTable(String recommendationType) throws IOException {
        if ("kmeans".equalsIgnoreCase(recommendationType)) {
            return new CSVUnlabeledFileReader(BASE_PATH + "/songs_test_withoutnames.csv")
                    .readTableFromSource();
        }
        return new CSVLabeledFileReader(BASE_PATH + "/songs_test.csv")
                .readTableFromSource();
    }

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

    public List<String> getSongTitles() {
        return new ArrayList<>(songTitles);
    }

    public List<String> getRecommendations() {
        return new ArrayList<>(recommendations);
    }

    public String getSelectedSong() {
        return selectedSong;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public int getNumberOfRecommendations() {
        return numberOfRecommendations;
    }
}
