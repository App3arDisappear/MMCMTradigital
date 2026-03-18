package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.Category;
import edu.mmcm.tradigital.repo.CategoryRepository;
import java.util.List;

public class CategoryService {
    private final CategoryRepository repository = new CategoryRepository();

    public void addCategory(Category category) { repository.addCategory(category); }
    public List<Category> getAllCategories() { return repository.getAllCategories(); }
    public void deleteCategory(String id) { repository.deleteCategory(id); }
}