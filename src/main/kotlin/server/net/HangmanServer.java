package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HangmanServer {
  private static final int LINGER_TIME = 5000; // Time to keep on sending if the connection is closed
  private static final int SOCKET_TIMEOUT = 1800000; // Set socket timeout to half a minute
  private static int port = 8080; // Server default listening port
  private final List<ClientHandler> clients = new ArrayList<>();

  public static void start(String[] args) {
    System.out.println("HangmanServer started!");

    parseArgs(args);
    HangmanServer server = new HangmanServer();
    server.serve();
  }

  private static void parseArgs(String[] args) {
    if (args.length < 1) return;

    for (String arg : args) {
      String[] split = arg.split("=");
      switch (split[0].toUpperCase()) {
        case "PORT":
          try {
            port = Integer.parseInt(split[1]);
          } catch (NumberFormatException e) {
            System.err.printf("\"%s\" is not a valid port number!", split[1]);
          }
          break;
        default:
          System.err.printf("\"%s\" is not a valid argument!", split[0]);
      }
    }
  }

  private void startClientHandler(Socket client) throws IOException {
    System.out.println("A client wants to connect!");

    client.setSoLinger(true, LINGER_TIME); // Set linger time
    client.setSoTimeout(SOCKET_TIMEOUT); // Set socket timeout

    ClientHandler handler = new ClientHandler(this, client);
    synchronized (clients) {
      clients.add(handler);
    }
    Thread handlerThread = new Thread(handler);
    handlerThread.setPriority(Thread.MAX_PRIORITY);
    handlerThread.start();

    System.out.printf("There are now %d clients connected!\n", clients.size());
  }

  void removeHandler(ClientHandler client) {
    clients.remove(client);
  }

  private void serve() {
    try (ServerSocket socket = new ServerSocket(port)) {
      // Continuously listen for incoming client connections
      while (true) {
        System.out.println("Listening...");
        Socket client = socket.accept(); // Blocking
        startClientHandler(client);
      }
    } catch (IOException e) {
      System.err.println("Server failed...");
      e.printStackTrace();
    }
  }
}
