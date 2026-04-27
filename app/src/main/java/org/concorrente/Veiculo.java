package org.concorrente;

import java.io.Serializable;

public class Veiculo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final String[] CORES = {"R", "G", "B"};
    private static final String[] TIPOS = {"SUV", "SEDAN"};
    private static int contador = 0;

    private final int id;
    private final String cor;
    private final String tipo;
    private final int idEstacao;
    private final int idFuncionario;
    private int posicaoEsteiraFabrica;

    public Veiculo(int idEstacao, int idFuncionario) {
        this.id = ++contador;
        this.cor = CORES[(this.id - 1) % 3];
        this.tipo = TIPOS[(this.id - 1) % 2];
        this.idEstacao = idEstacao;
        this.idFuncionario = idFuncionario;
    }

    public int getId() { return id; }
    public String getCor() { return cor; }
    public String getTipo() { return tipo; }
    public int getIdEstacao() { return idEstacao; }
    public int getIdFuncionario() { return idFuncionario; }
    public int getPosicaoEsteiraFabrica() { return posicaoEsteiraFabrica; }
    public void setPosicaoEsteiraFabrica(int pos) { this.posicaoEsteiraFabrica = pos; }

    @Override
    public String toString() {
        return "[Veiculo id=" + id + " cor=" + cor + " tipo=" + tipo +
               " estacao=" + idEstacao + " funcionario=" + idFuncionario +
               " posEsteira=" + posicaoEsteiraFabrica + "]";
    }
}