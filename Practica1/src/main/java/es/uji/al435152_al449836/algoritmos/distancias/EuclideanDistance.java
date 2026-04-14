package es.uji.al435152_al449836.algoritmos.distancias;

import java.util.List;

public class EuclideanDistance implements  Distance{

    @Override
    public double calculateDistance(List<Double> p, List<Double> q) {
        double suma = 0.0;

        // Cálculo de la distancia euclídea entre dos puntos
        for (int i = 0; i < p.size(); i++) {
            double diferencia = p.get(i) - q.get(i);
            suma += diferencia * diferencia;
        }

        return Math.sqrt(suma);
    }
}
