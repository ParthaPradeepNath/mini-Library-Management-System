import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

public class LibraryManagement extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField titleField, authorField;
    private JButton addBtn, removeBtn, issueBtn, returnBtn;

    private final String[] columns = {"S.No", "Title", "Author", "Status"};
    private final String DATA_FILE = "books.dat";

    public LibraryManagement() {
        setTitle("Mini Library Management");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom background panel
        setContentPane(new BackgroundPanel());

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make S.No non-editable
                return column != 0;
            }
        };
        table = new JTable(model);
        // Center align the Serial No. column (column index 0)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);

        titleField = new JTextField(10);
        authorField = new JTextField(10);

        addBtn = new JButton("Add Book");
        removeBtn = new JButton("Remove Book");
        issueBtn = new JButton("Issue Book");
        returnBtn = new JButton("Return Book");

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(addBtn);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(issueBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(removeBtn);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        // If no data loaded, preload some books (optional)
        if (model.getRowCount() == 0) {
            preloadBooks();
        }

        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            if (!title.isEmpty() && !author.isEmpty()) {
                model.addRow(new Object[]{0, title, author, "Available"});
                updateSerialNumbers();
                titleField.setText("");
                authorField.setText("");
                saveData();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter title and author.");
            }
        });

        removeBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                model.removeRow(selected);
                updateSerialNumbers();
                saveData();
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to remove.");
            }
        });

        issueBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String status = (String) model.getValueAt(selected, 3);
                if (status.equals("Available")) {
                    model.setValueAt("Issued", selected, 3);
                    saveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Book is already issued.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to issue.");
            }
        });

        returnBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                String status = (String) model.getValueAt(selected, 3);
                if (status.equals("Issued")) {
                    model.setValueAt("Available", selected, 3);
                    saveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Book is already available.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to return.");
            }
        });

        setVisible(true);
    }

    // Update serial numbers in the first column
    private void updateSerialNumbers() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

    // Preload some books on first launch
    private void preloadBooks() {
        String[][] books = {
            {"Atomic Habits", "James Clear"},
            {"The Power of Now", "Eckhart Tolle"},
            {"Thinking, Fast and Slow", "Daniel Kahneman"},
            {"The 7 Habits of Highly Effective People", "Stephen R. Covey"},
            {"Mindset", "Carol S. Dweck"},
            {"How to Win Friends and Influence People", "Dale Carnegie"},
            {"Deep Work", "Cal Newport"},
            {"The Four Agreements", "Don Miguel Ruiz"},
            {"Drive", "Daniel H. Pink"},
            {"Moby Dick", "Herman Melville"},
            {"War and Peace", "Leo Tolstoy"},
            {"The Catcher in the Rye", "J.D. Salinger"},
            {"The Hobbit", "J.R.R. Tolkien"},
            {"The Lord of the Rings", "J.R.R. Tolkien"},
            {"Harry Potter and the Sorcerer's Stone", "J.K. Rowling"},
            {"The Da Vinci Code", "Dan Brown"},
            {"Animal Farm", "George Orwell"},
            {"The Alchemist", "Paulo Coelho"},
            {"The Book Thief", "Markus Zusak"},
            {"Jane Eyre", "Charlotte Brontë"},
        };

        for (String[] book : books) {
            model.addRow(new Object[]{0, book[0], book[1], "Available"});
        }
        updateSerialNumbers();
        saveData();
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            Vector<?> dataVector = model.getDataVector();
            out.writeObject(dataVector);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Vector<?> data = (Vector<?>) in.readObject();
            model.setRowCount(0); // Clear existing data

            for (Object row : data) {
                if (row instanceof Vector<?>) {
                    Vector<?> vecRow = (Vector<?>) row;
                    // Ensure row has 3 columns for Title, Author, Status
                    if (vecRow.size() == 3) {
                        model.addRow(new Object[]{0, vecRow.get(0), vecRow.get(1), vecRow.get(2)});
                    }
                }
            }
            updateSerialNumbers();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom JPanel to paint background image
    class BackgroundPanel extends JPanel {
        private Image bg;

        public BackgroundPanel() {
            try {
                bg = new ImageIcon("background.jpg").getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(255, 255, 255, 80)); // semi-transparent overlay
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagement::new);
    }
}
