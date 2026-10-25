import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Classe que conversa com o banco (todo SQL fica aqui).
// Em todas as consultas eu uso PreparedStatement com "?" em vez de juntar
// o texto do usuário no SQL. Assim o valor vai separado do comando
// e não tem como fazer SQL Injection.
public class ChamadoDAO {

    // cadastra e devolve o id que o banco gerou
    // status e data eu nem mando, o banco preenche sozinho pelo DEFAULT
    public int inserir(String solicitante, String descricao, String prioridade) throws SQLException {
        String sql = "INSERT INTO chamados (solicitante, descricao, prioridade) VALUES (?, ?, ?)";
        try (Connection con = Conexao.conectar();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, solicitante);
            ps.setString(2, descricao);
            ps.setString(3, prioridade);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
    }

    // busca pelo id, se não achar volta null
    public Chamado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM chamados WHERE id = ?";
        try (Connection con = Conexao.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            List<Chamado> lista = lerResultado(ps.executeQuery());
            return lista.isEmpty() ? null : lista.get(0);
        }
    }

    public List<Chamado> listarTodos() throws SQLException {
        String sql = "SELECT * FROM chamados ORDER BY id";
        try (Connection con = Conexao.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            return lerResultado(ps.executeQuery());
        }
    }

    // serve pro filtro de prioridade e de status
    // o nome da coluna eu escolho no código (nunca vem do usuário), o valor vai no "?"
    public List<Chamado> filtrar(String coluna, String valor) throws SQLException {
        String sql = "SELECT * FROM chamados WHERE " + coluna + " = ? ORDER BY id";
        try (Connection con = Conexao.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor);
            return lerResultado(ps.executeQuery());
        }
    }

    public void atualizarStatus(int id, String novoStatus) throws SQLException {
        String sql = "UPDATE chamados SET status = ? WHERE id = ?";
        try (Connection con = Conexao.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // conta quantos tem em cada status
    public int contarPorStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM chamados WHERE status = ?";
        try (Connection con = Conexao.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    // transforma o resultado do SELECT numa lista de Chamado
    private List<Chamado> lerResultado(ResultSet rs) throws SQLException {
        List<Chamado> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(new Chamado(
                    rs.getInt("id"),
                    rs.getString("solicitante"),
                    rs.getString("descricao"),
                    rs.getString("prioridade"),
                    rs.getString("status"),
                    rs.getString("data_abertura")));
        }
        return lista;
    }
}
