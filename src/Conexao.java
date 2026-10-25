import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Aqui fica a conexão com o banco.
// Usei SQLite porque o banco é só um arquivo (chamados.db), não precisa instalar servidor.
// Como fica salvo em arquivo, os dados continuam lá quando fecha e abre o programa.
public class Conexao {

    static String arquivo = "chamados.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + arquivo);
    }

    // cria a tabela se ainda não existir (mesmo SQL do arquivo sql/criar_banco.sql)
    public static void criarTabela() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS chamados ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "solicitante TEXT NOT NULL CHECK (length(trim(solicitante)) >= 3), "
                + "descricao TEXT NOT NULL CHECK (length(trim(descricao)) >= 10), "
                + "prioridade TEXT NOT NULL CHECK (prioridade IN ('BAIXA','MEDIA','ALTA')), "
                + "status TEXT NOT NULL DEFAULT 'ABERTO' CHECK (status IN ('ABERTO','EM_ATENDIMENTO','CONCLUIDO')), "
                + "data_abertura TEXT NOT NULL DEFAULT (datetime('now','localtime')))";

        try (Connection con = conectar(); Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }
}
