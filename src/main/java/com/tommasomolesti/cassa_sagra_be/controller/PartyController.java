package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.dto.PartyRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.PartyResponseDTO;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.service.PartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/parties")
@Tag(name = "Party")
public class PartyController {
    private final PartyService partyService;


    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "Get User's Parties")
    public ResponseEntity<List<PartyResponseDTO>> getUserParties(@PathVariable UUID userId) {
        List<PartyResponseDTO> parties = partyService.getUserParties(userId);
        return ResponseEntity.ok().body(parties);
    }

    @PostMapping
    @Operation(summary = "Create a Party")
    public ResponseEntity<PartyResponseDTO> createParty(
            @RequestBody PartyRequestDTO partyRequest,
            @AuthenticationPrincipal User currentUser
            ) {
        UUID creatorId = currentUser.getId();
        PartyResponseDTO newParty = partyService.createParty(partyRequest, creatorId);
        return ResponseEntity.ok(newParty);
    }

    // TODO
    // - party deleting
    // - party info updating
}
