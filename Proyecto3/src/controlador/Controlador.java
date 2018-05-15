/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controllers.*;
import entities.*;
import vista.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author doria
 */
public class Controlador {
    
        //Creación del Entity Manager Factory
        private final EntityManagerFactory emf;
        
        //Creación de los controladores:
        
        private static ClubJpaController controClub;
        private static ContinenteJpaController controContinente;
        private static EntrenadorJpaController controEntrenador;
        private static EquipoPaisJpaController controEquipoPais;
        private static EstadioJpaController controEstadio;
        private static GolJpaController controGol;
        private static GrupoJpaController controGrupo;
        private static JuezJpaController controJuez;
        private static JugadorJpaController controJugador;
        private static PaisJpaController controPais;
        private static PartidoJpaController controPartido;
        private static PosicionJpaController controPosicion;
        private static RolJpaController controRol;
        private static SillaJpaController controSilla;
        private static TarjetaJpaController controTarjeta;
        private static UsuarioJpaController controUsuario;
        private Ventana vista;
        
        Controlador()
        {
            //Creación del Entity Manager Factory
            emf = Persistence.createEntityManagerFactory("ControladorPU");

            //Creación de los controladores:

            controClub = new ClubJpaController(emf);
            controContinente = new ContinenteJpaController(emf);
            controEntrenador = new EntrenadorJpaController(emf);
            controEquipoPais = new EquipoPaisJpaController(emf);
            controEstadio = new EstadioJpaController(emf);
            controGol = new GolJpaController(emf);
            controGrupo = new GrupoJpaController(emf);
            controJuez = new JuezJpaController(emf);
            controJugador = new JugadorJpaController(emf);
            controPais = new PaisJpaController(emf);
            controPartido = new PartidoJpaController(emf);
            controPosicion = new PosicionJpaController(emf);
            controRol = new RolJpaController(emf);
            controSilla = new SillaJpaController(emf);
            controTarjeta = new TarjetaJpaController(emf);
            controUsuario = new UsuarioJpaController(emf);
            
            
            
            vista = new Ventana(this);
            vista.setVisible(true);
        }
    public void mostrarPartidoRE (List<Partido> partidos){
        
        partidos.forEach(par -> System.out.println(par.getCodEquipoLocal() +" - "+par.getCodEquipoVisitante()));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        List<Partido> partidos = controlador.controPartido.findPartidoEntities(emf);
        controlador.mostrarPartidoRE(partidos);
    }
    
}