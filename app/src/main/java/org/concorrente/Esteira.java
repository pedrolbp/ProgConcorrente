package org.concorrente;


import java.util.concurrent.Semaphore;

public class Esteira {

    private final Veiculo[] slots;
    private final int capacidade;
    private int posInsercao;
    private int posRemocao;

    private final Semaphore mutex;
    private final Semaphore vazios;
    private final Semaphore cheios;

    public Esteira(int capacidade) {
        this.capacidade = capacidade;
        this.slots = new Veiculo[capacidade];
        this.posInsercao = 0;
        this.posRemocao = 0;
        this.mutex = new Semaphore(1);
        this.vazios = new Semaphore(capacidade);
        this.cheios = new Semaphore(0);
    }

    public int inserir(Veiculo v) throws InterruptedException {
        vazios.acquire();
        mutex.acquire();
        int pos = posInsercao;
        slots[posInsercao] = v;
        posInsercao = (posInsercao + 1) % capacidade;
        mutex.release();
        cheios.release();
        return pos;
    }

    public Veiculo remover() throws InterruptedException {
        cheios.acquire();
        mutex.acquire();
        Veiculo v = slots[posRemocao];
        slots[posRemocao] = null;
        posRemocao = (posRemocao + 1) % capacidade;
        mutex.release();
        vazios.release();
        return v;
    }

    public int disponiveis() {
        return cheios.availablePermits();
    }
}