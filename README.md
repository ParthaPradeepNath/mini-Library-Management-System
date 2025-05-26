# 📚 Mini Library Management System

A simple yet functional **Library Management System** built using **Java Swing** for GUI and **file I/O** for persistent data storage. The application allows users to manage books by adding, removing, issuing, and returning them. It also features a custom background image for visual enhancement.

---

## 🚀 Features

- **Add Book:** Insert new books by specifying the title and author.
- **Remove Book:** Delete selected books from the list.
- **Issue Book:** Change a book’s status to "Issued" when it's borrowed.
- **Return Book:** Mark issued books as "Available" when returned.
- **Persistent Storage:** Book data is saved to a `.dat` file and auto-loaded on restart.
- **Graphical User Interface (GUI):** Built using Java Swing with intuitive controls.
- **Custom Background:** Application window features a background image for better aesthetics.

---

## 🛠 Technologies Used

- **Java SE (Swing, AWT)**
- **Serialization (ObjectInputStream/ObjectOutputStream)**
- **JTable & DefaultTableModel**
- **Custom JPanel with background image**

---

## 📁 Project Structure

```
LibraryManagement.java         # Main application code
books.dat                      # Binary file storing book data
background.jpg                 # Background image used in the GUI
README.md                      # Project description and guide
```

---

## 🖥️ How to Run

1. **Clone the repository** or download the `.java` file.

2. **Place a background image** in the same folder and name it `background.jpg`.

3. **Compile and run** the program:
   ```bash
   javac LibraryManagement.java
   java LibraryManagement
   ```

---

## 📸 Screenshots

java\GUI.png

---

## 📌 Notes

- Ensure `books.dat` and `background.jpg` are in the same directory as your compiled `.class` files.
- The program will automatically create `books.dat` if it does not exist.

---

