package com.marcelo.bibliotech;

import com.marcelo.bibliotech.controller.BibliotecaController;
import com.marcelo.bibliotech.dao.LivroDAO;
import com.marcelo.bibliotech.model.Livro;
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
public class LivroTeste {

    @Mock
    private LivroDAO livroDAO;

    @InjectMocks
    private BibliotecaController controller;

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro(1, "Percy Jackson", "Rick Riordan");
    }

    @Test
    void testarCadastroLivro() {
        controller.cadastrarLivro(livro);
        verify(livroDAO, times(1)).save(livro);
    }

    @Test
    void testarListagemLivros() {
        when(livroDAO.findAll()).thenReturn(List.of(livro));
        List<Livro> livros = controller.listaLivros();
        assertFalse(livros.isEmpty());
    }

     @Test
    void testarBuscaLivro() {
        when(livroDAO.findById(1)).thenReturn(livro);
        Livro encontrado = controller.buscaLivro(1);
        assertNotNull(encontrado);
        assertEquals(1, encontrado.getId());
    }

     @Test
    void deveAtualizarLivroExistente() {
    Livro livroAtualizado = new Livro(1, "Diário de um banana", "Jeff Kinney");
    controller.atualizarLivro(livroAtualizado);
    verify(livroDAO, times(1)).update(livroAtualizado);
    }

    @Test
    void deveRemoverLivroPorId() {
    int idParaRemover = 1;
    controller.removerLivro(idParaRemover);
    verify(livroDAO, times(1)).delete(idParaRemover);
    }
}