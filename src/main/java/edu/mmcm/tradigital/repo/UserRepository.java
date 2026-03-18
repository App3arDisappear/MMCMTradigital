package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import edu.mmcm.tradigital.model.User;
import edu.mmcm.tradigital.database.DatabaseConnection;

public class UserRepository {
    private final MongoCollection<Document> collection;

    public UserRepository() {
        collection = DatabaseConnection.getDatabase().getCollection("users");
    }

    public void save(User user) {
        Document doc = new Document("email", user.getEmail())
                .append("password", user.getPassword())
                .append("role", user.getRole());
        collection.insertOne(doc);
    }

    public User findByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        if (doc != null) {
            return new User(
                    doc.getString("email"),
                    doc.getString("password"),
                    doc.getString("role")
            );
        }
        return null;
    }
}