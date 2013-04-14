/*
Copyright (c) 2013, Matt Joseph
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


University of Pittsburgh
CS1653
Homework 1
Spring Semester 2013
*/

package mgj7.cryptohw1;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class RSATest {
	
	// Constructor for comparison test
	public RSATest(boolean newKey, byte[][] testArray) {
		Security.addProvider(new BouncyCastleProvider());
		KeyPair rsaKeyPair;
		
		// Do SR once
		SecureRandom rand = new SecureRandom();
		
		// First key generation
		rsaKeyPair = generateRSAKey();
		
		for (int i = 0; i < testArray.length;i++) {
			// Encrypt the text
			encryptRSA(testArray[i], rsaKeyPair.getPublic(), rand);
			
			// Generate new key every time is it needs to
			if (newKey) {
				rsaKeyPair = generateRSAKey();
			}
		}
		
		System.out.println("RSATest done with array of length " + testArray.length);
	}
	
	// General constructor
	public RSATest(String inputText, boolean doSigTest) {
		Security.addProvider(new BouncyCastleProvider());
		Scanner input = new Scanner(System.in);
		String originalText = null;
		byte[] plainText = null;
		byte[] cipherText = null;
		KeyPair rsaKeyPair;
		SecureRandom rand = new SecureRandom();
		
		if (inputText == null) {
			// First, grab some text from the user
			System.out.print("Enter some text, then hit ENTER: ");
			originalText = input.nextLine();
			
			System.out.println("\nHere is the text you entered, just for reference:\n" + originalText + "\n");
		}
		else {
			originalText = inputText;
		}
		
		// Grab the bytes version of the text entered
		try {
			plainText = originalText.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Generate the key
		rsaKeyPair = generateRSAKey();
		
		// Encrypt the text
		cipherText = encryptRSA(plainText, rsaKeyPair.getPublic(), rand);
		
		// Print out the cipherText
		printByteArray(cipherText);
		
		// Decrypt the text
		plainText = decryptRSA(cipherText, rsaKeyPair.getPrivate(), rand);
		
		// Print out the plainText
		printByteArray(plainText);
		
		
		// Signature test. Uses SHA512 with RSA :)
		if (doSigTest) {
			sigTest(plainText);
		}
		
		input.close();
	}

	// Decrypts the specified byte array
	private byte[] decryptRSA (byte[] cipherText, PrivateKey privKey, SecureRandom rand) {
		Cipher outCipher = null;
		byte[] plainText = null;
		
		// Print start
		System.out.println("Starting decryption...");
		
		try {
			outCipher = Cipher.getInstance("RSA", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			outCipher.init(Cipher.DECRYPT_MODE, privKey, rand);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			plainText = outCipher.doFinal(cipherText);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Print start
		System.out.println("Finished decrypting.");
		
		return plainText;
	}
	
	// Encrypts the specified byte array
	private byte[] encryptRSA (byte[] plainText, PublicKey pubKey, SecureRandom rand) {
		Cipher inCipher = null;
		byte[] cipherText = null;
		
		// Print start
		System.out.println("Starting encryption...");
		
		try {
			inCipher = Cipher.getInstance("RSA", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			inCipher.init(Cipher.ENCRYPT_MODE, pubKey, rand);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			cipherText = inCipher.doFinal(plainText);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Print start
		System.out.println("Finished encryption.");
		
		return cipherText;
	}
	
	private KeyPair generateRSAKey() {
		System.out.println("Generating the RSA key...");
		KeyPairGenerator keyPairGen = null;
		
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
			keyPairGen.initialize(new RSAKeyGenParameterSpec(768, BigInteger.valueOf(65537)), new SecureRandom());
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		KeyPair kp = keyPairGen.generateKeyPair();
		
		System.out.println("Generated the public key: " + new String(Hex.encode(kp.getPublic().getEncoded())));
		System.out.println("Generated the private key: " + new String(Hex.encode(kp.getPrivate().getEncoded())));
		
		return kp;
	}
	
	// Prints a byte array as a string
	private void printByteArray(byte[] toPrint) {
		String text = null;
		try {
			text = new String(toPrint, "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Print out the text
		System.out.println("Resulting text: " + text);
	}
	
	// Test the signature portion
	public void sigTest (byte[] plainText) {
		Signature sig = null;
		KeyPair keyPair = null;
		byte[] sigBytes = null;
		boolean sigCheck = false;
		
		// Start
		System.out.println("Starting signature test..." +
				"\nFirst it will test unmodified data, then modified.");
		
		// Initialize signature
		try {
			sig = Signature.getInstance("SHA512WithRSAEncryption", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Generate a key pair
		keyPair = generateRSAKey();
		
		PrivateKey signingKey = keyPair.getPrivate();
		PublicKey verifyKey = keyPair.getPublic();
		
		try {
			sig.initSign(signingKey);
			sig.update(plainText);
			sigBytes = sig.sign();
			sig.initVerify(verifyKey);
			sig.update(plainText);
			sigCheck = sig.verify(sigBytes);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("Checking unmodified data...");
		if (!sigCheck) {
		    System.out.println("Bad signature!");
		}
		else {
			System.out.println("Good signature!");
		}
		
		// Change one byte via XOR :)
		plainText[0] = (byte)(plainText[0] ^ plainText[0]);
		
		try {
			sig.initVerify(verifyKey);
			sig.update(plainText);
			sigCheck = sig.verify(sigBytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("Checking modified data...");
		if (!sigCheck) {
		    System.out.println("Bad signature!");
		}
		else {
			System.out.println("Good signature!");
		}
	}
}
