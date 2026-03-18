package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.mmcm.tradigital.database.DatabaseConnection;
import edu.mmcm.tradigital.model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository {
    private final MongoCollection<Document> collection;

    public UserRepository() {
        this.collection = DatabaseConnection.getDatabase().getCollection("users");
    }

    public void addUser(User user) {
        Document doc = new Document("id", user.getId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("role", user.getRole());
        collection.insertOne(doc);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                users.add(new User(
                        doc.getString("id"),
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("role")
                ));
            }
        }
        return users;
    }

    public void updateUser(User user) {
        Document updatedDoc = new Document("id", user.getId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("role", user.getRole());
        collection.replaceOne(eq("id", user.getId()), updatedDoc);
    }

    public void deleteUser(String id) {
        collection.deleteOne(eq("id", id));
    }
}