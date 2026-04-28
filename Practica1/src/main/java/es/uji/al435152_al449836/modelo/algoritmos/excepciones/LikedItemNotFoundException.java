package es.uji.al435152_al449836.modelo.algoritmos.excepciones;

public class LikedItemNotFoundException extends RuntimeException {
    private String nombreInvalido;
    public LikedItemNotFoundException(String nombreInvalido) {
        super("El item que has insertado no es valido: "+nombreInvalido);
        this.nombreInvalido = nombreInvalido;
    }
    public String getNombreInvalido(){return nombreInvalido;}
}
