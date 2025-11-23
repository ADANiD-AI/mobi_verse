"use client";
import React, { useState, useEffect } from 'react';
import { auth, db, storage } from '@/lib/firebase';
import { collection, query, where, onSnapshot, addDoc } from 'firebase/firestore';
import { ref, uploadBytes, getDownloadURL } from 'firebase/storage';
import { encryptFileWithPassword, decryptPackedWithPassword, createRecoveryKit, decryptRecoveryKit } from '@/lib/crypto';
import LoadingSpinner from '@/components/LoadingSpinner';

const Vault = () => {
  const [files, setFiles] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [password, setPassword] = useState('');
  
  // New state for recovery flow
  const [showRecovery, setShowRecovery] = useState(false);
  const [masterPassword, setMasterPassword] = useState('');
  const [recoveryKitFile, setRecoveryKitFile] = useState<File | null>(null);

  const uid = auth.currentUser?.uid;

  useEffect(() => {
    if (!uid) {
      setLoading(false);
      return;
    }
    const q = query(collection(db, "vault_files"), where("user_id", "==", uid));
    const unsub = onSnapshot(q, (snap) => {
      const fileList = snap.docs.map(doc => ({ id: doc.id, ...doc.data() }));
      setFiles(fileList);
      setLoading(false);
    });
    return () => unsub();
  }, [uid]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile || !password || !uid) {
      setError('Please select a file and enter a password.');
      return;
    }
    setUploading(true);
    setError(null);

    try {
      const { packedBuffer, metadata } = await encryptFileWithPassword(selectedFile, password);
      const storageRef = ref(storage, `vault/${uid}/${Date.now()}-${metadata.original_filename}.enc`);
      const uploadResult = await uploadBytes(storageRef, packedBuffer);
      const fileUrl = await getDownloadURL(uploadResult.ref);

      await addDoc(collection(db, 'vault_files'), {
        user_id: uid,
        file_name: metadata.original_filename,
        file_url: fileUrl,
        gs_path: uploadResult.ref.fullPath,
        file_size: packedBuffer.byteLength,
        is_locked: true,
        ...metadata,
        created_at: new Date(),
      });

      setSelectedFile(null);
      // Do not clear password, user might want to upload more files
    } catch (err: any) {
      setError(`Upload failed: ${err.message}`);
    } finally {
      setUploading(false);
    }
  };

  const handleDownload = async (file: any) => {
    const pw = prompt(`Enter password for ${file.file_name}`);
    if (!pw) return;

    try {
      const res = await fetch(file.file_url);
      const encryptedBuffer = await res.arrayBuffer();
      const decryptedBuffer = await decryptPackedWithPassword(encryptedBuffer, pw, file.salt, file.iv);
      const blob = new Blob([decryptedBuffer], { type: file.mime_type });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = file.file_name;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (err) {
      alert('Decryption failed. Incorrect password or corrupted file.');
      console.error(err);
    }
  };

  const handleCreateRecoveryKit = async () => {
    if (!password) {
      alert('Please enter your current vault password first.');
      return;
    }
    const masterPw = prompt('Create a NEW MASTER password for your recovery kit. This password will be used to unlock the kit.');
    if (!masterPw) {
        alert('Recovery kit creation cancelled.');
        return;
    }
    try {
        const kitJson = await createRecoveryKit(password, masterPw);
        const blob = new Blob([kitJson], { type: 'application/json' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'MobiVerse_Vault_Recovery_Kit.json';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        alert('Recovery Kit downloaded! Store it and your MASTER password in a safe place, separately.');
    } catch (err) {
        console.error(err);
        alert('Failed to create recovery kit.');
    }
  };

  const handleRecoverPassword = async () => {
    if (!recoveryKitFile || !masterPassword) {
      alert('Please select your recovery kit file and enter the master password.');
      return;
    }
    const reader = new FileReader();
    reader.onload = async (e) => {
      try {
        const kitJson = e.target?.result as string;
        const recoveredPassword = await decryptRecoveryKit(kitJson, masterPassword);
        alert(`✅ Success! Your recovered vault password is: ${recoveredPassword}\n\nWrite it down and then clear this message.`);
        setPassword(recoveredPassword);
        setShowRecovery(false);
        setMasterPassword('');
        setRecoveryKitFile(null);
      } catch (err) {
        console.error(err);
        alert('❌ Recovery failed. Incorrect master password or corrupted kit file.');
      }
    };
    reader.readAsText(recoveryKitFile);
  };

  if (showRecovery) {
    return (
      <div style={{ padding: '20px', color: '#333', background: '#fff', height: '100%' }}>
        <h2>Recover Vault Password</h2>
        <p>Upload your Recovery Kit (.json file) and enter the MASTER password you created with it.</p>
        <div style={{ marginBottom: '15px' }}>
            <label>Recovery Kit File (.json)</label>
            <input type="file" accept=".json" onChange={(e) => setRecoveryKitFile(e.target.files ? e.target.files[0] : null)} />
        </div>
        <div style={{ marginBottom: '15px' }}>
            <input type="password" value={masterPassword} onChange={(e) => setMasterPassword(e.target.value)} placeholder="Enter Master Password" style={{ padding: '8px' }} />
        </div>
        <button onClick={handleRecoverPassword}>Recover Password</button>
        <button onClick={() => setShowRecovery(false)} style={{ marginLeft: '10px' }}>Cancel</button>
      </div>
    );
  }

  return (
    <div style={{ padding: '20px', color: '#333', background: '#fff', height: '100%', overflowY: 'auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>Secure Vault</h2>
        <a href="#" onClick={(e) => { e.preventDefault(); setShowRecovery(true); }}>Forgot Password?</a>
      </div>
      <p>Files are encrypted in your browser. Enter your password below to manage files.</p>
      
      <div style={{ border: '1px solid #ccc', padding: '15px', borderRadius: '8px', marginBottom: '20px' }}>
        <h3>Vault Password</h3>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Enter Your Main Vault Password"
          style={{ padding: '8px', width: 'calc(100% - 20px)', marginBottom: '10px' }}
        />
        <button onClick={handleCreateRecoveryKit} disabled={!password}>Create & Download Recovery Kit</button>
      </div>

      <div style={{ border: '1px solid #ccc', padding: '15px', borderRadius: '8px', marginBottom: '20px' }}>
        <h3>Upload New File</h3>
        <input type="file" onChange={handleFileChange} style={{ marginBottom: '10px' }} disabled={!password} />
        <button onClick={handleUpload} disabled={uploading || !selectedFile || !password}>
          {uploading ? 'Encrypting & Uploading...' : 'Upload'}
        </button>
        {uploading && <LoadingSpinner />}
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <h3>Your Encrypted Files</h3>
      {loading ? <LoadingSpinner /> : (
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {files.map(file => (
            <li key={file.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px', borderBottom: '1px solid #eee' }}>
              <span>🔒 {file.file_name} ({(file.file_size / 1024).toFixed(2)} KB)</span>
              <button onClick={() => handleDownload(file)}>Download & Decrypt</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Vault;
