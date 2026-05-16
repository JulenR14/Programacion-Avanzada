package es.uji.al435152_al449836.modelo.recomendaciones;

import es.uji.al435152_al449836.modelo.algoritmos.Algorithm;
import es.uji.al435152_al449836.modelo.algoritmos.KMeans;
import es.uji.al435152_al449836.modelo.algoritmos.KNN;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.modelo.datos.Table;
import es.uji.al435152_al449836.modelo.lecturas.CSVLabeledFileReader;
import es.uji.al435152_al449836.modelo.lecturas.CSVUnlabeledFileReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Ejemplo de uso por consola del sistema de recomendacion.
 *
 * No interviene en la interfaz JavaFX principal, pero sirve para seguir el
 * flujo completo de manera muy directa: cargar ficheros, crear algoritmo,
 * entrenar, inicializar el test y pedir recomendaciones.
 */
class SongRecSys {
    private RecSys recsys;

    /**
     * Monta un escenario completo de recomendacion para el metodo indicado.
     */
    SongRecSys(String method) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "recommender";

        Map<String, String> filenames = new HashMap<>();
        filenames.put("knn" + "train", ruta + sep + "songs_train.csv");
        filenames.put("knn" + "test", ruta + sep + "songs_test.csv");
        filenames.put("kmeans" + "train", ruta + sep + "songs_train_withoutnames.csv");
        filenames.put("kmeans" + "test", ruta + sep + "songs_test_withoutnames.csv");

        // Aqui se aprecia el patron Strategy: el algoritmo recibe desde fuera
        // la forma concreta de medir distancias.
        Map<String, Algorithm> algorithms = new HashMap<>();
        Distance distance = new EuclideanDistance();
        algorithms.put("knn", new KNN(distance));
        algorithms.put("kmeans", new KMeans(15, 200, 4321, distance));

        Map<String, Table> tables = new HashMap<>();
        String[] stages = {"train", "test"};
        for (String stage : stages) {
            tables.put("knn" + stage, new CSVLabeledFileReader(filenames.get("knn" + stage)).readTableFromSource());
            tables.put("kmeans" + stage, new CSVUnlabeledFileReader(filenames.get("kmeans" + stage)).readTableFromSource());
        }

        List<String> names = readNames(ruta + sep + "songs_test_names.csv");

        this.recsys = new RecSys(algorithms.get(method));
        this.recsys.train(tables.get(method + "train"));
        this.recsys.initialise(tables.get(method + "test"), names);

        // La cancion elegida se usa como semilla para buscar otras con la misma clase.
        String likedName = "Lootkemia";
        List<String> recommendedItems = this.recsys.recommend(likedName, 5);

        reportRecommendation(likedName, recommendedItems);
    }

    /**
     * Lee los nombres reales de las canciones del conjunto de test.
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
     * Muestra por consola el resultado de la recomendacion.
     */
    private void reportRecommendation(String likedName, List<String> recommendedItems) {
        System.out.println("If you liked \"" + likedName + "\" then you might like:");

        for (String name : recommendedItems) {
            System.out.println("\t * " + name);
        }
    }

    /**
     * Ejecuta un ejemplo con KNN y otro con KMeans.
     */
    public static void main(String[] args) throws Exception {
        new SongRecSys("knn");
        new SongRecSys("kmeans");
    }
}
