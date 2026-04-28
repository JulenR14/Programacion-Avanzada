package es.uji.al435152_al449836.modelo.algoritmos.distancias;

import java.util.List;

public class EuclideanDistance implements  Distance{

    @Override
    public double calculateDistance(List<Double> p, List<Double> q) {
        // Aquí vamos acumulando la suma de cuadrados de las diferencias.
        double suma = 0.0;

        // Recorremos ambas listas componente a componente.
        for (int i = 0; i < p.size(); i++) {
            double diferencia = p.get(i) - q.get(i);
            suma += diferencia * diferencia;
        }

        // La distancia euclídea termina sacando la raíz cuadrada.
        return Math.sqrt(suma);
    }
}
