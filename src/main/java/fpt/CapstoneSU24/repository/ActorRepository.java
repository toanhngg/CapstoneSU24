package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
@Override
List<Actor> findAll();
Actor findOneByActorId(int id);
//public Origin findOneByOriginId(int id);
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

        }
