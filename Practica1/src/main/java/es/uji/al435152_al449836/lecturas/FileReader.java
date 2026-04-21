package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.Table;

import java.net.URISyntaxException;
import java.util.Scanner;

public abstract class FileReader <T extends Table> extends ReaderTemplate<T>{
    private Scanner scanner;

    public FileReader(String source) {
        super(source);
    }

    public void openSource(String source){
        try{
            String ref = getClass().getClassLoader().getResource(source).toURI().getPath();
            scanner = new Scanner(ref);
        }catch (URISyntaxException e){
            System.err.print("Error: " + e.getMessage());
        }
    }

    @Override
    public void closeSource() {
        scanner.close();
    }

    @Override
    public boolean hasMoreData(){
        return scanner.hasNextLine();
    }
    @Override
    public String getNextData(){
        return scanner.nextLine();
    }


}
