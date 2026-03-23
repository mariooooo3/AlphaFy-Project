# AlphaFy – Java Music Player Application

AlphaFy is a Java desktop music player designed to showcase **OOP principles** and a clean, modular architecture. The application allows users to manage and play songs, albums, playlists, and folders with a flexible and intuitive playback system.

---

## Project Architecture

| Module | Link | Functionality |
|:------|:------|:---------------|
| **Main App** | [AlphaFy.java](./src/AlphaFy/AlphaFy.java) | Application entry point |
| **GUI** | [Interface.java](./src/GUI/Interface.java), [Interface.form](./src/GUI/Interface.form) | Graphical interface (Swing) |
| **Media** | [Album.java](./src/Media/Album.java), [Song.java](./src/Media/Song.java), [PlayList.java](./src/Media/PlayList.java), [Folder.java](./src/Media/Folder.java) | Handles songs, albums, playlists, and folder structures |
| **Playback** | [Playable.java](./src/Playback/Playable.java), [CompositePlayable.java](./src/Playback/CompositePlayable.java) | Defines playback behavior using Composite Pattern |

---

## Key Features

* Music playback via `Playable` (songs, albums, playlists)
* Composite Pattern for grouped playback
* Intuitive GUI using Swing
* Media organization into folders and playlists

---

## Technologies

* Java OOP
* Design Pattern: Composite
* Java Swing
* Modular Architecture

---

## Getting Started

1. Clone the repository
2. Install **Git LFS** for large audio files
3. Open the project in **IntelliJ IDEA**
4. Run [AlphaFy.java](./src/AlphaFy.java)

---

## Resources

* Audio files (`.wav`) and background images are managed with **Git LFS** for efficient repository size.