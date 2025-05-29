package campeonatosfifa.api.presentacion.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import campeonatosfifa.api.core.dominio.entidades.Campeonato;
import campeonatosfifa.api.core.interfaces.servicios.ICampeonatoServicio;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoControlador {

    private ICampeonatoServicio servicio;

    public CampeonatoControlador(ICampeonatoServicio servicio) {
        this.servicio = servicio;
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public List<Campeonato> listar() {
        return servicio.listar();
    }

    @RequestMapping(value = "/obtener/{id}", method = RequestMethod.GET)
    public Campeonato obtener(@PathVariable int id) {
        return servicio.obtener(id);
    }

    @RequestMapping(value = "/buscar/{dato}", method = RequestMethod.GET)
    public List<Campeonato> buscar(@PathVariable String dato) {
        return servicio.buscar(dato);
    }

    @PostMapping("/agregar")
    public Campeonato agregar(@RequestBody Campeonato campeonato) {
        return servicio.agregar(campeonato);
    }

    @PutMapping("/modificar")
    public Campeonato modificar(@RequestBody Campeonato campeonato) {
        return servicio.modificar(campeonato);
    }

    @DeleteMapping("/eliminar/{id}")
    public boolean eliminar(@PathVariable int id) {
        return servicio.eliminar(id);
    }
} 