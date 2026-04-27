package es.uji.al435152_al449836.recomendaciones;

import es.uji.al435152_al449836.algoritmos.Algorithm;
import es.uji.al435152_al449836.algoritmos.KMeans;
import es.uji.al435152_al449836.algoritmos.KNN;
import es.uji.al435152_al449836.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.datos.Table;
import es.uji.al435152_al449836.lecturas.CSVLabeledFileReader;
import es.uji.al435152_al449836.lecturas.CSVUnlabeledFileReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

class SongRecSys {
    private RecSys recsys;

    SongRecSys(String method) throws Exception {
        // Se usa el separador del sistema para construir rutas portables.
        String sep = System.getProperty("file.separator");
        String ruta = "recommender";

        // Aquí se agrupan los nombres de los ficheros que necesita cada algoritmo.
        Map<String,String> filenames = new HashMap<>();
        filenames.put("knn"+"train",ruta+sep+"songs_train.csv");
        filenames.put("knn"+"test",ruta+sep+"songs_test.csv");
        filenames.put("kmeans"+"train",ruta+sep+"songs_train_withoutnames.csv");
        filenames.put("kmeans"+"test",ruta+sep+"songs_test_withoutnames.csv");

        // Elegimos una distancia concreta y se la inyectamos a los algoritmos.
        // Esta es la parte donde se ve el patrón Strategy en uso.
        Map<String, Algorithm> algorithms = new HashMap<>();
        Distance distance = new EuclideanDistance();
        algorithms.put("knn",new KNN(distance));
        algorithms.put("kmeans",new KMeans(15, 200, 4321, distance));

        // Aquí cargamos los datos usando los nuevos lectores de la práctica 3.
        Map<String, Table> tables = new HashMap<>();
        String [] stages = {"train", "test"};
        for (String stage : stages) {
            tables.put("knn" + stage, new CSVLabeledFileReader(filenames.get("knn" + stage)).readTableFromSource());
            tables.put("kmeans" + stage, new CSVUnlabeledFileReader(filenames.get("kmeans" + stage)).readTableFromSource());
        }

        // Este fichero contiene los nombres de las canciones del conjunto de test.
        List<String> names = readNames(ruta+sep+"songs_test_names.csv");

        // Se crea el recomendador, se entrena y se inicializa con las canciones de test.
        this.recsys = new RecSys(algorithms.get(method));
        this.recsys.train(tables.get(method+"train"));
        this.recsys.initialise(tables.get(method+"test"), names);

        // Se elige una canción de referencia y se piden recomendaciones.
        String liked_name = "Lootkemia";
        List<String> recommended_items = this.recsys.recommend(liked_name,5);

        // Finalmente se muestran por pantalla.
        reportRecommendation(liked_name,recommended_items);
    }

    private List<String> readNames(String fileOfItemNames) throws IOException, URISyntaxException {
        // Convertimos el recurso a ruta física para poder leerlo con Scanner.
        String path = getClass().getClassLoader().getResource(fileOfItemNames).toURI().getPath();

        List<String> names = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));

        // Cada línea del fichero contiene un nombre de canción.
        while (scanner.hasNextLine()) {
            names.add(scanner.nextLine());
        }
        scanner.close();
        return names;
    }

    private void reportRecommendation(String liked_name, List<String> recommended_items) {
        // Mensaje principal de recomendación.
        System.out.println("If you liked \""+liked_name+"\" then you might like:");

        // Mostramos una a una las canciones sugeridas.
        for (String name : recommended_items)
        {
            System.out.println("\t * "+name);
        }
    }

    public static void main(String[] args) throws Exception {
        // Ejecutamos un ejemplo con KNN y otro con KMeans.
        new SongRecSys("knn");
        new SongRecSys("kmeans");
    }
}
