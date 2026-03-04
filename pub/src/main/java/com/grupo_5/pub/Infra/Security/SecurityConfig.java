@Configuration
@EnableMethodSecurity

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                //Esses endpoints qualquer um pode acessar
                .requestMatchers("/login").permitAll();
                .requestMatchers("/cadastro").permitAll();

                //Esses endpoints estão exigindo que tenha uma autorização pra acessar
                //Os ** na frente vão exigir que tudo que começa com o endpoint precisa estar autenticado
                .requestMatchers("/ingredientes/**").authenticated();
                .requestMatchers("/clientes/**").authenticated();
                .requestMatchers("/comandas/**").authenticated();
                .requestMatchers("/eventos/**").authenticated();
                .requestMatchers("/bebidas/**").authenticated();
                .requestMatchers("/mesas/**").authenticated();
                .requestMatchers("/api/promocoes/**").authenticated();

                .anyRequest().authenticated();
            )

        return http.build();
    }
}