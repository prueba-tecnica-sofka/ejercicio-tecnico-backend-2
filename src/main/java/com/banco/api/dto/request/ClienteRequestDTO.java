package com.banco.api.dto.request;

import com.banco.api.entity.Genero;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data  
public class ClienteRequestDTO {  
    @NotBlank(message = "El nombre es obligatorio")  
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")  
    private String nombre;  
      
    @NotNull(message = "El género es obligatorio")  
    private Genero genero;  
      
    @NotNull(message = "La edad es obligatoria")  
    @Min(value = 18, message = "La edad mínima es 18 años")  
    private Integer edad;  
      
    @NotBlank(message = "La identificación es obligatoria")  
    @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres")  
    private String identificacion;  
      
    private String direccion;  
    private String telefono;  
      
    @NotBlank(message = "La contraseña es obligatoria")  
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")  
    private String contraseña;  
      
    private Boolean estado = true;  
}