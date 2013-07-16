package net.peaxy.simulator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;

public class RSA {

	private final static SecureRandom random = new SecureRandom();
	private BigInteger a;
	private BigInteger b;
	private BigInteger n;
	private BigInteger p;
	private BigInteger q;
	private Hashtable<String, BigInteger> publicKey;
	private Hashtable<String, BigInteger> privateKey;

	/**
	 * get private key
	 * @return
	 */
	public Hashtable<String, BigInteger> getPrivateKey() {
		return privateKey;
	}

	/**
	 * set private key
	 * 
	 * @param privateKey
	 */
	public void setPrivateKey(Hashtable<String, BigInteger> privateKey) {
		this.privateKey = privateKey;
		p = privateKey.get("p");
		q = privateKey.get("q");
		a = privateKey.get("a");
		n = p.multiply(q);
	}

	/**
	 * get public key
	 * 
	 * @return 
	 */
	public Hashtable<String, BigInteger> getPublicKey() {
		return publicKey;
	}

	/**
	 * set public key
	 * 
	 * @param publicKey
	 */
	public void setPublicKey(Hashtable<String, BigInteger> publicKey) {
		this.publicKey = publicKey;
		n = publicKey.get("n");
		b = publicKey.get("b");
	}

	/**
	 * generate public key and private key
	 * 
	 * @param N
	 */
	public void genKey(int N) {
		p = BigInteger.probablePrime(N / 2, random);
		q = BigInteger.probablePrime(N / 2, random);
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q
				.subtract(BigInteger.ONE));
		n = p.multiply(q);
		b = new BigInteger("58900642106251431061251445073797446601895923308559852248833758533446853223283");
		a = b.modInverse(phi);
		publicKey = new Hashtable<String, BigInteger>();
		privateKey = new Hashtable<String, BigInteger>();
		publicKey.put("n", n);
		publicKey.put("b", b);
		privateKey.put("p", p);
		privateKey.put("q", q);
		privateKey.put("a", a);
	}

	/**
	 * encrypt data
	 * 
	 * @param plainText
	 */
	public byte[] encrypt(byte[] plainText) {
		return new BigInteger(plainText).modPow(b, n).toByteArray();
	}

	/**
	 * decrypt 
	 * 
	 * @param cipherText
	 */
	public byte[] decrypt(byte[] cipherText) {
		return new BigInteger(cipherText).modPow(a, n).toByteArray();
	}

	public static void main(String[] args) throws Exception {
		RSA rsa = new RSA();
		rsa.genKey(1024);
		BufferedWriter keyout = new BufferedWriter(
				new FileWriter("private.key"));
		keyout.write(rsa.getPrivateKey().get("p").toString());
		keyout.newLine();
		keyout.write(rsa.getPrivateKey().get("q").toString());
		keyout.newLine();
		keyout.write(rsa.getPrivateKey().get("a").toString());
		keyout.newLine();
		keyout.flush();
		keyout.close();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
		baos.write("Exception in thread \"main\" java.lang.NumberFormatException: Zero length BigInteger".getBytes());
		byte[] cipher = rsa.encrypt(baos.toByteArray());
		FileOutputStream fileout = new FileOutputStream(
				new File("license.fil"));
		fileout.write(cipher);
		fileout.flush();
		fileout.close();
		
		BufferedReader keyin = new BufferedReader(new FileReader("private.key"));
		RSA rsa1 = new RSA();
		Hashtable<String, BigInteger> privateKey = new Hashtable<String, BigInteger>();
		privateKey.put("p", new BigInteger(keyin.readLine()));
		privateKey.put("q", new BigInteger(keyin.readLine()));
		privateKey.put("a", new BigInteger(keyin.readLine()));
		rsa1.setPrivateKey(privateKey);
		keyin.close();
		FileInputStream filein = new FileInputStream("license.fil");
		byte[] cp = new byte[filein.available()];
		filein.read(cp);
		filein.close();
		//byte[] plain = rsa1.decrypt(cp);
		//System.out.println(new String(plain));
	}
}