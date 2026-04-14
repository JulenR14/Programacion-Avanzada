package es.uji.al435152_al449836.algoritmos;

import es.uji.al435152_al449836.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.algoritmos.excepciones.InvalidClusterNumberException;
import es.uji.al435152_al449836.datos.Table;

import java.util.*;

import es.uji.al435152_al449836.datos.Row;

public class KMeans implements Algorithm<Table,List<Double>,Integer>{
    // Atributos principales del algoritmo
    private int numClusters;                // número de clusters (K)
    private int numIterations;              // número de iteraciones del algoritmo
    private Random random;                  // generador aleatorio (controlado por seed)
    private Table data;                     // datos de entrenamiento
    private List<List<Double>> centroides;  // centroides actuales
    private Distance distance;


    // Constructor: inicializa parámetros del algoritmo
    public KMeans(int numClusters, int numIterations, long seed, Distance distance) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed); // importante para reproducibilidad
        this.centroides = new ArrayList<>();
        this.distance = distance;
    }
    public KMeans(int numClusters, int numIterations, long seed) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed); // importante para reproducibilidad
        this.centroides = new ArrayList<>();
        this.distance = new EuclideanDistance();
    }


    public void train(Table table){
        // Validación: no puede haber más clusters que datos
        if (numClusters>table.getRowCount())
            throw new InvalidClusterNumberException(numClusters,table.getRowCount());

        this.data = table;
        this.centroides = new ArrayList<>();

        // Paso 1: inicialización aleatoria de centroides
        inicializarCentroides();

        // Paso 2: iteraciones de refinamiento
        for (int i = 0; i < numIterations; i++) {
            List<List<Row>> clusters = asignarClusters(); // asignación de puntos
            recalcularCentroides(clusters);               // actualización de centroides
        }
    }

    public Integer estimate(List<Double> sample) {
        // Busca el centroide más cercano (misma lógica que en asignarClusters)
        int mejorCluster = 0;
        double mejorDistancia = distance.calculateDistance(sample,centroides.get(0));

        for (int i = 1; i < centroides.size(); i++) {
            double distanciaActual = distance.calculateDistance(sample,centroides.get(i));
            if (distanciaActual < mejorDistancia) {
                mejorDistancia = distanciaActual;
                mejorCluster = i;
            }
        }

        return mejorCluster; // devuelve índice del cluster más cercano
    }

    private void recalcularCentroides(List<List<Row>> clusters) {
        List<List<Double>> nuevosCentroides = new ArrayList<>();

        for (int i = 0; i < numClusters; i++) {
            List<Row> cluster = clusters.get(i);

            // Si un cluster queda vacío, se mantiene su centroide anterior
            if (cluster.isEmpty()) {
                nuevosCentroides.add(centroides.get(i));
            } else {
                nuevosCentroides.add(calcularCentroide(cluster));
            }
        }

        centroides = nuevosCentroides;
    }

    private void inicializarCentroides() {
        Set<Integer> usados = new HashSet<>();

        // Selecciona filas aleatorias sin repetir para inicializar centroides
        while (centroides.size() < numClusters) {
            int indice = random.nextInt(data.getRowCount());

            if (!usados.contains(indice)) {
                usados.add(indice);
                List<Double> fila = data.getRowAt(indice).getData();
                centroides.add(new ArrayList<>(fila)); // copia para evitar referencias
            }
        }
    }

    private List<List<Row>> asignarClusters() {
        List<List<Row>> clusters = new ArrayList<>();

        // Inicializa lista de clusters vacíos
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new ArrayList<>());
        }

        // Asigna cada fila al cluster cuyo centroide esté más cerca
        for (int i = 0; i < data.getRowCount(); i++) {
            Row row = data.getRowAt(i);
            int clusterMasCercano = estimate(row.getData()); // reutiliza estimate
            clusters.get(clusterMasCercano).add(row);
        }

        return clusters;
    }

    private List<Double> calcularCentroide(List<Row> cluster) {
        int dimensiones = cluster.get(0).getData().size();
        List<Double> centroide = new ArrayList<>();

        // Inicializa vector a 0
        for (int j = 0; j < dimensiones; j++) {
            centroide.add(0.0);
        }

        // Suma todos los puntos del cluster
        for (Row row : cluster) {
            for (int j = 0; j < dimensiones; j++) {
                centroide.set(j, centroide.get(j) + row.getData().get(j));
            }
        }

        // Divide entre el número de elementos (media)
        for (int j = 0; j < dimensiones; j++) {
            centroide.set(j, centroide.get(j) / cluster.size());
        }

        return centroide;
    }
}