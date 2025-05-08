import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L; 

    private String data;
    private long timestamp;
    private int nonce;
    private String hash;
    private String previousHash;

    public Block(String data) {
        this.data = data;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.previousHash = "";
        this.hash = calculateBlockHash();
    }

  
    public Block() {
        this("Genesis Block");
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

   
    public String calculateBlockHash() {
        String dataToHash = previousHash + Long.toString(timestamp) + Integer.toString(nonce) + data;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes("UTF-8"));

            StringBuffer buffer = new StringBuffer();
            for (byte b : hashBytes) {
                buffer.append(String.format("%02x", b));
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getHash() { return hash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    public String getPreviousHash() { return previousHash; }
    public void incrementNonce() { nonce++; }

    @Override
    public String toString() {
        return "Block{" + "data='" + data + '\'' + ", hash='" + hash + '\'' + ", previousHash='" + previousHash + '\'' + '}';
    }
}