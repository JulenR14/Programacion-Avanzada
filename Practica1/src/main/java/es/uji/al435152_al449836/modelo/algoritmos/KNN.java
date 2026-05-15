package es.uji.al435152_al449836.modelo.algoritmos;

import es.uji.al435152_al449836.modelo.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.modelo.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.modelo.datos.RowWithLabel;
import es.uji.al435152_al449836.modelo.datos.TableWithLabels;

import java.util.List;

/**
 * Implementacion de KNN para clasificacion.
 *
 * <p>En esta practica se esta usando la version mas simple del algoritmo:
 * para una muestra nueva se busca la fila etiquetada mas cercana y se devuelve
 * su etiqueta. La idea de cercania no esta fijada aqui, sino en la estrategia
 * {@link Distance} que se inyecta en el constructor.
 */
public class KNN implements Algorithm<TableWithLabels, List<Double>, Integer> {
    /**
     * Tabla etiquetada que el algoritmo conserva tras el entrenamiento.
     */
    private TableWithLabels tablaEntrenada;

    /**
     * Estrategia usada para medir la distancia entre canciones.
     */
    private Distance distance;

    /**
     * Construye KNN con la distancia indicada por el cliente.
     */
    public KNN(Distance distance) {
        this.distance = distance;
    }

    /**
     * Construye KNN usando distancia euclidea por defecto.
     */
    public KNN() {
        this.distance = new EuclideanDistance();
    }

    /**
     * En KNN entrenar significa guardar los ejemplos etiquetados.
     *
     * <p>No se calculan centroides ni parametros compactos: la "memoria" del
     * algoritmo es el propio conjunto de entrenamiento.
     */
    @Override
    public void train(TableWithLabels data) {
        tablaEntrenada = data;
    }

    /**
     * Busca la fila de entrenamiento mas parecida a la muestra y devuelve su clase.
     */
    @Override
    public Integer estimate(List<Double> data) {
        if (tablaEntrenada == null) {
            throw new IllegalStateException("No se ha entrenado la tabla");
        }

        double bestDist = Double.POSITIVE_INFINITY;
        String bestLabel = null;

        // Se examina cada fila del entrenamiento porque este KNN compara la
        // muestra nueva contra todos los ejemplos almacenados.
        int n = tablaEntrenada.getRowCount();
        for (int i = 0; i < n; i++) {
            RowWithLabel row = tablaEntrenada.getRowAt(i);
            List<Double> x = row.getData();
            double dist = distance.calculateDistance(data, x);

            if (dist < bestDist) {
                bestDist = dist;
                bestLabel = row.getLabel();
            }
        }

        // La tabla mantiene la correspondencia etiqueta -> entero para que el
        // recomendador pueda trabajar con clases numericas.
        return tablaEntrenada.getLabelAsInteger(bestLabel);
    }
}
