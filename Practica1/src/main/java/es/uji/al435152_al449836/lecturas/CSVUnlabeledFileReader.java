package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.Row;
import es.uji.al435152_al449836.datos.Table;

import java.util.ArrayList;
import java.util.List;

public class CSVUnlabeledFileReader extends FileReader <Table>{
    private String nombrefichero;

    public CSVUnlabeledFileReader(String nombrefichero){
        super(nombrefichero);
        this.nombrefichero = nombrefichero;
    }
    @Override
    public void processHeaders(String headerLine){
        this.table = new Table();

        String[] parts = headerLine.split(",");
        List<String> headers = new ArrayList<>();

        for (String part : parts) {
            headers.add(part);
        }

        table.setHeaders(headers);

    }
    @Override
    public void processData(String data){
        String[] partes = data.split(",");
        List<Double> valores = new ArrayList<>();

        for (String parte : partes) {
            valores.add(Double.parseDouble(parte));
        }

        Row fila = new Row(valores);
        this.table.addRow(fila);
    }


}
