package com.ldap_service.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Filtro responsável por validar o parâmetro 'texto' presente na URL.
 */
@Component
public class PathValidationFilter extends OncePerRequestFilter {

    private static final String INVALID_TEXTO_MESSAGE = "O parâmetro '%s' não pode ser vazio, inválido ou 'null'.";
    private static final String PATH_REGEX = "^/api/v1/personas/([^/]+)/filtro$"; // Regex para validação da URL

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String texto = extractTextoFromPath(request.getRequestURI());

        if (!isValidTexto(texto)) {
            sendErrorResponse(response, String.format(INVALID_TEXTO_MESSAGE, "texto"));
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o valor do parâmetro 'texto' da URL, validando a estrutura.
     *
     * @param requestURI A URI da requisição.
     * @return O valor do parâmetro 'texto' ou null se a URL for inválida.
     */
    public String extractTextoFromPath(String requestURI) {
        String decodedPath = URLDecoder.decode(requestURI, StandardCharsets.UTF_8);
        if (decodedPath.matches(PATH_REGEX)) {
            String[] parts = decodedPath.split("/");
            return parts[4];
        }
        return null; 
    }

    /**
     * Verifica se o texto é válido.
     *
     * @param texto O texto a ser validado.
     * @return true se o texto for válido, false caso contrário.
     */
    public boolean isValidTexto(String texto) {
        return texto != null && !texto.trim().isEmpty()
                && !texto.trim().equalsIgnoreCase("null") // Correção: Comparação ignorando maiúsculas/minúsculas
                && !texto.equals("''") && !texto.equals("\"\"");
    }

    /**
     * Envia uma resposta de erro em formato JSON.
     *
     * @param response A resposta HTTP.
     * @param message  A mensagem de erro.
     * @throws IOException Se houver um erro ao escrever a resposta.
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        List<Map<String, Object>> errors = List.of(
                Map.of("codigo", HttpStatus.BAD_REQUEST.value(), "mensagem", message)
        );

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("errors", errors)));
    }
}