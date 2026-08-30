package com.ramaldes.newpay;

import com.ramaldes.newpay.dto.ClienteRequestDTO;
import com.ramaldes.newpay.dto.ClienteResponseDTO;
import com.ramaldes.newpay.exception.ClienteNaoEncontradoException;
import com.ramaldes.newpay.exception.CpfJaCadastradoException;
import com.ramaldes.newpay.exception.EmailJaCadastradoException;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.repository.ClienteRepository;
import com.ramaldes.newpay.repository.ContaRepository;
import com.ramaldes.newpay.repository.MovimentacaoRepository;
import com.ramaldes.newpay.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    ContaRepository contaRepository;

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    ClienteService clienteService;

    //==============
    //CADASTRO
    //==============
    @Test
    void deveCadastrarClienteComSucesso() {

        ClienteRequestDTO dto = new ClienteRequestDTO(
                "Teste",
                "123456789",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        Cliente clienteSalvo = new Cliente("Teste", "123456789", "email@email.com", LocalDate.of(2000,1,1));

        when(clienteRepository.existsByCpf(dto.getCpf()))
                .thenReturn(false);
        when(clienteRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteSalvo);

        ClienteResponseDTO resultado = clienteService.cadastrarCliente(dto);

        assertEquals("Teste", resultado.getNome());
        assertEquals("123456789", resultado.getCpf());
        assertEquals("email@email.com", resultado.getEmail());
        assertEquals(LocalDate.of(2000,1,1), resultado.getDataNascimento());
    }

    @Test
    void deveLancarExceptionQuandoCpfJaExistir() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "Teste",
                "123456789",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        when(clienteRepository.existsByCpf(dto.getCpf()))
                .thenReturn(true);

        assertThrows(CpfJaCadastradoException.class,
                () -> clienteService.cadastrarCliente(dto)
                );
    }

    @Test
    void deveLancarExceptionQuandoEmailJaExistir() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "Teste",
                "123456789",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );
        when(clienteRepository.existsByCpf(dto.getCpf()))
                .thenReturn(false);
        when(clienteRepository.existsByEmail(dto.getEmail()))
                .thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> clienteService.cadastrarCliente(dto)
        );
    }

    //==============
    //LISTAGEM
    //==============
    @Test
    void deveBuscarClientePorIdComSucesso() {
        Cliente cliente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        ClienteResponseDTO resultado = clienteService.buscarPorId(1L);

        assertEquals("Teste", resultado.getNome());
        assertEquals("1234567890", resultado.getCpf());
        assertEquals("email@email.com", resultado.getEmail());
        assertEquals(LocalDate.of(2000,1,1), resultado.getDataNascimento());
    }

    @Test
    void deveLancarExceptionQuandoClienteNaoExistirPorId() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorId(1L)
                );
    }

    @Test
    void deveListarClientesComSucesso() {
        Cliente cliente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );
        Cliente cliente2 = new Cliente(
                "Teste2",
                "12345678902",
                "email2@email.com",
                LocalDate.of(2002,2,2)
        );

        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente, cliente2));

        List<ClienteResponseDTO> resultado = clienteService.listarClientes();

        assertEquals(2, resultado.size());

        assertEquals("Teste", resultado.get(0).getNome());
        assertEquals("Teste2", resultado.get(1).getNome());
    }

    //==============
    //ATUALIZAÇÃO
    //==============
    @Test
    void deveAtualizarClienteComSucesso() {
        Cliente clienteExistente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        ClienteRequestDTO clienteAtualizado = new ClienteRequestDTO(
                "name",
                "12345678990",
                "emailtwo@email.com",
                LocalDate.of(2001,2,10)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.existsByCpfAndIdNot(clienteAtualizado.getCpf(), 1L))
                .thenReturn(false);
        when(clienteRepository.existsByEmailAndIdNot(clienteAtualizado.getEmail(), 1L))
                .thenReturn(false);
        when(clienteRepository.save(clienteExistente))
                .thenReturn(clienteExistente);

        ClienteResponseDTO resultado = clienteService.atualizarCliente(1L, clienteAtualizado);

        assertEquals("name", resultado.getNome());
        assertEquals("12345678990", resultado.getCpf());
        assertEquals("emailtwo@email.com", resultado.getEmail());
        assertEquals(LocalDate.of(2001, 2, 10), resultado.getDataNascimento());
    }

    @Test
    void deveLancarExceptionQuandoAtualizarClienteInexistente() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "name",
                "12345678990",
                "emailtwo@email.com",
                LocalDate.of(2001,2,10)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.atualizarCliente(1L, dto)
                );
    }

    @Test
    void deveLancarExceptionQuandoAtualizarClienteCpfJaUsado() {
        Cliente cliente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        ClienteRequestDTO dto = new ClienteRequestDTO(
                "name",
                "12345678990",
                "emailtwo@email.com",
                LocalDate.of(2001,2,10)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByCpfAndIdNot(dto.getCpf(), 1L))
                .thenReturn(true);

        assertThrows(CpfJaCadastradoException.class,
                () -> clienteService.atualizarCliente(1L, dto));
    }

    @Test
    void deveLancarExceptionQuandoAtualizarClienteEmailJaUsado() {
        Cliente cliente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        ClienteRequestDTO dto = new ClienteRequestDTO(
                "name",
                "12345678990",
                "emailtwo@email.com",
                LocalDate.of(2001,2,10)
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByCpfAndIdNot(dto.getCpf(), 1L))
                .thenReturn(false);
        when(clienteRepository.existsByEmailAndIdNot(dto.getEmail(), 1L))
                .thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> clienteService.atualizarCliente(1L, dto));

    }

    //==============
    //DELETAR
    //==============

    @Test
    void deveDeletarClienteComSucesso() {
        Cliente cliente = new Cliente(
                "Teste",
                "1234567890",
                "email@email.com",
                LocalDate.of(2000,1,1)
        );

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));
       clienteService.deletarCliente(1L);

       verify(clienteRepository, times(1))
               .delete(cliente);

    }

    @Test
    void deveLancarExceptionQuandoClienteNaoEncontradoDelete() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.deletarCliente(1L)
                );
    }
}
