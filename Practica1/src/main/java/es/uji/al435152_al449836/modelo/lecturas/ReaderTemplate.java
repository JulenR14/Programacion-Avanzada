package es.uji.al435152_al449836.modelo.lecturas;

import es.uji.al435152_al449836.modelo.datos.Table;

/**
 * Plantilla general para la lectura de datasets.
 *
 * <p>Aplica el patron Template Method: el orden del proceso esta fijado aqui,
 * mientras que las subclases concretas deciden como abrir la fuente, como
 * procesar cabeceras y datos y como cerrar el recurso.
 */
public abstract class ReaderTemplate<T extends Table> {
    /**
     * Referencia logica a la fuente de datos.
     */
    private String source;

    /**
     * Tabla que se va construyendo durante la lectura.
     */
    protected T table;

    /**
     * Construye la plantilla recordando la fuente de datos.
     */
    public ReaderTemplate(String source) {
        this.source = source;
    }

    /**
     * Abre la fuente de datos.
     */
    public abstract void openSource(String source);

    /**
     * Procesa la linea de cabeceras.
     */
    public abstract void processHeaders(String headers);

    /**
     * Procesa una linea de datos.
     */
    public abstract void processData(String data);

    /**
     * Cierra la fuente de datos.
     */
    public abstract void closeSource();

    /**
     * Indica si quedan mas datos pendientes.
     */
    public abstract boolean hasMoreData();

    /**
     * Recupera la siguiente unidad de datos cruda.
     */
    public abstract String getNextData();

    /**
     * Ejecuta el algoritmo completo de lectura en el orden correcto.
     *
     * <p>Se declara final para garantizar que todas las subclases respetan el
     * mismo flujo: abrir, leer cabeceras, leer datos, cerrar y devolver la tabla.
     */
    public final T readTableFromSource() {
        openSource(source);
        if (hasMoreData()) {
            processHeaders(getNextData());
            while (hasMoreData()) {
                processData(getNextData());
            }
        }
        closeSource();
        return table;
    }

    /**
     * Devuelve la tabla construida hasta el momento.
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sustituye la tabla interna.
     */
    public void setTable(T table) {
        this.table = table;
    }
}
