package es.uji.al435152_al449836.modelo.algoritmos.distancias;

import java.util.List;

/**
 * Contrato de las estrategias de distancia.
 *
 * Esta interfaz desacopla a los algoritmos de una formula concreta para
 * medir similitud. Gracias a ello, KNN y KMeans pueden reutilizarse con
 * distintas metricas sin cambiar su estructura principal.
 */
public interface Distance {
    /**
     * Calcula la distancia entre dos vectores del mismo tamano.
     */
    double calculateDistance(List<Double> p, List<Double> q);
}
