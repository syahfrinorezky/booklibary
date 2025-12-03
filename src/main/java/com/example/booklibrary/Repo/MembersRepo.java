package com.example.booklibrary.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booklibrary.Model.Members;

public interface MembersRepo extends JpaRepository<Members, Long> {
    Optional<Members> findByMemberCodeAndDeletedAtIsNull(String memberCode);

    Optional<Members> findByEmailAndDeletedAtIsNull(String email);

    Optional<Members> findTopByOrderByIdDesc();

    List<Members> findAllByDeletedAtIsNull();
}
