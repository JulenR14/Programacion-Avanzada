package es.uji.al435152_al449836.modelo.algoritmos.distancias;

import java.util.List;

/**
 * Distancia euclidea clasica.
 *
 * Mide la separacion en linea recta entre dos puntos. Como eleva al cuadrado
 * cada diferencia antes de sumarla, castiga mas las desviaciones grandes.
 */
public class EuclideanDistance implements Distance {

    @Override
    public double calculateDistance(List<Double> p, List<Double> q) {
        double suma = 0.0;

        for (int i = 0; i < p.size(); i++) {
            double diferencia = p.get(i) - q.get(i);
            suma += diferencia * diferencia;
        }

        return Math.sqrt(suma);
    }
}
