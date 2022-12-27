import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
    TODO: make sure to add the space function remember -32 change to " "
        and make sure to convert every letter to lowercase.
 */
public class Server {
    //Listening for incoming connections for clients to communicate with them
    private ServerSocket serverSocket;


    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }



    public void startServer(){
        //Server socket  is constantly running unstil it is closed
        try{
            while(!serverSocket.isClosed()){
                // programminmg will be halted here until a client connects. When a client does connect a socket will be returned to
                //Socket object will be returned to be used to communicate with the client
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){

        }

    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String [] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }






}