package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.pagination.TicketResPagination;
import com.certimeter.bff.resourcemodel.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {
    @Value("${api.ticket.path}")
    private String ticketApiUrl;

    private final RestTemplate restTemplate;
    private final BffService bffService;
    private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);

    public TicketService(RestTemplate restTemplate, BffService bffService) {
        this.restTemplate = restTemplate;
        this.bffService = bffService;
    }

    public TicketResPagination getAllTickets(String accessToken, int pageNo, int pageSize,
                                             Optional<String> modelName,
                                             Optional<String> modelNameMatchMode,
                                             Optional<String> username,
                                             Optional<String> usernameMatchMode,
                                             Optional<String> title,
                                             Optional<String> titleMatchMode,
                                             Optional<String> context,
                                             Optional<String> contextMatchMode,
                                             Optional<String> ticketType,
                                             Optional<String> ticketTypeMatchMode,
                                             Optional<String> status,
                                             Optional<String> statusMatchMode,
                                             Optional<String> priority,
                                             Optional<String> priorityMatchMode,
                                             Optional<String> issuedAt,
                                             Optional<String> issuedAtMatchMode,
                                             Optional<String> closedAt,
                                             Optional<String> closedAtMatchMode,
                                             Optional<String> resolutionDetails,
                                             Optional<String> resolutionDetailsMatchMode,
                                             Optional<String> lastUpdatedAt,
                                             Optional<String> lastUpdatedAtMatchMode) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = buildUrlWithParams(pageNo, pageSize,
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
            LOG.info("Request URL: {}", url);
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

    private String buildUrlWithParams(int pageNo, int pageSize,
                                      Optional<String> modelName,
                                      Optional<String> modelNameMatchMode,
                                      Optional<String> username,
                                      Optional<String> usernameMatchMode,
                                      Optional<String> title,
                                      Optional<String> titleMatchMode,
                                      Optional<String> context,
                                      Optional<String> contextMatchMode,
                                      Optional<String> ticketType,
                                      Optional<String> ticketTypeMatchMode,
                                      Optional<String> status,
                                      Optional<String> statusMatchMode,
                                      Optional<String> priority,
                                      Optional<String> priorityMatchMode,
                                      Optional<String> issuedAt,
                                      Optional<String> issuedAtMatchMode,
                                      Optional<String> closedAt,
                                      Optional<String> closedAtMatchMode,
                                      Optional<String> resolutionDetails,
                                      Optional<String> resolutionDetailsMatchMode,
                                      Optional<String> lastUpdatedAt,
                                      Optional<String> lastUpdatedAtMatchMode) {
        StringBuilder urlBuilder = new StringBuilder(String.format("%s?page=%d&pageSize=%d", ticketApiUrl, pageNo, pageSize));
        LOG.info("URL: {}", urlBuilder.toString());

        bffService.appendOptionalParam(urlBuilder, "modelName", modelName);
        bffService.appendOptionalParam(urlBuilder, "modelNameMatchMode", modelNameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "username", username);
        bffService.appendOptionalParam(urlBuilder, "usernameMatchMode", usernameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "title", title);
        bffService.appendOptionalParam(urlBuilder, "titleMatchMode", titleMatchMode);
        bffService.appendOptionalParam(urlBuilder, "context", context);
        bffService.appendOptionalParam(urlBuilder, "contextMatchMode", contextMatchMode);
        bffService.appendOptionalParam(urlBuilder, "ticketType", ticketType);
        bffService.appendOptionalParam(urlBuilder, "ticketTypeMatchMode", ticketTypeMatchMode);
        bffService.appendOptionalParam(urlBuilder, "status", status);
        bffService.appendOptionalParam(urlBuilder, "statusMatchMode", statusMatchMode);
        bffService.appendOptionalParam(urlBuilder, "priority", priority);
        bffService.appendOptionalParam(urlBuilder, "priorityMatchMode", priorityMatchMode);
        bffService.appendOptionalParam(urlBuilder, "issuedAt", issuedAt);
        bffService.appendOptionalParam(urlBuilder, "issuedAtMatchMode", issuedAtMatchMode);

        return urlBuilder.toString();
    }
}
