# 🔗 URL Shortener

> A feature-rich **Java + JDBC + MySQL** URL Shortener with Base62 encoding, custom aliases, click analytics, URL expiry, and a layered architecture — built for learning and placement interviews.

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Auto Short URL** | Converts any URL to a short code using Base62 encoding |
| 2 | **Custom Alias** | Lets users define their own short code (e.g., `/github`) |
| 3 | **Collision Detection** | Rejects duplicate aliases using `UNIQUE` DB constraint |
| 4 | **URL Expiry** | Create temporary links that expire after N days |
| 5 | **Click Tracking** | Counts every redirect on a short URL |
| 6 | **URL Validation** | Only accepts valid `http://` or `https://` URLs |
| 7 | **Analytics** | Tracks Country, Browser, Device, and Time per click |
| 8 | **Statistics** | Per-URL and global click stats |
| 9 | **Delete URLs** | Remove any short URL by ID |

---

## Project Structure

```
URLShortener/
├── src/
│   ├── Main.java                 ← Entry point
│   ├── DBConnection.java         ← MySQL JDBC connection
│   ├── Url.java                  ← URL model (with expiry support)
│   ├── UrlValidator.java         ← HTTP/HTTPS URL validation
│   ├── Base62.java               ← ID to short code encoder/decoder
│   ├── PrimeIdGenerator.java     ← (Optional) Obfuscated ID generator
│   ├── UrlRepository.java        ← All SQL for urls table
│   ├── UrlService.java           ← Business logic layer
│   ├── Menu.java                 ← Console UI (10 options)
│   ├── ClickAnalytics.java       ← Click event model
│   ├── DeviceInfo.java           ← Auto-detects Country/Browser/Device
│   ├── AnalyticsRepository.java  ← All SQL for url_clicks table
│   └── AnalyticsService.java     ← Analytics charts and reports
├── mysql-connector-j.jar         ← MySQL JDBC driver (add manually)
└── README.md
```

---

## Architecture

```
User Input (Console)
        │
        ▼
     Menu.java              ← Presentation Layer
        │
        ▼
   UrlService.java          ← Business Logic Layer
   AnalyticsService.java
        │
        ▼
  UrlRepository.java        ← Data Access Layer (JDBC)
  AnalyticsRepository.java
        │
        ▼
  DBConnection.java         ← MySQL via JDBC
        │
        ▼
     MySQL DB
  ┌──────────────┐
  │  urls        │
  │  url_clicks  │
  └──────────────┘
```

---

## Database Schema

### Step 1 — Create Database
```sql
CREATE DATABASE url_shortener;
USE url_shortener;
```

### Step 2 — Create urls table
```sql
CREATE TABLE urls (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    short_code   VARCHAR(20) UNIQUE,
    clicks       INT DEFAULT 0,
    expiry_time  TIMESTAMP NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Step 3 — Create url_clicks table (Analytics)
```sql
CREATE TABLE url_clicks (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    url_id     INT NOT NULL,
    short_code VARCHAR(20),
    country    VARCHAR(100) DEFAULT 'Unknown',
    browser    VARCHAR(100) DEFAULT 'Unknown',
    device     VARCHAR(100) DEFAULT 'Unknown',
    clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (url_id) REFERENCES urls(id) ON DELETE CASCADE
);
```

---

## Setup and Installation

### Prerequisites
- Java JDK 17 or higher
- MySQL Server 8.x
- `mysql-connector-j.jar` — [Download from MySQL](https://dev.mysql.com/downloads/connector/j/)

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/url-shortener.git
cd url-shortener
```

### 2. Add JDBC Driver
Download `mysql-connector-j.jar` and place it in the project root (same level as `src/`).

### 3. Configure Database Credentials
Open `src/DBConnection.java` and update:
```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/url_shortener";
private static final String DB_USER     = "root";           // your MySQL username
private static final String DB_PASSWORD = "your_password";  // your MySQL password
```

### 4. Run the SQL Schema
Copy and run all three SQL blocks above in MySQL Workbench or the MySQL CLI.

---

## How to Run

### Compile
```bash
# Windows
javac -cp .;mysql-connector-j.jar src/*.java

# Linux / Mac
javac -cp .:mysql-connector-j.jar src/*.java
```

### Run
```bash
# Windows
java -cp .;mysql-connector-j.jar src.Main

# Linux / Mac
java -cp .:mysql-connector-j.jar src.Main
```

---

## Usage Examples

### Shorten a URL
```
Choice: 1
Enter URL: https://github.com

Short URL : http://localhost/b
```

### Open / Redirect
```
Choice: 2
Enter short code: b

Redirecting to   : https://github.com
Country detected : India
Device detected  : Windows
Browser detected : Console/CLI
```

### Custom Alias
```
Choice: 7
Enter URL: https://github.com
Enter custom code: github

Short URL : http://localhost/github
```

Collision example:
```
Enter custom code: github
Short code 'github' already taken. Choose another.
```

### URL with Expiry
```
Choice: 8
Enter URL: https://amazon.com
Expire after how many days? 7

Short URL  : http://localhost/c
Expires on : 2026-07-30 19:05:10
```

After expiry:
```
Choice: 2
Enter short code: c
This URL has expired.
```

---

## Analytics Dashboard

```
╔══════════════════════════════════════════╗
║      ANALYTICS REPORT                    ║
║      Short Code: b                       ║
╚══════════════════════════════════════════╝

Total Clicks : 8

── Clicks by Country ──────────────────────
  India           | ██████████████████████████████  5  (62.5%)
  United States   | ██████████████                  3  (37.5%)

── Clicks by Browser ──────────────────────
  Console/CLI     | ██████████████████████████████  8  (100.0%)

── Clicks by Device ───────────────────────
  Windows         | ██████████████████████████████  6  (75.0%)
  Linux           | ████████                        2  (25.0%)

── Clicks by Day ──────────────────────────
  2026-07-23      | ▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪  8 clicks

── Recent Clicks ──────────────────────────
  [2026-07-23 19:05:10] Code=b   Country=India   Browser=Console/CLI   Device=Windows
  [2026-07-23 18:50:22] Code=b   Country=India   Browser=Console/CLI   Device=Windows
```

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Database | MySQL 8.x |
| DB Driver | JDBC (mysql-connector-j) |
| Encoding | Base62 (custom implementation) |
| Architecture | Layered — Presentation, Service, Repository, DB |
| Build | Manual javac (no Maven/Gradle needed) |

---

## Resume Line

> Built a **Java-based URL Shortener** using JDBC and MySQL with **Base62 encoding**, **custom aliases**, **collision detection**, **URL expiry**, **click tracking**, and a **per-click analytics system** tracking Country, Browser, Device, and Time — implemented with a clean layered architecture (Service, Repository, JDBC).

---

## Future Improvements

| Feature | Complexity |
|---------|-----------|
| User Authentication (Register / Login) | Medium |
| REST API with Javalin or Spring Boot | Medium-Hard |
| Redis Cache for fast redirects | Medium-Hard |
| Rate Limiting (100 req/min per user) | Medium-Hard |
| Web UI (HTML + CSS + JS frontend) | Hard |
| Deploy to Railway / Render (free hosting) | Easy once REST API is added |

---

## License

This project is open-source and available under the [MIT License](LICENSE).

---

Made with Java — Built for learning and placement interviews
