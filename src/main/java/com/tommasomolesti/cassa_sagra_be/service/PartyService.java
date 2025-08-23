package com.tommasomolesti.cassa_sagra_be.service;

import com.tommasomolesti.cassa_sagra_be.dto.PartyRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.PartyResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.PartyResumeDTO;
import com.tommasomolesti.cassa_sagra_be.exception.AccessDeniedException;
import com.tommasomolesti.cassa_sagra_be.exception.PartyNotFoundException;
import com.tommasomolesti.cassa_sagra_be.exception.UserNotFoundException;
import com.tommasomolesti.cassa_sagra_be.mapper.PartyMapper;
import com.tommasomolesti.cassa_sagra_be.model.Party;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.repository.ArticleOrderedRepository;
import com.tommasomolesti.cassa_sagra_be.repository.PartyRepository;
import com.tommasomolesti.cassa_sagra_be.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;
    private final UserRepository userRepository;
    private final ArticleOrderedRepository articleOrderedRepository;

    public PartyService(
            PartyRepository partyRepository,
            PartyMapper partyMapper,
            UserRepository userRepository,
            ArticleOrderedRepository articleOrderedRepository
    ) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.userRepository = userRepository;
        this.articleOrderedRepository = articleOrderedRepository;
    }

    public List<PartyResponseDTO> getUserParties(UUID usedId) {
        List<Party> parties = partyRepository.findAllByCreatorId(usedId);
        return parties.stream().map(partyMapper::toDTO).toList();
    }

    public PartyResponseDTO createParty(PartyRequestDTO partyRequest, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("Creator user not found with id: " + creatorId));

        Party newParty = new Party();
        newParty.setName(partyRequest.getName());
        newParty.setCreator(creator);

        Party savedParty = partyRepository.save(newParty);

        return partyMapper.toDTO(savedParty);
    }

    public PartyResponseDTO updateParty(
            Integer partyId,
            PartyRequestDTO partyRequest,
            UUID authenticatedUserId
    ) {
        Party partyToUpdate = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));

        if(!partyToUpdate.getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to modify this party");
        }

        partyToUpdate.setName(partyRequest.getName());

        Party updatedParty = partyRepository.save(partyToUpdate);

        return partyMapper.toDTO(updatedParty);
    }

    @Transactional
    public void deleteParty(Integer partyId, UUID authenticatedUserId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));

        if (!party.getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to delete this party");
        }

        partyRepository.delete(party);
    }

    public List<PartyResumeDTO> getPartyResume(Integer partyId, UUID authenticatedUserId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException("Party not found with id: " + partyId));
        if (!party.getCreator().getId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("User is not authorized to access this party's resume");
        }

        return articleOrderedRepository.getPartyResume(partyId);
    }
}
