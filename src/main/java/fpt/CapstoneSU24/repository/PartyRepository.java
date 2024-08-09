package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Integer> {
    @Override
    List<Party> findAll();
    Party findOneByPartyId(int id);
//public Origin findOneByOriginId(int id);
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

    @Query("SELECT p FROM Party p WHERE p.email = :emailParty")
    Party findByPartyEmail(@Param("emailParty")  String emailParty);

}
