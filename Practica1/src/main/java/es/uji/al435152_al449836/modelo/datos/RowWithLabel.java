package es.uji.al435152_al449836.modelo.datos;

import java.util.List;

/**
 * Extension de {@link Row} que incorpora una etiqueta de clase.
 *
 * <p>Este tipo de fila es el que necesita KNN, porque cada ejemplo de
 * entrenamiento debe incluir tanto sus atributos numericos como la respuesta
 * correcta asociada.
 */
public class RowWithLabel extends Row {
    /**
     * Etiqueta textual de la fila.
     */
    private String label;

    /**
     * Construye una fila etiquetada con sus datos y su clase.
     */
    public RowWithLabel(List<Double> numberlist, String label) {
        super(numberlist);
        this.label = label;
    }

    /**
     * Devuelve la etiqueta asociada a la fila.
     */
    public String getLabel() {
        return label;
    }
}
