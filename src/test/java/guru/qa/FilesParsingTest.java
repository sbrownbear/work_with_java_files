package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    @DisplayName("Скачать PDF-файл и проверить содержимое")
    void pdfParseTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedPdf = $("a[href='junit-user-guide-5.10.0.pdf']").download();
        PDF content = new PDF(downloadedPdf);
        assertThat(content.author).contains("Sam Brannen");
    }

    @Test
    @DisplayName("XLS-файл - exel-документ")
    void xlsParseTest() throws Exception {
        try (InputStream resourceAsStream = cl.getResourceAsStream("sample-xlsx-file.xlsx")) {
            XLS content = new XLS(resourceAsStream);
            assertThat(content.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue()).contains("Dulce");
        }
    }

    @Test
    @DisplayName("CSV-файл - Comma-separated values (значения, разделённые запятыми)")
    void csvParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("qa_guru.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(resource))
        ) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)[1]).contains("salary");
        }
    }

    @Test
    @DisplayName("ZIP-файл. Зная имя, можем получить его содержимое.")
    void zipParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("sample.txt.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("sample.txt");
            }
        }
    }

    @Test
    @DisplayName("JSON-файл, проверяем ключ-значение")
    void jsonParseTest() throws Exception{
        Gson gson = new Gson();
        try (
                InputStream resource = cl.getResourceAsStream("glossary.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            assertThat(jsonObject.get("title").getAsString()).isEqualTo("example glossary");
            assertThat(jsonObject.get("GlossDiv").getAsJsonObject().get("title").getAsString()).isEqualTo("S");
            assertThat(jsonObject.get("GlossDiv").getAsJsonObject().get("flag").getAsBoolean()).isTrue();
        }
    }

    @Test
    @DisplayName("JSON-файл, рассшипенный тест (pojo)")
    void jsonParseWithModelTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream resource = cl.getResourceAsStream("glossary.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            Glossary jsonObject = gson.fromJson(reader, Glossary.class);
            assertThat(jsonObject.title).isEqualTo("example glossary");
            assertThat(jsonObject.glossDiv.title).isEqualTo("S");
            assertThat(jsonObject.glossDiv.flag).isTrue();
        }
    }
}
