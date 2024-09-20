package Controller;

import Model.Brand;
import Model.Category;
import Model.I_List;
import Model.Product;
import Utility.Form;
import Utility.Sort;
import Utility.Tools;
import View.Display;
import java.io.*;
import java.util.*;

public class ProductController extends TreeMap<String , Product > implements I_List {
 
    private Map<String, Brand> brandMap;
    private Map<String, Category> categoryMap;

    public ProductController() {
        this.brandMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
    }
    @Override
    public void add() {
        while (true) {
            String id = Form.validId("Input Product Id");
            if (this.containsKey(id)) {
                System.out.println("Duplicate product id!!!");
                continue;
            }

            String name = Tools.inputString("Input Product Name");
            String brandId = Tools.inputString("Input Brand ID");
            String categoryId = Tools.inputString("Input Category ID");
            int modelYear = Form.validYear("Input Model Year");
            double listPrice = Tools.inputDouble("Input Value");


            Brand brand = handleBrand(brandId);
            Category category = handleCategory(categoryId);


            if (brand == null || category == null) {
                System.out.println("Cannot add product due to invalid brand or category.");
                continue;
            }

            // Create a new product
            Product product = new Product(id, name, brandId, categoryId, modelYear, listPrice);
            this.put(id, product);
            System.out.println("Added Successfully!!!");

            if (!Tools.confirmYesNo("Do you want to continue to add new products (Y/N)?")) {
                break;
            }
        }
    }

    private Brand handleBrand(String brandId) {
        if (!brandMap.containsKey(brandId)) {
            String brandName = Tools.inputString("Input Brand Name: ");
            Brand newBrand = new Brand(brandName, brandId);
            brandMap.put(brandId, newBrand);
            return newBrand;
        } else {
            System.out.println("Brand ID already exists. Cannot add or update this brand.");
            return null;
        }
    }

    private Category handleCategory(String categoryId) {
        if (!categoryMap.containsKey(categoryId)) {
            String categoryName = Tools.inputString("Input Category Name: ");
            Category newCategory = new Category(categoryId, categoryName);
            categoryMap.put(categoryId, newCategory);
            return newCategory;
        } else {
            System.out.println("Category ID already exists. Cannot add or update this category.");
            return null;
        }
    }


    @Override
    public void update() {
        String id = Tools.inputString("Input Product ID to update: ");
        Product product = this.get(id);

        if (product == null) {
            System.out.println("Product does not exist.");
        } else {
            String newName = Tools.updateString("Input new Product Name", product.getName());
            product.setName(newName);

            String newBrandId = Tools.updateString("Input new Brand ID", product.getBrandId());
            handleBrand(newBrandId);
            product.setBrandId(newBrandId);

            String newCategoryId = Tools.updateString("Input new Category ID", product.getCategoryId());
            handleCategory(newCategoryId);
            product.setCategoryId(newCategoryId);

            int newModelYear = Tools.updateInt("Input new Model Year", 2000, 9999, product.getModelYear());
            product.setModelYear(newModelYear);
            double newListPrice = Tools.updateDouble("Input new List Price", product.getListPrice());
            product.setListPrice(newListPrice);

            System.out.println("Updated Successfully!");
        }
    }

    @Override
    public List<Product> searchByName() {
        String searchString = Tools.inputString("Input part of Product Name to search: ");
        List<Product> foundProducts = new ArrayList<>();

        for (Product product : this.values()) {
            if (product.getName().toLowerCase().contains(searchString.toLowerCase())) {
                foundProducts.add(product);
            }
        }

        if (foundProducts.isEmpty()) {
            System.out.println("Have no any Product");
        } else {
            foundProducts.sort(Sort.sortByModelYear);
        }

        return foundProducts;
    }

    @Override
    public void printProductList(List<Product> products) {
        for (Product product : products) {
            Brand brand = brandMap.get(product.getBrandId());
            Category category = categoryMap.get(product.getCategoryId());
            System.out.printf("%s, %s, %s, %s, %d, %.2f%n",
                    product.getId(),
                    product.getName(),
                    brand != null ? brand.getBrandName() : "Unknown",
                    category != null ? category.getCategoryName() : "Unknown",
                    product.getModelYear(),
                    product.getListPrice());
        }
    }






    @Override
    public void delete() {
        String id = Tools.inputString("Input Product ID to delete: ");
        Product product = this.get(id);

        if (product == null) {
            System.out.println("Product does not exist.");
        } else {
            Brand brand = brandMap.get(product.getBrandId());
            Category category = categoryMap.get(product.getCategoryId());

            if (brand != null) {
                brandMap.remove(product.getBrandId());
            }
            if (category != null) {
                categoryMap.remove(product.getCategoryId());
            }


            this.remove(id);
            System.out.println("Deleted Successfully!");


            save();
        }
    }
  
    @Override
    public void save() {
     
        try (ObjectOutputStream productOut = new ObjectOutputStream(new FileOutputStream("products.txt"));
                ObjectOutputStream brandOut = new ObjectOutputStream(new FileOutputStream("brands.txt"));
                ObjectOutputStream categoryOut = new ObjectOutputStream(new FileOutputStream("categories.txt"))) {

            productOut.writeObject(new ArrayList<>(this.values()));
            productOut.flush();

            brandOut.writeObject(new HashMap<>(brandMap));
            brandOut.flush();
      
            categoryOut.writeObject(new HashMap<>(categoryMap));
            categoryOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     *
     */
    @Override
    public void print() {
        
            try (ObjectInputStream productIn = new ObjectInputStream(new FileInputStream("products.txt"));
                ObjectInputStream brandIn = new ObjectInputStream(new FileInputStream("brands.txt"));
                ObjectInputStream categoryIn = new ObjectInputStream(new FileInputStream("categories.txt"))) {

            // Read list products
            List<Product> productList = (List<Product>) productIn.readObject();
            brandMap = (Map<String, Brand>) brandIn.readObject();
            categoryMap = (Map<String, Category>) categoryIn.readObject();
            //Sort products
                productList.sort(Sort.sortByPriceThenName);

            // Display product
            Display.printProducts(productList, brandMap, categoryMap);

            }    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                            throw new RuntimeException(e);
                          
            }
        }
    }





