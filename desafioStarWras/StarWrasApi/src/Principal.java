import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner lectura =  new Scanner(System.in);
        ConsultaPelicula consulta = new ConsultaPelicula();
        System.out.println("Escriba el número de película de Star Wars que quiere consultar : ");
        try {
            var numeroDePelicula = Integer.valueOf(lectura.nextLine());
            Pelicula pelicula = consulta.buscaPelicula(numeroDePelicula);
            System.out.println(pelicula);
            GeneradorDelArchivo generador = new GeneradorDelArchivo();
            generador.guardarJson(pelicula);
        } catch (NumberFormatException e){
            System.out.println("Número no encontrado "+e.getMessage());
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            System.out.println("Finalizando la aplicación");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
