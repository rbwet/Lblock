import java.io.*;
import java.net.Socket;

public class ReadHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;

    public ReadHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("ReadHandler started for connection: " + socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        try {
            while (true) {
                // read incoming block
                Block receivedBlock = (Block) inputStream.readObject();
                System.out.println("Received block: " + receivedBlock);
    
                //v.alidate and add the block to the blockchain
                if (BCNode.getInstance() != null) {
                    String expectedPreviousHash = BCNode.getInstance().getLatestBlock().getHash();
                    System.out.println("Expected previousHash: " + expectedPreviousHash);
                    System.out.println("Received previousHash: " + receivedBlock.getPreviousHash());
    
                    if (BCNode.getInstance().validateAndAddBlock(receivedBlock)) {
                        System.out.println("Block successfully added to blockchain: " + receivedBlock);
                        //relay the block to other connected nodes
                        BCNode.getInstance().broadcastBlock(receivedBlock);
                    } else {
                        System.out.println("Invalid block received; discarded.");
                    }
                } else {
                    System.out.println("BCNode instance not available; block discarded.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection lost with " + socket.getRemoteSocketAddress());
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}