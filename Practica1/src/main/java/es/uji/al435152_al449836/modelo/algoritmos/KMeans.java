package es.uji.al435152_al449836.modelo.algoritmos;

import es.uji.al435152_al449836.modelo.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.modelo.algoritmos.excepciones.InvalidClusterNumberException;
import es.uji.al435152_al449836.modelo.datos.Row;
import es.uji.al435152_al449836.modelo.datos.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Implementacion del algoritmo KMeans.
 *
 * A diferencia de KNN, este algoritmo no necesita etiquetas. Su objetivo es
 * descubrir grupos de canciones parecidas en funcion de sus caracteristicas
 * numericas. Despues, cualquier cancion nueva se asigna al cluster cuyo
 * centroide quede mas cerca.
 *
 * Dentro del recomendador, dos canciones se consideran afines cuando acaban
 * con la misma estimacion de cluster.
 */
public class KMeans implements Algorithm<Table, List<Double>, Integer> {
    /**
     * Numero de clusters que el algoritmo intentara formar.
     */
    private int numClusters;

    /**
     * Numero de iteraciones usadas para refinar los centroides.
     */
    private int numIterations;

    /**
     * Generador aleatorio para la inicializacion reproducible de centroides.
     */
    private Random random;

    /**
     * Datos de entrenamiento sobre los que se construyen los clusters.
     */
    private Table data;

    /**
     * Lista de centroides actuales, uno por cluster.
     */
    private List<List<Double>> centroides;

    /**
     * Distancia usada para asignar puntos y comparar centroides.
     */
    private Distance distance;

    /**
     * Constructor completo con configuracion explicita.
     */
    public KMeans(int numClusters, int numIterations, long seed, Distance distance) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.centroides = new ArrayList<>();
        this.distance = distance;
    }

    /**
     * Variante que usa distancia euclidea por defecto.
     */
    public KMeans(int numClusters, int numIterations, long seed) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.centroides = new ArrayList<>();
        this.distance = new EuclideanDistance();
    }

    /**
     * Ejecuta el ciclo clasico de KMeans: inicializar, asignar y recalcular.
     */
    @Override
    public void train(Table table) {
        if (numClusters > table.getRowCount()) {
            throw new InvalidClusterNumberException(numClusters, table.getRowCount());
        }

        this.data = table;
        this.centroides = new ArrayList<>();

        inicializarCentroides();

        // Cada iteracion recoloca todos los puntos y actualiza el centro medio
        // de cada grupo. Repetir este proceso hace que los clusters se estabilicen.
        for (int i = 0; i < numIterations; i++) {
            List<List<Row>> clusters = asignarClusters();
            recalcularCentroides(clusters);
        }
    }

    /**
     * Devuelve el indice del cluster cuyo centroide esta mas cerca de la muestra.
     */
    @Override
    public Integer estimate(List<Double> sample) {
        int mejorCluster = 0;
        double mejorDistancia = distance.calculateDistance(sample, centroides.get(0));

        for (int i = 1; i < centroides.size(); i++) {
            double distanciaActual = distance.calculateDistance(sample, centroides.get(i));
            if (distanciaActual < mejorDistancia) {
                mejorDistancia = distanciaActual;
                mejorCluster = i;
            }
        }

        return mejorCluster;
    }

    /**
     * Recalcula cada centroide como la media de las filas asignadas a su cluster.
     */
    private void recalcularCentroides(List<List<Row>> clusters) {
        List<List<Double>> nuevosCentroides = new ArrayList<>();

        for (int i = 0; i < numClusters; i++) {
            List<Row> cluster = clusters.get(i);

            // Si un cluster queda vacio, se conserva el centroide anterior para
            // no perder ese grupo durante la siguiente iteracion.
            if (cluster.isEmpty()) {
                nuevosCentroides.add(centroides.get(i));
            } else {
                nuevosCentroides.add(calcularCentroide(cluster));
            }
        }

        centroides = nuevosCentroides;
    }

    /**
     * Elige filas aleatorias distintas como centroides iniciales.
     *
     * La semilla fija hace que el proceso sea reproducible y defendible:
     * con los mismos datos y parametros se obtendra siempre el mismo arranque.
     */
    private void inicializarCentroides() {
        Set<Integer> usados = new HashSet<>();

        while (centroides.size() < numClusters) {
            int indice = random.nextInt(data.getRowCount());

            if (usados.contains(indice)) {
                continue;
            }

            usados.add(indice);
            List<Double> fila = data.getRowAt(indice).getData();
            centroides.add(new ArrayList<>(fila));
        }
    }

    /**
     * Asigna cada fila del entrenamiento al centroide mas cercano.
     */
    private List<List<Row>> asignarClusters() {
        List<List<Row>> clusters = new ArrayList<>();

        for (int i = 0; i < numClusters; i++) {
            clusters.add(new ArrayList<>());
        }

        for (int i = 0; i < data.getRowCount(); i++) {
            Row row = data.getRowAt(i);
            int clusterMasCercano = estimate(row.getData());
            clusters.get(clusterMasCercano).add(row);
        }

        return clusters;
    }

    /**
     * Calcula el centroide medio de un cluster promediando cada dimension.
     */
    private List<Double> calcularCentroide(List<Row> cluster) {
        int dimensiones = cluster.get(0).getData().size();
        List<Double> centroide = new ArrayList<>();

        for (int j = 0; j < dimensiones; j++) {
            centroide.add(0.0);
        }

        for (Row row : cluster) {
            for (int j = 0; j < dimensiones; j++) {
                centroide.set(j, centroide.get(j) + row.getData().get(j));
            }
        }

        for (int j = 0; j < dimensiones; j++) {
            centroide.set(j, centroide.get(j) / cluster.size());
        }

        return centroide;
    }
}
