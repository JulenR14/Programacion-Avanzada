package es.uji.al435152_al449836.modelo.datos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tabla generica de datos numericos.
 *
 * <p>Sirve como estructura comun para lectores, algoritmos y recomendadores.
 * Mantiene cabeceras y filas, y ofrece operaciones basicas para acceder por
 * indice, inspeccionar columnas y conocer el tamano del conjunto.
 */
public class Table {
    /**
     * Nombres de las columnas del dataset.
     */
    private List<String> headers;

    /**
     * Filas almacenadas por posicion.
     */
    private HashMap<Integer, Row> rows;

    /**
     * Construye una tabla vacia.
     */
    public Table() {
        headers = new ArrayList<String>();
        rows = new HashMap<Integer, Row>();
    }

    /**
     * Devuelve la fila situada en la posicion indicada.
     */
    public Row getRowAt(int row) {
        return rows.get(row);
    }

    /**
     * Construye una columna a partir de la misma posicion en todas las filas.
     */
    public List<Double> getColumnAt(int colum) {
        List<Double> columnas = new ArrayList<>();
        for (Row r : rows.values()) {
            columnas.add(r.getData().get(colum));
        }
        return columnas;
    }

    /**
     * Devuelve el numero actual de filas.
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Devuelve las cabeceras del dataset.
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * Sustituye la lista de cabeceras.
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * Inserta una fila al final de la tabla.
     *
     * <p>El indice se genera de forma incremental a partir del tamano actual.
     */
    public void addRow(Row row) {
        rows.put(rows.size(), row);
    }
}
