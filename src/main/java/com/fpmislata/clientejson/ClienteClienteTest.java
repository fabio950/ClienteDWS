/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.clientejson;

import com.fpmislata.domain.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author alumno
 */
public class ClienteClienteTest {
    public static void main(String[] args) throws IOException {
        //Listamos los clientes
        System.out.println("Listado de clientes");
        //List<Cliente> lista = getListCliente("http://localhost:8080/ProyectoFinal20162017-web/webservice/ClientesService/Clientes");
        //for (Cliente cliente : lista) {
            //System.out.println(cliente.toString());
        //}
        System.out.println("----------------------------\n");
        
        //Recuperamos un cliente
        System.out.println("Recuperando un cliente en concrero del sistema");
        //Cliente cliente = getCliente("http://localhost:8080/ProyectoFinal20162017-web/webservice/ClientesService/Clientes/FindById/1");
        //System.out.println("El cliente recuperado es: " + cliente.toString());
        System.out.println("----------------------------\n");
        
        //Anyadir cliente
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("NewCliente");
        nuevoCliente.setApellidos("NewApellidos");
        nuevoCliente.setNif("NewNif");
        nuevoCliente.setDireccion("NewDireccion");
        nuevoCliente.setPoblacion("NiPu");
        nuevoCliente.setProvincia("Chorrilandia");
        nuevoCliente.setCodigopostal("45678");
        nuevoCliente.setTelefono("961325678");
        addCliente("http://localhost:8080/ProyectoFinal20162017-web/webservice/ClientesService/Clientes/add/", nuevoCliente);
        
    }
    
    private static List<Cliente> getListCliente(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("accept", "application/json; charset=UTF-8");
        HttpResponse response = httpClient.execute(httpGet);
        String lista = readObject(response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Cliente>>() {
        }.getType();
        return gson.fromJson(lista, type);
    }
    
    private static Cliente getCliente(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("accept", "application/json; charset=UTF-8");
        HttpResponse response = httpClient.execute(httpGet);
        String cliente = readObject(response);
        Gson gson = new Gson();
        return gson.fromJson(cliente, Cliente.class);
    }
    
    // Añadimos una persona al sistema
    private static Cliente addCliente(String url, Cliente cliente) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("accept", "application/json; charset=UTF-8");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(cliente);
        StringEntity input = new StringEntity(jsonString, "UTF-8");
        input.setContentType("application/json");
        httpPost.setEntity(input);
        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        String personaResult = readObject(response);
        return gson.fromJson(personaResult, Cliente.class);
    }
    
    private static String readObject(HttpResponse httpResponse) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            char[] dataLength = new char[1024];
            int read;
            while ((read = bufferedReader.read(dataLength)) != -1) {
                buffer.append(dataLength, 0, read);
            }
            System.out.println(buffer.toString());
            return buffer.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }
}
