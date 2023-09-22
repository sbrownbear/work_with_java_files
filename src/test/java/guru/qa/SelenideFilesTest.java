package guru.qa;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {

    @Test
    @DisplayName("Скачать файл")
    void selenideDownloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $(byAttribute("href","/junit-team/junit5/raw/main/README.md")).download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            byte[] bytes = is.readAllBytes();
            String textContent = new String(bytes, StandardCharsets.UTF_8);
            assertThat(textContent).contains("This repository is the home of _JUnit 5_.");
        }
    }

    @Test
    @DisplayName("Загрузить файл")
    void selenideUploadTest() {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("Jacque_Fresco.jpg");
        $("div.qq-file-info").shouldHave(Condition.text("Jacque_Fresco.jpg"));
    }
}
