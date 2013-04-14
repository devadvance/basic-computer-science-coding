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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class AESTest {
	// Constructor for comparison test
	public AESTest(boolean newKey, byte[][] testArray) {
		Security.addProvider(new BouncyCastleProvider());
		Key aesKey = null;
		
		// Generate the IV once
		IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
		
		aesKey = generateAESKey();
		
		for (int i = 0; i < testArray.length;i++) {
			// Encrypt the text
			encryptAES(testArray[i], aesKey, ivSpec);
			
			// Generate new key every time is it needs to
			if (newKey) {
				aesKey = generateAESKey();
			}
		}
		
		System.out.println("AESTest done with array of length " + testArray.length);
	}
	
	// General constructor
	public AESTest(String inputText) {
		Security.addProvider(new BouncyCastleProvider());
		Scanner input = new Scanner(System.in);
		String originalText = null;
		byte[] plainText = null;
		byte[] cipherText = null;
		Key aesKey = null;
		IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
		
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
		aesKey = generateAESKey();
		
		// Encrypt the text
		cipherText = encryptAES(plainText, aesKey, ivSpec);
		
		// Print out the cipherText
		printByteArray(cipherText);
		
		// Decrypt the text
		plainText = decryptAES(cipherText, aesKey, ivSpec);
		
		// Print out the plainText
		printByteArray(plainText);
		
		input.close();
	}
	
	// Generates a new AES 128 key
	private Key generateAESKey () {
		KeyGenerator AESkeygen = null;
		Key AES128key = null;
		// Generate an AES128 key
		System.out.println("Generating an AES 128 key...");
		
		try {
			AESkeygen = KeyGenerator.getInstance("AES", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Initialize with a key size of 128
		AESkeygen.init(128);
		
		// Actual key generation
		AES128key = AESkeygen.generateKey();
		
		// Print it out
		System.out.println("Generated the key: " + new String(Hex.encode(AES128key.getEncoded())));
		
		return AES128key;
	}
	
	// Encrypts the specified byte array
	private byte[] encryptAES (byte[] plainText, Key key, IvParameterSpec ivSpec) {
		Cipher inCipher = null;
		byte[] cipherText = null;
		
        System.out.println("Starting encryption...");
		
		// Creates the AES Cipher object with CBC
		try {
			inCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Initialize the Cipher object.
        try {
			inCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Actual encryption 
		try {
			cipherText = inCipher.doFinal(plainText);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished encrypting.");
		
		return cipherText;
	}
	
	// Decrypts the specified byte array
	private byte[] decryptAES (byte[] cipherText, Key key, IvParameterSpec ivSpec) {
		Cipher outCipher = null;
		byte[] plainText = null;
		
		// Decryption
		System.out.println("Starting decryption...");
		
		// Creates the AES Cipher object with CBC
		try {
			outCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Initialize the Cipher object
		try {
			outCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Actual decryption
		try {
			plainText = outCipher.doFinal(cipherText);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Finished decrypting.");
		
		return plainText;
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
}