# ChronoView

ChronoView is a native Android premium watch store app built with Kotlin + Jetpack Compose.

## Features
- **Watch Catalog:** Responsive grid layout with images.
- **Advanced Search:** Search by watch name or brand.
- **Category Filters:** Filter by Luxury, Sport, and Casual categories.
- **Product Details:** High-quality image carousels and AI-generated descriptions.
- **Authentication:** Mock Login and Sign-up system.
- **Shopping Cart:** Add/remove items, manage quantities, and see real-time updates.
- **Admin Dashboard:** CRUD operations for products (Add, Edit, Delete) with image support.
- **Purchase History:** Persistent log of all previous orders.
- **Modern UI:** Sticky headers, global snackbar feedback, and Material 3 design.

## Demo Credentials
The app uses mock authentication logic. You can use any credentials that meet the validation rules, but for testing roles, use:

### **Standard User**
- **Email:** `user@example.com` (or any valid email with `@`)
- **Password:** `123456` (min 6 characters)

### **Admin User**
- **Email:** `admin@chronoview.com`
- **Password:** `admin123` (min 6 characters)
*The Admin Dashboard is accessible via the Settings icon in the header only when logged in as admin.*

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Navigation:** Navigation Compose
- **Database:** Room (Persistence for Cart, Orders, and Products)
- **Image Loading:** Coil
- **Serialization:** Gson

## Development & Build
1. Open the project in Android Studio (Giraffe+ recommended).
2. Let Gradle sync.
3. Run the `app` configuration.

**Command Line Build:**
```bash
./gradlew assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.
