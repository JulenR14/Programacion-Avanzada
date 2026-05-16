package es.uji.al435152_al449836.modelo.algoritmos.distancias;

import java.util.List;

/**
 * Distancia Manhattan.
 *
 * En vez de medir una diagonal directa, suma las diferencias absolutas de
 * cada dimension. Es una metrica util cuando interesa tratar cada eje de forma
 * aditiva y sin amplificar tanto los valores extremos.
 */
public class ManhattanDistance implements Distance {
    @Override
    public double calculateDistance(List<Double> p, List<Double> q) {
        double distancia = 0.0;

        for (int i = 0; i < p.size(); i++) {
            distancia += Math.abs(p.get(i) - q.get(i));
        }

        return distancia;
    }
}
