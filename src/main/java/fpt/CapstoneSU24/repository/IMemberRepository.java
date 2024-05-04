package fpt.CapstoneSU24.repository;



import fpt.CapstoneSU24.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IMemberRepository extends JpaRepository<Member, Integer> {
    public List<Member> findAll();
/*    public int save(Member member);
    public int update(Member member);
    public int  deleteById(int id);*/
}
