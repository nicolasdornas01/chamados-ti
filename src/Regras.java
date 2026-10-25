// Validações e regras de negócio do sistema.
// Se der algo errado eu lanço IllegalArgumentException com a mensagem,
// e lá no Main eu pego e mostro pro atendente sem fechar o programa.
public class Regras {

    // solicitante: obrigatório e com pelo menos 3 letras depois do trim
    // (o trim tira os espaços do começo e do fim, então "     " vira "")
    public static String validarSolicitante(String texto) {
        String limpo = texto.trim();
        if (limpo.isEmpty()) {
            throw new IllegalArgumentException("Solicitante é obrigatório.");
        }
        if (limpo.length() < 3) {
            throw new IllegalArgumentException("Solicitante precisa ter pelo menos 3 caracteres.");
        }
        return limpo;
    }

    // descrição: obrigatória e com pelo menos 10 caracteres depois do trim
    public static String validarDescricao(String texto) {
        String limpo = texto.trim();
        if (limpo.length() < 10) {
            throw new IllegalArgumentException("Descrição precisa ter pelo menos 10 caracteres.");
        }
        return limpo;
    }

    // prioridade: aceito o número da opção ou o nome
    public static String validarPrioridade(String texto) {
        String p = texto.trim().toUpperCase();
        if (p.equals("1") || p.equals("BAIXA")) return "BAIXA";
        if (p.equals("2") || p.equals("MEDIA") || p.equals("MÉDIA")) return "MEDIA";
        if (p.equals("3") || p.equals("ALTA")) return "ALTA";
        throw new IllegalArgumentException("Prioridade inválida. Use 1 (Baixa), 2 (Média) ou 3 (Alta).");
    }

    public static String validarStatus(String texto) {
        String s = texto.trim();
        if (s.equals("1")) return "ABERTO";
        if (s.equals("2")) return "EM_ATENDIMENTO";
        if (s.equals("3")) return "CONCLUIDO";
        throw new IllegalArgumentException("Status inválido. Use 1, 2 ou 3.");
    }

    // regra das transições: só pode ir ABERTO -> EM_ATENDIMENTO -> CONCLUIDO
    // não pode pular etapa nem voltar
    public static void validarTransicao(String atual, String novo) {
        boolean ok = (atual.equals("ABERTO") && novo.equals("EM_ATENDIMENTO"))
                || (atual.equals("EM_ATENDIMENTO") && novo.equals("CONCLUIDO"));
        if (!ok) {
            throw new IllegalArgumentException("Não pode mudar de " + atual + " para " + novo
                    + ". O fluxo é ABERTO -> EM_ATENDIMENTO -> CONCLUIDO.");
        }
    }
}
