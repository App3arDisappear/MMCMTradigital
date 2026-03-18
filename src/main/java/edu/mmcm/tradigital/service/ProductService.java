package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.Product;
import edu.mmcm.tradigital.repo.ProductRepository;
import java.util.List;

public class ProductService {
    private final ProductRepository repository = new ProductRepository();

    public void addProduct(Product product) { repository.addProduct(product); }
    public List<Product> getAllProducts() { return repository.getAllProducts(); }
    public void updateProduct(Product product) { repository.updateProduct(product); }
    public void deleteProduct(String sku) { repository.deleteProduct(sku); }
}