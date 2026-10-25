-- Script do banco (SQLite)
-- O programa já roda esse CREATE sozinho quando abre, mas deixei aqui separado também.
-- No SQLite o banco é o próprio arquivo chamados.db

CREATE TABLE IF NOT EXISTS chamados (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,                       -- gerado sozinho e único
    solicitante   TEXT NOT NULL CHECK (length(trim(solicitante)) >= 3),    -- mínimo 3 sem os espaços
    descricao     TEXT NOT NULL CHECK (length(trim(descricao)) >= 10),     -- mínimo 10 sem os espaços
    prioridade    TEXT NOT NULL CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA')),
    status        TEXT NOT NULL DEFAULT 'ABERTO'                           -- todo chamado nasce aberto
                  CHECK (status IN ('ABERTO', 'EM_ATENDIMENTO', 'CONCLUIDO')),
    data_abertura TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))     -- data automática
);
