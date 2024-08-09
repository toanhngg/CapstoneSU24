package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.ImageSupportSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageSupportSystemRepository extends JpaRepository<ImageSupportSystem, Integer> {
    @Query("SELECT i.filePath " +
            "FROM ImageSupportSystem i " +
            "WHERE i.supportSystem.supportSystemId = :id AND i.type =:type")
    List<String> findImagesBySupSystemIdAndType(@Param("id") int id, @Param("type") int type);
}