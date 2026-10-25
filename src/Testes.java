import java.io.File;

// Roda os testes do plano (T01 a T10) num banco separado, pra não bagunçar o chamados.db
public class Testes {

    static ChamadoDAO dao = new ChamadoDAO();
    static int passou = 0, falhou = 0;

    public static void main(String[] args) throws Exception {
        Conexao.arquivo = "teste.db";
        new File("teste.db").delete();
        Conexao.criarTabela();

        // T01 - cadastro válido
        int id = dao.inserir("Ana Souza", "Computador não inicia ao pressionar o botão.", "ALTA");
        Chamado c = dao.buscarPorId(id);
        verificar("T01 cadastro válido", c.status.equals("ABERTO") && c.dataAbertura != null);

        // T02 a T04 - entradas inválidas tem que dar erro
        verificar("T02 solicitante só espaços", deuErro(() -> Regras.validarSolicitante("     ")));
        verificar("T03 descrição curta", deuErro(() -> Regras.validarDescricao("Pane")));
        verificar("T04 prioridade inválida", deuErro(() -> Regras.validarPrioridade("urgente")));

        // T05 - id que não existe
        verificar("T05 id inexistente", dao.buscarPorId(999) == null);

        // T06 - aberto -> em atendimento
        Regras.validarTransicao("ABERTO", "EM_ATENDIMENTO");
        dao.atualizarStatus(id, "EM_ATENDIMENTO");
        verificar("T06 aberto -> em atendimento", dao.buscarPorId(id).status.equals("EM_ATENDIMENTO"));

        // T07 - não pode pular direto pra concluído
        verificar("T07 aberto -> concluído recusado",
                deuErro(() -> Regras.validarTransicao("ABERTO", "CONCLUIDO")));

        // T08 - em atendimento -> concluído
        Regras.validarTransicao("EM_ATENDIMENTO", "CONCLUIDO");
        dao.atualizarStatus(id, "CONCLUIDO");
        verificar("T08 em atendimento -> concluído", dao.buscarPorId(id).status.equals("CONCLUIDO"));

        // T09 - dados continuam no arquivo (cria um DAO novo, como se reabrisse)
        verificar("T09 dados preservados", new ChamadoDAO().listarTodos().size() == 1);

        // T10 - dados da demonstração e resumo
        new File("teste.db").delete();
        Conexao.criarTabela();
        int ana = dao.inserir("Ana Souza", "Computador não inicia ao pressionar o botão.", "ALTA");
        int bruno = dao.inserir("Bruno Lima", "Impressora do setor não imprime documentos.", "MEDIA");
        dao.inserir("Carla Santos", "Solicitação de instalação de editor de texto.", "BAIXA");
        dao.inserir("Diego Alves", "Sistema de estoque apresenta erro no acesso.", "ALTA");
        dao.atualizarStatus(ana, "EM_ATENDIMENTO");
        dao.atualizarStatus(bruno, "EM_ATENDIMENTO");
        dao.atualizarStatus(bruno, "CONCLUIDO");
        verificar("T10 filtro alta + resumo 2/1/1",
                dao.filtrar("prioridade", "ALTA").size() == 2
                && dao.contarPorStatus("ABERTO") == 2
                && dao.contarPorStatus("EM_ATENDIMENTO") == 1
                && dao.contarPorStatus("CONCLUIDO") == 1);

        System.out.println("\nPassaram: " + passou + " | Falharam: " + falhou);
        new File("teste.db").delete();
    }

    static void verificar(String nome, boolean ok) {
        System.out.println((ok ? "[OK]    " : "[FALHA] ") + nome);
        if (ok) passou++; else falhou++;
    }

    // devolve true se o código lançar erro de validação
    static boolean deuErro(Runnable codigo) {
        try {
            codigo.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
