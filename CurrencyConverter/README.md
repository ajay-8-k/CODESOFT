# 💱 Real-Time Currency Converter CLI

A clean, beginner-friendly, and professional **Java Console-Based Currency Converter** application. This project fetches real-time currency exchange rates from a public API using standard Java libraries and handles conversions dynamically. It is designed as a standalone utility, adhering to clean coding standards and Object-Oriented Programming (OOP) concepts, making it perfect for **Internship Task 4**.

---

## 🚀 Features

- **Supported Currencies**: Easy conversion between major global currencies:
  - `USD` (United States Dollar)
  - `INR` (Indian Rupee)
  - `EUR` (Euro)
  - `GBP` (British Pound)
  - `JPY` (Japanese Yen)
  - `AUD` (Australian Dollar)
  - `CAD` (Canadian Dollar)
- **Live Exchange Rates**: Uses `HttpURLConnection` to fetch real-time, up-to-date rates from the free, open-access [ExchangeRate-API](https://www.exchangerate-api.com/).
- **Lightweight JSON Parsing**: Features a lightweight parser written entirely in **Core Java** (using Regular Expressions), eliminating the need for third-party libraries like Gson or Jackson.
- **Robust Validation & Error Handling**:
  - Handles invalid currency code inputs with user prompts.
  - Sanitizes user numeric inputs (rejects negative numbers, alphabetical characters, etc.).
  - Gracefully captures connection timeouts and offline/network failure issues without crashing.
- **Formatted Outputs**: Displays structured summary tables showcasing original/converted amounts, symbols (e.g., `$`, `₹`, `€`), conversion rate, and full currency names.
- **Interactive Console Loop**: Allows the user to perform multiple conversions sequentially and exit whenever they choose.

---

## 📁 Folder Structure

```text
CurrencyConverter/
├── CurrencyConverter.java       # Main class managing the interactive CLI loop and outputs.
├── ExchangeRateService.java    # Handles HTTP connection and JSON regex-based parsing.
├── CurrencyUtils.java          # Utility maps for symbols, currency details, and formatting.
└── README.md                   # Project documentation.
```

---

## 🛠️ Requirements

- **Java Development Kit (JDK) 8** or higher installed.
- Internet connection (to fetch real-time exchange rates).
- Any standard Command Prompt / Terminal or IDE (like VS Code, IntelliJ, Eclipse).

---

## 💻 How to Compile and Run

### Option 1: Command Prompt / Terminal (Recommended)

1. Open your terminal or Command Prompt.
2. Navigate to the parent directory of `CurrencyConverter` (the directory containing the `CurrencyConverter` folder):
   ```bash
   cd /path/to/parent/directory
   ```
3. Compile all Java files:
   ```bash
   javac CurrencyConverter/*.java
   ```
4. Run the application:
   ```bash
   java CurrencyConverter.CurrencyConverter
   ```

### Option 2: Visual Studio Code (VS Code)

1. Launch VS Code and open the parent folder containing the `CurrencyConverter` directory (`File -> Open Folder...`).
2. Make sure you have the **Extension Pack for Java** installed in VS Code.
3. Open [CurrencyConverter.java](file:///d:/PROJECT%204/CurrencyConverter/CurrencyConverter.java).
4. Click the **Run** button (or press `F5` / click `Run Without Debugging` on top of the `main` method).
5. The application will start executing in the integrated terminal.

---

## 📊 Sample Output Demo

```text
=================================================
        REAL-TIME CURRENCY CONVERTER CLI        
            (Internship Task 4 Project)          
=================================================
Welcome! Convert amounts between various currencies
using live, real-time exchange rates.
=================================================

=================================================
          SUPPORTED CURRENCIES LIST              
=================================================
Code   | Currency Name             | Symbol
-------------------------------------------------
EUR    | Euro                      | €     
USD    | United States Dollar      | $     
GBP    | British Pound             | £     
INR    | Indian Rupee              | ₹     
CAD    | Canadian Dollar           | C$    
JPY    | Japanese Yen              | ¥     
AUD    | Australian Dollar         | A$    
=================================================
Enter BASE currency code (e.g., USD): USD
Enter TARGET currency code (e.g., EUR): INR
Enter amount to convert: 150.75

Connecting to API to fetch live rates...

=================================================
              CONVERSION SUMMARY                 
=================================================
  Base Currency:    USD (United States Dollar)
  Target Currency:  INR (Indian Rupee)
  Exchange Rate:    1 USD = 83.4500 INR
  -----------------------------------------------
  Original Amount:  $150.75                        
  Converted Amount: ₹12,580.09                     
  Currency Symbol:  ₹ ($ -> ₹)
=================================================

Do you want to perform another conversion? (yes/no or y/n): n

=================================================
   Thank you for using Currency Converter! Bye!  
=================================================
```

---

## 🛡️ Exception Handling & Code Quality

- **Modular Design**: The project is split cleanly into `CurrencyConverter` (Interface/Loop logic), `ExchangeRateService` (Network & Parsing logic), and `CurrencyUtils` (Data/UI helper methods) following **Single Responsibility Principle (SRP)**.
- **Offline Resiliency**: Catching `UnknownHostException` alerts the user to check their internet connection instead of outputting a generic stack trace.
- **Input Robustness**: Employs `try-catch` blocks inside loop prompts to ensure formatting errors in user inputs (like typing `abc` for the amount) do not break the runtime loop.
- **Resource Safety**: Uses `finally` blocks to guarantee that HTTP readers and socket connections are closed even if exception conditions are met.
