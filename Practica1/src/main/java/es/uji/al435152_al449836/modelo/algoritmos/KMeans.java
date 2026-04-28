package es.uji.al435152_al449836.modelo.algoritmos;

import es.uji.al435152_al449836.modelo.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.modelo.algoritmos.excepciones.InvalidClusterNumberException;
import es.uji.al435152_al449836.modelo.datos.Table;

import java.util.*;

import es.uji.al435152_al449836.modelo.datos.Row;

public class KMeans implements Algorithm<Table,List<Double>,Integer>{
    // Número de grupos que queremos formar.
    private int numClusters;

    // Cuántas veces refinamos los centroides.
    private int numIterations;

    // Se usa para que la inicialización aleatoria sea reproducible.
    private Random random;

    // Aquí se guardan los datos de entrenamiento.
    private Table data;

    // Cada centroide representa el "centro" de un cluster.
    private List<List<Double>> centroides;

    // Igual que en KNN, la forma de medir distancia se inyecta desde fuera.
    private Distance distance;


    public KMeans(int numClusters, int numIterations, long seed, Distance distance) {
        // Este constructor deja totalmente explícita la distancia a usar.
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.centroides = new ArrayList<>();
        this.distance = distance;
    }
    public KMeans(int numClusters, int numIterations, long seed) {
        // Esta versión usa distancia euclídea por defecto.
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.centroides = new ArrayList<>();
        this.distance = new EuclideanDistance();
    }


    public void train(Table table){
        // No tiene sentido pedir más clusters que muestras.
        if (numClusters>table.getRowCount())
            throw new InvalidClusterNumberException(numClusters,table.getRowCount());

        // Guardamos los datos y reiniciamos la lista de centroides.
        this.data = table;
        this.centroides = new ArrayList<>();

        // Paso 1: elegimos centroides iniciales.
        inicializarCentroides();

        // Paso 2: repetimos el ciclo típico de KMeans:
        // asignar puntos y recalcular centroides.
        for (int i = 0; i < numIterations; i++) {
            List<List<Row>> clusters = asignarClusters();
            recalcularCentroides(clusters);
        }
    }

    public Integer estimate(List<Double> sample) {
        // Para estimar, buscamos qué centroide está más cerca de la muestra.
        int mejorCluster = 0;
        double mejorDistancia = distance.calculateDistance(sample,centroides.get(0));

        // Comparamos la muestra con el resto de centroides.
        for (int i = 1; i < centroides.size(); i++) {
            double distanciaActual = distance.calculateDistance(sample,centroides.get(i));
            if (distanciaActual < mejorDistancia) {
                mejorDistancia = distanciaActual;
                mejorCluster = i;
            }
        }

        // Devolvemos el índice del cluster ganador.
        return mejorCluster;
    }

    private void recalcularCentroides(List<List<Row>> clusters) {
        // Aquí construiremos la nueva versión de todos los centroides.
        List<List<Double>> nuevosCentroides = new ArrayList<>();

        for (int i = 0; i < numClusters; i++) {
            List<Row> cluster = clusters.get(i);

            // Si un cluster se queda vacío, mantenemos el centroide anterior.
            if (cluster.isEmpty()) {
                nuevosCentroides.add(centroides.get(i));
            } else {
                // Si tiene puntos, calculamos su nuevo centro.
                nuevosCentroides.add(calcularCentroide(cluster));
            }
        }

        // Sustituimos los centroides antiguos por los nuevos.
        centroides = nuevosCentroides;
    }

    private void inicializarCentroides() {
        // Este conjunto sirve para no repetir índices al elegir centroides.
        Set<Integer> usados = new HashSet<>();
        int indice = random.nextInt(data.getRowCount());

        // Vamos tomando filas aleatorias de la tabla para arrancar el algoritmo.
        while (centroides.size() < numClusters && !usados.contains(indice)) {

            indice = random.nextInt(data.getRowCount());
            usados.add(indice);
            List<Double> fila = data.getRowAt(indice).getData();
            // Copiamos la fila para que el centroide tenga su propia lista.
            centroides.add(new ArrayList<>(fila));
        }
    }

    private List<List<Row>> asignarClusters() {
        // Esta estructura guardará qué filas caen en cada cluster.
        List<List<Row>> clusters = new ArrayList<>();

        // Primero creamos todos los clusters vacíos.
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new ArrayList<>());
        }

        // Después colocamos cada fila en el cluster cuyo centroide esté más cerca.
        for (int i = 0; i < data.getRowCount(); i++) {
            Row row = data.getRowAt(i);
            int clusterMasCercano = estimate(row.getData());
            clusters.get(clusterMasCercano).add(row);
        }

        return clusters;
    }

    private List<Double> calcularCentroide(List<Row> cluster) {
        // Todas las filas del cluster tienen el mismo número de columnas.
        int dimensiones = cluster.get(0).getData().size();
        List<Double> centroide = new ArrayList<>();

        // Empezamos con un vector lleno de ceros.
        for (int j = 0; j < dimensiones; j++) {
            centroide.add(0.0);
        }

        // Sumamos, columna a columna, todos los puntos del cluster.
        for (Row row : cluster) {
            for (int j = 0; j < dimensiones; j++) {
                centroide.set(j, centroide.get(j) + row.getData().get(j));
            }
        }

        // Dividimos entre el tamaño del cluster para obtener la media.
        for (int j = 0; j < dimensiones; j++) {
            centroide.set(j, centroide.get(j) / cluster.size());
        }

        // Ese vector medio es el nuevo centroide.
        return centroide;
    }
}
