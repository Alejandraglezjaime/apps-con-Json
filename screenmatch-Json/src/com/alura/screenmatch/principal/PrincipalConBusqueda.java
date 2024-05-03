package com.alura.screenmatch.principal;

import com.alura.screenmatch.excepcion.ErrorEnConversionDeDuracionException;
import com.alura.screenmatch.modelos.Titulo;
import com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalConBusqueda {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner lectura = new Scanner(System.in);

        /* Haremos que lea las películas y las almacene en una lista.
        Para hacer esto, vamos a crear una lista de títulos antes del loop.
        Ese loop podría ser con un while.

        La variable json está en este momento con el contenido devolvido en el body de la respuesta,
        es decir, los datos encontrados referentes a la película buscada.

        El próximo paso es convertir ese contenido textual en un objeto del tipo Titulo, y para esto usaremos
        la biblioteca Gson. Antes de utilizarla, vamos a instanciar una variable a partir de ella:*/

        List<Titulo> titulos = new ArrayList<>();

        /*Con esto tendremos un archivo llamado peliculas.json con todas las películas buscadas.
        Es posible que ese archivo quede en un formato extraño, con todos los datos en la misma línea.
        Gson permite que escribamos ese archivo con una formatación más amigable.
        Para esto solo es necesario incluir el siguiente código, informando que queremos el “pretty printing”.*/

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while(true){

            /*Dentro del while, si el usuario escribe salir vamos a interrumpir la búsqueda.
            Debido a esto es que vamos a comparar si el usuario escribió la palabra salir,
            después de escribir el nombre de la película. */

            System.out.println("Escriba el nombre de una pelicula: ");
            var busqueda = lectura.nextLine();

            /*Después de darle valor a la variable direccion concatenando la url base con el nombre del título
             que se va a buscar, haremos la request http, que está dentro de un bloque try, ya que podría ocurrir
             algún error y lanzarse una excepción, como vimos en el aula:*/

            if(busqueda.equalsIgnoreCase("salir")){
                break;
            }

            String direccion = "https://www.omdbapi.com/?t="+
                    busqueda.replace(" ", "+") +
                    "&apikey=d4d0bf92";

            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);

                TituloOmdb miTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println(miTituloOmdb);

                Titulo miTitulo = new Titulo(miTituloOmdb);
                System.out.println("Titulo ya convertido: " + miTitulo);

                titulos.add(miTitulo);
            }catch (NumberFormatException e){
                System.out.println("Ocurrió un error: ");
                System.out.println(e.getMessage());
            }catch(IllegalArgumentException e){
                System.out.println("Error en la URI, verifique la dirección.");
            }catch (ErrorEnConversionDeDuracionException e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println(titulos);

        FileWriter escritura = new FileWriter("titulos.json");
        escritura.write(gson.toJson(titulos));
        escritura.close();
        System.out.println("Finalizó la ejecución del programa!");
    }
}
