# NgPad Android

NgPad is a modern Android application designed to provide interactive programming tutorials, quizzes, and learning resources. The app features a clean UI, offline-first data caching, and a modular architecture for maintainability and scalability.

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Interactive Tutorials:** Browse and read programming tutorials organized by categories and courses.
- **Quizzes:** Test your knowledge with topic-based quizzes and view detailed results.
- **Bookmarks:** Save lessons for quick access.
- **Offline Support:** Data is cached locally for offline usage.
- **Modern UI:** Material Design components and smooth navigation.
- **SVG Support:** Efficient SVG loading and caching for course and section icons.
- **Expandable Settings:** Customize font size and theme from the navigation drawer.

## Screenshots

<!-- Add screenshots here if available -->
<!--
![Home Screen](screenshots/home.png)
![Quiz Result](screenshots/quiz_result.png)
-->

## Architecture

NgPad follows modern Android architecture best practices:

- **MVVM Pattern:** Separation of concerns using ViewModel, LiveData, and Repository.
- **Room Database:** Local data persistence and caching.
- **Retrofit:** Networking and API integration.
- **Data Binding:** Efficient UI updates.
- **Executors & Handlers:** Background threading for smooth UI.

## Getting Started

### Prerequisites

- Android Studio (latest recommended)
- Android SDK 24+
- Java 8+

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Chaos-19/NgPad_Android_clone.git
   cd ngpad-android
   ```

2. **Open in Android Studio:**
   - Open the project folder.
   - Let Gradle sync and resolve dependencies.

3. **Configure API Endpoints:**
   - Update API base URLs in `RetrofitClient.java` if needed.

4. **Build & Run:**
   - Connect an Android device or start an emulator.
   - Click "Run" in Android Studio.

## Project Structure

```
app/
  ├── src/main/java/com/chaosdev/ngpad/
  │     ├── data/           # Data sources, repositories, database
  │     ├── model/          # Data models (entities, DTOs)
  │     ├── view/           # Activities, fragments, adapters
  │     ├── viewmodel/      # ViewModels for MVVM
  │     ├── utils/          # Utilities (SVG loader, string utils, etc.)
  │     └── ...             # Other modules
  ├── res/                  # Resources (layouts, drawables, etc.)
  └── AndroidManifest.xml
```

## Development

- **Code Style:** Follow standard Java and Android code conventions.
- **Branching:** Use feature branches for new features or bug fixes.
- **Commits:** Write clear, descriptive commit messages.

### Useful Commands

- **Clean Build:**
  ```bash
  ./gradlew clean build
  ```
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```

## Contributing

Contributions are welcome! Please open issues or submit pull requests for improvements and bug fixes.

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature`
3. Make your changes.
4. Commit and push: `git commit -am 'Add new feature' && git push origin feature/your-feature`
5. Open a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

---

**NgPad Android**  
Crafted with ❤️ by ChaosDev
