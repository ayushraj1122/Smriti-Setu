# Smriti Setu (স্মৃতি সেতু / स्मृति सेतु)
### Multilingual Cognitive Care & Dementia Support Platform

**Smriti Setu** ("Bridge of Memories") is an assistive cognitive care and dementia support application developed for elderly individuals, people experiencing Mild Cognitive Impairment (MCI), and their caregivers. Designed with cultural sensitivity, multilingual support (English, Assamese, and Hindi), and an accessibility-first interface, Smriti Setu bridges cognitive stimulation, daily orientation, and compassionate caregiver tracking into one integrated mobile experience.

---

## 🌟 Key Highlights

- **Culturally Tailored for North East India & Multilingual Families:** Fully localized in **Assamese (অসমীয়া)**, **Hindi (हिंदी)**, and **English**, incorporating regional cultural contexts, relatable everyday scenarios, and familiar temporal landmarks.
- **8 Evidence-Based Cognitive Stimulation Games:** Structured across core cognitive domains including temporal orientation, short-term visual memory, selective attention, executive function, and everyday problem-solving.
- **Progressive Adaptive Recommendation Engine:** Guides patients through balanced cognitive routines—ensuring all 8 cognitive activities are introduced at Level 1 before smoothly advancing levels in a non-repetitive, circular flow.
- **Accessibility & Senior-Friendly Design:** Built with high-contrast themes, large touch targets (48dp+), clutter-free typography, and integrated **Text-to-Speech (TTS)** voice guidance.
- **Dual-Role Ecosystem (Patient & Caregiver):**
  - **Patient Mode:** Zero-friction patient code login, one-tap and long-press code copying, daily activity routines, and encouraging feedback.
  - **Caregiver Portal:** Multi-patient account linking, active patient selector, granular performance analytics, cognitive domain breakdown charts, and practical dementia caregiving tips.
- **Offline-First & Private:** Backed by a local Room SQLite database ensuring patient data, session logs, and personal settings remain securely stored on device.

---

## 🧠 Cognitive Games & Stimulation Modules

Smriti Setu features 8 tailored cognitive activities designed to engage distinct neuropsychological domains:

| Module | Cognitive Domain | Description & Mechanics |
| :--- | :--- | :--- |
| **Day & Date Orientation** | Temporal & Situational Orientation | Identifies current day of the week, month, and season with visual and calendar cues. |
| **Memory Match** | Short-Term & Visual Working Memory | Pairs hidden cards with matching familiar symbols and pictures. |
| **Find Target** | Selective Attention & Visual Search | Locates specific target items within an array of visual distractors. |
| **Simple Pattern** | Logical Reasoning & Executive Function | Completes logical sequences and geometric/symbolic color patterns. |
| **Place & Time** | Environmental & Spatial Orientation | Situational questions connecting time of day to familiar household activities. |
| **Remember Objects** | Working Memory & Delayed Recall | Brief presentation of everyday objects followed by recall and identification. |
| **Sequence Attention** | Sustained & Sequential Attention | Taps numbers, letters, or items in correct chronological/numerical order. |
| **Everyday Choice** | Functional Problem Solving & Daily Living | Practical choices regarding safety, nutrition, hydration, and daily routines. |

---

## ⚙️ Intelligent Adaptive Progression Engine

The custom **Adaptive Engine** (`ScoringAndAdaptive.kt`) ensures that activities neither frustrate nor under-stimulate the user:

1. **Foundational Phase (Level 1 across all 8 games):** Every patient is first introduced to all eight game modules at Level 1 to build confidence and familiarize them with touch mechanics.
2. **Phase Completion & Elevation:** Only when all 8 activities have been completed at the current level does the patient transition to the subsequent difficulty tier (Level 2, Level 3).
3. **Circular Variety:** Tracks recent completions to recommend the next unplayed activity in the active phase, preventing monotony and ensuring well-rounded cognitive stimulation.
4. **Seamless Flow:** The end-of-game summary prominently offers a single-tap **"Play Next Recommended Activity"** button to sustain engagement without confusing navigation menus.

---

## 👥 Dual-Mode Experience

### 🧑‍🦳 Patient Experience
- **Code-Based Authentication:** Clean sign-in with assigned patient codes (e.g., `PAT-101`), eliminating complex passwords.
- **Long-Press & Tap Code Copy:** Patients or family members can easily tap or long-press their patient code badge with haptic feedback to copy it for caregiver linking.
- **Voice Read-Aloud (TTS):** Integrated Text-to-Speech engine audibly narrates instructions, questions, and prompts in English, Assamese, or Hindi.
- **High Contrast Toggle:** Instant contrast enhancement for users with low vision or cataract-related vision difficulties.
- **Dopamine-Friendly Reinforcement:** Gentle celebratory animations and uplifting audio tones celebrate progress regardless of score.

