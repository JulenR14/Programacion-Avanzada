package es.uji.al435152_al449836.modelo.algoritmos;

import es.uji.al435152_al449836.modelo.datos.Table;

/**
 * Contrato comun para los algoritmos usados por el sistema.
 *
 * <p>La interfaz separa dos fases tipicas:
 * - {@code train}: el algoritmo aprende o memoriza informacion a partir de una tabla.
 * - {@code estimate}: el algoritmo responde una prediccion para una nueva muestra.
 *
 * <p>Se usa con genericos porque no todos los algoritmos trabajan con el mismo
 * tipo de tabla, de entrada ni de salida.
 */
public interface Algorithm<T extends Table, U, W> {
    /**
     * Entrena el algoritmo con la tabla indicada.
     */
    void train(T table);

    /**
     * Calcula una estimacion para la muestra recibida.
     */
    W estimate(U lista);
}
