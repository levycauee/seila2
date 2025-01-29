package br.ufc.quixada.gestao.model;

public abstract class Funcionario {
    private String cpf;
    private String nome;
    private double salarioBase;
    private int diarias;
    private double lucroParticipacao;

    public Funcionario(String cpf, String nome, double salarioBase) {
        this.cpf = cpf;
        this.nome = nome;
        this.salarioBase = salarioBase;
        this.diarias = 0; // Inicializa diárias como 0
        this.lucroParticipacao = 0; // Inicializa participação nos lucros
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public int getDiarias() {
        return diarias;
    }

    public void setDiarias(int diarias) {
        this.diarias = diarias;
    }

    public double getLucroParticipacao() {
        return lucroParticipacao;
    }

    public void setLucroParticipacao(double lucroParticipacao) {
        this.lucroParticipacao = lucroParticipacao;
    }

    // Método abstrato para ser implementado nas subclasses
    public abstract double calcularSalario();

    public abstract void resetarDiarias();

    public boolean isInsalubridade() {
        return false;
    }
}
