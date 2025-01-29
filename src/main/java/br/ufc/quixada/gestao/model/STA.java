package br.ufc.quixada.gestao.model;

public class STA extends Funcionario {
    private int nivel;

    public STA(String cpf, String nome, int nivel) {
        super(cpf, nome, calcularSalarioBase(nivel)); // Calcula o salário base com base no nível
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean isNivelado(){
        return true;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + (getDiarias() * 100) + getLucroParticipacao(); // Inclui diárias e participação nos lucros
    }

    @Override
    public void resetarDiarias() {
        setDiarias(0);
    }

    // Método para calcular o salário base de acordo com o nível
    private static double calcularSalarioBase(int nivel) {
        if (nivel >= 1 && nivel <= 30) {
            return 1000 + (100 * nivel); // Fórmula de cálculo do salário base
        }
        return 0;
    }
}
