package com.example.demo.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PasoDTO {
    private String nombrePaso;
    private int proceso;
    private MultipartFile imagen; // Se carga manualmente en el controller
}
