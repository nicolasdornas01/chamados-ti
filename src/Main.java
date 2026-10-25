import java.util.List;
import java.util.Scanner;

// Menu do sistema, é aqui que o atendente usa
public class Main {

    static Scanner sc = new Scanner(System.in);
    static ChamadoDAO dao = new ChamadoDAO();

    public static void main(String[] args) {
        try {
            Conexao.criarTabela();
        } catch (Exception e) {
            System.out.println("Erro ao abrir o banco: " + e.getMessage());
            return;
        }

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== TechServ - Chamados de TI =====");
            System.out.println("1 - Cadastrar chamado");
            System.out.println("2 - Listar chamados");
            System.out.println("3 - Consultar chamado");
            System.out.println("4 - Atualizar status");
            System.out.println("5 - Filtrar chamados");
            System.out.println("6 - Resumo por status");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            if (!sc.hasNextLine()) break;   // se a entrada acabar, sai do loop

            // try/catch em volta de tudo: se der erro mostra a mensagem
            // e volta pro menu, o programa só fecha quando digitar 0
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
                switch (opcao) {
                    case 1: cadastrar(); break;
                    case 2: listar(dao.listarTodos()); break;
                    case 3: consultar(); break;
                    case 4: atualizarStatus(); break;
                    case 5: filtrar(); break;
                    case 6: resumo(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite só números.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERRO: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro no banco: " + e.getMessage());
            }
        }
    }

    static void cadastrar() throws Exception {
        // valido cada campo logo depois de digitar
        System.out.print("Solicitante: ");
        String solicitante = Regras.validarSolicitante(sc.nextLine());

        System.out.print("Descrição: ");
        String descricao = Regras.validarDescricao(sc.nextLine());

        System.out.print("Prioridade (1-Baixa, 2-Média, 3-Alta): ");
        String prioridade = Regras.validarPrioridade(sc.nextLine());

        int id = dao.inserir(solicitante, descricao, prioridade);
        Chamado c = dao.buscarPorId(id);
        System.out.println("Chamado cadastrado! " + c);
    }

    static void listar(List<Chamado> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum chamado encontrado.");
            return;
        }
        System.out.println("ID | Solicitante | Prioridade | Status | Abertura | Descrição");
        for (Chamado c : lista) {
            System.out.println(c);
        }
    }

    // pede o id e já busca, se não existir avisa (é usado no consultar e no atualizar)
    static Chamado pedirChamado() throws Exception {
        System.out.print("ID do chamado: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Chamado c = dao.buscarPorId(id);
        if (c == null) {
            throw new IllegalArgumentException("Chamado " + id + " não encontrado.");
        }
        return c;
    }

    static void consultar() throws Exception {
        System.out.println(pedirChamado());
    }

    static void atualizarStatus() throws Exception {
        Chamado c = pedirChamado();
        System.out.println("Status atual: " + c.status);
        System.out.print("Novo status (1-Aberto, 2-Em atendimento, 3-Concluído): ");
        String novo = Regras.validarStatus(sc.nextLine());

        Regras.validarTransicao(c.status, novo);   // se não puder, lança erro aqui
        dao.atualizarStatus(c.id, novo);
        System.out.println("Status atualizado para " + novo);
    }

    static void filtrar() throws Exception {
        System.out.print("Filtrar por: 1-Prioridade  2-Status: ");
        String tipo = sc.nextLine().trim();

        if (tipo.equals("1")) {
            System.out.print("Prioridade (1-Baixa, 2-Média, 3-Alta): ");
            listar(dao.filtrar("prioridade", Regras.validarPrioridade(sc.nextLine())));
        } else if (tipo.equals("2")) {
            System.out.print("Status (1-Aberto, 2-Em atendimento, 3-Concluído): ");
            listar(dao.filtrar("status", Regras.validarStatus(sc.nextLine())));
        } else {
            System.out.println("Opção inválida.");
        }
    }

    static void resumo() throws Exception {
        System.out.println("Abertos: " + dao.contarPorStatus("ABERTO"));
        System.out.println("Em atendimento: " + dao.contarPorStatus("EM_ATENDIMENTO"));
        System.out.println("Concluídos: " + dao.contarPorStatus("CONCLUIDO"));
    }
}
