# 🔐 AuthModule - Android User Authentication Module

A plug-and-play **User Authentication Module** built in **Android Studio** using **MVVM architecture**, **Manual Dependency Injection**, and **Firebase Authentication**, all wrapped in **Clean Architecture** principles.

> Easily integrate this module into any Android app that requires a user authentication system. Just set up Firebase, and you're good to go.

---

## 📦 Features

- 🔑 **User Authentication** via Firebase (Email/Password)
- 🧱 **MVVM Architecture** for modularity and testability
- 🧼 **Clean Architecture** separation (UI / Domain / Data)
- ⚙️ **Manual Dependency Injection** (No Dagger/Hilt)
- ✅ Easy to integrate into any existing project
- 📃 Proper error handling and UI states (loading, success, error)

---

## 🧱 Architecture Overview


- **core/di**: manual wiring of repositories, use‑cases, view‑models (no Hilt/Dagger).
- **core/navigation**: definitions for app navigation graph & routes.
- **data/**: data layer (repo impl, external auth managers).
- **domain/**: repository interface.
- **presentation/**: UI layer (Compose/screens, ViewModels).

---

## 🛠️ Getting Started

### 1. Clone the Repo



git clone https://github.com/siddhantnirhali/AuthModule.git
---

## 2. Integrate the UserAuth Module into Your Project

 **1. Include module in settings.gradle.kts:**
        include(":userauth")
 **2. Add dependency in your app’s build.gradle.kts:**
        implementation(project(":userauth"))
 **3. Set namespace in your app’s build.gradle.kts:**
        android {
            namespace = "com.example.userauth"
            // …
        }
 **4. Add Firebase Web Client ID to local.properties:**
        WEB_CLIENT_ID="your_firebase_web_client_id"
 
Now the module is ready to use!

## 🔥 Firebase Setup

**1. Go to Firebase Console**

**2. Create (or select) your project**

**3. Under Authentication**
    → Sign-in method, enable Email/Password
    → Sign-in method, enable Google login

**Download google-services.json and place it in:**
    app/google-services.json

**Ensure your Gradle is set up:** 
    Project‑level build.gradle: classpath "com.google.gms:google-services:4.3.15"