### 👩‍⚕️ Caregiver Experience
- **Caregiver Registration & Login:** Dedicated account creation and authentication for family members, nurses, or clinical staff.
- **Multi-Patient Linking:** Caregivers can link multiple patient profiles using unique patient codes (`PAT-XXX`).
- **Active Patient Switching:** Seamlessly select which patient’s analytics and historical records to monitor from settings or dashboard headers.
- **Unlink / Remove Patient:** Full control to unlink or manage patient associations with safety confirmations.
- **Visual Analytics & Progress Charts:**
  - Weekly session completion frequency.
  - Accuracy and reaction time trends.
  - Cognitive domain balance breakdown (Orientation, Memory, Attention, Executive Function).
  - Chronological activity log with question-by-question performance.
- **Caregiver Guides:** Evidence-based communication tips, de-escalation strategies, and self-care resources for caregivers.

---

## 🛠️ Architecture & Tech Stack

The application is engineered following Android best practices, Clean Architecture, and MVVM:

```
app/src/main/java/com/example/
├── MainActivity.kt               # Single Activity host & navigation orchestrator
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt        # Room Database configuration & type converters
│   │   ├── Daos.kt               # DAOs for Users, Attempts, and Linked Accounts
│   │   └── DemoDataSeeder.kt     # Initial seed data for demo/evaluation accounts
│   ├── model/
│   │   ├── Entities.kt           # Room entities (UserEntity, GameAttemptEntity, etc.)
│   │   ├── Enums.kt              # CognitiveDomain, GameCategory, Language enums
│   │   ├── GameCatalog.kt        # Game metadata, descriptions, and icon mappings
│   │   ├── GameModels.kt         # Question, option, and result data structures
│   │   └── GameQuestionGenerator.kt # Procedural and multilingual question generator
│   └── repository/
│       └── SmritiRepository.kt   # Single source of truth for database operations
├── engine/
│   ├── ScoringAndAdaptive.kt    # Adaptive progression, circular queue & scoring engine
│   └── TtsManager.kt             # Android TextToSpeech manager supporting multi-locales
├── i18n/
│   └── StringsProvider.kt        # Multilingual strings dictionary (EN, AS, HI)
├── ui/
│   ├── components/               # AccessibleButton, StatCard, AccessibleCharts, etc.
│   ├── navigation/               # Type-safe Screen destinations
│   ├── screens/                  # Splash, Welcome, Auth, PatientMain, CaregiverMain, GamePlay
│   ├── theme/                    # Material 3 ColorScheme, Typography, Shapes
│   └── viewmodel/                # AppViewModel with reactive StateFlows
```

### Technologies Used
- **Language:** Kotlin (100%)
- **UI Framework:** Jetpack Compose with Material Design 3 (M3)
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Persistence:** Android Room Database (SQLite) with Kotlin Coroutines
- **State Management:** Kotlin StateFlow & State / Compose runtime
- **Speech Engine:** Android TextToSpeech (`android.speech.tts.TextToSpeech`)
- **Graphics & Charts:** Custom accessible Compose Canvas charts for high performance and zero external web dependencies

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio Ladybug (2024.2.1+) or newer](https://developer.android.com/studio)
- Android SDK 34+
- JDK 17+
- Physical Android device or Android Emulator running Android 8.0 (API 26) or higher

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/smriti-setu.git
   cd smriti-setu
   ```

2. **Open in Android Studio:**
   - Launch Android Studio.
   - Select **Open** and choose the `smriti-setu` directory.
   - Allow Gradle to sync dependencies.

3. **Build the Project:**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on Device or Emulator:**
   - Select your target device in Android Studio.
   - Click **Run** (Shift + F10) or execute:
   ```bash
   ./gradlew installDebug
   ```

---

## 🧪 Testing

To run unit tests and local JVM Robolectric tests:
```bash
./gradlew testDebugUnitTest
```

To run lint checks:
```bash
./gradlew lintDebug
```

---

## 🩺 Clinical & Medical Disclaimer

> **Notice:** Smriti Setu is an assistive cognitive stimulation and lifestyle support tool designed to encourage mental engagement and assist caregivers. It is **not** a diagnostic medical device and is not intended to diagnose, treat, cure, or prevent dementia, Alzheimer's disease, or any neurological condition. Always consult qualified healthcare professionals and neurologists for clinical diagnoses and medical care plans.

---

## 📄 License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
