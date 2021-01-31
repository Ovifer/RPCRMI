import java.io.IOException;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        //Conexion con la base de datos PostgreSQL
        Connection connection = null;

        try{
            Class.forName("org.postgresql.Driver");

            //Datos del jbdc señalando cual es la base de datos, usuario y contraseña
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/RPC", "postgres", "admin");

            //Comprobando conexion exitosa
            if(connection != null){
                System.out.println("Conection OK");
            }else{
                System.out.println("Connection Failed");
            }

        }catch (Exception e){
            System.out.println(e);
        }

        InputStreamReader entrada = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(entrada);
        String op = "";
        do {
            System.out.println ("*************************************");
            System.out.println ("***** Bienvenido al sistema ATM *****");
            System.out.println ("*****   1. Apertura de cuenta   *****");
            System.out.println ("*****   2. Segunda opción       *****");
            System.out.println ("*****   3. Tercer opción        *****");
            System.out.println ("*****   4. Salir                *****");
            System.out.println ("*************************************");
            System.out.print ("Elije la opción: ");
            Scanner input = new Scanner(System.in);
            try {
                op = buffer.readLine();
                switch(Integer.parseInt(op)) {
                    case 1: System.out.print("Introduzca su numero de documento de identidad: ");
                            String documento = input.nextLine();

                            //Creamos un SELECT query para buscar los documentos que hay en la base
                            String query = "SELECT document_id FROM usuario";

                            //Creamos un statement
                            Statement st = connection.createStatement();

                            //Ejecutamos el query que creamos en el string
                            ResultSet rs = st.executeQuery(query);

                            //Iteramos entre los valores dados por el SELECT
                            while(rs.next()){
                                //Asignamos cada valor del SELECT encontrado a document_id
                                String document_id = rs.getString("document_id");
                                System.out.println(document_id);

                                //Verificar si el input es igual a la info de la base de datos
                                if(documento.equals(document_id)){
                                    //String con el query
                                    query = "SELECT count(numero) as num FROM account, usuario WHERE fk_usuario = document_id AND document_id = ?;";
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.setString(1, documento);
                                    rs = preparedStatement.executeQuery();
                                    rs.next();

                                    if (rs.getInt("num") == 3){
                                        System.out.println("Usted ya posee el limite de cuentas");
                                        System.out.println(rs.getInt("num"));
                                        preparedStatement.close();
                                        break;
                                    }else{
                                        System.out.println("Crear nueva cuenta");
                                        System.out.print("Nombre de usuario: ");
                                        String usuario = input.nextLine();
                                        System.out.print("Contraseña: ");
                                        String contrasena = input.nextLine();
                                        query = "SELECT username, pass FROM usuario WHERE document_id = ?";
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.setString(1, documento);
                                        rs = preparedStatement.executeQuery();
                                        rs.next();
                                        
                                        
                                        if((rs.getString("username").equals(usuario)) & (rs.getString("pass").equals(contrasena))){
                                            System.out.println("Cuenta creada");
                                        }else{
                                            System.out.println("Las credenciales son invalidas");
                                        }

                                        preparedStatement.close();

                                    }

                                }else if((document_id != documento) & (rs.isLast())){
                                    System.out.println("El numero de documento no existe");
                                    System.out.println("Crear nuevo usuario");
                                    System.out.print("Nombre: ");
                                    String nombre = input.nextLine();
                                    System.out.print("Nombre de usuario: ");
                                    String usuario = input.nextLine();
                                    System.out.print("Contraseña: ");
                                    String contrasena = input.nextLine();

                                    //String con el query de insert
                                    query = "INSERT INTO usuario VALUES (?, ?, ?, ?)";

                                    //Crear  preparedstatement
                                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.setString(1, documento);
                                    preparedStatement.setString(2, nombre);
                                    preparedStatement.setString(3, usuario);
                                    preparedStatement.setString(4, contrasena);

                                    //Ejecutar el preparedstatement
                                    preparedStatement.execute();

                                    System.out.println("Usuario creado satisfactoriamente");
                                    preparedStatement.close();
                                }
                            }

                            //Cerramos el statement
                            rs.close();
                            st.close();
                            
                                    break;
                    case 2: System.out.println("Opción 2 ---");
                                    break;
                    case 3: System.out.println("Opción 3 ---");
                                    break;
                    case 4: System.out.println("Salir ---");
                    connection.close();
                                    break;
                    default : break;
                }
            } catch (IOException e) {
                System.out.println (e.toString());
                input.close();
            }
        } while (!op.equals("4"));
    }
}