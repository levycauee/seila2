package br.ufc.quixada.gestao.model;

public class Terceirizado extends Funcionario {
    private boolean insalubre; // Atributo para indicar se o terceirizado recebe insalubridade

    public Terceirizado(String cpf, String nome, boolean insalubre) {
        super(cpf, nome, calcularSalarioBase(insalubre));
        this.insalubre = insalubre;
    }

    public boolean isInsalubre() {
        return insalubre;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + (getDiarias() * 100) + getLucroParticipacao(); // Inclui diárias e participação nos lucros
    }

    @Override
    public void resetarDiarias() {
        setDiarias(0);
    }

    // Método para calcular o salário base de acordo com a insalubridade
    private static double calcularSalarioBase(boolean insalubre) {
        return insalubre ? 1500 : 1000; // Salário com ou sem insalubridade
    }
}
