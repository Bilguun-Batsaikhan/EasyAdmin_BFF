package com.certimeter.bff.controller;

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

import java.util.Map;

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
    public ResponseEntity<TicketResPagination> getAllTickets(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(ticketService.getAllTickets(accessTokenTrunked, pageNo, pageSize));
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

                AssetResPagination assets = assetService.getAllAssets(accessTokenTrunked, 0, 100, null, null, null, null, null, null, null, null, null, null);
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
