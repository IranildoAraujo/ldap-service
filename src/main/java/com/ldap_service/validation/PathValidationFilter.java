package com.ldap_service.validation;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//TODO: 2°
@Component
public class PathValidationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();

		// Decodificar a URL para pegar os valores corretos
		String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);

		// Extrair a parte do path que contém o "texto"
		String[] pathParts = decodedPath.split("/");
		if (pathParts.length >= 5) { // considerando a estrutura /api/v1/personas/{texto}/filtro
			String texto = pathParts[4]; // O valor do PathVariable "texto"
			
			 if (texto == null || texto.trim().isEmpty() || texto.equals("''") || texto.equals("\"\"")
	                    || texto.equalsIgnoreCase("null")) {
	                // Criar a estrutura de erro personalizada
	                List<Map<String, Object>> errors = new ArrayList<>();
	                Map<String, Object> error = new HashMap<>();
	                error.put("codigo", HttpStatus.BAD_REQUEST.value());
	                error.put("mensagem", "O parâmetro 'texto' não pode ser vazio, inválido ou 'null'.");
	                errors.add(error);

	                // Configurar a resposta
	                response.setStatus(HttpStatus.BAD_REQUEST.value());
	                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("errors", errors)));
	                return;
	            }
		}

		filterChain.doFilter(request, response); // Continua a execução se estiver correto
	}
}
