package es.uji.al435152_al449836.algoritmos;

import es.uji.al435152_al449836.datos.Table;

import java.util.*;

import es.uji.al435152_al449836.datos.Row;


public class KMeans {
    //Atributos
    private int numClusters;                //numero de grupos
    private int numIterations;              //numero de iteraciones
    private Random random;                  //Random a utilizar
    private Table data;                     //Data
    private List<List<Double>> centroides;  //Centroides


    //Contructor
    public KMeans(int numClusters, int numIterations, long seed) {
        this.numClusters = numClusters;
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.centroides = new ArrayList<>();
    }



    public void train(Table table){
        this.data = table;
        this.centroides = new ArrayList<>();

        inicializarCentroides();
        for (int i = 0; i < numIterations; i++) {
            List<List<Row>> clusters = asignarClusters();
            recalcularCentroides(clusters);
        }

    }

    public Integer estimate(List<Double> sample) {
        int mejorCluster = 0;
        double mejorDistancia = distanciaEuclidea(sample, centroides.get(0));

        for (int i = 1; i < centroides.size(); i++) {
            double distanciaActual = distanciaEuclidea(sample, centroides.get(i));
            if (distanciaActual < mejorDistancia) {
                mejorDistancia = distanciaActual;
                mejorCluster = i;
            }
        }

        return mejorCluster;
    }
    private void recalcularCentroides(List<List<Row>> clusters) {
        List<List<Double>> nuevosCentroides = new ArrayList<>();

        for (int i = 0; i < numClusters; i++) {
            List<Row> cluster = clusters.get(i);

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

        while (centroides.size() < numClusters) {
            int indice = random.nextInt(data.getRowCount());

            if (!usados.contains(indice)) {
                usados.add(indice);
                List<Double> fila = data.getRowAt(indice).getData();
                centroides.add(new ArrayList<>(fila));
            }
        }
    }

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

    private double distanciaEuclidea(List<Double> a, List<Double> b) {
        double suma = 0.0;

        for (int i = 0; i < a.size(); i++) {
            double diferencia = a.get(i) - b.get(i);
            suma += diferencia * diferencia;
        }

        return Math.sqrt(suma);
    }
}
