package es.uji.al435152_al449836.modelo.lecturas;

import es.uji.al435152_al449836.modelo.datos.Table;

import java.io.File;
import java.util.Scanner;

public abstract class FileReader <T extends Table> extends ReaderTemplate<T>{
    // Este scanner se encarga de ir leyendo el fichero línea a línea.
    private Scanner scanner;

    public FileReader(String source) {
        super(source);
    }

    public void openSource(String source){
        try{
            // Convertimos el recurso del proyecto a una ruta real del sistema.
            String ref = getClass().getClassLoader().getResource(source).toURI().getPath();

            // Abrimos el fichero para poder leerlo con Scanner.
            scanner = new Scanner(new File(ref));
        }catch (Exception e){
            System.err.print("Error: " + e.getMessage());
        }
    }

    @Override
    public void closeSource() {
        // Al terminar, cerramos el fichero para liberar el recurso.
        scanner.close();
    }

    @Override
    public boolean hasMoreData(){
        // Devuelve si quedan más líneas por leer.
        return scanner.hasNextLine();
    }
    @Override
    public String getNextData(){
        // Devuelve la siguiente línea del fichero.
        return scanner.nextLine();
    }


}
