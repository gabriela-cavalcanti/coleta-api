package com.github.fiap.coleta_api.cucumber.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fiap.coleta_api.dto.NotificacaoCreateDto;
import com.github.fiap.coleta_api.dto.NotificacaoUpdateDto;
import com.github.fiap.coleta_api.model.Notificacao;
import com.github.fiap.coleta_api.service.NotificacaoService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class ProcessarNotificacaoStepdefs extends StepDefs {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final MockMvc mockMvc;
    private final NotificacaoService notificacaoService;

    private String endpoint;
    private String url;
    private String requestBody;
    private NotificacaoCreateDto notificacaoCreateDto;
    private NotificacaoUpdateDto notificacaoUpdateDto;
    private Notificacao notificacaoExistente;
    private ResultActions resultActions;

    // Step 1: Set the endpoint
    @Dado("que o endpoint {string} está disponível")
    public void queOEndpointEstaDisponivel(String endpoint) {
        this.endpoint = endpoint;
    }

    // Step 2: Perform GET request and store the result
    @Quando("eu faço uma requisição GET para {string}")
    public void euFacoUmaRequisicaoGETPara(String url) throws Exception {
        this.url = url;
        resultActions = mockMvc.perform(get(url));
    }

    // Step 3: Ensure notifications exist in the system (stubbing)
    @Dado("que existem notificações no sistema")
    public void queExistemNotificacoesNoSistema() {
        List<Notificacao> listaNotificacoes = Arrays.asList(
                criarNotificacao(1L, "Alerta", "Descrição 1", "email1@example.com", LocalDate.now()),
                criarNotificacao(2L, "Aviso", "Descrição 2", "email2@example.com", LocalDate.now())
        );
        when(notificacaoService.list()).thenReturn(listaNotificacoes);
    }

    // Step 4: Assert the response contains the list of notifications
    @Então("o sistema retorna uma lista de notificações com status {int}")
    public void oSistemaRetornaUmaListaDeNotificacoesComStatus(int status) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    // Step 5: Ensure a notification exists with a specific ID (stubbing)
    @Dado("que existe uma notificação com ID {int}")
    public void queExisteUmaNotificacaoComID(int id) {
        notificacaoExistente = criarNotificacao((long) id, "Alerta", "Descrição existente", "email@example.com", LocalDate.now());
        when(notificacaoService.findById(id)).thenReturn(Optional.of(notificacaoExistente));
        when(notificacaoService.existsById(id)).thenReturn(true);
    }

    // Step 6: Assert the response contains the notification details
    @Então("o sistema retorna a notificação com status {int}")
    public void oSistemaRetornaANotificacaoComStatus(int status) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notificacaoExistente.getId()))
                .andExpect(jsonPath("$.tipo").value(notificacaoExistente.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacaoExistente.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacaoExistente.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacaoExistente.getDataEnvio().toString()));
    }

    // Step 7: Ensure a notification does not exist with a specific ID (stubbing)
    @Dado("que não existe uma notificação com ID {int}")
    public void queNaoExisteUmaNotificacaoComID(int id) {
        when(notificacaoService.findById(id)).thenReturn(Optional.empty());
        when(notificacaoService.existsById(id)).thenReturn(false);
    }

    // Step 8: Assert the response contains the expected status and message
    @Então("o sistema retorna status {int} com mensagem {string}")
    public void oSistemaRetornaStatusComMensagem(int status, String mensagem) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(status().reason(Matchers.containsString(mensagem)));
    }

    // Step 9: Prepare valid data for creating a new notification
    @Dado("que eu tenho os dados válidos para uma nova notificação")
    public void queEuTenhoOsDadosValidosParaUmaNovaNotificacao(DataTable dataTable) throws Exception {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        notificacaoCreateDto = new NotificacaoCreateDto();
        notificacaoCreateDto.setTipo(data.get("tipo"));
        notificacaoCreateDto.setDescricao(data.get("descricao"));
        notificacaoCreateDto.setEmailDestinatario(data.get("emailDestinatario"));
        notificacaoCreateDto.setDataEnvio(LocalDate.parse(data.get("dataEnvio")));

        requestBody = objectMapper.writeValueAsString(notificacaoCreateDto);
    }

    // Step 10: Perform POST request and store the result
    @Quando("eu faço uma requisição POST para {string} com os dados acima")
    public void euFacoUmaRequisicaoPOSTParaComOsDadosAcima(String url) throws Exception {
        this.url = url;
        Notificacao notificacaoSalva = criarNotificacao(1L, notificacaoCreateDto.getTipo(), notificacaoCreateDto.getDescricao(),
                notificacaoCreateDto.getEmailDestinatario(), notificacaoCreateDto.getDataEnvio());
        when(notificacaoService.saveOrUpdate(any(Notificacao.class))).thenReturn(notificacaoSalva);

        resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    // Step 11: Assert the response contains the created notification details
    @Então("o sistema cria a notificação e retorna status {int} com a localização da nova notificação")
    public void oSistemaCriaANotificacaoERetornaStatusComALocalizacaoDaNovaNotificacao(int status) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(header().string("Location", Matchers.containsString("/notificacoes/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value(notificacaoCreateDto.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacaoCreateDto.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacaoCreateDto.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacaoCreateDto.getDataEnvio().toString()));
    }

    // Step 12: Prepare updated data for an existing notification
    @E("eu tenho os dados atualizados para a notificação")
    public void euTenhoOsDadosAtualizadosParaANotificacao(DataTable dataTable) throws Exception {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        notificacaoUpdateDto = new NotificacaoUpdateDto();
        notificacaoUpdateDto.setId(notificacaoExistente.getId());
        notificacaoUpdateDto.setTipo(data.get("tipo"));
        notificacaoUpdateDto.setDescricao(data.get("descricao"));
        notificacaoUpdateDto.setEmailDestinatario(data.get("emailDestinatario"));
        notificacaoUpdateDto.setDataEnvio(LocalDate.parse(data.get("dataEnvio")));

        requestBody = objectMapper.writeValueAsString(notificacaoUpdateDto);
    }

    // Step 13: Perform PUT request and store the result
    @Quando("eu faço uma requisição PUT para {string} com os dados acima")
    public void euFacoUmaRequisicaoPUTParaComOsDadosAcima(String url) throws Exception {
        this.url = url;
        Notificacao notificacaoAtualizada = criarNotificacao(notificacaoExistente.getId(), notificacaoUpdateDto.getTipo(),
                notificacaoUpdateDto.getDescricao(), notificacaoUpdateDto.getEmailDestinatario(), notificacaoUpdateDto.getDataEnvio());
        when(notificacaoService.saveOrUpdate(any(Notificacao.class))).thenReturn(notificacaoAtualizada);
        when(notificacaoService.existsById(notificacaoExistente.getId())).thenReturn(true);

        resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    // Step 14: Assert the response contains the updated notification details
    @Então("o sistema atualiza a notificação e retorna status {int}")
    public void oSistemaAtualizaANotificacaoERetornaStatus(int status) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(jsonPath("$.id").value(notificacaoUpdateDto.getId()))
                .andExpect(jsonPath("$.tipo").value(notificacaoUpdateDto.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacaoUpdateDto.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacaoUpdateDto.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacaoUpdateDto.getDataEnvio().toString()));
    }

    // Step 15: Perform PUT request with arbitrary data and store the result
    @Quando("eu faço uma requisição PUT para {string} com dados quaisquer")
    public void euFacoUmaRequisicaoPUTParaComDadosQuaisquer(String url) throws Exception {
        this.url = url;
        when(notificacaoService.existsById(anyLong())).thenReturn(false);

        resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    // Step 16: Perform DELETE request and store the result
    @Quando("eu faço uma requisição DELETE para {string}")
    public void euFacoUmaRequisicaoDELETEPara(String url) throws Exception {
        this.url = url;

        String[] parts = url.split("/");
        Long id = Long.parseLong(parts[parts.length - 1]);

        when(notificacaoService.existsById(id)).thenReturn(true);
        doNothing().when(notificacaoService).delete(id);

        resultActions = mockMvc.perform(delete(url));
    }

    @Quando("eu faço uma requisição DELETE para {string} com dados quaisquer")
    public void euFacoUmaRequisicaoDELETEComDadosQuaisquer(String url) throws Exception {
        this.url = url;
        when(notificacaoService.existsById(anyLong())).thenReturn(false);

        resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    // Step 17: Assert the DELETE operation was successful
    @Então("o sistema deleta a notificação e retorna status {int}")
    public void oSistemaDeletaANotificacaoERetornaStatus(int status) throws Exception {
        resultActions
                .andExpect(status().is(status));
    }

    // Step 18: Perform GET request for a non-existent ID and store the result
    @Quando("eu faço uma requisição GET para {string} com ID inexistente")
    public void euFacoUmaRequisicaoGETParaComIDInexistente(String url) throws Exception {
        this.url = url;
        when(notificacaoService.findById(anyLong())).thenReturn(Optional.empty());

        resultActions = mockMvc.perform(get(url));
    }

    // Step 19: Assert the response contains the expected status and error message
    @Então("o sistema retorna status {int} com mensagem de erro {string}")
    public void oSistemaRetornaStatusComMensagemDeErro(int status, String mensagem) throws Exception {
        resultActions
                .andExpect(status().is(status))
                .andExpect(status().reason(Matchers.containsString(mensagem)));
    }

    // Auxiliary method to create a Notificacao instance
    private Notificacao criarNotificacao(Long id, String tipo, String descricao, String emailDestinatario, LocalDate dataEnvio) {
        Notificacao notificacao = new Notificacao();
        notificacao.setId(id);
        notificacao.setTipo(tipo);
        notificacao.setDescricao(descricao);
        notificacao.setEmailDestinatario(emailDestinatario);
        notificacao.setDataEnvio(dataEnvio);
        return notificacao;
    }
}
