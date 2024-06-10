package ua.epam.mishchenko.ticketbooking.datamigration;

import com.mongodb.ConnectionString;
import com.mongodb.Function;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import org.bson.Document;

import java.sql.*;
import java.util.*;

import static java.util.Arrays.asList;

public class MigrationJob {

    public static void main(String[] args) throws SQLException {
        // Connect to PostgreSQL
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/ticket_booking",
                "postgres",
                "root")
        ) {
            // Connect to MongoDB
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("ticket_booking");

            // Migrate events
            Map<String, Document> eventsById = new HashMap<>();
            migrateTable(conn, "events", database.getCollection("events"), rs -> {
                String eventId = null;
                try {
                    eventId = rs.getString("id");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Document eventDoc = null;
                try {
                    eventDoc = new Document()
                            .append("id", eventId)
                            .append("title", rs.getString("title"))
                            .append("date", rs.getTimestamp("date").toInstant())
                            .append("ticketPrice", rs.getBigDecimal("ticket_price"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                eventsById.put(eventId, eventDoc);
                return eventDoc;
            });

            // Migrate tickets
            Map<String, List<Document>> ticketsByUser = new HashMap<>();
            migrateTable(conn, "tickets", database.getCollection("tickets"), rs -> {
                String userId = null;
                try {
                    userId = rs.getString("user_id");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Document ticketDoc = null;
                try {
                    ticketDoc = new Document()
                            .append("id", rs.getString("id"))
                            .append("event", eventsById.get(rs.getString("event_id")))
                            .append("place", rs.getInt("place"))
                            .append("category", rs.getString("category"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ticketsByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(ticketDoc);
                return ticketDoc;
            });

            // Migrate users
            migrateUsers(conn, database.getCollection("users"), ticketsByUser);
        }
    }

    private static void migrateUsers(Connection conn, MongoCollection<Document> collection, Map<String, List<Document>> ticketsByUser) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT users.id, users.name, users.email, user_accounts.money FROM users JOIN user_accounts ON users.id = user_accounts.user_id")) {
            while (rs.next()) {
                String userId = rs.getString("id");
                Document doc = new Document()
                        .append("id", userId)
                        .append("name", rs.getString("name"))
                        .append("email", rs.getString("email"))
                        .append("money", rs.getBigDecimal("money"))
                        .append("tickets", ticketsByUser.getOrDefault(userId, Collections.emptyList()));
                collection.insertOne(doc);
            }
        }
    }

    private static void migrateTable(Connection conn, String tableName, MongoCollection<Document> collection, Function<ResultSet, Document> mapper) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                Document doc = mapper.apply(rs);
                collection.insertOne(doc);
            }
        }
    }
}
