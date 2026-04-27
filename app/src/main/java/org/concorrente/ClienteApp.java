package org.concorrente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ClienteApp {
    public static void main(String[] args) {
        int[] portasLojas = {8081, 8082, 8083};

        for (int i = 0; i < 20; i++) {
            final int idCliente = i;
            new Thread(() -> iniciarCliente(idCliente, portasLojas)).start();
        }
    }

    private static void iniciarCliente(int idCliente, int[] portasLojas) {
        List<Veiculo> garagem = new ArrayList<>();
        Semaphore mutexLog = new Semaphore(1);

        while (true) {
            int porta = portasLojas[(int) (Math.random() * portasLojas.length)];
            
            try (Socket socket = new Socket("localhost", porta);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject("COMPRAR");
                out.flush();
                
                Veiculo v = (Veiculo) in.readObject();
                garagem.add(v);

                mutexLog.acquire();
                System.out.println("[LOG COMPRA] cliente=" + idCliente + " comprou na porta=" + porta + 
                                   " | garagem=" + garagem.size() + " carros | " + v);
                mutexLog.release();

                Thread.sleep((long) (Math.random() * 1000 + 500));

            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}