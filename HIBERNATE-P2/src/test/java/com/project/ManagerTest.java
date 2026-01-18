package com.project;

import com.project.dao.Manager;
import com.project.domain.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagerTest {

    @BeforeAll
    void init() {
        Manager.createSessionFactory();
    }

    @AfterAll
    void close() {
        Manager.close();
    }

    @Test
    void testAddAutor() {
        Autor autor = Manager.addAutor("Joan Miró");
        assertNotNull(autor);
        assertNotNull(autor.getAutorId());
        assertEquals("Joan Miró", autor.getNom());
    }

    @Test
    void testAddLlibre() {
        Llibre llibre = Manager.addLlibre("ISBN001", "Art Contemporani", "Editorial X", 2020);
        assertNotNull(llibre);
        assertEquals("ISBN001", llibre.getIsbn());
        assertEquals("Art Contemporani", llibre.getTitol());
    }

    @Test 
    void testAddBiblioteca(){
        Biblioteca biblio = Manager.addBiblioteca("Biblioteca Central", "Barcelona", "C/ Gran 1", "930000000", "info@bib.cat");
        assertNotNull(biblio);
        assertEquals("Biblioteca Central", biblio.getNom());

    }

    @Test
    void testAddExemplar() {
        //dependencias de exemplar
        Biblioteca biblio = Manager.addBiblioteca("Biblioteca Test","Barcelona","C/ Test 1","900000000","test@bib.cat");
        Llibre llibre = Manager.addLlibre("ISBN-TEST-EX","Llibre Exemplar","Editorial Test",2023);

        Exemplar exemplar = Manager.addExemplar("COD-EX-001", llibre, biblio);

        assertNotNull(exemplar);
        //assertNotNull(exemplar.getExemplarId());
        assertEquals("COD-EX-001", exemplar.getCodiBarres());
        assertTrue(exemplar.isDisponible());
        assertEquals(llibre.getIsbn(), exemplar.getLlibre().getIsbn());
        assertEquals(biblio.getNom(), exemplar.getBiblioteca().getNom());
    }

    @Test
    void testAddPrestecNoDisponible() {
        Biblioteca biblio = Manager.addBiblioteca("Central 2", "Barcelona", "C/ Ex. 2", "123456789", "biblio2@ex.com");
        Llibre llibre = Manager.addLlibre("ISBN003", "Llibre Ocupat", "Editorial Z", 2022);
        Exemplar exemplar = Manager.addExemplar("CB002", llibre, biblio);
        Persona persona1 = Manager.addPersona("11111111B", "Marc", "987654321", "marc@ex.com");
        Persona persona2 = Manager.addPersona("22222222C", "Laia", "987654322", "laia@ex.com");

        // 1er prest
        Prestec p1 = Manager.addPrestec(exemplar, persona1, LocalDate.now(), LocalDate.now().plusDays(5));
        //assertNotNull(p1);

        // 2ndo prest ()
        Prestec p2 = Manager.addPrestec(exemplar, persona2, LocalDate.now(), LocalDate.now().plusDays(5));
        assertNull(p2);
    }
}