package com.example.booklibrary.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.booklibrary.Dto.ApiResponse.PageResponse;
import com.example.booklibrary.Dto.ApiResponse.PaginationMeta;
import com.example.booklibrary.Dto.Members.MembersReq;
import com.example.booklibrary.Dto.Members.MembersRes;
import com.example.booklibrary.Model.Members;
import com.example.booklibrary.Repo.MembersRepo;

import jakarta.transaction.Transactional;

@Service
public class MembersService {
    private final MembersRepo membersRepo;

    private String generateMemberCode() {
        Members lastMember = membersRepo.findTopByOrderByIdDesc().orElse(null);
        int nextNumber = (lastMember == null) ? 1 : lastMember.getId().intValue() + 1;
        return String.format("MB%04d", nextNumber);
    }

    private MembersRes mapToRes(Members member) {
        return MembersRes.builder()
                .id(member.getId())
                .memberCode(member.getMemberCode())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public MembersService(MembersRepo membersRepo) {
        this.membersRepo = membersRepo;
    }

    public List<MembersRes> getAllMembers() {
        List<Members> members = membersRepo.findAllByDeletedAtIsNull();
        return members.stream().map(this::mapToRes).toList();
    }

    public PageResponse<MembersRes> getAllMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Members> membersPage = membersRepo.findAllByDeletedAtIsNull(pageable);

        List<MembersRes> membersResList = membersPage.getContent().stream()
                .map(this::mapToRes)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .page(membersPage.getNumber())
                .size(membersPage.getSize())
                .totalPages(membersPage.getTotalPages())
                .totalElements(membersPage.getTotalElements())
                .build();

        return PageResponse.<MembersRes>builder()
                .content(membersResList)
                .meta(meta)
                .build();
    }

    public MembersRes getMemberByCode(String memberCode) {
        Members member = membersRepo.findByMemberCodeAndDeletedAtIsNull(memberCode)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return mapToRes(member);
    }

    @Transactional
    public MembersRes createMember(MembersReq dto) {
        if (membersRepo.findByMemberCodeAndDeletedAtIsNull(dto.getMemberCode()).isPresent()) {
            throw new RuntimeException("Member code already exists");
        }

        Members member = new Members();
        if (dto.getMemberCode() == null || dto.getMemberCode().isEmpty()) {
            member.setMemberCode(generateMemberCode());
        } else {
            member.setMemberCode(dto.getMemberCode());
        }
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());

        membersRepo.save(member);

        return mapToRes(member);
    }

    @Transactional
    public MembersRes updateMember(String memberCode, MembersReq dto) {
        Members member = membersRepo.findByMemberCodeAndDeletedAtIsNull(memberCode)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (dto.getName() != null) {
            if (dto.getName().isBlank()) {
                throw new RuntimeException("Name cannot be empty");
            }
            member.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            if (dto.getEmail().isBlank()) {
                throw new RuntimeException("Email cannot be empty");
            }
            membersRepo.findByEmailAndDeletedAtIsNull(dto.getEmail()).filter(m -> !m.getId().equals(member.getId()))
                    .ifPresent(m -> {
                        throw new RuntimeException("Email is already exists");
                    });
            member.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null) {
            if (dto.getPhone().isBlank()) {
                throw new RuntimeException("Phone cannot be empty");
            }
            member.setPhone(dto.getPhone());
        }

        if (dto.getAddress() != null) {
            if (dto.getAddress().isBlank()) {
                throw new RuntimeException("Address cannot be empty");
            }
            member.setAddress(dto.getAddress());
        }

        membersRepo.save(member);

        return mapToRes(member);
    }

    @Transactional
    public void deleteMember(String memberCode) {
        Members member = membersRepo.findByMemberCodeAndDeletedAtIsNull(memberCode)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setDeletedAt(java.time.LocalDateTime.now());
        membersRepo.save(member);
    }
}
