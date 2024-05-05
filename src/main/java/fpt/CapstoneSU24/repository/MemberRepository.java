package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    @Override
    List<Member> findAll();
}
