import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/*
    TODO: try doing rsa first if rsa works then we shall add huffman

    TODO: implement huffman into rsa to make the code cleaner.

    TODO: HUFFMAN HAS AN ERROR WHERE IT IS NULL. MY HYPTHOSIS IS THAT THE FIRST CLIENT READS FASTER THAN THE SECOND CLIENT
 */

public class Client{

    private  Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public RSA secert;

    public Huffman huffman;



    public void LoadEncryptionData() throws IOException{
        FileInputStream fileIn = new FileInputStream("data.ser");

        ObjectInputStream in = new ObjectInputStream(fileIn);




    }

    public void SaveEncryptionData(Huffman huff){

        try{
            FileOutputStream fileOut = new FileOutputStream("huffData.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(huff);
            out.close();
            fileOut.close();
            //System.out.println("CHECK DATA huffDATA");
        }catch(Exception e){
            System.out.println("Did not save huffdata");
        }

    }


    public void SaveEncryptionData(RSA secert) throws IOException {

        try{
            FileOutputStream fileOut = new FileOutputStream("data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);


            out.writeObject(secert);
            out.close();
            fileOut.close();
            //System.out.println("CHECK DATA RSADATA");


        }catch(Exception e){
            System.out.println("Failed to save RSA data");
        }


        /*
        try{
            FileOutputStream fileOut = new FileOutputStream("data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            FileOutputStream fileOutHuff = new FileOutputStream("huffData.ser");
            ObjectOutputStream outHuff = new ObjectOutputStream(fileOutHuff);

            //Why is there an error here


            //saves huffman
            outHuff.writeObject(huff);
            outHuff.close();
            fileOutHuff.close();

            //saves RSA
            out.writeObject(secert);
            out.close();
            fileOut.close();
            System.out.println("CHECK DATA");
        }catch(Exception e){

        }
        */




    }


    public Huffman loadEncryptionDataHuff(){
        try{

            huffman = null;

            FileInputStream fileInHuff = new FileInputStream("D:\\project\\theRealFinalProject\\huffData.ser");
            ObjectInputStream inHuff = new ObjectInputStream(fileInHuff);

            huffman = (Huffman) inHuff.readObject();

            fileInHuff.close();
            inHuff.close();

            //System.out.print(secert.message);
        }catch(Exception x){

        }

        return huffman;
    }




//TODO: fix he object instream input it is not readin fileIn
    public RSA loadEncryptionData() throws IOException, ClassNotFoundException {
        try{
            secert = null;


            FileInputStream fileIn = new FileInputStream("data.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            secert = (RSA) in.readObject();

            in.close();
            fileIn.close();


            //System.out.print(secert.message);
        }catch(Exception x){

        }

        return secert;
    }



    //We have socket object for commmunication
    //Takes in a username to represent the clinet
    public Client(@NotNull Socket socket, String username) throws IOException, ClassNotFoundException {
        //loadEncryptionData(); testing no need to use this anymore
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    //sends messages to the client handler
    public void sendMessage(){
        try{
            bufferedWriter.write(username); //whatever we send here the client handle
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);


            while(socket.isConnected()){
                String messageToSend = scanner.nextLine(); // this reads the user text
                messageToSend = messageToSend.toLowerCase();

                if(messageToSend.equals("quit")){
                    resetAllData();
                    decrementOnlineStatus();
                    System.exit(0);
                }

                //insert RSA here and huffman
                this.secert = new RSA(messageToSend.toLowerCase()); //this encrypts the message
                Huffman huffman = new Huffman();
                String compressedEncryption = huffman.createHuffmanTree(secert.getEncrpytMessageString());//compressing RSA encrpytion
                SaveEncryptionData(secert);
                SaveEncryptionData(huffman);

                //bufferedWriter.write(username + ":" + secert.getEncrpytMessageString());
                //changed that there is no space

                bufferedWriter.write(username + ":" + compressedEncryption);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                //SaveEncryptionData(secert, huffman);

            }
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    private void decrementOnlineStatus() {
        int countOnline = readOnlineFile();

        try{
            File file = new File("appearOnline.txt");
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            if(countOnline == 0){
                pw.println(countOnline);
                pw.close();
                return;
            }

            //pw.println() this will write to the file writer
            pw.println(--countOnline);
            pw.close();
        }catch(Exception e){
            System.out.println("file did not decrement please check code if you are using correct txt file.");
        }

    }

    private void resetAllData(){
        this.huffman = null;
        this.secert = null;

        try{
            SaveEncryptionData(secert);
            SaveEncryptionData(huffman);
        } catch(Exception e){
            System.out.println("Did not reset properly");
        }



    }


    public void listenForMessage(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat = null;
                int howmanyOnline = 0;
                try{
                    while(socket.isConnected()){





                        //TODO: issue with the communication. The second client ends up getting decrypted message by the user that logs in before him you can fix this
                        //TODO: with serilization or a txt-file that tells us that the user is already logged in so type bufferedReader.readLine();






                            howmanyOnline = getNumberOfUsersOnline(); //if there is two then this means we won't go through the if statment

                            //howmanyOnline = appearOnline();


                            if(howmanyOnline < 1){
                                howmanyOnline = appearOnline();
                                msgFromGroupChat = bufferedReader.readLine();
                                System.out.println(msgFromGroupChat);
                                continue;
                            }







                        //if Client has more than 1 online we go here
                        msgFromGroupChat = bufferedReader.readLine();

                        String parts [] = msgFromGroupChat.split(":");
                        String encryptedMsg = parts[1]; //right side of the delimeter


                        huffman = loadEncryptionDataHuff();
                        try{
                            secert = loadEncryptionData();
                            huffman = loadEncryptionDataHuff();
                        }catch(Exception e){
                            System.out.println("failed to load rsa object");
                        }




                        try{
                            huffman.decodeData(huffman.getRoot(), huffman.getSb());
                            msgFromGroupChat = huffman.getHuffmanDecodedString();
                            msgFromGroupChat = parts[0] + ": " + secert.decrypt(msgFromGroupChat);
                            System.out.println(msgFromGroupChat);
                        }catch(NullPointerException e){
                            System.out.println(msgFromGroupChat);
                            continue;
                        }






                        /*
                        if(msgFromGroupChat == null){
                            msgFromGroupChat = bufferedReader.readLine();
                            System.out.println(msgFromGroupChat);
                        }
                        else{
                            msgFromGroupChat = bufferedReader.readLine();
                            String parts [] = msgFromGroupChat.split(":");
                            String encryptedMsg = parts[1]; //right side of the delimeter
                            huffman = loadEncryptionDataHuff();
                            secert = loadEncryptionData();

                            huffman.decodeData(huffman.getRoot(), huffman.getSb());
                            msgFromGroupChat = huffman.getHuffmanDecodedString();

                            msgFromGroupChat = parts[0] + ": " + secert.decrypt(encryptedMsg); //reads broadcast message ERROR HERE
                            System.out.println(msgFromGroupChat);
                        }
                        */






                    }
                }
                catch(IOException e){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                }


            }
        }).start();
    }

    private int getNumberOfUsersOnline() {
        int countOnline = 0;
        try {
            countOnline = readOnlineFile();
        } catch (Exception e){
            System.out.println("Did not read the file");
        }
        return countOnline;
    }

    private int appearOnline(){
        int countOnline = readOnlineFile();
        try{
            File file = new File("appearOnline.txt");
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);


            //pw.println() this will write to the file writer
            pw.println(++countOnline);
            pw.close();
            return countOnline;
        }catch(Exception e){
            System.out.println("file did not write");
        }




        return 0;
    }

    private int readOnlineFile() {
        String n = ""; //number of people online
        try{
                Scanner x = new Scanner(new File("appearOnline.txt"));
                while(x.hasNext()) {
                    n = x.nextLine();
                }
        } catch (FileNotFoundException e) {
            System.out.println("Did not scan items");
            return 0;
        }


        return Integer.parseInt(n);
    }





    /*
    public void listenForMessage(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                try{
                    while(socket.isConnected()){

                        //secert = new RSA();

                        System.out.println(username);
                        msgFromGroupChat = bufferedReader.readLine();
                        String parts[] = msgFromGroupChat.split(":");
                        String encryptedMsg = parts[1]; //right side of the delimeter
                        msgFromGroupChat = username + secert.decrypt(encryptedMsg); //reads broadcast message ERROR HERE

                        //once it reaches the client it should decrypt
                        //msgFromGroupChat = secert.decrypt(msgFromGroupChat);

                        System.out.println(msgFromGroupChat);
                    }
                }
                 catch(IOException e){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                }
            }
        }).start();
    }

       */

    //reset everything when user decides to log off.
    public void closeEverything(Socket socket, BufferedReader bufferedreader, BufferedWriter bufferedWriter){

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





    public static void main(String[] args) throws IOException, ClassNotFoundException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your username for the group chat: ");
    String username = scanner.nextLine();
    System.out.println("Type quit to leave chat");



    Socket socket = new Socket("localhost", 1234);
    Client client = new Client(socket, username);

    client.listenForMessage(); // goes into here FIRST then it creates a null error

    client.sendMessage();

    }




}
