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
            String insertQuery = "INSERT INTO Estudiantes (Nombre, Apoderado, Telefono_apoderado, Colegio, Modalidad, Horario_elegido) VALUES (?, ?, ?, ?, ?, ?)";
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

    public static boolean agregarAsesor(AsesorData asesor) {
        try (Connection connection = getConnection()) {
            String insertQuery = "INSERT INTO Profesores (Nombre, Telefono, Universidad, Horario_disponible) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, asesor.getNombre());
            preparedStatement.setString(2, asesor.getTelefono());
            preparedStatement.setString(3, asesor.getUniversidad());
            preparedStatement.setString(4, asesor.getHorario());
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
            String selectQuery = "SELECT * FROM Estudiantes WHERE Nombre = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String apoderado = resultSet.getString("Apoderado");
                String telefonoApoderado = resultSet.getString("Telefono_apoderado");
                String colegio = resultSet.getString("Colegio");
                String modalidad = resultSet.getString("Modalidad");
                String horario = resultSet.getString("Horario_elegido"); // Cambiar a la columna correcta
                String paquete = resultSet.getString("Paquete_elegido"); // Agregar columna si es necesario
                // Obtener días y asesores, reemplazar los nombres de columna adecuados
                List<String> dias = obtenerDiasDeEstudiante(resultSet.getInt("ID"));
                Map<String, String> asesores = obtenerAsesoresDeEstudiante(resultSet.getInt("ID"));
                return new AlumnoData(nombre, apoderado, telefonoApoderado, colegio, modalidad, horario, paquete, dias, asesores);
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
            String selectQuery = "SELECT Nombre FROM Estudiantes"; // Cambiar a la tabla correcta si es necesario
            PreparedStatement statement = connection.prepareStatement(selectQuery);
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

    private static List<String> obtenerDiasDeEstudiante(int idEstudiante) {
        List<String> dias = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String selectQuery = "SELECT Dia FROM DiasEstudiante WHERE ID_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String dia = resultSet.getString("Dia");
                dias.add(dia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dias;
    }

    private static Map<String, String> obtenerAsesoresDeEstudiante(int idEstudiante) {
        Map<String, String> asesores = new HashMap<>();
        try (Connection connection = getConnection()) {
            String selectQuery = "SELECT Nombre, Asesor FROM AsesoresEstudiante WHERE ID_estudiante = ?";
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, idEstudiante);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombreAsesor = resultSet.getString("Nombre");
                String comentario = resultSet.getString("Asesor");
                asesores.put(nombreAsesor, comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asesores;
    }

    public static boolean borrarAsesor(String nombre) {
        try (Connection connection = getConnection()) {
            String deleteQuery = "DELETE FROM Profesores WHERE Nombre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, nombre);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean borrarAlumno(String nombre) {
        try (Connection connection = getConnection()) {
            String deleteQuery = "DELETE FROM Estudiantes WHERE Nombre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, nombre);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void borrarAlumnoEnSegundoPlano(String nombreAlumno, final OnAlumnoBorradoListener listener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    return borrarAlumno(nombreAlumno);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean borradoExitoso) {
                if (listener != null) {
                    listener.onAlumnoBorrado(borradoExitoso);
                }
            }
        }.execute();
    }

    public interface OnAlumnoBorradoListener {
        void onAlumnoBorrado(boolean borradoExitoso);
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

    public static void obtenerAsistenciaDeEstudianteAsync(int idEstudiante, OnAsistenciaObtenidaListener listener) {
        new AsyncTask<Integer, Void, List<Pair<Date, Boolean>>>() {
            @Override
            protected List<Pair<Date, Boolean>> doInBackground(Integer... params) {
                int idEstudiante = params[0];
                List<Pair<Date, Boolean>> asistenciaEstudiante = new ArrayList<>();

                try (Connection connection = getConnection()) {
                    String callProcedure = "{ CALL ObtenerFechasYAsistioPorEstudiante(?) }";
                    try (CallableStatement statement = connection.prepareCall(callProcedure)) {
                        statement.setInt(1, idEstudiante);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                Date fecha = resultSet.getDate("Fecha");
                                boolean asistio = resultSet.getBoolean("Asistio");
                                asistenciaEstudiante.add(new Pair<>(fecha, asistio));
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return asistenciaEstudiante;
            }

            @Override
            protected void onPostExecute(List<Pair<Date, Boolean>> result) {
                super.onPostExecute(result);
                listener.onAsistenciaObtenida(result);
            }
        }.execute(idEstudiante);
    }

    public interface OnAsistenciaObtenidaListener {
        void onAsistenciaObtenida(List<Pair<Date, Boolean>> asistencia);
    }




}