package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import edu.mmcm.tradigital.model.Product;
import edu.mmcm.tradigital.database.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final MongoCollection<Document> collection;

    public ProductRepository() {
        collection = DatabaseConnection.getDatabase().getCollection("products");
    }

    public void addProduct(Product product) {
        Document doc = new Document("sku", product.getSku())
                .append("name", product.getName())
                .append("price", product.getPrice())
                .append("categoryId", product.getCategoryId())
                .append("stockQuantity", product.getStockQuantity());
        collection.insertOne(doc);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                products.add(new Product(
                        doc.getString("sku"),
                        doc.getString("name"),
                        doc.getDouble("price"),
                        doc.getString("categoryId"),
                        doc.getInteger("stockQuantity")
                ));
            }
        }
        return products;
    }

    public void updateProduct(Product product) {
        Document update = new Document("$set", new Document("name", product.getName())
                .append("price", product.getPrice())
                .append("categoryId", product.getCategoryId())
                .append("stockQuantity", product.getStockQuantity()));
        collection.updateOne(Filters.eq("sku", product.getSku()), update);
    }

    public void deleteProduct(String sku) {
        collection.deleteOne(Filters.eq("sku", sku));
    }
}