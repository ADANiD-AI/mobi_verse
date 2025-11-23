# Nebula Messenger 

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue.svg" alt="Version">
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Status-Active-brightgreen.svg" alt="Status">
</p>

<p align="center">
  <b>A modern, offline-first, and secure voice messaging system built for the future.</b>
</p>

<details>
<summary><strong>اردو ترجمہ (Urdu Translation)</strong></summary>
<p align="right">
  <b>ایک جدید، آف لائن فرسٹ، اور محفوظ وائس میسجنگ سسٹم جو مستقبل کے لیے تیار ہے۔</b>
</p>
</details>

---

Nebula Messenger is not just another chat app; it's a hyper-reliable and sophisticated communication platform engineered with cutting-edge technology. Its core design philosophy is to provide a seamless experience that remains fully functional even without an internet connection.

<details>
<summary><strong>اردو ترجمہ (Urdu Translation)</strong></summary>
<p align="right">
نیبیولا میسنجر صرف ایک اور چیٹ ایپ نہیں ہے، بلکہ یہ ایک انتہائی قابل اعتماد اور جدید مواصلاتی پلیٹ فارم ہے جسے جدید ترین ٹیکنالوجی کی بنیاد پر بنایا گیا ہے۔ اس کا بنیادی مقصد ایک ایسا تجربہ فراہم کرنا ہے جو انٹرنیٹ کنکشن کے بغیر بھی مکمل طور پر فعال رہے۔
</p>
</details>

## 🔮 Core Features

*   **📡 Offline-First Architecture:** Send messages even when offline. They sync automatically and reliably the moment a connection is re-established.
*   **⚡ Real-time Reactive UI:** A blazing-fast and modern UI built with Jetpack Compose that updates instantly with new messages.
*   **⚙️ Resilient Background Sync:** Leverages Android's `WorkManager` to ensure your messages are sent reliably in the background, even if the app is closed.
*   **☁️ Cloud-Native Backend:** Fully integrated with Firebase (Firestore, Storage, Auth) for a robust and scalable backend.
*   **🎙️ Full Voice Messaging Cycle:** Complete audio recording, sending, receiving, and playback capabilities.
*   **🛡️ Secure by Design:** Firebase Security Rules ensure that users can only access their own messages.
*   **🏗️ Modern Architecture:** Built on an MVVM (Model-View-ViewModel) architecture, keeping the codebase clean, scalable, and maintainable.

<details>
<summary><strong>اردو ترجمہ (Urdu Translation)</strong></summary>
<ul dir="rtl">
  <li><b>آف لائن فرسٹ (Offline-First):</b> انٹرنیٹ نہ ہونے پر بھی پیغامات بھیجیں۔ جیسے ہی کنکشن بحال ہوگا، یہ خود بخود مطابقت پذیر (sync) ہوجائے گا۔</li>
  <li><b>ریئل ٹائم UI:</b> Jetpack Compose کے ساتھ بنایا گیا ایک انتہائی تیز اور جدید انٹرفیس جو نئے پیغامات کے ساتھ فوری طور پر اپ ڈیٹ ہوتا ہے۔</li>
  <li><b>پس منظر میں مطابقت پذیری (Background Sync):</b> Android کے `WorkManager` کا استعمال کرتے ہوئے، یہ یقینی بناتا ہے کہ آپ کے پیغامات قابل اعتماد طریقے سے پس منظر میں بھیجے جائیں، چاہے ایپ بند ہی کیوں نہ ہو۔</li>
  <li><b>کلاؤڈ انٹیگریشن:</b> Firebase (Firestore, Storage, Auth) کے ساتھ مکمل طور پر مربوط، جو ایک مضبوط اور قابل توسیع (scalable) بیک اینڈ فراہم کرتا ہے۔</li>
  <li><b>وائس میسجنگ سائیکل:</b> مکمل آڈیو ریکارڈنگ، بھیجنے، وصول کرنے اور پلے بیک کی صلاحیت۔</li>
  <li><b>محفوظ ڈیٹا بیس:</b> Firebase Security Rules اس بات کو یقینی بناتے ہیں کہ کوئی بھی صارف صرف اپنے پیغامات تک رسائی حاصل کر سکتا ہے۔</li>
  <li><b>جدید فن تعمیر (Modern Architecture):</b> MVVM (Model-View-ViewModel) فن تعمیر پر بنایا گیا ہے، جو کوڈ کو صاف، منظم اور قابل انتظام بناتا ہے۔</li>
</ul>
</details>

##  Architectural Diagram

This diagram illustrates the entire system flow, showing how each component interacts.

```mermaid
graph TD
    subgraph "Android App (Client)"
        A[ChatScreen UI (Compose)] -- User Action --> B(ChatViewModel)
        B -- Sends/Receives Data --> C{MessageRepository}
        C -- Caches/Reads Local Data --> D[(Room Database)]
        B -- Initiates Send --> E{SatelliteMessenger}
        E -- Persists Message Locally --> D
        E -- Enqueues Upload Task --> F(WorkManager)
        A -- Plays Audio --> G(AudioPlayer)
        A -- Records Audio --> H(AudioRecorder)
    end

    subgraph "Firebase (Backend)"
        I[(Firebase Storage)]
        J[(Firestore Database)]
        K[(Firebase Auth)]
    end

    F -- Uploads Audio File --> I
    F -- Updates Message Status --> J
    C -- Listens for New Messages --> J
    B -- Authenticates User --> K

    style A fill:#bde0fe,stroke:#333,stroke-width:2px
    style B fill:#a2d2ff,stroke:#333,stroke-width:2px
    style C fill:#ffafcc,stroke:#333,stroke-width:2px
    style E fill:#ffc8dd,stroke:#333,stroke-width:2px
    style F fill:#cdb4db,stroke:#333,stroke-width:2px
```

## 🛠️ Technology Stack

| Component         | Technology                               |
| ----------------- | ---------------------------------------- |
| **Language**      | Kotlin                                   |
| **UI**            | Jetpack Compose                          |
| **Architecture**  | MVVM, Repository, Clean Architecture     |
| **Backend**       | Firebase (Firestore, Storage, Auth)      |
| **Local Database**| Room Persistence Library                 |
| **Background Jobs**| Android WorkManager                      |

## 🛡️ Security Rules

Our app's security is paramount. We have implemented strict Firestore rules to ensure data integrity and user privacy.

<details>
<summary>Click to view `firestore.rules`</summary>

```json
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {

    // Helper function to check if the user is the authenticated sender
    function isSender(userId) {
      return request.auth.uid == userId;
    }

    // Messages can only be created by the sender.
    // A user can read a message if they are the sender or the receiver.
    match /messages/{messageId} {
      allow create: if isSender(request.resource.data.senderId);
      allow read: if isSender(resource.data.senderId) || isSender(resource.data.receiverId);
      
      // Updates and deletes are disallowed to maintain message history integrity.
      allow update, delete: if false;
    }
  }
}
```
</details>

## 🚀 Getting Started

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    ```
2.  **Open in Android Studio:** Import the project into the latest version of Android Studio.
3.  **Connect to Firebase:**
    *   Create a new project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add a new Android app to the project with the package name `com.mobiverse.nebula`.
    *   Download the `google-services.json` file and place it in the `app/` directory.
    *   Enable **Authentication** (Anonymous), **Firestore**, and **Storage** in the Firebase Console.
4.  **Build and Run:** Build the app and run it on an emulator or a physical device.

