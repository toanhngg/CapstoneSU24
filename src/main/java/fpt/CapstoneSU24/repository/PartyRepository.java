package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Integer> {
    @Override
    List<Party> findAll();
    Party findOneByPartyId(int id);
//public Origin findOneByOriginId(int id);
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

}
