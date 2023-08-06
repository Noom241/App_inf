import com.example.app_inf.AlumnoData;
import com.example.app_inf.AsesorData;
import com.example.app_inf.Horario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLConnection {
    private static Connection connection;

    private MySQLConnection() {}

    public static synchronized Connection getConnection() {
        if (connection == null) {
            String url = "jdbc:mysql://containers-us-west-206.railway.app:7879/railway";
            String user = "root";
            String password = "cPVTtNkTyCZrFOnrqk5Q";

            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static boolean agregarAlumno(AlumnoData alumno) {
        try (Connection connection = getConnection()) {
            String insertQuery = "INSERT INTO Estudiantes (Nombre, Apoderado, TelefonoApoderado, Colegio, Modalidad, Horario) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, alumno.getNombre());
            preparedStatement.setString(2, alumno.getApoderado());
            preparedStatement.setString(3, alumno.getTelefonoApoderado());
            preparedStatement.setString(4, alumno.getColegio());
            preparedStatement.setString(5, alumno.getModalidad());
            preparedStatement.setString(6, alumno.getHorario());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<AsesorData> obtenerAsesores() {
        List<AsesorData> asesores = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String selectQuery = "SELECT * FROM Profesores";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                String telefono = resultSet.getString("Telefono");
                String universidad = resultSet.getString("Universidad");
                String horario = resultSet.getString("Horario_disponible");
                AsesorData asesor = new AsesorData(nombre, telefono, horario, universidad);
                asesores.add(asesor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asesores;
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

    public List<Horario> obtenerHorariosDisponiblesDeAsesor(int idProfesor) {
        List<Horario> horariosDisponibles = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection()) {
            String selectQuery = "SELECT * FROM Horarios WHERE ID IN (SELECT Horario_disponible FROM Profesores WHERE ID = ?)";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idProfesor);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String dia = resultSet.getString("Dia");
                Time horaInicio = resultSet.getTime("Hora_inicio");
                Time horaFin = resultSet.getTime("Hora_fin");
                Horario horario = new Horario(id, dia, horaInicio, horaFin);
                horariosDisponibles.add(horario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horariosDisponibles;
    }

    public boolean registrarAsistenciaEstudiante(int idEstudiante, int idProfesor, Date fecha, boolean asistio) {
        try (Connection connection = MySQLConnection.getConnection()) {
            String insertQuery = "INSERT INTO Asistencia_Estudiantes (ID_estudiante, ID_profesor, Fecha, Asistio) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, idEstudiante);
            preparedStatement.setInt(2, idProfesor);
            preparedStatement.setDate(3, new java.sql.Date(fecha.getTime()));
            preparedStatement.setString(4, asistio ? "Sí" : "No");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Date, String> generarCalendarioAsistenciaEstudiante(int idEstudiante) {
        Map<Date, String> calendarioAsistencia = new HashMap<>();
        try (Connection connection = MySQLConnection.getConnection()) {
            String selectQuery = "SELECT Fecha, Asistio FROM Asistencia_Estudiantes WHERE ID_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date fecha = resultSet.getDate("Fecha");
                String asistio = resultSet.getString("Asistio");
                calendarioAsistencia.put(fecha, asistio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarioAsistencia;
    }

    public List<Horario> obtenerHorariosDisponiblesDeAlumno(int idAlumno) {
        List<Horario> horariosDisponibles = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection()) {
            String selectQuery = "SELECT * FROM Horarios WHERE ID IN (SELECT Horario_elegido FROM Estudiantes WHERE ID = ?)";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idAlumno);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String dia = resultSet.getString("Dia");
                Time horaInicio = resultSet.getTime("Hora_inicio");
                Time horaFin = resultSet.getTime("Hora_fin");
                Horario horario = new Horario(id, dia, horaInicio, horaFin);
                horariosDisponibles.add(horario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horariosDisponibles;
    }

    public boolean asignarAsesorAAlumno(int idAlumno, int idAsesor, int idHorario) {
        try (Connection connection = MySQLConnection.getConnection()) {
            // Verificar disponibilidad del asesor y horario aquí

            String updateQuery = "UPDATE Estudiantes SET ID_asesor = ?, Horario_elegido = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, idAsesor);
            preparedStatement.setInt(2, idHorario);
            preparedStatement.setInt(3, idAlumno);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Date, String> generarCalendarioAsistenciaAsesor(int idAsesor) {
        Map<Date, String> calendarioAsistencia = new HashMap<>();
        try (Connection connection = MySQLConnection.getConnection()) {
            String selectQuery = "SELECT Fecha, Asistio FROM Asistencia_Profesores WHERE ID_profesor = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idAsesor);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date fecha = resultSet.getDate("Fecha");
                String asistio = resultSet.getString("Asistio");
                calendarioAsistencia.put(fecha, asistio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendarioAsistencia;
    }

    public boolean tomarAsistenciaAsesor(int idAsesor, Date fecha, boolean asistio) {
        try (Connection connection = MySQLConnection.getConnection()) {
            String insertQuery = "INSERT INTO Asistencia_Profesores (ID_profesor, Fecha, Asistio) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, idAsesor);
            preparedStatement.setDate(2, new java.sql.Date(fecha.getTime()));
            preparedStatement.setString(3, asistio ? "Sí" : "No");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static AsesorData obtenerAsesorPorNombre(String nombre) {
        Connection connection = null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT * FROM Profesores WHERE Nombre = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String telefono = resultSet.getString("Telefono");
                String universidad = resultSet.getString("Universidad");
                String horario = resultSet.getString("Horario_disponible");
                return new AsesorData(nombre, telefono, horario, universidad);
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


    public static List<String> obtenerNombresDeProfesores() {
        List<String> nombresProfesores = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String selectQuery = "SELECT Nombre FROM Profesores"; // Cambiar a la tabla correcta si es necesario
            PreparedStatement statement = connection.prepareStatement(selectQuery);
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


}
