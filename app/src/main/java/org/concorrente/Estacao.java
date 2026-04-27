package org.concorrente;


import java.util.concurrent.Semaphore;

public class Estacao {

    private final int id;
    private final Esteira esteira;
    private final Thread[] threads;

    public Estacao(int id, Fabrica fabrica) {
        this.id = id;
        this.esteira = new Esteira(40);
        int numFuncionarios = 5;
        Semaphore[] ferramentas = new Semaphore[numFuncionarios];
        for (int i = 0; i < numFuncionarios; i++) {
            ferramentas[i] = new Semaphore(1);
        }
        this.threads = new Thread[numFuncionarios];
        for (int i = 0; i < numFuncionarios; i++) {
            Semaphore esquerda = ferramentas[i];
            Semaphore direita = ferramentas[(i + 1) % numFuncionarios];
            Funcionario f = new Funcionario(i, id, esquerda, direita, fabrica, esteira);
            threads[i] = new Thread(f, "Estacao-" + id + "-Func-" + i);
        }
    }

    public void iniciar() {
        for (Thread t : threads) {
            t.start();
        }
    }

    public Esteira getEsteira() {
        return esteira;
    }

    public int getId() {
        return id;
    }
}