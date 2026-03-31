package es.uji.al435152_al449836.algoritmos.excepciones;

public class LikedItemNotFoundException extends RuntimeException {
    public LikedItemNotFoundException(String nombreInvalido) {
        super("El item que has insertado no es valido: "+nombreInvalido);
    }
}
