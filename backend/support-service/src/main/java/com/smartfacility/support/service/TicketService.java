package com.smartfacility.support.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartfacility.support.dto.TicketStatusRequest;
import com.smartfacility.support.entity.Ticket;
import com.smartfacility.support.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repo;

    public List<Ticket> all() {
        return repo.findAll();
    }

    public Ticket updateStatus(Long id, TicketStatusRequest req) {

        Ticket t = repo.findById(id).orElseThrow();

        if (!List.of("OPEN", "IN_PROGRESS", "RESOLVED")
                .contains(req.getStatus())) {
            throw new RuntimeException("Invalid Status");
        }

        if ("RESOLVED".equals(req.getStatus()) &&
            (req.getRemarks() == null ||
             req.getRemarks().isBlank())) {

            throw new RuntimeException(
              "Remarks required for resolve");
        }

        t.setStatus(req.getStatus());
        t.setRemarks(req.getRemarks());

        if ("RESOLVED".equals(req.getStatus())) {
            t.setResolvedAt(LocalDateTime.now());
        }

        return repo.save(t);
    }
}