package br.com.db.system.votingsystem.v1.controller;

import br.com.db.system.votingsystem.v1.dto.AgendaDTO;
import br.com.db.system.votingsystem.v1.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    private ObjectMapper objectMapper;
    private AgendaDTO agendaDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        agendaDTO = new AgendaDTO();
        agendaDTO.setId(1L);
        agendaDTO.setDescription("Test Agenda");
        agendaDTO.setAssemblyId(1L);
        agendaDTO.setStart(LocalDateTime.now().plusMinutes(5));
        agendaDTO.setEnd(LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void shouldFindAllAgendas() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));
        Page<AgendaDTO> page = new PageImpl<>(Collections.singletonList(agendaDTO));
        Mockito.when(agendaService.findAll(eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/agenda/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(agendaDTO.getId()));
    }

    @Test
    void shouldFindAgendaById() throws Exception {
        Mockito.when(agendaService.findById(1L)).thenReturn(agendaDTO);

        mockMvc.perform(get("/api/agenda/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaDTO.getId()));
    }

    @Test
    void shouldCreateAgenda() throws Exception {
        Mockito.when(agendaService.create(any(AgendaDTO.class))).thenReturn(agendaDTO);

        mockMvc.perform(post("/api/agenda/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(agendaDTO.getId()));
    }

    @Test
    void shouldUpdateAgenda() throws Exception {
        Mockito.when(agendaService.update(any(AgendaDTO.class))).thenReturn(agendaDTO);

        mockMvc.perform(put("/api/agenda/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaDTO.getId()));
    }

    @Test
    void shouldDeleteAgenda() throws Exception {
        Mockito.doNothing().when(agendaService).deleteById(1L);

        mockMvc.perform(delete("/api/agenda/v1/1"))
                .andExpect(status().isNoContent());
    }
}