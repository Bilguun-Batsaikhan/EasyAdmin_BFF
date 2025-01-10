package com.certimeter.bff.dto;

import com.certimeter.bff.enumeration.Priority;
import com.certimeter.bff.enumeration.Status;
import com.certimeter.bff.enumeration.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private String modelName;
    private String username;
    private String title;
    private String context;
    private TicketType ticketType;
    private Status status;
    private Priority priority;
    private Timestamp issuedAt;
    private Timestamp closedAt;
    private String resolutionDetails;
    private Timestamp lastUpdatedAt;
}