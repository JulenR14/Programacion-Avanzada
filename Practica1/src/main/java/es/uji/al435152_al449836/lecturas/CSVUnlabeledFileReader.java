package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.Row;
import es.uji.al435152_al449836.datos.Table;

import java.util.ArrayList;
import java.util.List;

public class CSVUnlabeledFileReader extends FileReader <Table>{
    private String nombrefichero;

    public CSVUnlabeledFileReader(String nombrefichero){
        // La clase madre ya guarda la fuente, así que aquí solo la pasamos.
        super(nombrefichero);
        this.nombrefichero = nombrefichero;
    }
    @Override
    public void processHeaders(String headerLine){
        // Al empezar la lectura, creamos la tabla sin etiquetas.
        this.table = new Table();

        // La primera línea del CSV contiene los nombres de columnas.
        String[] parts = headerLine.split(",");
        List<String> headers = new ArrayList<>();

        // Guardamos cada nombre de columna en la lista de cabeceras.
        for (String part : parts) {
            headers.add(part);
        }

        table.setHeaders(headers);

    }
    @Override
    public void processData(String data){
        // Cada línea de datos se separa por comas.
        String[] partes = data.split(",");
        List<Double> valores = new ArrayList<>();

        // Convertimos cada texto numérico a Double.
        for (String parte : partes) {
            valores.add(Double.parseDouble(parte));
        }

        // Con esos valores construimos una fila normal y la añadimos a la tabla.
        Row fila = new Row(valores);
        this.table.addRow(fila);
    }


}
