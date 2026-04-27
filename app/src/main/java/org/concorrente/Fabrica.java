package org.concorrente;

import java.util.concurrent.Semaphore;

public class Fabrica {

    private static final int CAPACIDADE_ESTOQUE = 500;
    private static final int CAPACIDADE_ESTEIRA = 5;

    private int estoque;
    private final Semaphore mutexEstoque;
    private final Semaphore esteira;
    private final Semaphore pecasDisponiveis;

    private final Estacao[] estacoes;
    private final Semaphore mutexLog;

    public Fabrica() {
        this.estoque = CAPACIDADE_ESTOQUE;
        this.mutexEstoque = new Semaphore(1);
        this.esteira = new Semaphore(CAPACIDADE_ESTEIRA);
        this.pecasDisponiveis = new Semaphore(CAPACIDADE_ESTOQUE);
        this.mutexLog = new Semaphore(1);
        this.estacoes = new Estacao[4];
        for (int i = 0; i < 4; i++) {
            estacoes[i] = new Estacao(i, this);
        }
    }

    public void solicitarPeca() throws InterruptedException {
        esteira.acquire();
        pecasDisponiveis.acquire();
        mutexEstoque.acquire();
        estoque--;
        mutexEstoque.release();
        esteira.release();
    }

    public void registrarProducao(Veiculo v) throws InterruptedException {
        mutexLog.acquire();
        System.out.println("[PRODUCAO] " + v);
        mutexLog.release();
    }

    public void registrarVendaLoja(Veiculo v, int idLoja, int posEsteiraLoja) throws InterruptedException {
        mutexLog.acquire();
        System.out.println("[VENDA FABRICA->LOJA] lojaDestino=" + idLoja +
                           " posEsteiraLoja=" + posEsteiraLoja + " " + v);
        mutexLog.release();
    }

    public void iniciar() {
        for (Estacao e : estacoes) {
            e.iniciar();
        }
    }

    public Estacao[] getEstacoes() {
        return estacoes;
    }
}