package com.certimeter.bff.resourcemodel;

import com.certimeter.bff.enumeration.Priority;
import com.certimeter.bff.enumeration.ProgressStage;
import com.certimeter.bff.enumeration.Status;
import com.certimeter.bff.enumeration.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long id;
    private Long assetId;
    private Long userId;
    private String title;
    private String context;
    private TicketType ticketType;
    private Status status;
    private Priority priority = Priority.MEDIUM;
    private Timestamp issuedAt;
    private Timestamp closedAt;
    private String resolutionDetails;
    private Timestamp lastUpdatedAt;
    private ProgressStage progressStage = ProgressStage.NEW;
}
