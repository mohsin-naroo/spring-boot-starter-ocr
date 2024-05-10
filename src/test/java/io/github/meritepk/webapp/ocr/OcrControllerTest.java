package io.github.meritepk.webapp.ocr;

import java.io.FileInputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OcrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        String text = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/ocr/img2txt")
                .file(new MockMultipartFile("image",
                        new FileInputStream("src/main/resources/static/ui/images/urd.gif")))
                .param("language", "urd")
                .param("mode", "best"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertNotNull(text);
    }
}
