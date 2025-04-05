import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Book {
    String title;
    String author;
    boolean isIssued;
    String issuedTo;
    Date issueDate;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    long calculateFine() {
        if (!isIssued) return 0;
        long daysLate = (new Date().getTime() - issueDate.getTime()) / (1000 * 60 * 60 * 24);
        return (daysLate > 0) ? daysLate * 1000 : 0;
    }
}

class Admin {
    List<Book> books = new ArrayList<>();

    Admin() {
        books.add(new Book("To Kill a Mockingbird", "Harper Lee"));
        books.add(new Book("1984", "George Orwell"));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book("Pride and Prejudice", "Jane Austen"));
        books.add(new Book("Moby Dick", "Herman Melville"));
    }

    void addBook(String title, String author) {
        books.add(new Book(title, author));
    }

    void deleteBook(String title) {
        books.removeIf(book -> book.title.equalsIgnoreCase(title));
    }

    List<Book> viewBooks() {
        return books;
    }

    void modifyBook(String currentTitle, String newTitle, String newAuthor) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(currentTitle)) {
                book.title = newTitle;
                book.author = newAuthor;
                return;
            }
        }
    }
}

class User {
    void issueBook(String title, List<Book> books, String userName) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title) && !book.isIssued) {
                book.isIssued = true;
                book.issuedTo = userName;
                book.issueDate = new Date();
                return;
            }
        }
    }

    String returnBook(String title, List<Book> books) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title) && book.isIssued) {
                long fine = book.calculateFine();
                book.isIssued = false;
                book.issuedTo = null;
                book.issueDate = null;
                return fine > 0 ? "Fine for late return: Rs." + fine : "No fine.";
            }
        }
        return "Book not currently issued or not found.";
    }
}

public class DigitalLibraryManagementGUI extends JFrame {
    private Admin admin = new Admin();
    private User user = new User();

    public DigitalLibraryManagementGUI() {
        setTitle("Digital Library Management System");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        JButton adminButton = new JButton("Admin Actions");
        JButton userButton = new JButton("User Actions");
        JButton exitButton = new JButton("Exit");

        adminButton.addActionListener(new AdminActionListener());
        userButton.addActionListener(new UserActionListener());
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        add(adminButton);
        add(userButton);
        add(exitButton);
    }

    private class AdminActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"Add Book", "Delete Book", "View Books", "Modify Book", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Select an action", "Admin Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    String title = JOptionPane.showInputDialog("Enter book title:");
                    String author = JOptionPane.showInputDialog("Enter book author:");
                    admin.addBook(title, author);
                    break;
                case 1:
                    String deleteTitle = JOptionPane.showInputDialog("Enter book title to delete:");
                    admin.deleteBook(deleteTitle);
                    break;
                case 2:
                    StringBuilder booksList = new StringBuilder("Available Books:\n");
                    for (Book book : admin.viewBooks()) {
                        booksList.append("Title: ").append(book.title).append(", Author: ").append(book.author).append(", Issued: ").append(book.isIssued).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, booksList.toString());
                    break;
                case 3:
                    String currentTitle = JOptionPane.showInputDialog("Enter current title of the book to modify:");
                    String newTitle = JOptionPane.showInputDialog("Enter new title:");
                    String newAuthor = JOptionPane.showInputDialog("Enter new author:");
                    admin.modifyBook(currentTitle, newTitle, newAuthor);
                    break;
            }
        }
    }

    private class UserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"View Books", "Issue Book", "Return Book", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Select an action", "User Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    StringBuilder booksList = new StringBuilder("Available Books:\n");
                    for (Book book : admin.viewBooks()) {
                        booksList.append("Title: ").append(book.title).append(", Author: ").append(book.author).append(", Issued: ").append(book.isIssued).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, booksList.toString());
                    break;
                case 1:
                    String issueTitle = JOptionPane.showInputDialog("Enter book title to issue:");
                    String userName = JOptionPane.showInputDialog("Enter your name:");
                    user.issueBook(issueTitle, admin.books, userName);
                    JOptionPane.showMessageDialog(null, "Book issued: " + issueTitle + " to " + userName);
                    break;
                case 2:
                    String returnTitle = JOptionPane.showInputDialog("Enter book title to return:");
                    String returnMessage = user.returnBook(returnTitle, admin.books);
                    JOptionPane.showMessageDialog(null, returnMessage);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DigitalLibraryManagementGUI frame = new DigitalLibraryManagementGUI();
            frame.setVisible(true);
        });
    }
}
