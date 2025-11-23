<<<<<<< HEAD
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

=======
#MobiVerse🤖

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/Adnanmd76/Z-Ai)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An intelligent Android application that provides an AI-powered choice selection interface with modern Material Design 3 UI, smart notifications, and auto-start capabilities.

## ✨ Features

- **🎯 AI-Powered Choice Selection**: Intelligent choice recommendation system
- **🎨 Material Design 3**: Modern UI following Google's latest design guidelines
- **🔔 Smart Notifications**: Context-aware notification system
- **🚀 Auto-Start**: Optional automatic app launch on device boot
- **💾 Choice Persistence**: Save and restore user choices
- **⚡ Performance Optimized**: Efficient memory usage and fast response

## 🏗️ Architecture

```
Z-Ai/
├── app/
│   ├── src/main/
│   │   ├── java/com/zai/choicescreen/
│   │   │   ├── ChoiceActionReceiver.java    # Choice action handling
│   │   │   └── BootReceiver.java            # Auto-start functionality
│   │   └── res/
│   │       ├── drawable/
│   │       │   └── ic_notification.xml      # Notification icon
│   │       └── values/
│   │           ├── strings.xml              # String resources
│   │           └── themes.xml               # Material Design themes
│   └── proguard-rules.pro                   # ProGuard configuration
├── build.gradle                             # Project-level build script
├── settings.gradle                          # Project settings
├── gradle.properties                        # Gradle properties
└── README.md                                # This file
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- Android SDK API level 24 (Android 7.0) or higher
- Java JDK 8 or higher
- Gradle 8.4 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Adnanmd76/Z-Ai.git
   cd Z-Ai
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Build the project**
   ```bash
   ./gradlew build
   ```

## 🔧 Configuration

### Build Configuration

- **Compile SDK**: 34 (Android 14)
- **Target SDK**: 34
- **Min SDK**: 24 (Android 7.0)
- **Kotlin**: 1.9.20
- **AGP**: 8.1.4

### Key Components

#### ChoiceActionReceiver
Handles choice-related broadcast actions:
- Choice selection processing
- Choice confirmation handling
- Notification management
- SharedPreferences integration

#### BootReceiver
Manages auto-start functionality:
- Device boot detection
- Auto-start preference checking
- Background service initialization
- Boot statistics tracking

#### Material Design 3 Theming
- Light, dark, and system themes
- Custom button styles
- Card-based layouts
- Accessibility support

## 🛠️ Development

### Building for Release

```bash
# Build release APK
./gradlew assembleRelease

# Build App Bundle
./gradlew bundleRelease
```

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Performance Optimization

- **R8 Code Shrinking**: Removes unused code
- **ProGuard Obfuscation**: Protects and optimizes code
- **Resource Shrinking**: Removes unused resources
- **Build Cache**: Faster incremental builds
- **Parallel Execution**: Multi-threaded compilation

## 📊 Project Status

### Completed ✅
- [x] Project structure setup
- [x] Material Design 3 theming system
- [x] Choice action handling (ChoiceActionReceiver)
- [x] Auto-start functionality (BootReceiver)
- [x] Notification system implementation
- [x] ProGuard/R8 optimization configuration
- [x] Build performance optimizations
- [x] String resources and themes
- [x] Notification icon design
- [x] Comprehensive documentation

### TODO 📋
- [ ] Implement MainActivity with choice selection UI
- [ ] Create app manifest with proper permissions
- [ ] Add app-level build.gradle configuration
- [ ] Design choice selection layouts
- [ ] Add Material Design color resources
- [ ] Create app launcher icons
- [ ] Implement choice processing logic
- [ ] Add comprehensive unit tests
- [ ] Create instrumentation tests
- [ ] Add user documentation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Development Guidelines

- Follow Android development best practices
- Write clear, concise commit messages
- Add tests for new features
- Update documentation as needed
- Follow the existing code style

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Muhammad Adnan Ul Mustafa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

## 👨‍💻 Author

**Muhammad Adnan Ul Mustafa**
- Email: [adnanmd76@gmail.com](mailto:adnanmd76@gmail.com)
- GitHub: [@Adnanmd76](https://github.com/Adnanmd76)
- Project: [Z-AI Choice Screen](https://github.com/Adnanmd76/Z-Ai)

## 🙏 Acknowledgments

- Google for Android development tools and Material Design
- AndroidX team for modern Android libraries
- Open source community for inspiration and resources
- Material Design team for the excellent design system

## 📞 Support

If you have any questions, issues, or suggestions:

1. **GitHub Issues**: [Create an issue](https://github.com/Adnanmd76/Z-Ai/issues)
2. **Email**: [adnanmd76@gmail.com](mailto:adnanmd76@gmail.com)
3. **Documentation**: Check this README and code comments

---

<div align="center">
  <p><strong>Made with ❤️ for the Android community</strong></p>
  <p>⭐ Star this repository if you find it helpful!</p>
</div>
>>>>>>> 55bd7b5c92be6666b58530779ed811bdc8a815e6
