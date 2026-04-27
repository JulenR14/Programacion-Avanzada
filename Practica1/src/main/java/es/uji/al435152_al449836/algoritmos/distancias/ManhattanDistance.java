package es.uji.al435152_al449836.algoritmos.distancias;

import java.util.List;

public class ManhattanDistance implements Distance{
    @Override
    public double calculateDistance(List<Double> p, List<Double> q) {
        // Aquí sumaremos cuánto se separan los dos puntos en cada eje.
        double distancia = 0.0;

        // En Manhattan no elevamos al cuadrado:
        // simplemente sumamos las diferencias en valor absoluto.
        for (int i = 0; i < p.size(); i++) {
            distancia += Math.abs(p.get(i) - q.get(i));
        }

        return distancia;
    }
}
