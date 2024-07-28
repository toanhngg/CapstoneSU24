package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.OrgNameUserDTO;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OriginRepository extends JpaRepository<Origin, Integer> {
    @Override
    List<Origin> findAll();
    public Origin findOneByOriginId(int id);
//    @Query("SELECT o " +
//            "FROM Item i " +
//            "LEFT JOIN i.origin o " +
//            "LEFT JOIN User u ON o.org_name = u.org_name " +
//            "LEFT JOIN ItemLog il ON il.item.itemId = i.itemId " +
//            "GROUP BY o.org_name " +
//            "ORDER BY COUNT(il.itemLogId) DESC")
//    List<Origin> findTop5OrgNames(Pageable pageable);

    @Query("SELECT new fpt.CapstoneSU24.dto.OrgNameUserDTO(o.org_name, u.userId, u.profileImage) " +
            "FROM Item i " +
            "LEFT JOIN i.origin o " +
            "LEFT JOIN User u ON o.org_name = u.org_name " +
            "LEFT JOIN ItemLog il ON il.item.itemId = i.itemId " +
            "GROUP BY o.org_name, u.userId, u.profileImage " +
            "ORDER BY COUNT(il.itemLogId) DESC")
    List<OrgNameUserDTO> findTop5OrgNames(Pageable pageable);
//    @Query("SELECT o FROM Origin o WHERE o.Product.productId = :id")
//    List<Origin> findAllByProductId(@Param("id")  int id);

}
