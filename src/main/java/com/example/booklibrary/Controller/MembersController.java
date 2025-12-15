package com.example.booklibrary.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booklibrary.Dto.ApiResponse.ApiResponse;
import com.example.booklibrary.Dto.Members.MembersReq;
import com.example.booklibrary.Dto.Members.MembersRes;
import com.example.booklibrary.Service.MembersService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/members")
public class MembersController {
    private final MembersService membersService;

    public MembersController(MembersService membersService) {
        this.membersService = membersService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MembersRes>>> getAllMembers() {
        List<MembersRes> members = membersService.getAllMembers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Members retrieved successfully", members));
    }

    @GetMapping("/{memberCode}")
    public ResponseEntity<ApiResponse<MembersRes>> getMemberByCode(@PathVariable String memberCode) {
        MembersRes member = membersService.getMemberByCode(memberCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member retrieved successfully", member));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MembersRes>> setMember(@RequestBody @Valid MembersReq dto) {
        MembersRes response = membersService.createMember(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member created successfully", response));
    }

    @PatchMapping("/{memberCode}")
    public ResponseEntity<MembersRes> updateMember(@PathVariable String memberCode,
            @RequestBody @Valid MembersReq dto) {
        MembersRes updated = membersService.updateMember(memberCode, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{memberCode}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable String memberCode) {
        membersService.deleteMember(memberCode);
        return ResponseEntity.ok(new ApiResponse<>(true, "Member deleted successfully", null));
    }
}
