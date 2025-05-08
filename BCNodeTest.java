// public class BCNodeTest {
//     public static void main(String[] args) {
//         // Initialize the blockchain node with the Genesis Block
//         BCNode node = new BCNode();
        
//         System.out.println("Initial Blockchain:");
//         System.out.println(node);

//         // Add a new block to the chain and mine it
//         // Mining will generate a valid hash with leading zeros based on difficulty
//         Block newBlock = new Block("First Block Data");
//         node.addBlock(newBlock);

//         System.out.println("\nBlockchain after adding one block:");
//         System.out.println(node);

//         // Validate the blockchain to ensure all blocks are linked correctly
//         // and meet proof-of-work requirements
//         boolean isValid = node.validateChain();
//         System.out.println("\nIs blockchain valid? " + isValid);
//     }
// }