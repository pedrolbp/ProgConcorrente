package org.concorrente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FabricaApp {
    public static void main(String[] args) {
        try {
            Fabrica fabrica = new Fabrica();
            fabrica.iniciar();

            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("[SISTEMA] Fabrica iniciada na porta 8080.");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new FabricaClientHandler(socket, fabrica)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class FabricaClientHandler implements Runnable {
    private final Socket socket;
    private final Fabrica fabrica;

    public FabricaClientHandler(Socket socket, Fabrica fabrica) {
        this.socket = socket;
        this.fabrica = fabrica;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            while (true) {
                Object req = in.readObject();
                if ("COMPRAR".equals(req)) {
                    Estacao[] estacoes = fabrica.getEstacoes();
                    Estacao estacao = estacoes[(int) (Math.random() * estacoes.length)];
                    Veiculo v = estacao.getEsteira().remover();
                    
                    out.writeObject(v);
                    out.flush();
                } else if (req instanceof String && ((String) req).startsWith("CONFIRMA")) {
                    // Recebe o aviso da Loja para gerar o Log de Venda (Tópico V)
                    String[] parts = ((String) req).split(",");
                    int idLoja = Integer.parseInt(parts[1]);
                    int posEsteira = Integer.parseInt(parts[2]);
                    Veiculo v = (Veiculo) in.readObject();
                    
                    fabrica.registrarVendaLoja(v, idLoja, posEsteira);
                }
            }
        } catch (Exception e) {
            System.out.println("[SISTEMA] Conexão com uma loja foi encerrada.");
        }
    }
}