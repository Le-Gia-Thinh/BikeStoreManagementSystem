package Model;

import java.util.List;

public interface I_List {



    void add();

    List<Product> searchByName ();

    void printProductList (List<Product> products);

    void update();

    void delete();

    void save();

    void print();



}
