package com.nairbdev.academiasbackend.dto.programaPruebaFisica;

import java.util.List;

public record ProgramaPruebasFisicasBulkSaveRequest(
        List<ProgramaPruebaFisicaItemDTO> items
) {}