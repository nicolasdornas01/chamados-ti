# Planejamento

## Requisitos funcionais
- RF01 - Cadastrar chamado (solicitante, descrição e prioridade)
- RF02 - Listar chamados com id e status
- RF03 - Consultar chamado pelo id
- RF04 - Atualizar status
- RF05 - Filtrar por prioridade ou status
- RF06 - Mostrar resumo da quantidade por status

## Regras de negócio
- RN01 - Id gerado automático e único
- RN02 - Solicitante obrigatório, mínimo 3 caracteres (depois do trim)
- RN03 - Descrição obrigatória, mínimo 10 caracteres (depois do trim)
- RN04 - Prioridade só Baixa, Média ou Alta
- RN05 - Status só Aberto, Em atendimento ou Concluído
- RN06 - Data de abertura automática
- RN07 - Todo chamado começa como Aberto
- RN08 - Transição só Aberto -> Em atendimento -> Concluído
- RN09 - Chamado concluído continua aparecendo na consulta
- RN10 - Id que não existe mostra mensagem e não fecha o programa
- RN11 - Dados ficam salvos depois de fechar o programa
- RN12 - SQL com dado do usuário usa parâmetro (?)

## Menu
```
===== TechServ - Chamados de TI =====
1 - Cadastrar chamado
2 - Listar chamados
3 - Consultar chamado
4 - Atualizar status
5 - Filtrar chamados
6 - Resumo por status
0 - Sair
Opção:
```

## Modelo da tabela `chamados`

| Campo | Tipo | Regra |
|---|---|---|
| id | INTEGER | PK, AUTOINCREMENT |
| solicitante | TEXT | NOT NULL, CHECK length(trim) >= 3 |
| descricao | TEXT | NOT NULL, CHECK length(trim) >= 10 |
| prioridade | TEXT | NOT NULL, CHECK IN ('BAIXA','MEDIA','ALTA') |
| status | TEXT | NOT NULL, DEFAULT 'ABERTO', CHECK IN ('ABERTO','EM_ATENDIMENTO','CONCLUIDO') |
| data_abertura | TEXT | NOT NULL, DEFAULT data/hora atual |

Obs: a data é TEXT porque o SQLite não tem tipo DATETIME, ele guarda como texto `AAAA-MM-DD HH:MM:SS`.
As regras ficam no Java (pra mostrar mensagem) e também no banco com CHECK (pra garantir mesmo se inserir por fora).
