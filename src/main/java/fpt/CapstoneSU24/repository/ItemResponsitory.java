package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemResponsitory extends JpaRepository<Item, Integer> {
    @Query("select i from Item i where i.product.productId = :id ")
    List<Item> findByProductId(@Param("id")  Integer id);

    @Query("SELECT i FROM Item i WHERE i.productRecognition = :productRecognition")
    Item findByProductRecognition(@Param("productRecognition") String productRecognition);


    @Query("SELECT i FROM Item i WHERE i.currentOwner = :currentOwner")
    List<Item> findByCurrentOwner(@Param("currentOwner") String currentOwner);
}


