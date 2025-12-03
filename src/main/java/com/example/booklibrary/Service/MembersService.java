package com.example.booklibrary.Service;

import java.util.List;

import org.springframework.stereotype.Service;

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
        return String.format("MB%05d", nextNumber);
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

        membersRepo.findByEmailAndDeletedAtIsNull(dto.getEmail()).filter(m -> !m.getId().equals(member.getId()))
                .ifPresent(m -> {
                    throw new RuntimeException("Email is already exists");
                });

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());

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
