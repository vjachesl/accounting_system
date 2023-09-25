package com.task.repository;

import com.task.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findCompanyByCompanyCode(@Param("companyCode") Integer companyCode);
    boolean existsCompanyByCompanyCode(@Param("companyCode")Integer companyCode);
}
