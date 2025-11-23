/**
 * This file contains client-side cryptographic functions for the Secure Vault.
 * It uses the Web Crypto API for strong encryption in the browser.
 * No plaintext passwords or keys are ever sent to the server.
 */

/**
 * Converts an ArrayBuffer to a base64 string.
 */
export function ab2b64(buf: ArrayBuffer): string {
  return btoa(String.fromCharCode(...new Uint8Array(buf)));
}

/**
 * Converts a base64 string to an ArrayBuffer.
 */
export function b642ab(b64: string): ArrayBuffer {
  const bin = atob(b64);
  const arr = new Uint8Array(bin.length);
  for (let i = 0; i < bin.length; i++) {
    arr[i] = bin.charCodeAt(i);
  }
  return arr.buffer;
}

/**
 * Derives a cryptographic key from a password using PBKDF2.
 * This is a standard key derivation function that adds a salt and iterations
 * to protect against rainbow table and brute-force attacks.
 */
export async function deriveKeyFromPassword(password: string, salt: Uint8Array, iterations = 150000): Promise<CryptoKey> {
  const enc = new TextEncoder();
  const baseKey = await crypto.subtle.importKey("raw", enc.encode(password), "PBKDF2", false, ["deriveKey"]);
  return crypto.subtle.deriveKey(
    { name: "PBKDF2", salt, iterations, hash: "SHA-256" },
    baseKey,
    { name: "AES-GCM", length: 256 }, // AES-GCM is an authenticated encryption mode
    true,
    ["encrypt", "decrypt"]
  );
}

/**
 * Encrypts a file using a password. The file is encrypted entirely in the browser.
 * Returns a packed ArrayBuffer (salt + iv + ciphertext) and metadata for storage.
 */
export async function encryptFileWithPassword(file: File, password: string): Promise<{ packedBuffer: ArrayBuffer; metadata: any; }> {
  const salt = crypto.getRandomValues(new Uint8Array(16));
  const iv = crypto.getRandomValues(new Uint8Array(12));
  const key = await deriveKeyFromPassword(password, salt);
  const plain = await file.arrayBuffer();
  const cipher = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, plain);

  const packed = new Uint8Array(salt.byteLength + iv.byteLength + cipher.byteLength);
  packed.set(salt, 0);
  packed.set(iv, salt.byteLength);
  packed.set(new Uint8Array(cipher), salt.byteLength + iv.byteLength);

  return {
    packedBuffer: packed.buffer,
    metadata: {
      encryption: "AES-GCM-256",
      salt: ab2b64(salt.buffer),
      iv: ab2b64(iv.buffer),
      keyDerivation: { method: "PBKDF2", iterations: 150000, hash: "SHA-256" },
      original_filename: file.name,
      mime_type: file.type,
    }
  };
}

/**
 * Decrypts a packed buffer using a password and the stored metadata.
 * The decryption happens entirely in the browser.
 */
export async function decryptPackedWithPassword(packedBuffer: ArrayBuffer, password: string, saltB64: string, ivB64: string, iterations = 150000): Promise<ArrayBuffer> {
  const salt = b642ab(saltB64);
  const iv = b642ab(ivB64);
  const key = await deriveKeyFromPassword(password, new Uint8Array(salt), iterations);
  const ciphertext = packedBuffer.slice(salt.byteLength + iv.byteLength);

  return crypto.subtle.decrypt({ name: "AES-GCM", iv: new Uint8Array(iv) }, key, ciphertext);
}


// --- Password Recovery Kit Functions ---

/**
 * Creates an encrypted recovery kit for the user's vault password.
 * The vault password itself is encrypted with a separate, user-provided master password.
 * Returns a JSON string representing the recovery kit, ready to be saved as a file.
 */
export async function createRecoveryKit(vaultPassword: string, masterPassword: string): Promise<string> {
    const salt = crypto.getRandomValues(new Uint8Array(16));
    const iv = crypto.getRandomValues(new Uint8Array(12));
    const iterations = 150000;
    const key = await deriveKeyFromPassword(masterPassword, salt, iterations); // Key from MASTER password
    const enc = new TextEncoder();
    const cipherBuffer = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, enc.encode(vaultPassword)); // Encrypt VAULT password

    const kit = {
        description: "MobiVerse Vault Recovery Kit. DO NOT SHARE. Store this file and your master password safely and separately.",
        encryption: "AES-GCM-256",
        keyDerivation: { method: "PBKDF2", iterations, hash: "SHA-256" },
        salt: ab2b64(salt.buffer),
        iv: ab2b64(iv.buffer),
        encryptedData: ab2b64(cipherBuffer)
    };

    return JSON.stringify(kit, null, 2);
}

/**
 * Decrypts a recovery kit using the master password.
 * Returns the original vault password if successful.
 */
export async function decryptRecoveryKit(kitJson: string, masterPassword: string): Promise<string> {
    const kit = JSON.parse(kitJson);
    const salt = b642ab(kit.salt);
    const iv = b642ab(kit.iv);
    const cipher = b642ab(kit.encryptedData);
    const iterations = kit.keyDerivation.iterations || 150000;

    const key = await deriveKeyFromPassword(masterPassword, new Uint8Array(salt), iterations);
    const plainBuffer = await crypto.subtle.decrypt({ name: "AES-GCM", iv: new Uint8Array(iv) }, key, cipher);

    const dec = new TextDecoder();
    return dec.decode(plainBuffer); // Returns the original vault password
}
