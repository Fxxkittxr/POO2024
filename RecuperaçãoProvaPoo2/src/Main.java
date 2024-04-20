import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:RECUPERACAO";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "turmas" se não existir
            criarTabelaTurmas(conn);

            // Inserção de um novo registro na tabela
            System.out.println("Inserindo registro na tabela...");
            inserirTurma(conn, new Turma("Turma 3", "Medicina", "Bruna"));
            System.out.println("Registro inserido com sucesso!");

            // Atualização de um registro na tabela
            System.out.println("Atualizando registro na tabela...");
            atualizarCursoTurma(conn, "Turma A", "Ciência da Computação");
            System.out.println("Registro atualizado com sucesso!");

            // Exclusão de um registro na tabela
            System.out.println("Excluindo registro da tabela...");
            excluirTurma(conn, "Turma A");
            System.out.println("Registro excluído com sucesso!");

            // Consulta dos registros na tabela
            System.out.println("Consultando registros na tabela...");
            consultarTurmas(conn);

        } catch (SQLException se) {
            // Tratamento de erros do JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Tratamento de outros erros
            e.printStackTrace();
        } finally {
            // Fechando os recursos em um bloco finally
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                // Nada a fazer
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

    // Método para criar a tabela "turmas" se não existir
    private static void criarTabelaTurmas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS turmas (" +
                    "id_turma INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome_turma TEXT," +
                    "curso TEXT," +
                    "professor TEXT)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir uma nova turma na tabela
    private static void inserirTurma(Connection conn, Turma turma) throws SQLException {
        String sql = "INSERT INTO turmas (nome_turma, curso, professor) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, turma.getNomeTurma());
            stmt.setString(2, turma.getCurso());
            stmt.setString(3, turma.getProfessor());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o curso de uma turma na tabela
    private static void atualizarCursoTurma(Connection conn, String nomeTurma, String novoCurso) throws SQLException {
        String sql = "UPDATE turmas SET curso=? WHERE nome_turma=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoCurso);
            stmt.setString(2, nomeTurma);
            stmt.executeUpdate();
        }
    }

    // Método para excluir uma turma da tabela
    private static void excluirTurma(Connection conn, String nomeTurma) throws SQLException {
        String sql = "DELETE FROM turmas WHERE nome_turma=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeTurma);
            stmt.executeUpdate();
        }
    }

    // Método para consultar as turmas na tabela e imprimir os registros
    private static void consultarTurmas(Connection conn) throws SQLException {
        String sql = "SELECT * FROM turmas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_turma");
                String nomeTurma = rs.getString("nome_turma");
                String curso = rs.getString("curso");
                String professor = rs.getString("professor");

                System.out.println("ID: " + id);
                System.out.println("Nome da Turma: " + nomeTurma);
                System.out.println("Curso: " + curso);
                System.out.println("Professor: " + professor);
                System.out.println();
            }
        }
    }
}

// Classe Turma
class Turma {
    private String nomeTurma;
    private String curso;
    private String professor;

    public Turma(String nomeTurma, String curso, String professor) {
        this.nomeTurma = nomeTurma;
        this.curso = curso;
        this.professor = professor;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public String getCurso() {
        return curso;
    }

    public String getProfessor() {
        return professor;
    }
}
