package com.gmail.arsenycholexandra;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://127.0.0.1:3306/orders_db?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "pass123";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add new Client");
                    System.out.println("2: add new Product");
                    System.out.println("3: make order");
                    System.out.println("4: show clients");
                    System.out.println("->");

                    String st = sc.nextLine();
                    int s = Integer.parseInt(st);
                    switch (s) {
                        case 1:
                            addClient(sc);
                            break;
                        case 2:
                            addProduct(sc);
                            break;
                        case 3:
                            makeOrder(sc);
                        case 4:
                            showClients();
                        default:
                            break;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }

    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients2");
            st.execute("CREATE TABLE Clients2(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY , name VARCHAR(30) NOT NULL , surname VARCHAR(30) NOT NULL, phone INT(11) )");
            st.execute("DROP TABLE IF EXISTS Goods2");
            st.execute("CREATE TABLE Goods2(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY , name VARCHAR(30) NOT NULL , producer VARCHAR(30) NOT NULL, price INT(11) )");
            st.execute("DROP TABLE IF EXISTS Orders2");
            st.execute("CREATE TABLE Orders2(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,  product_id INT(11) NOT NULL, quantity INT(11), customer_id INT(11) NOT NULL )");
        } finally {
            st.close();
        }
    }

    private static void addClient(Scanner sc) throws SQLException {
        System.out.println("Enter client name: ");
        String name = sc.nextLine();
        System.out.println("Enter client surname: ");
        String surname = sc.nextLine();
        System.out.println("Enter client phone number: ");
        String sphone = sc.nextLine();
        int phone = Integer.parseInt(sphone);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients2(name, surname, phone) VALUES (?,?,?)");
        try {
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setInt(3, phone);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void addProduct(Scanner sc) throws SQLException {
        System.out.println("Enter product name: ");
        String name = sc.nextLine();
        System.out.println("Enter producer: ");
        String producer = sc.nextLine();
        System.out.println("Enter product price: ");
        String sprice = sc.nextLine();
        int price = Integer.parseInt(sprice);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Goods2(name, producer, price) VALUES (?,?,?)");
        try {
            ps.setString(1, name);
            ps.setString(2, producer);
            ps.setInt(3, price);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void makeOrder(Scanner sc) throws SQLException {
        System.out.println("Enter product id: ");
        int product_id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter product quantity: ");
        int quantity = Integer.parseInt(sc.nextLine());
        System.out.println("Enter customer id: ");
        int customer_id = Integer.parseInt(sc.nextLine());

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients2(product_id, quantity, customer_id) VALUES (?,?,?)");
        try {

            ps.setInt(1, product_id);
            ps.setInt(2, quantity);
            ps.setInt(3, customer_id);
            ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void showClients() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients2");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    System.out.println(metaData.getColumnName(i) + "\t\t");
                }
                System.out.println();
                while (rs.next()) {
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        System.out.println(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
}
