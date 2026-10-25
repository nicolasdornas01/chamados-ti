# Relatório de testes

Testei rodando o programa pelo menu e também com a classe `Testes.java` (resultado: 10 passaram, 0 falharam).

| Teste | Resultado obtido | Aprovado / Reprovado | Evidência ou correção |
|---|---|---|---|
| T01 | Chamado #1 cadastrado com status ABERTO e data automática | Aprovado | `Chamado cadastrado! #1 \| Ana Souza \| ALTA \| ABERTO \| 2026-10-25 19:16:57` |
| T02 | Solicitante "     " recusado | Aprovado | `ERRO: Solicitante é obrigatório.` |
| T03 | Descrição "Pane" recusada | Aprovado | `ERRO: Descrição precisa ter pelo menos 10 caracteres.` |
| T04 | Prioridade "urgente" recusada | Aprovado | `ERRO: Prioridade inválida. Use 1 (Baixa), 2 (Média) ou 3 (Alta).` |
| T05 | Id 999 não encontrado, voltou pro menu | Aprovado | `ERRO: Chamado 999 não encontrado.` |
| T06 | Chamado 1 foi pra EM_ATENDIMENTO | Aprovado | `Status atualizado para EM_ATENDIMENTO` |
| T07 | Chamado 3 de ABERTO direto pra CONCLUIDO recusado | Aprovado | `ERRO: Não pode mudar de ABERTO para CONCLUIDO.` |
| T08 | Chamado 2 de EM_ATENDIMENTO pra CONCLUIDO | Aprovado | `Status atualizado para CONCLUIDO` |
| T09 | Fechei e abri de novo, os 4 chamados continuaram lá | Aprovado | Listagem (opção 2) depois de reabrir |
| T10 | Filtro alta mostrou Ana e Diego, resumo 2 abertos / 1 em atendimento / 1 concluído | Aprovado | Opções 5 e 6 |

## Falhas encontradas e correções
1. **Acentos saindo como "?" no console** - o terminal do Windows não estava em UTF-8. Corrigi colocando `chcp 65001` e `-Dfile.encoding=UTF-8` no `executar.bat`.
2. **Digitar letra no lugar do número fechava o programa** - o `Integer.parseInt` dava `NumberFormatException`. Coloquei um `catch` no loop do menu que mostra "Digite só números." e volta pro menu.
3. **Programa ficava em loop infinito quando a entrada acabava** - o `nextLine()` dava erro, caía no catch e repetia. Coloquei `if (!sc.hasNextLine()) break;` antes de ler a opção.
