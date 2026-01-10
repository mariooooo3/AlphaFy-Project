# 🎵 AlphaFy – Java Music Player Application

AlphaFy is a Java desktop application for music playback, built to demonstrate **OOP principles** and modular design. It supports playlists, albums, folders, and a flexible playback system.

---

## 🏗️ **Project Architecture**

| Module | Link | Functionality |
| :--- | :--- | :--- |
| **Main App** | [AlphaFy.java](AlphaFy/AlphaFy.java) | Application entry point |
| **GUI** | [Interface.java](GUI/Interface.java), [Interface.form](GUI/Interface.form) | Graphical interface (Swing) |
| **Media** | [Album.java](Media/Album.java), [Song.java](Media/Song.java), [PlayList.java](Media/PlayList.java), [Folder.java](Media/Folder.java) | Handles songs, albums, playlists, and folder structures |
| **Playback** | [Playable.java](Playback/Playable.java), [CompositePlayable.java](Playback/CompositePlayable.java) | Defines playback behavior using Composite Pattern |

---

## 📝 **Key Features**

* 🎶 Music playback via `Playable` (songs, albums, playlists)
* 🧩 Composite Pattern for grouped playback
* 🖥️ Intuitive GUI using Swing
* 📂 Media organization into folders and playlists

---

## 📚 **Technologies**

* Java OOP
* Design Pattern: Composite
* Java Swing
* Modular Architecture

---

## 🚀 **Getting Started**

1. Clone the repository
2. Install **Git LFS** for large audio files
3. Open the project in **IntelliJ IDEA**
4. Run [AlphaFy.java](AlphaFy/AlphaFy.java)

---

## 📦 **Resources**

* Audio files (`.wav`) and background images are managed with **Git LFS** for efficient repository size.
