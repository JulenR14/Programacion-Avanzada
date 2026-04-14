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

    private TableWithLabels tablaEntrenada;
    private Distance distance;

    public KNN (Distance distance){
        this.distance = distance;
    }
    public KNN(){
        this.distance = new EuclideanDistance();
    }
    public void train(TableWithLabels data){
        tablaEntrenada = (TableWithLabels) data;
    }

    /**

     */
    public Integer estimate(List<Double> data){
        if (tablaEntrenada == null) throw new IllegalStateException("No se ha entrenado la tabla");

        double bestDist = Double.POSITIVE_INFINITY;             //aqui guardaremos la distancia minima encontrada
        String bestLabel = null;                                //etiqueta del vecino mas cercano
        int n = tablaEntrenada.getRowCount();                   //longitud de la tabla
        for (int i = 0; i < n; i++) {                           //recorremos la tabla

            RowWithLabel row = tablaEntrenada.getRowAt(i);      //por cada fila
            List<Double> x = row.getData();                     //extraemos los datos
            double dist = distance.calculateDistance(data, x);           //y calculamos la distancia entre ambas

            if (dist < bestDist) {                              //si la distancia que hemos encontrado es menor que
                bestDist = dist;                                // la mejor distancia que habiamos encontrados, entonces
                bestLabel = row.getLabel();                     // hacemos el cambio de variables
            }
        }

        return tablaEntrenada.getLabelAsInteger(bestLabel);     //devolvemos la etiqueta del vecino mas cercano convertida a entero

    }

}
