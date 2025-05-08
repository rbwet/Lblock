import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BCNode {
    private List<Block> blockchain;
    private List<Socket> connections = new ArrayList<>();
    private List<ObjectOutputStream> outputStreams = new ArrayList<>();
    private final int difficulty = 5;
    //singleton instance
    private static BCNode instance;

    public static BCNode getInstance() {
        return instance;
    }

    public BCNode(int port, List<Integer> remotePorts) throws IOException {
        instance = this; //init

        blockchain = new ArrayList<>();

//made this a consistent hash key
        Block genesisBlock = new Block("Genesis Block");
        genesisBlock.setHash("genesishash"); // Use a fixed hash for the genesis block
        blockchain.add(genesisBlock);

        //start listening for incoming connections on a separate thread
        new Thread(new ConnectionHandler(port)).start();

        //connect to other nodes on remotePorts
        for (int remotePort : remotePorts) {
            try {
                Socket socket = new Socket("localhost", remotePort);
                connections.add(socket);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStreams.add(outputStream);
                System.out.println("Connected to remote node on port " + remotePort);
            } catch (IOException e) {
                System.out.println("Failed to connect to port " + remotePort);
                e.printStackTrace();
            }
        }
    }

    public synchronized void addBlock(Block block) {
        Block lastBlock = blockchain.get(blockchain.size() - 1);
        block.setPreviousHash(lastBlock.getHash());
        mineBlock(block);
        blockchain.add(block);
        System.out.println("Block mined and added to blockchain: " + block);
        broadcastBlock(block);
    }

    private void mineBlock(Block block) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        String minedHash;
        do {
            block.incrementNonce();
            minedHash = block.calculateBlockHash();
        } while (!minedHash.substring(0, difficulty).equals(target));
        block.setHash(minedHash);
        System.out.println("Block mined with hash: " + minedHash);
    }

    public synchronized boolean validateAndAddBlock(Block block) {
        Block lastBlock = blockchain.get(blockchain.size() - 1);
    
        System.out.println("Expected previousHash: " + lastBlock.getHash());
        System.out.println("Received previousHash: " + block.getPreviousHash());
    
        if (!block.getPreviousHash().equals(lastBlock.getHash())) {
            System.out.println("Validation failed: previousHash mismatch.");
            return false;
        }
    
        if (!block.getHash().equals(block.calculateBlockHash())) {
            System.out.println("Validation failed: incorrect block hash.");
            return false;
        }
    
        blockchain.add(block);
        System.out.println("Received block validated and added: " + block);
        return true;
    }
    
    public synchronized Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
    public void broadcastBlock(Block block) {
        System.out.println("Broadcasting block to connected nodes: " + block);
        for (ObjectOutputStream outputStream : outputStreams) {
            try {
                outputStream.writeObject(block);
                outputStream.reset(); //feset to avoid caching issues
            } catch (IOException e) {
                System.out.println("Failed to broadcast block to one of the nodes.");
                e.printStackTrace();
            }
        }
    }

    public boolean validateChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash())) return false;
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder chain = new StringBuilder();
        for (Block block : blockchain) {
            chain.append(block.toString()).append("\n");
        }
        return chain.toString();
    }

    public static void main(String[] args) {
        Scanner keyScan = new Scanner(System.in);

        // Grab my port number on which to start this node
        System.out.print("Enter port to start (on current IP): ");
        int myPort = keyScan.nextInt();

        // Need to get what other Nodes to connect to
        System.out.print("Enter remote ports (current IP is assumed): ");
        keyScan.nextLine(); // skip the NL at the end of the previous scan int
        String line = keyScan.nextLine();
        List<Integer> remotePorts = new ArrayList<Integer>();
        if (!line.isEmpty()) {
            String[] splitLine = line.split(" ");
            for (String port : splitLine) {
                remotePorts.add(Integer.parseInt(port));
            }
        }

        // Create the Node
        BCNode n = null;
        try {
            n = new BCNode(myPort, remotePorts);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String ip = "";
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Node started on " + ip + ": " + myPort);

        // Node command line interface
        while (true) {
            System.out.println("\nNODE on port: " + myPort);
            System.out.println("1. Display Node's blockchain");
            System.out.println("2. Create/mine new Block");
            System.out.println("3. Kill Node");
            System.out.print("Enter option: ");
            int in = keyScan.nextInt();

            if (in == 1) {
                System.out.println(n);
            } else if (in == 2) {
                // Grab the information to put in the block
                System.out.print("Enter information for new Block: ");
                String blockInfo = keyScan.next();
                Block b = new Block(blockInfo);
                n.addBlock(b);
            } else if (in == 3) {
                // Take down the whole virtual machine (and all the threads)
                keyScan.close();
                System.exit(0);
            }
        }
    }
}