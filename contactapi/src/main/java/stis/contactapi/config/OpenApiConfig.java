package stis.contactapi.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "STIS Contact API", version = "1.0.0", description = "API ini menyediakan layanan untuk mengelola dan mengakses informasi kontak yang dimiliki oleh mahasiswa dan dosen di Politeknik Statistika STIS. "
                + "Layanan ini dirancang untuk mempermudah pencarian informasi kontak dengan menyediakan berbagai endpoint untuk menambah, memperbarui, "
                + "dan menghapus kontak dari mahasiswa dan dosen. API ini juga mendukung pengelompokan kontak berdasarkan organisasi mahasiswa "
                + "atau mata kuliah yang diampu oleh dosen serta menawarkan fitur pencarian yang fleksibel. \n\n" +
                "Fitur utama meliputi: \n" +
                "- Manajemen kontak mahasiswa dan dosen \n" +
                "- Pengelompokan kontak berdasarkan organisasi atau jurusan \n" +
                "- Dukungan endpoint untuk CRUD (Create, Read, Update, Delete) pada data kontak \n" +
                "- Fitur pencarian kontak berdasarkan organisasi atau mata kuliah yang diampu oleh dosen\n\n", contact = @Contact(name = "Dodi Firmansyah", email = "222212572@stis.ac.id", url = "https://github.com/dodfrm")), servers = {
                                @Server(description = "Local Development Server", url = "http://localhost:8080")
                }, security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Input token sebagai 'Bearer <token>'")
@Configuration
public class OpenApiConfig {

        @Bean
        public GroupedOpenApi customApi() {
                return GroupedOpenApi.builder()
                                .group("stis-contact-api")
                                .pathsToMatch("/api/**", "/login", "/register", "/user/**", "/user-profile/**")
                                .build();
        }
}
