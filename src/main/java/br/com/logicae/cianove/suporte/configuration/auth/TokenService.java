package br.com.logicae.cianove.suporte.configuration.auth;


import br.com.logicae.cianove.suporte.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {
	
	@Value("${security.jwt.expiration}")
	private String expiration;

	@Value("${security.jwt.refresh.expiration}")
	private String refreshExpritation;
	
	@Value("${security.jwt.secret}")
	private String secret;

	public TokenDto gerarToken(Authentication authentication) throws JsonProcessingException {
		User logado = (User) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		Date dataRefresh = new Date(hoje.getTime() + Long.parseLong(refreshExpritation));
		ObjectMapper objectMapper = new ObjectMapper();


		 String token = Jwts.builder()
				.setIssuer("API do Fórum da Alura")
				.setSubject(objectMapper.writeValueAsString(userToJwtDataDto(logado)))
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();

		String refreshToken = Jwts.builder()
				.setIssuer("API do Fórum da Alura")
				.setSubject(objectMapper.writeValueAsString(userToJwtDataDto(logado)))
				.setIssuedAt(hoje)
				.setNotBefore(dataExpiracao)
				.setExpiration(dataRefresh)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();

		return new TokenDto(token, refreshToken,"Bearer", dataExpiracao.getTime());
	}

	public JwtDataDto userToJwtDataDto(User user) {
		JwtDataDto jwtDataDto = new JwtDataDto();
		jwtDataDto.setCdUser(user.getCdUser());
		jwtDataDto.setDsName(user.getDsName());
		jwtDataDto.setDsUser(user.getDsUser());
		jwtDataDto.setDtGenerated(LocalDateTime.now());
		List<String> roles = new ArrayList<>();
		if (user.getClients() != null) {
			user.getClients().forEach( c -> {
				roles.add(c.getAuthority());
			});
		}
		jwtDataDto.setRoles(roles);
		return jwtDataDto;
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public JwtDataDto getUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.convertValue(claims.getSubject(),JwtDataDto.class);
	}

}
