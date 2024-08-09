package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Role;
import fpt.CapstoneSU24.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    public Role findOneByRoleId(int id);

}