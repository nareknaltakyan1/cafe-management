package com.sflpro.cafe.web.rest;

import com.sflpro.cafe.dto.TableDTO;
import com.sflpro.cafe.security.UserPrincipal;
import com.sflpro.cafe.service.TableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/table")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createTable(@Valid @RequestBody TableDTO tableDTO) {

        return new ResponseEntity<>(tableService.createTable(tableDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllTables() {

        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping(value = "/{tableId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getTable(@PathVariable Long tableId) {

        return ResponseEntity.ok(tableService.getTable(tableId));
    }

    @PutMapping("/{tableId}/assign")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> assignWaiter(@PathVariable @NotNull @Positive Long tableId, @RequestBody @NotNull @Positive Long waiterId) {

        return ResponseEntity.ok(tableService.assignWaiter(tableId, waiterId));
    }

    @GetMapping(value = "/assigned")
    @PreAuthorize("hasRole('ROLE_WAITER')")
    public ResponseEntity<?> getAssignedTables() {

        Long currentUserId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        return ResponseEntity.ok(tableService.getAssignedTables(currentUserId));
    }
}
