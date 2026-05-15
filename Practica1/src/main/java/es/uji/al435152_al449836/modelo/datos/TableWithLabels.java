package es.uji.al435152_al449836.modelo.datos;

import java.util.HashMap;
import java.util.Map;

/**
 * Variante de {@link Table} preparada para aprendizaje supervisado.
 *
 * <p>Ademas de las filas numericas, esta clase mantiene una conversion entre
 * etiquetas textuales e identificadores enteros. Esa conversion resulta util
 * porque KNN termina devolviendo clases numericas al recomendador.
 */
public class TableWithLabels extends Table {
    /**
     * Mapa de etiqueta textual a identificador entero.
     */
    private Map<String, Integer> labelsToIndex;

    /**
     * Nombre de la columna de etiqueta del CSV original.
     */
    private String headerLabel;

    /**
     * Construye una tabla etiquetada vacia.
     */
    public TableWithLabels() {
        super();
        labelsToIndex = new HashMap<>();
    }

    /**
     * Devuelve la fila ya convertida a {@link RowWithLabel}.
     */
    @Override
    public RowWithLabel getRowAt(int row) {
        return (RowWithLabel) super.getRowAt(row);
    }

    /**
     * Traduce una etiqueta textual a su identificador entero.
     */
    public Integer getLabelAsInteger(String label) {
        return labelsToIndex.get(label);
    }

    /**
     * Inserta una fila etiquetada y registra su clase si es la primera vez que aparece.
     */
    @Override
    public void addRow(Row row) {
        super.addRow(row);
        RowWithLabel rowW = (RowWithLabel) row;

        // El primer encuentro con cada etiqueta fija su identificador.
        if (!labelsToIndex.containsKey(rowW.getLabel())) {
            labelsToIndex.put(rowW.getLabel(), labelsToIndex.size());
        }
    }

    /**
     * Guarda el nombre de la columna que contiene la etiqueta.
     */
    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    /**
     * Devuelve el numero de filas como alias expresivo.
     */
    public int getNumRows() {
        return super.getRowCount();
    }
}
