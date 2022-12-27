import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; //used to read data
    private BufferedWriter bufferedWriter; //used to send data specfically messages to the client these will be messages from other clients



    private String clientUsername; //used to id each client

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            //outputstream means character stream because we are sending messages
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //use this stream to send things
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //this stream to read things

            //Client handler will be waiting here for the username when the user writes their username in the Client.java
            //The data of the username will be passed in line 30 Client.java
            this.clientUsername = bufferedReader.readLine(); //read from the buffer reader until they hit enter

            clientHandlers.add(this); //Then we add the client to the arraylist so they can recieve messages from the users
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!"); // sends this message to other connected users

        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }




    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                //decrypt here probably?? or do we encrypt here
                messageFromClient = bufferedReader.readLine(); //read the message from the client
                broadcastMessage(messageFromClient); //broadcast the message from the client
            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                //if current clienthandle in the for looop if it does not equal the clientusername then we send data
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");

    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket!= null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
