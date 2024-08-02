package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT r FROM Report r " +
            "WHERE (:code IS NULL OR :code = '' OR r.code LIKE %:code%) " +
            "AND (:title IS NULL OR :title = '' OR r.title LIKE %:title%) " +
            "AND (:emailReport IS NULL OR :emailReport = '' OR r.createBy.email LIKE %:emailReport%) " +
            "AND (:productId IS NULL OR :productId < 1 OR r.itemId.product.productId = :productId) " +
            "AND (:reportTo IS NULL OR :reportTo < 1 OR r.reportTo.userId = :reportTo) " +
            "AND (:type IS NULL OR :type < 1 OR r.type = :type) " +
            "AND (:status IS NULL OR :status < 1 OR r.status = :status) " +
            "AND (:dateFrom IS NULL OR :dateFrom = 0 OR r.createOn >= :dateFrom) " +
            "AND (:dateTo IS NULL OR :dateTo = 0 OR r.createOn <= :dateTo)")
    Page<Report> findReports(@Param("code") String code,
                             @Param("title") String title,
                             @Param("reportTo") Integer reportTo,
                             @Param("type") Integer type,
                             @Param("dateFrom") Long dateFrom,
                             @Param("dateTo") Long dateTo,
                             @Param("status") Integer status,
                             @Param("emailReport") String emailReport,
                             @Param("productId") Integer productId,
                             Pageable pageable);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.itemId.itemId = :itemId")
    int countItem(@Param("itemId") Integer itemId);

    Report getOneByreportId(int id);

}
