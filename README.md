# Mobiverse 🤖

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue.svg" alt="Version">
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform">
  <img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg" alt="API">
</p>

<p align="center">
  <b>An intelligent Android application featuring an AI-powered choice interface and offline-first voice messaging.</b>
</p>

---

Mobiverse is a hyper-reliable and sophisticated communication platform engineered with cutting-edge technology. It combines an intelligent choice-recommendation system with a seamless, offline-first voice messaging experience that remains fully functional even without an internet connection.

## 🔮 Core Features

*   **🎯 AI-Powered Choice Selection**: An intelligent system that provides smart recommendations and choice selection.
*   **📡 Offline-First Architecture:** Send voice messages even when offline. They sync automatically and reliably the moment a connection is re-established.
*   **⚡ Real-time Reactive UI:** A blazing-fast and modern UI built with Jetpack Compose (Material Design 3) that provides a fluid user experience.
*   **⚙️ Resilient Background Sync:** Leverages Android's `WorkManager` to ensure your messages are sent reliably in the background.
*   **☁️ Cloud-Native Backend:** Fully integrated with Firebase (Firestore, Storage, Auth) for a robust and scalable backend.
*   **🎙️ Full Voice Messaging Cycle:** Complete audio recording, sending, receiving, and playback capabilities.
*   **🛡️ Secure by Design:** Firebase Security Rules ensure that users can only access their own data.
*   **🚀 Auto-Start**: Optional automatic app launch on device boot for seamless operation.

## 🚀 Getting Started

Follow these steps to get the project up and running on your local machine.

### Prerequisites

*   Android Studio (Latest Version Recommended)
*   Android SDK API level 24 (Android 7.0) or higher

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/ADANiD-AI/mobi_verse.git
    cd mobi_verse
    ```
2.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open" and navigate to the cloned `mobi_verse` directory.
3.  **Connect to Firebase:**
    *   Create a new project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add a new Android app to the project with the package name `com.mobiverse.chat`. (Please verify the package name in your project).
    *   Download the `google-services.json` file and place it in the `app/` directory.
    *   Enable **Authentication** (e.g., Anonymous), **Firestore**, and **Storage** in the Firebase Console.
4.  **Build and Run:** Build the app and run it on an emulator or a physical device.

## 🛠️ Technology Stack

| Component         | Technology                               |
| ----------------- | ---------------------------------------- |
| **Language**      | Kotlin                                   |
| **UI**            | Jetpack Compose (Material 3)             |
| **Architecture**  | MVVM, Repository                         |
| **Backend**       | Firebase (Firestore, Storage, Auth)      |
| **Local Database**| Room Persistence Library                 |
| **Background Jobs**| Android WorkManager                      |


## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new feature branch: `git checkout -b feature/your-amazing-feature`
3.  Commit your changes: `git commit -m 'Add your amazing feature'`
4.  Push to the branch: `git push origin feature/your-amazing-feature`
5.  Open a Pull Request.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

Copyright (c) 2024 Muhammad Adnan Ul Mustafa

## 👨‍💻 Author

**Muhammad Adnan Ul Mustafa**
- **GitHub:** [@Adnanmd76](https://github.com/Adnanmd76)
- **Project Repository:** [mobi_verse](https://github.com/ADANiD-AI/mobi_verse)

---

<div align="center">
  <p><strong>Made with ❤️ for the Android community</strong></p>
  <p>⭐ Star this repository if you find it helpful!</p>
</div>
gradlew.bat assembleRelease
