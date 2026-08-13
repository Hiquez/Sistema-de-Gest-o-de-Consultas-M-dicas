package Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum;

public enum StatusConsulta {
    AGENDADA("Agendada"),
    CONFIRMADA("Confirmada"),
    EM_ATENDIMENTO("Em Atendimento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private String descricao;
    private StatusConsulta(String descricao) {
        this.descricao = descricao;
    }
}
