package org.panero.gateway.web;

import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.panero.gateway.config.MessageFlowConfiguration;
import org.panero.gateway.config.WebConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebConfiguration.class)
public class MeasurementsControllerTest {
    private MockMvc mockMvc;

    private MediaType contentType;

    @Mock
    private MessageFlowConfiguration.MeasurementsGateway mockGateway;

    @InjectMocks
    private MeasurementsController controller;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).build();
        contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf8"));
    }

    @Test
    public void test_post_metrics() throws Exception {
        final ClassPathResource resource = new ClassPathResource("test.json");
        final String payload = FileUtils.readFileToString(resource.getFile(), CharEncoding.UTF_8);
        mockMvc
                .perform(
                        post("/write")
                                .contentType(contentType)
                                .content(payload)
                )
                .andExpect(status().isCreated());
        verify(mockGateway, times(1)).send(any());
    }
}
