package org.concorrente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class LojaApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java org.concorrente.LojaApp <ID_LOJA> <PORTA_LOCAL>");
            return;
        }

        int idLoja = Integer.parseInt(args[0]);
        int portaLoja = Integer.parseInt(args[1]);
        Esteira esteiraLoja = new Esteira(40);
        Semaphore mutexLog = new Semaphore(1);

        new Thread(() -> {
            try (Socket fabricaSocket = new Socket("localhost", 8080);
                 ObjectOutputStream outFabrica = new ObjectOutputStream(fabricaSocket.getOutputStream());
                 ObjectInputStream inFabrica = new ObjectInputStream(fabricaSocket.getInputStream())) {

                while (true) {
                    outFabrica.writeObject("COMPRAR");
                    outFabrica.flush();
                    
                    Veiculo v = (Veiculo) inFabrica.readObject();
                    int pos = esteiraLoja.inserir(v);
                    
                    mutexLog.acquire();
                    System.out.println("[LOG RECEBIMENTO] loja=" + idLoja + " posEsteiraLoja=" + pos + " " + v);
                    mutexLog.release();

                    outFabrica.writeObject("CONFIRMA," + idLoja + "," + pos);
                    outFabrica.writeObject(v);
                    outFabrica.flush();
                }
            } catch (Exception e) {
                System.out.println("[SISTEMA] Erro na conexão da Loja " + idLoja + " com a Fábrica.");
            }
        }).start();

        try (ServerSocket serverSocket = new ServerSocket(portaLoja)) {
            System.out.println("[SISTEMA] Loja " + idLoja + " iniciada na porta " + portaLoja);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                new Thread(() -> atenderCliente(clienteSocket, idLoja, esteiraLoja, mutexLog)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atenderCliente(Socket clienteSocket, int idLoja, Esteira esteiraLoja, Semaphore mutexLog) {
        try (ObjectOutputStream outCliente = new ObjectOutputStream(clienteSocket.getOutputStream());
             ObjectInputStream inCliente = new ObjectInputStream(clienteSocket.getInputStream())) {

            while (true) {
                Object req = inCliente.readObject();
                if ("COMPRAR".equals(req)) {
                    Veiculo v = esteiraLoja.remover();
                    
                    mutexLog.acquire();
                    System.out.println("[LOG VENDA] loja=" + idLoja + " -> CLIENTE COMPROU: " + v);
                    mutexLog.release();
                    
                    outCliente.writeObject(v);
                    outCliente.flush();
                }
            }
        } catch (Exception e) {
        }
    }
}