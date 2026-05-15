package es.uji.al435152_al449836.modelo.lecturas;

import es.uji.al435152_al449836.modelo.datos.Row;
import es.uji.al435152_al449836.modelo.datos.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Lector de CSV sin columna de etiqueta.
 *
 * <p>Se utiliza con los datasets que alimentan a KMeans, donde todas las
 * columnas representan caracteristicas numericas y no existe una clase final.
 */
public class CSVUnlabeledFileReader extends FileReader<Table> {
    private String nombrefichero;

    /**
     * Construye el lector para el recurso indicado.
     */
    public CSVUnlabeledFileReader(String nombrefichero) {
        super(nombrefichero);
        this.nombrefichero = nombrefichero;
    }

    /**
     * Interpreta la primera linea como lista de cabeceras.
     */
    @Override
    public void processHeaders(String headerLine) {
        this.table = new Table();

        String[] parts = headerLine.split(",");
        List<String> headers = new ArrayList<>();

        for (String part : parts) {
            headers.add(part);
        }

        table.setHeaders(headers);
    }

    /**
     * Convierte una linea CSV en una fila numerica y la anade a la tabla.
     */
    @Override
    public void processData(String data) {
        String[] partes = data.split(",");
        List<Double> valores = new ArrayList<>();

        for (String parte : partes) {
            valores.add(Double.parseDouble(parte));
        }

        Row fila = new Row(valores);
        this.table.addRow(fila);
    }
}
