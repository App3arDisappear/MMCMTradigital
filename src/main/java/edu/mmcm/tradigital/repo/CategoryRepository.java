package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import edu.mmcm.tradigital.model.Category;
import edu.mmcm.tradigital.database.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final MongoCollection<Document> collection;

    public CategoryRepository() {
        // Connects to the "categories" collection in your MongoDB
        collection = DatabaseConnection.getDatabase().getCollection("categories");
    }

    public void addCategory(Category category) {
        Document doc = new Document("categoryId", category.getId())
                .append("name", category.getName());
        collection.insertOne(doc);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Category cat = new Category(
                        doc.getString("categoryId"),
                        doc.getString("name")
                );
                categories.add(cat);
            }
        }
        return categories;
    }

    public void updateCategory(Category category) {
        Document update = new Document("$set", new Document("name", category.getName()));
        collection.updateOne(Filters.eq("categoryId", category.getId()), update);
    }

    public void deleteCategory(String categoryId) {
        collection.deleteOne(Filters.eq("categoryId", categoryId));
    }
}