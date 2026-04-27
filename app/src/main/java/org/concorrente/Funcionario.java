package org.concorrente;


import java.util.concurrent.Semaphore;

public class Funcionario implements Runnable {

    private final int idFuncionario;
    private final int idEstacao;
    private final Semaphore ferramentaEsquerda;
    private final Semaphore ferramentaDireita;
    private final Fabrica fabrica;
    private final Esteira esteiraEstacao;

    public Funcionario(int idFuncionario, int idEstacao,
                       Semaphore ferramentaEsquerda, Semaphore ferramentaDireita,
                       Fabrica fabrica, Esteira esteiraEstacao) {
        this.idFuncionario = idFuncionario;
        this.idEstacao = idEstacao;
        this.ferramentaEsquerda = ferramentaEsquerda;
        this.ferramentaDireita = ferramentaDireita;
        this.fabrica = fabrica;
        this.esteiraEstacao = esteiraEstacao;
    }

    @Override
    public void run() {
        while (true) {
            try {
                fabrica.solicitarPeca();

                if (idFuncionario % 2 == 0) {
                    ferramentaEsquerda.acquire();
                    ferramentaDireita.acquire();
                } else {
                    ferramentaDireita.acquire();
                    ferramentaEsquerda.acquire();
                }

                Veiculo v = new Veiculo(idEstacao, idFuncionario);

                ferramentaEsquerda.release();
                ferramentaDireita.release();

                int pos = esteiraEstacao.inserir(v);
                v.setPosicaoEsteiraFabrica(pos);

                fabrica.registrarProducao(v);

                Thread.sleep((long) (Math.random() * 500 + 200));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}