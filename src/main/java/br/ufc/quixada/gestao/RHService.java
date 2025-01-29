package br.ufc.quixada.gestao;

import br.ufc.quixada.gestao.model.Funcionario;
import br.ufc.quixada.gestao.contrato.IRHService;
import br.ufc.quixada.gestao.model.Professor;
import br.ufc.quixada.gestao.model.STA;
import br.ufc.quixada.gestao.model.Terceirizado;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RHService implements IRHService {
    private List<Funcionario> funcionarios = new ArrayList<>();
    private double lucroDividido = 0.0;

    @Override
    public boolean cadastrar(Funcionario funcionario) {
        // Verifica se o funcionário é válido
        if (funcionario == null || funcionario.getCpf() == null || funcionario.getCpf().isEmpty()) {
            return false; // CPF inválido
        }

        // Verifica se o funcionário já foi cadastrado
        for (Funcionario f : funcionarios) {
            if (f.getCpf().equals(funcionario.getCpf())) {
                return false; // CPF já cadastrado
            }
        }

        // Verifica se o professor tem classe válida
        if (funcionario instanceof Professor) {
            Professor professor = (Professor) funcionario;
            if (!isClasseValida(professor.getClasse())) {
                return false; // Classe inválida
            }
        }

        // Verifica se o STA tem nível válido (entre 1 e 30)
        if (funcionario instanceof STA) {
            STA sta = (STA) funcionario;

            // Verifica se o nível do STA está entre 1 e 30
            if (sta.getNivel() < 1 || sta.getNivel() > 30) {
                return false; // Nível inválido
            }
        }

        // Adiciona o funcionário à lista de cadastrados
        funcionarios.add(funcionario);
        return true;
    }


    private boolean isClasseValida(char classe) {
        return classe == 'A' || classe == 'B' || classe == 'C' || classe == 'D' || classe == 'E';
    }

    @Override
    public boolean remover(String cpf) {
        Funcionario funcionario = obterFuncionario(cpf);
        if (funcionario != null) {
            funcionarios.remove(funcionario);
            return true;
        }
        return false;
    }

    @Override
    public Funcionario obterFuncionario(String cpf) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getCpf().equals(cpf)) {
                return funcionario;
            }
        }
        return null;
    }

    @Override
    public List<Funcionario> getFuncionarios() {
        // Cria uma nova lista para não modificar a original
        List<Funcionario> funcionariosOrdenados = new ArrayList<>(funcionarios);

        // Ordena a lista pelo nome dos funcionários, usando uma classe anônima
        Collections.sort(funcionariosOrdenados, new Comparator<Funcionario>() {
            @Override
            public int compare(Funcionario f1, Funcionario f2) {
                return f1.getNome().compareTo(f2.getNome()); // Compara os nomes
            }
        });

        // Retorna a lista ordenada
        return funcionariosOrdenados;
    }


    @Override
    public List<Funcionario> getFuncionariosPorCategoria(IRHService.Tipo tipo) {
        List<Funcionario> categoriaFuncionarios = new ArrayList<>();

        // Filtra os funcionários por categoria e aplica o método isNivelado() para o tipo STA
        for (Funcionario f : funcionarios) {
            switch (tipo) {
                case PROF:
                    if (f instanceof Professor) {
                        categoriaFuncionarios.add(f);
                    }
                    break;
                case STA:
                    if (f instanceof STA && ((STA) f).isNivelado()) {  // Verifica o nível do STA
                        categoriaFuncionarios.add(f);
                    }
                    break;
                case TERC:
                    if (f instanceof Terceirizado) {
                        categoriaFuncionarios.add(f);
                    }
                    break;
            }
        }

        Collections.sort(categoriaFuncionarios, new Comparator<Funcionario>() {
            @Override
            public int compare(Funcionario f1, Funcionario f2) {
                return f1.getNome().compareTo(f2.getNome());
            }
        });

        return categoriaFuncionarios;
    }


    @Override
    public int getTotalFuncionarios() {
        return funcionarios.size();
    }

    @Override
    public boolean solicitarDiaria(String cpf) {
        Funcionario funcionario = obterFuncionario(cpf);
        if (funcionario == null) {
            return false;
        }

        if (funcionario instanceof Professor && funcionario.getDiarias() < 3) {
            funcionario.setDiarias(funcionario.getDiarias() + 1);
            return true;
        } else if (funcionario instanceof STA && funcionario.getDiarias() < 1) {
            funcionario.setDiarias(funcionario.getDiarias() + 1);
            return true;
        } else if (funcionario instanceof Terceirizado) {
            return false; // Terceirizados não têm direito a diárias
        }

        return false;
    }

    @Override
    public void partilharLucros(double valor) {
        if (funcionarios.size() > 0) {
            lucroDividido = valor / funcionarios.size();
        }
    }

    @Override
    public void iniciarMes() {
        // Limpar diárias e a participação nos lucros dos funcionários
        for (Funcionario f : funcionarios) {
            lucroDividido = 0.0;
            f.resetarDiarias(); // Resetar as diárias
            f.setLucroParticipacao(0); // Resetar a participação nos lucros
        }
    }

    @Override
    public Double calcularSalarioDoFuncionario(String cpf) {
        Funcionario funcionario = obterFuncionario(cpf);
        if (funcionario == null) {
            return null;
        }

        double salarioBase = 0;

        if (funcionario instanceof Professor) {
            Professor professor = (Professor) funcionario;
            salarioBase = calcularSalarioBaseProfessor(professor.getClasse());
        } else if (funcionario instanceof STA) {
            STA sta = (STA) funcionario;
            salarioBase = calcularSalarioBaseSTA(sta);
        } else if (funcionario instanceof Terceirizado) {
            // Considera o salário base fixo para terceirizados
            salarioBase = (((Terceirizado) funcionario).isInsalubre()) ? 1500 : 1000;
        }

        double diarias = funcionario.getDiarias() * 100; // Exemplo: valor das diárias
        return salarioBase + diarias + lucroDividido;
    }

    private double calcularSalarioBaseProfessor(char classe) {
        switch (classe) {
            case 'A':
                return 3000;
            case 'B':
                return 5000;
            case 'C':
                return 7000;
            case 'D':
                return 9000;
            case 'E':
                return 11000;
            default:
                return 0;
        }
    }

    private double calcularSalarioBaseSTA(STA sta) {
        return 1000 + (100 * sta.getNivel());
    }

    @Override
    public double calcularFolhaDePagamento() {
        double total = 0;
        for (Funcionario f : funcionarios) {
            Double salario = calcularSalarioDoFuncionario(f.getCpf());
            if (salario != null) {
                total += salario;
            }
        }
        return total;
    }
}
