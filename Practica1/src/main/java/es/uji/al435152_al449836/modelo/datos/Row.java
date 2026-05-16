package es.uji.al435152_al449836.modelo.datos;

import java.util.List;

/**
 * Representa una fila numerica del dataset.
 *
 * Cada posicion de la lista corresponde con una caracteristica de la
 * cancion en el mismo orden en que aparecen las cabeceras de la tabla.
 */
public class Row {
    /**
     * Valores numericos de la fila.
     */
    private List<Double> data;

    /**
     * Construye una fila a partir de su lista de caracteristicas.
     */
    public Row(List<Double> numberlist) {
        data = numberlist;
    }

    /**
     * Devuelve los valores numericos almacenados en la fila.
     */
    public List<Double> getData() {
        return data;
    }
}
