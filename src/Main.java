import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:VENDA_CELULARES";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "celulares" se não existir
            criarTabelaCelulares(conn);

            // Inserção de um novo registro na tabela
            System.out.println("Inserindo registro na tabela...");
            inserirCelular(conn, new Celular("iPhone 13", "Apple", 2500.00));
            System.out.println("Registro inserido com sucesso!");

            // Atualização do preço de um celular na tabela
            System.out.println("Atualizando preço na tabela...");
            atualizarPrecoCelular(conn, "Samsung Galaxy S21", 1800.00);
            System.out.println("Preço atualizado com sucesso!");

            // Exclusão de um registro na tabela
            System.out.println("Excluindo registro da tabela...");
            excluirCelular(conn, "OnePlus 8 Pro");
            System.out.println("Registro excluído com sucesso!");

            // Consulta dos registros na tabela
            System.out.println("Consultando registros na tabela...");
            consultarCelulares(conn);

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

    // Método para criar a tabela "celulares" se não existir
    private static void criarTabelaCelulares(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS celulares (" +
                    "id_celular INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "modelo TEXT," +
                    "marca TEXT," +
                    "preco REAL)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir um novo celular na tabela
    private static void inserirCelular(Connection conn, Celular celular) throws SQLException {
        String sql = "INSERT INTO celulares (modelo, marca, preco) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, celular.getModelo());
            stmt.setString(2, celular.getMarca());
            stmt.setDouble(3, celular.getPreco());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o preço de um celular na tabela
    private static void atualizarPrecoCelular(Connection conn, String modelo, double novoPreco) throws SQLException {
        String sql = "UPDATE celulares SET preco=? WHERE modelo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, modelo);
            stmt.executeUpdate();
        }
    }

    // Método para excluir um celular da tabela
    private static void excluirCelular(Connection conn, String modelo) throws SQLException {
        String sql = "DELETE FROM celulares WHERE modelo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            stmt.executeUpdate();
        }
    }

    // Método para consultar os celulares na tabela e imprimir os registros
    private static void consultarCelulares(Connection conn) throws SQLException {
        String sql = "SELECT * FROM celulares";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_celular");
                String modelo = rs.getString("modelo");
                String marca = rs.getString("marca");
                double preco = rs.getDouble("preco");

                System.out.println("ID: " + id);
                System.out.println("Modelo: " + modelo);
                System.out.println("Marca: " + marca);
                System.out.println("Preço: " + preco);
                System.out.println();
            }
        }
    }
}

// Classe Celular
class Celular {
    private String modelo;
    private String marca;
    private double preco;

    public Celular(String modelo, String marca, double preco) {
        this.modelo = modelo;
        this.marca = marca;
        this.preco = preco;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public double getPreco() {
        return preco;
    }
}