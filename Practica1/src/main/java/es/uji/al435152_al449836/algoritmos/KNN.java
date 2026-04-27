package es.uji.al435152_al449836.algoritmos;

import es.uji.al435152_al449836.algoritmos.distancias.Distance;
import es.uji.al435152_al449836.algoritmos.distancias.EuclideanDistance;
import es.uji.al435152_al449836.datos.RowWithLabel;
import es.uji.al435152_al449836.datos.Table;
import es.uji.al435152_al449836.datos.TableWithLabels;
import org.junit.jupiter.api.Disabled;

import java.util.Collection;
import java.util.List;

public class KNN implements Algorithm<TableWithLabels,List<Double>,Integer> {

    // Guardamos la tabla con la que se entrena el algoritmo.
    private TableWithLabels tablaEntrenada;

    // Esta referencia permite cambiar la forma de medir distancias
    // sin tocar el resto del algoritmo.
    private Distance distance;

    public KNN (Distance distance){
        // El algoritmo recibe desde fuera la estrategia de distancia.
        this.distance = distance;
    }
    public KNN(){
        // Si no se indica ninguna distancia, usamos la euclídea por defecto.
        this.distance = new EuclideanDistance();
    }
    public void train(TableWithLabels data){
        // Entrenar KNN aquí consiste simplemente en guardar los datos.
        tablaEntrenada = (TableWithLabels) data;
    }

    public Integer estimate(List<Double> data){
        // No podemos estimar nada si antes no se ha hecho train.
        if (tablaEntrenada == null) throw new IllegalStateException("No se ha entrenado la tabla");

        // Estas variables guardan el mejor candidato encontrado hasta ahora.
        double bestDist = Double.POSITIVE_INFINITY;
        String bestLabel = null;

        // Recorremos todas las filas de entrenamiento para buscar
        // cuál está más cerca de la muestra recibida.
        int n = tablaEntrenada.getRowCount();
        for (int i = 0; i < n; i++) {

            RowWithLabel row = tablaEntrenada.getRowAt(i);
            List<Double> x = row.getData();

            // La distancia concreta no la calcula KNN:
            // la delega en el objeto Distance que tenga inyectado.
            double dist = distance.calculateDistance(data, x);

            // Si encontramos una fila más cercana, pasa a ser la mejor.
            if (dist < bestDist) {
                bestDist = dist;
                bestLabel = row.getLabel();
            }
        }

        // Al final devolvemos la etiqueta ganadora convertida a entero.
        return tablaEntrenada.getLabelAsInteger(bestLabel);

    }

}
