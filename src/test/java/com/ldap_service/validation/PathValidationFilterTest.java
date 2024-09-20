package com.ldap_service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class PathValidationFilterTest {

    private PathValidationFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new PathValidationFilter();
    }

    @Test
    void testDoFilterInternal_ValidPath_ShouldContinueFilterChain() throws ServletException, IOException {
        // Configuração
        when(request.getRequestURI()).thenReturn("/api/v1/personas/valid-text/filtro");

        // Execução
        filter.doFilterInternal(request, response, filterChain);

        // Verificação
        verify(filterChain).doFilter(request, response); // Apenas uma verificação por método
    }

    @Test
    void testDoFilterInternal_InvalidPath_ShouldReturnBadRequest() throws ServletException, IOException {
        // Configuração
        when(request.getRequestURI()).thenReturn("/api/v1/personas/invalido/caminho");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Execução
        filter.doFilterInternal(request, response, filterChain);

        // Verificação
        verify(response).setStatus(HttpStatus.BAD_REQUEST.value()); // Apenas uma verificação
    }

    @Test
    void testExtractTextoFromPath_ValidPath_ShouldReturnTexto() {
        // Execução
        String texto = filter.extractTextoFromPath("/api/v1/personas/text-to-extract/filtro");

        // Verificação
        assertEquals("text-to-extract", texto); // Apenas uma verificação
    }

    @Test
    void testExtractTextoFromPath_InvalidPath_ShouldReturnNull() {
        // Execução e Verificação
        assertNull(filter.extractTextoFromPath("/api/v2/personas/text/filtro")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_ValidText_ReturnsTrue() {
        // Execução e Verificação
        assertTrue(filter.isValidTexto("valid-text")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_NullText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto(null)); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_EmptyText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto("")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_BlankText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto("   ")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_SingleQuotesText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto("''")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_DoubleQuotesText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto("\"\"")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_NullLowerCaseText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto("null")); // Apenas uma verificação
    }

    @Test
    void testIsValidTexto_NullMixedCaseText_ReturnsFalse() {
        // Execução e Verificação
        assertFalse(filter.isValidTexto(" NuLl ")); // Apenas uma verificação
    }
}