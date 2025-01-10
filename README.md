# Spacecraft Catalog

A modern Android application that showcases a catalog of space agencies and spacecraft, allowing users to explore detailed information and dynamically shuffle the data. The app supports offline functionality through local caching, providing a seamless experience even without an internet connection.

---

## Features

- **Agency and Spacecraft Catalogs**:
    - View a list of space agencies and spacecraft.
    - Explore detailed information for each agency or spacecraft, including images, descriptions, and related metadata.

- **Dynamic Shuffle**:
    - Refresh and shuffle the displayed data, either from the server (online) or from cached data (offline).

- **Offline Support**:
    - Fully functional offline experience using Room database for local caching.

- **Modern Android Architecture**:
    - MVVM architecture with Jetpack components.
    - Dependency injection using Hilt.
    - State management with Kotlin Flow and Compose.

- **UI/UX**:
    - Intuitive and responsive UI built with Jetpack Compose.
    - Dark and light mode support.

---

## Tech Stack

- **Programming Language**: Kotlin
- **Architecture**: MVVM
- **UI Toolkit**: Jetpack Compose
- **Networking**: Retrofit
- **Dependency Injection**: Hilt
- **Database**: Room
- **Asynchronous Programming**: Kotlin Coroutines and Flow
- **Image Loading**: Coil
- **Build System**: Gradle

---


## Screenshots


https://github.com/user-attachments/assets/f6ed74c8-cf4d-489d-b3a8-d67fd3bbc71b




```

## Installation and Setup

### Prerequisites
- **Android Studio**: Version 2022.3 or higher.
- **Android Device/Emulator**: Android 7.0 (API level 24) or higher.

### Clone the Repository
```bash
git clone <repository-url>
cd spacecraft-catalog
```

### Open in Android Studio
1. Open Android Studio.
2. Select **File > Open**.
3. Navigate to the cloned project directory and select it.

### Build the Project
1. Sync Gradle by clicking **Sync Now** when prompted.
2. Select a target device or emulator.
3. Run the project by clicking the **Run** button or pressing `Shift + F10`.

---

## API Usage

The app utilizes the **Space Devs API** to fetch data:

- **Endpoints Used**:
    - Agencies: `/agencies/`
    - Spacecraft: `/spacecraft/`

- **Pagination**:
    - The API provides data in batches of 100 items, using an offset parameter for pagination.

- **Offline Mode**:
    - Data fetched from the API is cached locally using Room database.
    - Shuffle functionality works offline by randomizing the cached data.

---

## Directory Structure

```
app/src/main/java/com/example/spacecraftcatalog/
|
├── data/          # Data layer (API, DB, Repositories)
|├── domain/        # Domain layer (Models, Use Cases, Repositories)
|├── ui/            # UI layer (Screens, Components, ViewModels)
|├── util/          # Utility classes and helpers
|├── di/            # Dependency Injection modules
```

---

## Key Files

### Data Layer
- **`data/api/SpaceAgencyApi.kt`**:
    - Defines Retrofit API endpoints for fetching agency and spacecraft data.
- **`data/db/SpaceCatalogDatabase.kt`**:
    - Implements the Room database for local caching.
- **`data/repository/AgencyRepositoryImpl.kt`**:
    - Repository implementation for agencies with online and offline handling.
- **`data/repository/SpacecraftRepositoryImpl.kt`**:
    - Repository implementation for spacecraft with similar functionality.

### UI Layer
- **`ui/screens/AgencyListScreen.kt`**:
    - Displays the list of space agencies.
    - Includes shuffle functionality with offline support.
- **`ui/screens/SpacecraftListScreen.kt`**:
    - Displays the list of spacecraft.
- **`ui/screens/DetailsScreens`**:
    - Details for agencies and spacecraft with images and metadata.

### Domain Layer
- **`domain/usecase`**:
    - Includes use cases for fetching and refreshing agencies and spacecraft.

---

## Offline Functionality

### How It Works
1. **Caching**:
    - Data fetched from the API is stored in a Room database.
2. **Fallback to Cache**:
    - If the device is offline, the app retrieves data from the local cache.
3. **Shuffling Offline**:
    - The app randomizes the locally cached data for shuffle functionality.

---

## Migration Updates

### Database Migrations
- **Version 2 to 3**:
    - Updated `spacecraft` table structure to improve flexibility and maintainability.
    - Migration logic ensures data preservation during upgrades.

---

## Contributing

We welcome contributions to the Spacecraft Catalog project! To contribute:
1. Fork the repository.
2. Create a feature branch.
3. Commit your changes and push the branch.
4. Submit a pull request.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

---

## Acknowledgments

- **Space Devs API**: For providing open data on space agencies and spacecraft.
- **Jetpack Compose Community**: For resources and inspiration.




