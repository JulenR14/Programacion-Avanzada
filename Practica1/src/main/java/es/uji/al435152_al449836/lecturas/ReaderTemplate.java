package es.uji.al435152_al449836.lecturas;


import es.uji.al435152_al449836.datos.Table;

import java.net.URISyntaxException;

public abstract class ReaderTemplate <T extends Table>{
    // Guardamos la referencia a la fuente de datos que se quiere leer.
    private String source;

    // Aquí se irá construyendo la tabla resultado.
    protected T table;

    public ReaderTemplate(String source){
        this.source = source;
    }

    // Estos métodos representan los pasos del algoritmo general de lectura.
    // Cada subclase concreta decide cómo hacer cada paso.
    public abstract void openSource(String source);
    public abstract void processHeaders(String headers);
    public abstract void processData(String data);
    public abstract void closeSource();
    public abstract boolean hasMoreData();
    public abstract String getNextData();


    public final T readTableFromSource() {
        // El método plantilla define el orden completo del proceso:
        // abrir, leer cabecera, leer datos, cerrar y devolver la tabla.
        openSource(source);
        if (hasMoreData()){
            processHeaders(getNextData());
            while (hasMoreData()){
                 processData(getNextData());
            }
        }
        closeSource();
        return table;
    }

    // Estos getters y setters permiten consultar o fijar la tabla si hace falta.
    public Table getTable(){return table;}

    public void setTable(T table){
        this.table = table;
    }


}
