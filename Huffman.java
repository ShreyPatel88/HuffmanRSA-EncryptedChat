import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;




public class Huffman implements Serializable {
    class Node  implements Serializable{
        //stores var in ch
        Character ch;
        //soring freq
        Integer freq;
        //left and right start off as null
        Node left = null;
        Node right = null;

        //constructor
        Node(Character ch, Integer freq){
            this.ch = ch;
            this.freq = freq;
        }

        //constructor
        public Node (Character ch, Integer freq, Node left, Node right){
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

    }

        private  StringBuilder sb = new StringBuilder();
        private  Node root;
        private  String huffmanDecodedString = "";
        //This funciton helps build the huffmantree

    Huffman(){

    }

    public Node getRoot(){
        return root;
    }

    public StringBuilder getSb(){
        return sb;
    }

    public String getHuffmanDecodedString(){
        return huffmanDecodedString;
    }


        /*
        Huffman(String RSAsecert){
            createHuffmanTree(RSAsecert);
        }
        */



        public  String createHuffmanTree(String text){

            //base case: if user does not give string
            if(text == null || text.length() == 0){
                return "";
            }



            //creating an instance of the Map
            Map<Character, Integer> freq = new HashMap<>();


            //count the frequency of appearance of each character and store it in a map
            //TODO: check if this is actually counting the frequency I would put in a string like "aaabcd" if the freq is (3) then it is working
            for(char c: text.toCharArray()){
                // //storing character and their frequency into Map by invoking the put() method
                freq.put(c, freq.getOrDefault(c,0) + 1);
            }

            
            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));


            for(var entry: freq.entrySet()){
                pq.add(new Node(entry.getKey(), entry.getValue()));
            }

            while(pq.size() != 1){
                Node left = pq.poll();
                Node right = pq.poll();

                int sum = left.freq + right.freq;

                pq.add(new Node(null, sum, left, right));
            }

            root = pq.peek();

            Map<Character, String> huffmanCode = new HashMap<>();
            encodeData(root, "", huffmanCode);

            //System.out.println("Huffman Codes of the characters are: " + huffmanCode);

            //System.out.println("The initial string is: " + text);




            for(char c: text.toCharArray()){
                sb.append(huffmanCode.get(c));
            }

            //System.out.println("The encoded string is: " + sb);
            //System.out.print("The decoded string is: ");
            String compressedTxt = sb.toString();

            return compressedTxt; //returns encoded huffman

           //
        }

        public  void decodeData(Node root, StringBuilder sb){
            if(isLeaf(root)){

                while(root.freq --> 0){
                    //System.out.println(root.ch);

                }


            }
            else{
                int index = -1;
                while(index < sb.length() - 1){
                    index = decodeData(root,index,sb);
                }
            }
        }


    //traverse the Huffman Tree and store Huffman Codes in a Map
    //function that encodes the data
    public  void encodeData(Node root, String str, Map<Character, String> huffmanCode)
    {
        if (root == null)
        {
            return;
        }
        //checks if the node is a leaf node or not
        if (isLeaf(root))
        {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }



    //traverse the Huffman Tree and decode the encoded string function that decodes the encoded data
    public  int decodeData(Node root, int index, StringBuilder sb)
    {
        //checks if the root node is null or not
        if (root == null)
        {
            return index;
        }
        //checks if the node is a leaf node or not
        if (isLeaf(root))
        {
            //System.out.print(root.ch);
            huffmanDecodedString+=root.ch;
            return index;
        }
        index++;
        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decodeData(root, index, sb);
        return index;
    }

    public  boolean isLeaf(Node root)
    {
        //returns true if both conditions return ture
        return root.left == null && root.right == null;
    }


    public static void main(String args[])
    {
        RSA x = new RSA("hi d");

        //function calling
        //createHuffmanTree(x.getEncrpytMessageString());
        //decodeData(root,sb);
        //System.out.println(x.decrypt(huffmanDecodedString));


    }
}
