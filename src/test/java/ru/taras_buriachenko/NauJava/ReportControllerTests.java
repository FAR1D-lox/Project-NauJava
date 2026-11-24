package ru.taras_buriachenko.NauJava;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.taras_buriachenko.NauJava.service.ReportService;
import ru.taras_buriachenko.NauJava.type.ReportStatus;

import java.util.concurrent.CompletableFuture;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private String sessionCookie;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";

        UserDetails testUser = User.withUsername("testuser")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUser);

        Response loginResponse = given()
                .contentType(ContentType.URLENC)
                .formParam("username", "testuser")
                .formParam("password", "password")
                .when()
                .post("/login");

        sessionCookie = loginResponse.getCookie("JSESSIONID");
        RestAssured.basePath = "/reports";
    }

    @Test
    void createReport_Returns201AndReportId() {
        when(reportService.createReport()).thenReturn(42L);
        when(reportService.generateReport(42L)).thenReturn(CompletableFuture.completedFuture(null));

        given()
                .cookie("JSESSIONID", sessionCookie)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body("reportId", equalTo(42));

        verify(reportService, times(1)).createReport();
        verify(reportService, times(1)).generateReport(42L);
    }

    @Test
    void getReport_WhenCompleted_Returns200AndTextHtml() {
        Long reportId = 1L;
        String htmlContent = "<html><body><h1>Report #1</h1></body></html>";

        when(reportService.getReportStatus(reportId)).thenReturn(ReportStatus.COMPLETED);
        when(reportService.getReportContent(reportId)).thenReturn(htmlContent);

        given()
                .cookie("JSESSIONID", sessionCookie)
                .when()
                .get("/{reportId}", reportId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(containsString("text/html"))
                .contentType(containsString("UTF-8"))
                .body(equalTo(htmlContent));

        verify(reportService, times(1)).getReportStatus(reportId);
        verify(reportService, times(1)).getReportContent(reportId);
    }

    @Test
    void getReport_WhenCreated_Returns200AndTextPlain() {
        Long reportId = 2L;
        String plainContent = "Report is still generating...";

        when(reportService.getReportStatus(reportId)).thenReturn(ReportStatus.CREATED);
        when(reportService.getReportContent(reportId)).thenReturn(plainContent);

        given()
                .cookie("JSESSIONID", sessionCookie)
                .when()
                .get("/{reportId}", reportId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(containsString("text/plain"))
                .contentType(containsString("UTF-8"))
                .body(equalTo(plainContent));
    }

    @Test
    void getReport_WhenError_Returns200AndTextPlain() {
        Long reportId = 3L;
        String errorContent = "Error occurred during report generation";

        when(reportService.getReportStatus(reportId)).thenReturn(ReportStatus.ERROR);
        when(reportService.getReportContent(reportId)).thenReturn(errorContent);

        given()
                .cookie("JSESSIONID", sessionCookie)
                .when()
                .get("/{reportId}", reportId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(containsString("text/plain"))
                .contentType(containsString("UTF-8"))
                .body(equalTo(errorContent));
    }

    @Test
    void getReport_WhenNotFound_Returns500() {
        Long nonExistentId = 999L;
        when(reportService.getReportStatus(nonExistentId))
                .thenThrow(new EntityNotFoundException("Report not found"));

        given()
                .cookie("JSESSIONID", sessionCookie)
                .when()
                .get("/{reportId}", nonExistentId)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        verify(reportService, never()).getReportContent(any());
    }

    @Test
    void getReport_InvalidPathVariableType_Returns500() {
        given()
                .cookie("JSESSIONID", sessionCookie)
                .when()
                .get("/{reportId}", "invalid-id")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        verifyNoInteractions(reportService);
    }

    @Test
    void getReport_Unauthorized_RedirectsToLogin() {
        given()
                .redirects().follow(false)
                .when()
                .get("/{reportId}", 1L)
                .then()
                .statusCode(HttpStatus.FOUND.value())
                .header("Location", containsString("/login"));
    }
}