package com.example.sen800_project;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RSAHelper {

    private BigInteger n, phi, e, d;

    // Encrypt the password using RSA
    public List<BigInteger> encrypt(String plaintext, BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;

        List<BigInteger> encryptedBlocks = new ArrayList<>();
        byte[] data = plaintext.getBytes();

        for (byte b : data) {
            BigInteger m = BigInteger.valueOf(b & 0xFF);  // convert byte to unsigned
            BigInteger c = m.modPow(e, n); // Encryption: c = m^e % n
            encryptedBlocks.add(c);
        }

        return encryptedBlocks;
    }

    // Decrypt the password using RSA
    public String decrypt(List<BigInteger> encryptedBlocks, BigInteger d, BigInteger n) {
        this.d = d;
        this.n = n;

        StringBuilder result = new StringBuilder();

        for (BigInteger c : encryptedBlocks) {
            BigInteger m = c.modPow(d, n); // Decryption: m = c^d % n
            result.append((char) m.intValue());
        }

        return result.toString();
    }

    // Method to generate d (private key) from e (public key) and phi(n)
    public BigInteger generatePrivateKey(BigInteger e, BigInteger phi) {
        return e.modInverse(phi);
    }

    // Method to compute n and phi for two primes p and q
    public void generateKeys(BigInteger p, BigInteger q) {
        n = p.multiply(q);
        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = new BigInteger("65537"); // Common choice for e

        // Generate d (private key)
        d = e.modInverse(phi);
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }
}
