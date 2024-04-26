import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryManagementSystem extends JFrame {
    private JTextField titleField, authorField, borrowerIdField;
    private JButton addBookButton, viewBooksButton,clearButton ;
    private JTextArea bookListArea;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LibraryManagementSystem() {
        super("Library Management System");
        initializeUI();
        connectToDatabase();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        authorField = new JTextField();
        panel.add(authorField);

        addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook());
        panel.add(addBookButton);

        viewBooksButton = new JButton("View Books");
        viewBooksButton.addActionListener(e -> viewBooks());
        panel.add(viewBooksButton);

        clearButton = new JButton("Clear Books");
        clearButton.addActionListener(e -> clearBooks());
        panel.add(clearButton);

        bookListArea = new JTextArea();
        bookListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookListArea);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/library", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBook() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();

            preparedStatement = connection.prepareStatement("INSERT INTO books (title, author) VALUES (?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add book. Please check input data.");
        }
    }

    private void clearBooks() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE books");
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Books cleared successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to clear books. Please check input data.");
        }
    }

    private void viewBooks() {
        try {
            StringBuilder bookList = new StringBuilder();
            bookList.append("Available Books:\n");

            preparedStatement = connection.prepareStatement("SELECT * FROM books");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");

                bookList.append(title).append(" by ").append(author).append("\n");
            }

            bookListArea.setText(bookList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve books. Please try again later.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystem::new);
    }
}