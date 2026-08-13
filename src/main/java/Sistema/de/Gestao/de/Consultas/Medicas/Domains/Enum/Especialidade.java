package Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum;

public enum Especialidade {
    CARDIOLOGIA("Cardiologia"),
    DERMATOLOGIA("Dermatologia"),
    PEDIATRIA("Pediatria"),
    ORTOPEDIA("Ortopedia"),
    CLINICO_GERAL("Clínico Geral"),
    GINECOLOGIA("Ginecologia");

    private String descricao;
    private Especialidade(String descricao) {
        this.descricao = descricao;
    }
}
