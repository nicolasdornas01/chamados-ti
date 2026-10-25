// Classe que representa um chamado (cada objeto = uma linha da tabela chamados)
public class Chamado {

    int id;
    String solicitante;
    String descricao;
    String prioridade;   // BAIXA, MEDIA ou ALTA
    String status;       // ABERTO, EM_ATENDIMENTO ou CONCLUIDO
    String dataAbertura;

    public Chamado(int id, String solicitante, String descricao,
                   String prioridade, String status, String dataAbertura) {
        this.id = id;
        this.solicitante = solicitante;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataAbertura = dataAbertura;
    }

    // usado na hora de listar, pra cada chamado sair numa linha só
    @Override
    public String toString() {
        return "#" + id + " | " + solicitante + " | " + prioridade + " | "
                + status + " | " + dataAbertura + " | " + descricao;
    }
}
