package com.certimeter.bff.controller;

import com.certimeter.bff.dto.TicketDTO;
import com.certimeter.bff.dto.TicketResPagDTO;
import com.certimeter.bff.enumeration.Status;
import com.certimeter.bff.enumeration.UserRoleEnum;
import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.pagination.AssetResPagination;
import com.certimeter.bff.pagination.TicketResPagination;
import com.certimeter.bff.resourcemodel.Ticket;
import com.certimeter.bff.service.AssetService;
import com.certimeter.bff.service.JWTService;
import com.certimeter.bff.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("bff/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final JWTService jwtService;
    private final AssetService assetService;
    public TicketController(TicketService ticketService, JWTService jwtService, AssetService assetService) {
        this.ticketService = ticketService;
        this.jwtService = jwtService;
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<TicketResPagDTO> getAllTickets(
            @RequestParam Optional<String> modelName,
            @RequestParam Optional<String> modelNameMatchMode,
            @RequestParam Optional<String> username,
            @RequestParam Optional<String> usernameMatchMode,
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> titleMatchMode,
            @RequestParam Optional<String> context,
            @RequestParam Optional<String> contextMatchMode,
            @RequestParam Optional<String> ticketType,
            @RequestParam Optional<String> ticketTypeMatchMode,
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> statusMatchMode,
            @RequestParam Optional<String> priority,
            @RequestParam Optional<String> priorityMatchMode,
            @RequestParam Optional<String> issuedAt,
            @RequestParam Optional<String> issuedAtMatchMode,
            @RequestParam Optional<String> closedAt,
            @RequestParam Optional<String> closedAtMatchMode,
            @RequestParam Optional<String> resolutionDetails,
            @RequestParam Optional<String> resolutionDetailsMatchMode,
            @RequestParam Optional<String> lastUpdatedAt,
            @RequestParam Optional<String> lastUpdatedAtMatchMode,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,@RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);

        TicketResPagination tickets = ticketService.getAllTickets(accessTokenTrunked, pageNo,
                pageSize,
                modelName,
                modelNameMatchMode,
                username,
                usernameMatchMode,
                title,
                titleMatchMode,
                context,
                contextMatchMode,
                ticketType,
                ticketTypeMatchMode,
                status,
                statusMatchMode,
                priority,
                priorityMatchMode,
                issuedAt,
                issuedAtMatchMode,
                closedAt,
                closedAtMatchMode,
                resolutionDetails,
                resolutionDetailsMatchMode,
                lastUpdatedAt,
                lastUpdatedAtMatchMode);
                List<TicketDTO> ticketDTOS = tickets.getData().stream().map(ticket -> {
                    TicketDTO ticketDTO = new TicketDTO();
                    ticketDTO.setId(ticket.getId());
                    ticketDTO.setModelName(ticket.getAsset().getModelName());
                    ticketDTO.setUsername(ticket.getUser().getUsername());
                    ticketDTO.setTitle(ticket.getTitle());
                    ticketDTO.setContext(ticket.getContext());
                    ticketDTO.setTicketType(ticket.getTicketType());
                    ticketDTO.setStatus(ticket.getStatus());
                    ticketDTO.setPriority(ticket.getPriority());
                    ticketDTO.setIssuedAt(ticket.getIssuedAt());
                    ticketDTO.setClosedAt(ticket.getClosedAt());
                    ticketDTO.setResolutionDetails(ticket.getResolutionDetails());
                    ticketDTO.setLastUpdatedAt(ticket.getLastUpdatedAt());
                    return ticketDTO;
                }).toList();

        TicketResPagDTO ticketResPagDTO = new TicketResPagDTO();
        ticketResPagDTO.setData(ticketDTOS);
        ticketResPagDTO.setPageNo(tickets.getPageNo());
        ticketResPagDTO.setPageSize(tickets.getPageSize());
        ticketResPagDTO.setTotalPages(tickets.getTotalPages());
        ticketResPagDTO.setTotalElements(tickets.getTotalElements());
        ticketResPagDTO.setLast(tickets.isLast());

        return ResponseEntity.ok(ticketResPagDTO);
    }

    //Only users can create tickets
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);

        Long userId = jwtService.getClaimFromAccessToken(accessTokenTrunked, "userId", Long.class);
        ticket.setUserId(userId);
        String role = jwtService.getClaimFromAccessToken(accessTokenTrunked, "role", String.class);
        ResponseEntity<Ticket> response = null;
            if(UserRoleEnum.USER.name().equals(role)) {
                ticket.setStatus(Status.OPEN);

                AssetResPagination assets = assetService.getAllAssets(accessTokenTrunked, 0, 100, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
                if(assets.getData().stream().noneMatch(asset -> asset.getId().equals(ticket.getAssetId()))) {
                    throw new CustomClientException(HttpStatus.FORBIDDEN,"Asset is not assigned to the user.");
                }
                response = ResponseEntity.ok(ticketService.createTicket(accessTokenTrunked, ticket));
            }
            if(response == null) {
                throw new CustomClientException(HttpStatus.FORBIDDEN, role +" is not authorized to create a ticket.");
            }
            return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(ticketService.updateTicket(accessTokenTrunked, id, updates));
    }
}
