# Bank Account Manager 
**Console-based Java application for managing user bank accounts.**

This project simulates a secure bank account system that supports both user and admin operations. Designed with HashMap and ArrayList for efficiency and session persistence, the program allows account holders to manage their finances while providing administrative tools for oversight and maintenance.

---

## 🚀 Features

- 🔐 **PIN Authentication**  


- 🧾 **User Operations**  
  Users can:
  - Withdraw and deposit funds  
  - Check current balance  
  - Create new accounts  

- 🛠️ **Admin Mode (Account Number: 0, Password: 0000)**  
  Admins can:
  - View all accounts  
  - Reset user balances  
  - Delete accounts  
  - View account statistics (min, max, avg balance)

- 💾 **Session Persistence**  
  All account data is saved to `initialBankWithPin.txt` after each session. Changes are tracked and written to a temporary file before being transferred and cleared.

---

## 🧱 Technologies Used

- **Java** (HashMap, ArrayList, Scanner, PrintWriter, File I/O)
- No external libraries or frameworks

---
