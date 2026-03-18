package edu.mmcm.tradigital.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.mmcm.tradigital.database.DatabaseConnection;
import edu.mmcm.tradigital.model.Order;
import edu.mmcm.tradigital.model.OrderLine;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class OrderRepository {
    private final MongoCollection<Document> collection;

    public OrderRepository() {
        this.collection = DatabaseConnection.getDatabase().getCollection("orders");
    }

    public void addOrder(Order order) {
        List<Document> itemsList = new ArrayList<>();
        for (OrderLine item : order.getItems()) {
            itemsList.add(new Document("productId", item.getProductId())
                    .append("quantity", item.getQuantity())
                    .append("unitPrice", item.getUnitPrice()));
        }

        Document doc = new Document("orderId", order.getOrderId())
                .append("userId", order.getUserId())
                .append("items", itemsList)
                .append("orderType", order.getOrderType())
                .append("totalAmount", order.getTotalAmount())
                .append("orderDate", order.getOrderDate().toString())
                .append("dueDate", order.getDueDate() != null ? order.getDueDate().toString() : null)
                .append("status", order.getStatus());
        collection.insertOne(doc);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                // Rebuild the items list
                List<Document> itemsDocs = (List<Document>) doc.get("items");
                List<OrderLine> itemsList = new ArrayList<>();
                if (itemsDocs != null) {
                    for (Document itemDoc : itemsDocs) {
                        itemsList.add(new OrderLine(
                                itemDoc.getString("productId"),
                                itemDoc.getInteger("quantity"),
                                itemDoc.getDouble("unitPrice")
                        ));
                    }
                }

                // Parse dates
                LocalDate orderDate = LocalDate.parse(doc.getString("orderDate"));
                String dueDateStr = doc.getString("dueDate");
                LocalDate dueDate = dueDateStr != null ? LocalDate.parse(dueDateStr) : null;

                orders.add(new Order(
                        doc.getString("orderId"),
                        doc.getString("userId"),
                        itemsList,
                        doc.getString("orderType"),
                        doc.getDouble("totalAmount"),
                        orderDate,
                        dueDate,
                        doc.getString("status")
                ));
            }
        }
        return orders;
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        Document update = new Document("$set", new Document("status", newStatus));
        collection.updateOne(eq("orderId", orderId), update);
    }

    public void deleteOrder(String orderId) {
        collection.deleteOne(eq("orderId", orderId));
    }
}