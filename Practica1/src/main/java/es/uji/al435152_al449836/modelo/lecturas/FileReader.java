package es.uji.al435152_al449836.modelo.lecturas;

import es.uji.al435152_al449836.modelo.datos.Table;

import java.io.File;
import java.util.Scanner;

/**
 * Lector generico de ficheros apoyado en {@link Scanner}.
 *
 * Esta clase resuelve el recurso desde el classpath, lo abre, permite
 * consumirlo linea a linea y lo cierra al final. La interpretacion concreta de
 * cabeceras y datos sigue delegada en las subclases.
 */
public abstract class FileReader<T extends Table> extends ReaderTemplate<T> {
    /**
     * Scanner asociado al fichero actualmente abierto.
     */
    private Scanner scanner;

    /**
     * Construye el lector recordando la ruta del recurso a consumir.
     */
    public FileReader(String source) {
        super(source);
    }

    /**
     * Abre el recurso indicado y lo deja listo para lectura secuencial.
     */
    @Override
    public void openSource(String source) {
        try {
            String ref = getClass().getClassLoader().getResource(source).toURI().getPath();
            scanner = new Scanner(new File(ref));
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
        }
    }

    /**
     * Cierra el recurso asociado al scanner.
     */
    @Override
    public void closeSource() {
        scanner.close();
    }

    /**
     * Indica si quedan lineas pendientes de lectura.
     */
    @Override
    public boolean hasMoreData() {
        return scanner.hasNextLine();
    }

    /**
     * Devuelve la siguiente linea del fichero.
     */
    @Override
    public String getNextData() {
        return scanner.nextLine();
    }
}
