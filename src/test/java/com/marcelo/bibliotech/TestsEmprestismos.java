package com.marcelo.bibliotech;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.marcelo.bibliotech.controller.BibliotecaController;
import com.marcelo.bibliotech.controller.LivroIndisponivelException;
import com.marcelo.bibliotech.controller.LivroNaoEncontradoException;
import com.marcelo.bibliotech.controller.MultaPendenteException;
import com.marcelo.bibliotech.dao.EmprestimoDAO;
import com.marcelo.bibliotech.dao.LivroDAO;
import com.marcelo.bibliotech.dao.UsuarioDAO;
import com.marcelo.bibliotech.model.Emprestimo;
import com.marcelo.bibliotech.model.Livro;
import com.marcelo.bibliotech.model.Usuario;

@ExtendWith(MockitoExtension.class)
public class TestsEmprestismos {
    @Mock private LivroDAO livroDAO;
    @Mock private UsuarioDAO usuarioDAO;
    @Mock private EmprestimoDAO emprestimoDAO;

    @InjectMocks private BibliotecaController controller;

    private Livro livro;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        livro = new Livro(1, "Clean Code", "Robert Martin");
        usuario = new Usuario("Ana Luísa", "20251234");
    }

    @Test
    void realiazarEmprestimo_deveRetornarEmprestimo_quandoDadosValidos()
            throws LivroNaoEncontradoException, LivroIndisponivelException, MultaPendenteException {
        Emprestimo resultado = controller.realizarEmprestimo(livro, usuario);

        assertNotNull(resultado);
        assertFalse(livro.isDisponivel());
        verify(emprestimoDAO).save(any(Emprestimo.class));
        verify(livroDAO).update(livro);
    }

    @Test
    void realizarEmprestimo_deveLancarExcessaoQuandoLivroIndisponivel(){
        livro.setStatus(false);
        assertThrows(LivroIndisponivelException.class, () -> controller.realizarEmprestimo(livro, usuario));
    }
    
    @Test
    void realizarDevolucao_deveRetornarEmprestimo_quandoNoPrazo(){
        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(livro, usuario, hoje.minusDays(3), hoje.plusDays(4));

        when(emprestimoDAO.findByLivroId(livro.getId())).thenReturn(emprestimo);

        Emprestimo resultado = controller.realizarDevolucao(livro);

        assertNotNull(resultado);
        assertEquals(0.0, usuario.getMulta());
        assertTrue(livro.isDisponivel());
        verify(usuarioDAO, never()).update(usuario);
        verify(emprestimoDAO).delete(emprestimo.getId());
    }

    @Test
    void realizarDevolucao_deveAplicarMulta_quandoAtrasado() {
        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(livro, usuario, hoje.minusDays(10), hoje.minusDays(3));

        when(emprestimoDAO.findByLivroId(livro.getId())).thenReturn(emprestimo);

        Emprestimo resultado = controller.realizarDevolucao(livro);

        assertNotNull(resultado);
        assertEquals(6.0, usuario.getMulta());
        verify(usuarioDAO).update(usuario);
        verify(emprestimoDAO).delete(emprestimo.getId());
    }

    @Test
    void realizarDevolucao_deveRetornarNull_quandoSemEmprestimoAtivo() {
        
        when(emprestimoDAO.findByLivroId(livro.getId())).thenReturn(null);

        Emprestimo resultado = controller.realizarDevolucao(livro);

        assertNull(resultado);
        verifyNoInteractions(usuarioDAO);
        verify(livroDAO, never()).update(any());
    }

    @Test
    void listarEmprestimos_deveRetornarLista_quandoExistemEmprestimos() {

        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo1 = new Emprestimo(livro, usuario, hoje.minusDays(2), hoje.plusDays(5));
        Emprestimo emprestimo2 = new Emprestimo(livro, usuario, hoje.minusDays(1), hoje.plusDays(6));

        when(emprestimoDAO.findAll()).thenReturn(List.of(emprestimo1, emprestimo2));

        List<Emprestimo> resultado = controller.listarEmprestimos();

        assertEquals(2, resultado.size());
        verify(emprestimoDAO).findAll();
    }

    @Test
    void listarEmprestimos_deveRetornarListaVazia_quandoNaoExistemEmprestimos() {

        when(emprestimoDAO.findAll()).thenReturn(List.of());

        List<Emprestimo> resultado = controller.listarEmprestimos();

        assertNotNull(resultado);        // nunca null — só vazia
        assertTrue(resultado.isEmpty());
        verify(emprestimoDAO).findAll();
    }
}
