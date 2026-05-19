package com.marcelo.bibliotech;

import com.marcelo.bibliotech.controller.BibliotecaController;
import com.marcelo.bibliotech.controller.LivroIndisponivelException;
import com.marcelo.bibliotech.controller.LivroNaoEncontradoException;
import com.marcelo.bibliotech.controller.MultaPendenteException;
import com.marcelo.bibliotech.dao.EmprestimoDAO;
import com.marcelo.bibliotech.dao.LivroDAO;
import com.marcelo.bibliotech.dao.UsuarioDAO;
import com.marcelo.bibliotech.model.Livro;
import com.marcelo.bibliotech.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TratamentoErrosTeste {

    @Mock
    private LivroDAO livroDAO;

    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private EmprestimoDAO emprestimoDAO;

    @InjectMocks
    private BibliotecaController controller;

    private Livro livro;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        livro = new Livro(1, "Percy Jackson", "Rick Riordan");
        usuario = new Usuario("Rafael", "MAT-001");
        usuario.setId(1);
    }

    @Test
    void testarEmprestimoComLivroNulo() {
        assertThrows(LivroNaoEncontradoException.class, () ->
            controller.realizarEmprestimo(null, usuario)
        );
    }

    @Test
    void testarCadastroUsuarioComNomeVazio() {
        Usuario usuarioSemNome = new Usuario("", "MAT-002");
        // O save deve ser chamado mesmo assim — validação de campo vazio
        // é responsabilidade que ainda não existe no controller.
        // Este teste documenta o comportamento atual.
        assertDoesNotThrow(() -> controller.cadastrarUsuario(usuarioSemNome));
    }

    @Test
    void testarCadastroLivroComTituloVazio() {
        Livro livroSemTitulo = new Livro(0, "", "Autor");
        assertDoesNotThrow(() -> controller.cadastrarLivro(livroSemTitulo));
    }

    @Test
    void testarEmprestimoComUsuarioComMulta() {
        usuario.setMulta(50.0);

        MultaPendenteException ex = assertThrows(MultaPendenteException.class, () ->
            controller.realizarEmprestimo(livro, usuario)
        );

        assertTrue(ex.getMessage().contains("Rafael"));
        assertTrue(ex.getMessage().contains("50.0"));
    }

    @Test
    void testarEmprestimoComLivroIndisponivel() {
        livro.setStatus(false);

        assertThrows(LivroIndisponivelException.class, () ->
            controller.realizarEmprestimo(livro, usuario)
        );
    }

    @Test
    void testarBuscaLivroIdInexistente() {
        Livro encontrado = controller.buscaLivro(999);
        assertNull(encontrado);
    }

    @Test
    void testarBuscaUsuarioIdInexistente() {
        Usuario encontrado = controller.buscarUsuario(999);
        assertNull(encontrado);
    }

    @Test
    void testarDevolucaoLivroSemEmprestimoAtivo() {
        // findByLivroId retorna null por padrão no mock
        var resultado = controller.realizarDevolucao(livro);
        assertNull(resultado);
    }

    @Test
    void testarMensagemLivroNaoEncontrado() {
        LivroNaoEncontradoException ex = assertThrows(LivroNaoEncontradoException.class, () ->
            controller.realizarEmprestimo(null, usuario)
        );

        assertEquals("O livro nao foi encontrado no sistema.", ex.getMessage());
    }

    @Test
    void testarMensagemLivroIndisponivel() {
        livro.setStatus(false);

        LivroIndisponivelException ex = assertThrows(LivroIndisponivelException.class, () ->
            controller.realizarEmprestimo(livro, usuario)
        );

        assertEquals("O livro esta indisponivel no momento!", ex.getMessage());
    }

    @Test
    void testarMensagemMultaPendente() {
        usuario.setMulta(75.5);

        MultaPendenteException ex = assertThrows(MultaPendenteException.class, () ->
            controller.realizarEmprestimo(livro, usuario)
        );

        assertTrue(ex.getMessage().contains("Rafael"));
        assertTrue(ex.getMessage().contains("75.5"));
    }
}