package es.uji.al435152_al449836.modelo.algoritmos.distancias;

import java.util.List;

public interface Distance {
    // Este método representa "la estrategia":
    // cualquier clase que implemente esta interfaz sabrá calcular
    // la distancia entre dos puntos.
    public double calculateDistance(List<Double> p, List<Double> q);

}
