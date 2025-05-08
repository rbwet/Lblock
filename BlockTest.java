public class BlockTest {
    public static void main(String[] args) {
        // Create the Genesis Block
        Block genesisBlock = new Block();
        System.out.println("Genesis Block: " + genesisBlock);

        // Create a new Block
        Block newBlock = new Block("First Block Data");
        newBlock.setPreviousHash(genesisBlock.getHash());
        System.out.println("New Block: " + newBlock);

        // Check if the blocks are correctly linked
        System.out.println("Genesis Block Hash: " + genesisBlock.getHash());
        System.out.println("New Block's Previous Hash: " + newBlock.getPreviousHash());
    }
}