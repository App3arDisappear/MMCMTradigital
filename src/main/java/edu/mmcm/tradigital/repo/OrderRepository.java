package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import edu.mmcm.tradigital.model.Order;
import edu.mmcm.tradigital.model.Product;
import edu.mmcm.tradigital.database.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final MongoCollection<Document> collection;

    public OrderRepository() {
        collection = DatabaseConnection.getDatabase().getCollection("orders");
    }

    public void saveOrder(Order order) {
        List<Document> itemDocs = new ArrayList<>();
        for (Product p : order.getItems()) {
            itemDocs.add(new Document("sku", p.getSku())
                    .append("name", p.getName())
                    .append("price", p.getPrice()));
        }

        Document doc = new Document("orderId", order.getOrderId())
                .append("userEmail", order.getUserEmail())
                .append("total", order.getTotalAmount())
                .append("date", order.getOrderDate())
                .append("items", itemDocs);

        collection.insertOne(doc);
    }
}