package com.marcelo.bibliotech;
import com.marcelo.bibliotech.controller.BibliotecaController;
import com.marcelo.bibliotech.dao.UsuarioDAO;
import com.marcelo.bibliotech.model.Cliente;
import com.marcelo.bibliotech.model.Emprestimo;
import com.marcelo.bibliotech.model.Livro;
import com.marcelo.bibliotech.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioTeste {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private BibliotecaController controller;

    private Livro livro;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Cliente("Rafael", "MAT-001");
        usuario.setId(1);
    }

    @Test
    void testarCadastroUsuario() {
        controller.cadastrarUsuario(usuario);
        verify(usuarioDAO, times(1)).save(usuario);
    }

    @Test
    void testarListagemUsuarios() {
        when(usuarioDAO.findAll()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = controller.listaUsuarios();

        assertFalse(usuarios.isEmpty());
        assertEquals(1, usuarios.size());
    }

    @Test
    void testarListagemUsuariosVazia() {
        when(usuarioDAO.findAll()).thenReturn(List.of());

        List<Usuario> usuarios = controller.listaUsuarios();

        assertTrue(usuarios.isEmpty());
    }

    @Test
    void testarAtualizacaoUsuario() {
        usuario.setNome("Rafael Atualizado");
        usuario.setMatricula("MAT-999");

        controller.atualizarUsuario(usuario);

        verify(usuarioDAO, times(1)).update(usuario);
    }

    @Test
    void testarExclusaoUsuario() {
        controller.removerUsuario(1);
        verify(usuarioDAO, times(1)).delete(1);
    }

    @Test
    void testarBuscaUsuarioPorId() {
        when(usuarioDAO.findById(1)).thenReturn(usuario);

        Usuario encontrado = controller.buscarUsuario(1);

        assertNotNull(encontrado);
        assertEquals(1, encontrado.getId());
        assertEquals("Rafael", encontrado.getNome());
    }

    @Test
    void testarBuscaUsuarioInexistente() {
        when(usuarioDAO.findById(99)).thenReturn(null);

        Usuario encontrado = controller.buscarUsuario(99);

        assertNull(encontrado);
    }

    @Test
    void historicoDeveConterEmprestimo_quandoAdicionado() {
        Emprestimo emprestimo = new Emprestimo(livro, usuario);
        usuario.adiconarHistorico(emprestimo);

        assertDoesNotThrow(() -> usuario.exibirHistorico());
    }

    @Test
    void historicoVazio_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> usuario.exibirHistorico());
    }
}