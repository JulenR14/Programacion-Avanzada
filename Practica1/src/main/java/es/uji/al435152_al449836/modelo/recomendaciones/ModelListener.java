package es.uji.al435152_al449836.modelo.recomendaciones;

/**
 * Contrato minimo para objetos que quieran observar cambios del modelo.
 *
 * <p>Se usa una forma simple del patron Observer: cuando el modelo cambia, los
 * listeners registrados reciben esta notificacion y actualizan su interfaz.
 */
public interface ModelListener {
    /**
     * Informa de que el estado observable del modelo ha cambiado.
     */
    void modelUpdated();
}
