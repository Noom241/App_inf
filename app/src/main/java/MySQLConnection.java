import android.os.AsyncTask;

import com.example.app_inf.data.AlumnoData;
import com.example.app_inf.data.AsesorData;
import com.example.app_inf.data.Horario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.AsyncTask;


import kotlin.Pair;

public class MySQLConnection {
    private static Connection connection;

    private MySQLConnection() {}

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://containers-us-west-206.railway.app:7879/railway";
        String user = "root";
        String password = "cPVTtNkTyCZrFOnrqk5Q";

        return DriverManager.getConnection(url, user, password);
    }

    public static boolean agregarAlumno(AlumnoData alumno) {
        try (Connection connection = getConnection()) {
            String insertQuery = "CALL CrearAlumno(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, alumno.getNombre());
            preparedStatement.setString(2, alumno.getApoderado());
            preparedStatement.setString(3, alumno.getTelefonoApoderado());
            preparedStatement.setString(4, alumno.getColegio());
            preparedStatement.setString(5, alumno.getModalidad());
            preparedStatement.setInt(6, alumno.getHorario());
            preparedStatement.setString(7, alumno.getPaquete_elegido());
            preparedStatement.setString(8, alumno.getHorario_semana());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean actualizarAsistencia(String alumnoNombre, String fecha, boolean asistio) {
        try (Connection connection = getConnection()) {
            String updateQuery = "UPDATE asistencia SET Asistio = ? WHERE AlumnoNombre = ? AND Fecha = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, asistio);
            preparedStatement.setString(2, alumnoNombre);
            preparedStatement.setString(3, fecha);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





    public static AsesorData obtenerAsesorPorNombre(String nombre) {
        AsesorData asesor = null;
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerAsesorPorNombre(?) }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String telefono = resultSet.getString("Telefono");
                String universidad = resultSet.getString("Universidad");
                String horario = resultSet.getString("Horario_semana");
                asesor = new AsesorData(nombre, telefono, universidad, horario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asesor;
    }



    public static List<String> obtenerNombresDeProfesores() {
        List<String> nombresProfesores = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerNombresDeProfesores() }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                nombresProfesores.add(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombresProfesores;
    }


    public static boolean agregarAsesor(AsesorData asesor) {
        try (Connection connection = getConnection()) {
            String insertQuery = "CALL CrearProfesor(?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, asesor.getNombre());
            preparedStatement.setString(2, asesor.getTelefono());
            preparedStatement.setString(3, asesor.getUniversidad());
            preparedStatement.setString(4, asesor.getHorario_semana());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static AlumnoData obtenerEstudiantePorNombre(String nombre) {
        Connection connection = null;
        try {
            connection = getConnection();
            String selectQuery = "CALL ObtenerEstudiantePorNombre(?)";
            CallableStatement statement = connection.prepareCall(selectQuery);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String apoderado = resultSet.getString("Apoderado");
                String telefonoApoderado = resultSet.getString("Telefono_apoderado");
                String colegio = resultSet.getString("Colegio");
                String modalidad = resultSet.getString("Modalidad");
                int horario = resultSet.getInt("Horario_elegido");
                String paquete = resultSet.getString("Paquete_elegido");
                String horarioSemana = resultSet.getString("Horario_semana");

                return new AlumnoData(nombre, apoderado, telefonoApoderado, colegio, modalidad, horario, paquete, horarioSemana);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public static List<String> obtenerNombresDeAlumnos() {
        List<String> nombresAlumnos = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerNombresDeAlumnos() }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                nombresAlumnos.add(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombresAlumnos;
    }



    public static boolean borrarAsesor(int id) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL BorrarAsesorPorID(?) }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean borrarAlumno(String nombre) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL BorrarAlumnoPorNombre(?) }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            statement.setString(1, nombre);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public static List<String> obtenerProfesoresDisponibles(String dia, int horarioElegido) {
        List<String> nombresProfesores = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String storedProcedureCall = "{ CALL ObtenerProfesoresDisponibles(?, ?) }";
            try (PreparedStatement statement = connection.prepareCall(storedProcedureCall)) {
                statement.setString(1, dia);
                statement.setInt(2, horarioElegido);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String nombreProfesor = resultSet.getString("Nombre");
                    nombresProfesores.add(nombreProfesor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombresProfesores;
    }

    public static List<Pair<Date, String>> obtenerAsistenciaDeEstudiante(int idEstudiante) {
        List<Pair<Date, String>> asistenciaEstudiante = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerFechasYAsistioPorEstudiante(?) }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date fecha = resultSet.getDate("Fecha");
                String asistencia = resultSet.getString("Asistio");
                asistenciaEstudiante.add(new Pair<>(fecha, asistencia));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asistenciaEstudiante;
    }



    public static int obtenerIDPorNombre(String nombre, String tabla) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerIDPorNombre(?, ?, ?) }";
            try (CallableStatement statement = connection.prepareCall(callProcedure)) {
                statement.setString(1, nombre);
                statement.setString(2, tabla);
                statement.registerOutParameter(3, Types.INTEGER);
                statement.executeUpdate();
                return statement.getInt(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Manejar el error adecuadamente en tu aplicación
        }
    }

    public static boolean registrarAsistenciaEstudiante(Date fecha, int idEstudiante, int idProfesor, String asistio) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL Registrar_Asistencia(?, ?, ?, ?) }";
            try (CallableStatement statement = connection.prepareCall(callProcedure)) {
                statement.setDate(1, new java.sql.Date(fecha.getTime()));
                statement.setInt(2, idEstudiante);
                statement.setInt(3, idProfesor);
                statement.setString(4, asistio);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean actualizarProfesor(int profesorID, String nuevoNombre, String nuevoTelefono, String nuevaUniversidad, String nuevoHorarioSemana) {
        try (Connection connection = getConnection()) {
            String storedProcedureCall = "{ CALL ActualizarProfesor(?, ?, ?, ?, ?) }";
            try (PreparedStatement statement = connection.prepareCall(storedProcedureCall)) {
                statement.setInt(1, profesorID);
                statement.setString(2, nuevoNombre);
                statement.setString(3, nuevoTelefono);
                statement.setString(4, nuevaUniversidad);
                statement.setString(5, nuevoHorarioSemana);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarAlumno(int id, AlumnoData alumno) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ActualizarAlumno(?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement statement = connection.prepareCall(callProcedure);
            statement.setInt(1, id);
            statement.setString(2, alumno.getNombre());
            statement.setString(3, alumno.getApoderado());
            statement.setString(4, alumno.getTelefonoApoderado());
            statement.setString(5, alumno.getColegio());
            statement.setString(6, alumno.getModalidad());
            statement.setInt(7, alumno.getHorario());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String obtenerNombreEstudiantePorID(int idEstudiante) {
        try (Connection connection = getConnection()) {
            String callProcedure = "{ CALL ObtenerNombreEstudiantePorID(?, ?) }";
            try (CallableStatement statement = connection.prepareCall(callProcedure)) {
                statement.setInt(1, idEstudiante);
                statement.registerOutParameter(2, Types.VARCHAR);
                statement.executeUpdate();
                return statement.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Manejar el error adecuadamente en tu aplicación
        }
    }






}