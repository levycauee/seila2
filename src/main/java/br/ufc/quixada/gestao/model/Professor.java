package br.ufc.quixada.gestao.model;

public class Professor extends Funcionario {
    private char classe;

    public Professor(String cpf, String nome, char classe) {
        super(cpf, nome, calcularSalarioBase(classe)); // Calcula o salário base com base na classe
        this.classe = classe;
    }

    public char getClasse() {
        return classe;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + (getDiarias() * 100) + getLucroParticipacao(); // Inclui participação nos lucros
    }

    @Override
    public void resetarDiarias() {
        setDiarias(0);
    }

    // Método para calcular o salário base de acordo com a classe
    private static double calcularSalarioBase(char classe) {
        switch (classe) {
            case 'A': return 3000;
            case 'B': return 5000;
            case 'C': return 7000;
            case 'D': return 9000;
            case 'E': return 11000;
            default: return 0;
        }
    }
}
