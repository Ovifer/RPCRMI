import java.io.IOException;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.getConnection();

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
                            String query = "SELECT document_id FROM userr";

                            //Iteramos entre los valores dados por el SELECT
                            while(databaseConnection.loop(query)){
                                //Asignamos cada valor del SELECT encontrado a document_id
                                System.out.println(databaseConnection.getResultSet(query).getString("document_id"));

                                //Verificar si el input es igual a la info de la base de datos
                                if(documento.equals(databaseConnection.getResultSet(query).getString("document_id"))){
                                    //String con el query
                                    query = "SELECT count(number) as num FROM account, userr WHERE fk_usuario = document_id AND document_id = " + documento + ");";

                                    if (databaseConnection.getNum(query) == 3){
                                        System.out.println("Usted ya posee el limite de cuentas");
                                        break;
                                    }else{
                                        System.out.println("Crear nueva cuenta");
                                        System.out.print("Nombre de usuario: ");
                                        String usuario = input.nextLine();
                                        System.out.print("Contraseña: ");
                                        String contrasena = input.nextLine();
                                        query = "SELECT username, password FROM userr WHERE document_id = " + documento + ");";                                     
                                        
                                        if((databaseConnection.getUsername(query).equals(usuario)) & (databaseConnection.getPass(query).equals(contrasena))){
                                            System.out.println("Cuenta creada");
                                        }else{
                                            System.out.println("Las credenciales son invalidas");
                                        }

                                    }

                                }else if((databaseConnection.getResultSet(query).getString("document_id") != documento) & (databaseConnection.getResultSet(query).isLast())){
                                    System.out.println("El numero de documento no existe");
                                    System.out.println("Crear nuevo usuario");
                                    System.out.print("Nombre: ");
                                    String nombre = input.nextLine();
                                    System.out.print("Nombre de usuario: ");
                                    String usuario = input.nextLine();
                                    System.out.print("Contraseña: ");
                                    String contrasena = input.nextLine();

                                    //String con el query de insert
                                    query = "INSERT INTO userr VALUES (?, ?, ?, ?);";
                                    databaseConnection.getPreparedStatement(query, documento, nombre, usuario, contrasena);

                                    System.out.println("Usuario creado satisfactoriamente");
                                }
                                
                            }
                            
                                    break;
                    case 2: System.out.println("Opción 2 ---");
                                    break;
                    case 3: System.out.println("Opción 3 ---");
                                    break;
                    case 4: System.out.println("Salir ---");
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