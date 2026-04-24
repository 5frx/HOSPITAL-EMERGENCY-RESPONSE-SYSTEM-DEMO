# Hospital Emergency Response System

A Java desktop application simulating a hospital emergency dispatch system.
Built with JavaFX for the GUI and implements four design patterns:
Singleton, Factory, Strategy and Observer.

---

## What the system does

- Receives emergency requests with type, urgency level and location
- Automatically selects the closest ambulance carrying the required equipment
- Notifies relevant hospital staff based on urgency level
- Displays a live dispatch map, ambulance fleet status and dispatch log

---

## Requirements

- Java JDK 24 or higher
- JavaFX SDK 26
- Windows (PowerShell)

### Check your Java version
Open PowerShell and run:

`java -version`

You need version 24 or higher. If not, download JDK 24 from:
https://www.oracle.com/java/technologies/downloads/

---

## Setup — 3 steps

### Step 1 — Download JavaFX SDK 26
Go to: https://gluonhq.com/products/javafx/

Select:
- Version: JavaFX 26
- Operating System: **your OS**
- Architecture: x64 (or aarch64 for Apple Silicon Macs)
- Type: SDK

Download and extract the zip.

### Step 2 — Place the SDK in the project
Inside the project folder you will find a `lib/` folder.
Place the extracted `javafx-sdk-26` folder inside it so the
structure looks like this:
```
final project/
├── src/
├── lib/
│   └── javafx-sdk-26/    ← place it here
│       └── lib/
│           ├── javafx.controls.jar
│           ├── javafx.graphics.jar
│           └── ...
├── run.ps1
└── README.md
```
### Step 3 — Run the application
**Windows (PowerShell):**
```
.\run.ps1
```
If PowerShell blocks the script, run this once as administrator:
```
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```

**macOS / Linux (Terminal):**
```
chmod +x run.sh
./run.sh
```

If Java is not found, make sure JDK 24 is on your PATH:

```
export JAVA_HOME=/path/to/jdk-24
export PATH=JAVAHOME/bin:JAVA_HOME/bin:
JAVAH​OME/bin:PATH
```

The application window will open automatically.