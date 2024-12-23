package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.pagination.TicketResPagination;
import com.certimeter.bff.resourcemodel.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TicketService {
    @Value("${api.ticket.path}")
    private String ticketApiUrl;

    private final RestTemplate restTemplate;
    private final BffService bffService;
    public TicketService(RestTemplate restTemplate, BffService bffService) {
        this.restTemplate = restTemplate;
        this.bffService = bffService;
    }

    public TicketResPagination getAllTickets(String accessToken, int pageNo, int pageSize) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = String.format("%s?page=%d&pageSize=%d", ticketApiUrl, pageNo, pageSize);
            ResponseEntity<TicketResPagination> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    TicketResPagination.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Ticket service is currently unavailable. Please try again later.");
        }
    }

    public Ticket createTicket(String accessToken, Ticket ticket) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Ticket> entity = new HttpEntity<>(ticket, headers);
            ResponseEntity<Ticket> response = restTemplate.exchange(
                    ticketApiUrl,
                    HttpMethod.POST,
                    entity,
                    Ticket.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Ticket service is currently unavailable. Please try again later.");
        }
    }

    public Ticket updateTicket(String accessToken, Long id, Map<String, Object> updates) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
            String url = String.format("%s/%d", ticketApiUrl, id);
            ResponseEntity<Ticket> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    Ticket.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Ticket service is currently unavailable. Please try again later.");
        }
    }
}
