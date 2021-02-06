import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {
    
    public Connection connection = null;

    public Connection getConnection() throws ClassNotFoundException {
        if (connection == null){
            try{
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PracticaRMI", "postgres", "admin");
                System.out.println("Conexion Exitosa");
                System.out.println(connection);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return connection;
    }

    private Statement statement;
    
    private ResultSet resultSet;

    public ResultSet getResultSet(String query) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    private PreparedStatement preparedStatement;

    public void getPreparedStatement(String query, String documento, String nombre, String usuario, String contrasena) throws SQLException {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, documento);
        preparedStatement.setString(2, nombre);
        preparedStatement.setString(3, usuario);
        preparedStatement.setString(4, contrasena);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public Integer getNum(String query) throws SQLException{
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("number");
    }

    public String getUsername(String query) throws SQLException{
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getString("username");
    }

    public String getPass(String query) throws SQLException{
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getString("password");
    }

    public Boolean loop(String query) throws SQLException{
        statement = connection.createStatement();
        return resultSet.next();
    }
}