package es.uji.al435152_al449836.modelo.algoritmos.excepciones;

/**
 * Error lanzado cuando se pide recomendar a partir de una cancion desconocida.
 *
 * <p>El recomendador solo puede trabajar con nombres que hayan sido indexados
 * previamente durante la fase de inicializacion del conjunto de test.
 */
public class LikedItemNotFoundException extends RuntimeException {
    private String nombreInvalido;

    /**
     * Construye la excepcion guardando el nombre que no se ha podido resolver.
     */
    public LikedItemNotFoundException(String nombreInvalido) {
        super("El item que has insertado no es valido: " + nombreInvalido);
        this.nombreInvalido = nombreInvalido;
    }

    /**
     * Devuelve el nombre que provoco el error.
     */
    public String getNombreInvalido() {
        return nombreInvalido;
    }
}
